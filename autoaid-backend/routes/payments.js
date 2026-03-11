import express from "express";
import axios from "axios";
import { v4 as uuidv4 } from "uuid";
import nodemailer from "nodemailer";
import Twilio from "twilio";
import dotenv from "dotenv";
import User from "../models/User.js";

dotenv.config();
const router = express.Router();

const MTN_SUBSCRIPTION_KEY = process.env.MTN_SUBSCRIPTION_KEY;
const MTN_API_USER = process.env.MTN_API_USER;
const MTN_API_KEY = process.env.MTN_API_KEY;
const MTN_ENV = process.env.MTN_ENV || "sandbox";
const MTN_BASE_URL = process.env.MTN_BASE_URL || "https://sandbox.momodeveloper.mtn.com";

const AIRTEL_SANDBOX = process.env.AIRTEL_SANDBOX === "true";
const AIRTEL_BASE_URL = process.env.AIRTEL_BASE_URL || "https://openapi.airtel.africa";
const AIRTEL_API_KEY = process.env.AIRTEL_API_KEY;
const AIRTEL_API_SECRET = process.env.AIRTEL_API_SECRET;

const EMAIL_FROM = process.env.EMAIL_FROM;
const TWILIO_SID = process.env.TWILIO_SID;
const TWILIO_TOKEN = process.env.TWILIO_TOKEN;
const TWILIO_FROM = process.env.TWILIO_FROM;


const PLAN_CONFIG = {
  monthly: { amount: 10000, days: 30 },
  quarterly: { amount: 25000, days: 90 },
  yearly: { amount: 80000, days: 365 },
};


async function sendConfirmations({ user, planName, expiryDate }) {

  try {
    if (process.env.SMTP_HOST && process.env.SMTP_USER && process.env.SMTP_PASS && user?.email) {
      const transporter = nodemailer.createTransport({
        host: process.env.SMTP_HOST,
        port: Number(process.env.SMTP_PORT) || 587,
        secure: process.env.SMTP_SECURE === "true",
        auth: {
          user: process.env.SMTP_USER,
          pass: process.env.SMTP_PASS,
        },
      });

      await transporter.sendMail({
        from: EMAIL_FROM || process.env.SMTP_USER,
        to: user.email,
        subject: "Subscription Activated",
        text: `Hi ${user.name || ""}, your ${planName} subscription is now active until ${expiryDate.toDateString()}.`,
      });
    }
  } catch (err) {
    console.warn("EMAIL FAILED:", err.message || err);
  }


  try {
    if (TWILIO_SID && TWILIO_TOKEN && TWILIO_FROM && user?.phone) {
      const client = Twilio(TWILIO_SID, TWILIO_TOKEN);
      await client.messages.create({
        from: TWILIO_FROM,
        to: user.phone,
        body: `Your ${planName} subscription is now active until ${expiryDate.toDateString()}.`,
      });
    }
  } catch (err) {
    console.warn("SMS FAILED:", err.message || err);
  }
}

async function getMtnToken() {
  if (!MTN_SUBSCRIPTION_KEY || !MTN_API_USER || !MTN_API_KEY) {
    throw new Error("Missing MTN credentials in env");
  }

  const basic = Buffer.from(`${MTN_API_USER}:${MTN_API_KEY}`).toString("base64");

  const headers = {
    Authorization: `Basic ${basic}`,
    "Ocp-Apim-Subscription-Key": MTN_SUBSCRIPTION_KEY,
    "X-Target-Environment": MTN_ENV,
    "Cache-Control": "no-cache",
  };

  const url = `${MTN_BASE_URL}/collection/token/`;
  const res = await axios.post(url, null, { headers, validateStatus: () => true });

  if (!res.data?.access_token) {
    throw new Error("Failed to get MTN access token");
  }

  return res.data.access_token;
}


async function createMtnRequestToPay({ amount, msisdn, externalId, payerMessage = "Payment", payeeNote = "Thank you" }) {
  const token = await getMtnToken();
  const referenceId = uuidv4();

  const url = `${MTN_BASE_URL}/collection/v1_0/requesttopay`;

  const headers = {
    Authorization: `Bearer ${token}`,
    "X-Target-Environment": MTN_ENV,
    "Ocp-Apim-Subscription-Key": MTN_SUBSCRIPTION_KEY,
    "X-Reference-Id": referenceId,
    "Content-Type": "application/json",
  };

  const body = {
    amount: String(amount),
    currency: MTN_ENV === "sandbox" ? "EUR" : "UGX",
    externalId,
    payer: {
      partyIdType: "MSISDN",
      partyId: msisdn,
    },
    payerMessage,
    payeeNote,
  };

  const res = await axios.post(url, body, { headers, validateStatus: () => true });
  return { referenceId, status: res.status, data: res.data };
}


async function checkMtnRequestToPayStatus(referenceId) {
  if (!referenceId) throw new Error("referenceId required");

  const token = await getMtnToken();

  const url = `${MTN_BASE_URL}/collection/v1_0/requesttopay/${referenceId}`;

  const headers = {
    Authorization: `Bearer ${token}`,
    "X-Target-Environment": MTN_ENV,
    "Ocp-Apim-Subscription-Key": MTN_SUBSCRIPTION_KEY,
  };

  const res = await axios.get(url, { headers, validateStatus: () => true });
  return { status: res.status, data: res.data };
}


async function airtelRequestToPay({ amount, phone }) {

  if (AIRTEL_SANDBOX) {
    const referenceId = `AIR-${uuidv4()}`;
  
    return { referenceId, status: 202, data: { message: "Airtel sandbox simulated request created" } };
  }

  if (!AIRTEL_API_KEY || !AIRTEL_API_SECRET) {
    throw new Error("Missing Airtel credentials for live mode");
  }

  const tokenRes = await axios.post(`${AIRTEL_BASE_URL}/auth/oauth2/token`, {
    client_id: AIRTEL_API_KEY,
    client_secret: AIRTEL_API_SECRET,
    grant_type: "client_credentials",
  }, { validateStatus: () => true });

  const token = tokenRes.data?.access_token;
  if (!token) throw new Error("Failed to get Airtel token");

  const payload = {
    reference: `AIR_${Date.now()}`,
    subscriber: {
      country: "UG",
      currency: "UGX",
      msisdn: phone.startsWith("0") ? phone.substring(1) : phone,
    },
    transaction: {
      amount,
      country: "UG",
      currency: "UGX",
      id: `TXN_${Date.now()}`,
    },
  };

  const headers = {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };

  const res = await axios.post(`${AIRTEL_BASE_URL}/merchant/v1/payments/`, payload, { headers, validateStatus: () => true });

  return { referenceId: res.data?.reference || `AIR-${uuidv4()}`, status: res.status, data: res.data };
}

/* AIRTEL: Check status */
async function checkAirtelStatus(referenceId) {
  if (AIRTEL_SANDBOX) {
    return { status: 200, data: { status: "SUCCESS", referenceId } };
  }

  throw new Error("Airtel live status checking not implemented in this example");
}


   
async function simulateAndActivateSubscription({ user, planId, network, plan }) {
  const now = new Date();
  const expiry = new Date(now.getTime() + plan.days * 86400000);

  user.subscription = {
    plan: planId,
    active: true,
    price: plan.amount,
    startDate: now,
    expiryDate: expiry,
    paymentMethod: network === "mtn" ? "mtn_momo_sandbox" : "airtel_sandbox",
  };

  user.pendingPayment = undefined;
  await user.save();

 
  sendConfirmations({ user, planName: planId, expiryDate: expiry }).catch(() => {});

  return { sandboxMode: true, subscription: user.subscription, expiry, message: "Sandbox activated" };
}


router.post("/subscribe", async (req, res) => {
  try {
    const { providerId, planId, phone, network } = req.body;

    if (!providerId || !planId || !phone || !network) {
      return res.status(400).json({ message: "All fields required (providerId, planId, phone, network)" });
    }

    const user = await User.findById(providerId);
    if (!user) return res.status(404).json({ message: "Provider not found" });

    const plan = PLAN_CONFIG[planId];
    if (!plan) return res.status(400).json({ message: "Invalid plan" });

   
    const normalizedPhone = phone.startsWith("0") ? `256${phone.slice(1)}` : phone;

   
    const externalId = `${planId}_${providerId}_${Date.now()}`;

   
    if ((network === "mtn" && MTN_ENV === "sandbox") || (network === "airtel" && AIRTEL_SANDBOX)) {
      const sim = await simulateAndActivateSubscription({ user, planId, network, plan });
      return res.json({
        success: true,
        sandboxMode: true,
        message:
          network === "mtn"
            ? `${network.toUpperCase()} sandbox simulated — subscription activated automatically.`
            : `${network.toUpperCase()} sandbox simulated — subscription activated automatically.`,
        subscription: sim.subscription,
        referenceId: sim.referenceId || null,
        externalId,
      });
    }

  
    let result;
    if (network === "mtn") {
      result = await createMtnRequestToPay({
        amount: plan.amount,
        msisdn: normalizedPhone,
        externalId,
        payerMessage: `Subscription ${planId}`,
        payeeNote: "AutoAID subscription",
      });
    } else if (network === "airtel") {
      result = await airtelRequestToPay({
        amount: plan.amount,
        phone: normalizedPhone,
      });
    } else {
      return res.status(400).json({ message: "Invalid network (must be 'mtn' or 'airtel')" });
    }

  
    user.pendingPayment = {
      referenceId: result.referenceId,
      externalId,
      plan: planId,
      amount: plan.amount,
      network,
      createdAt: new Date(),
    };

    await user.save();

    return res.json({
      success: true,
      message: "Payment started — approve MoMo prompt on phone.",
      referenceId: result.referenceId,
      externalId,
    });
  } catch (err) {
    console.error("Subscribe error:", err?.message || err);
    return res.status(500).json({ message: "Failed to initiate payment", details: err?.message || err });
  }
});

router.get("/status", async (req, res) => {
  try {
    const { referenceId, network } = req.query;
    if (!referenceId) return res.status(400).json({ message: "referenceId required" });

    if (network === "mtn") {
      const result = await checkMtnRequestToPayStatus(referenceId);
      return res.json({ success: true, statusCode: result.status, data: result.data });
    }

    if (network === "airtel") {
      const result = await checkAirtelStatus(referenceId);
      return res.json({ success: true, statusCode: result.status, data: result.data });
    }

    try {
      const mtnRes = await checkMtnRequestToPayStatus(referenceId);
      return res.json({ success: true, statusCode: mtnRes.status, data: mtnRes.data, network: "mtn" });
    } catch (_) {
      if (AIRTEL_SANDBOX) {
        const airtelRes = await checkAirtelStatus(referenceId);
        return res.json({ success: true, statusCode: airtelRes.status, data: airtelRes.data, network: "airtel" });
      }
      return res.status(400).json({ message: "Unable to determine network for this referenceId" });
    }
  } catch (err) {
    console.error("Status check error:", err?.message || err);
    return res.status(500).json({ message: "Failed to check status", details: err?.message || err });
  }
});

export default router;
