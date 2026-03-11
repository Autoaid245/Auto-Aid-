import express from "express";
import mongoose from "mongoose";
import Request from "../models/Request.js";
import User from "../models/User.js";
import { protect } from "../middleware/authMiddleware.js";

const router = express.Router();

// -----------------------------
// Helper: validate ObjectId
// -----------------------------
function ensureObjectId(id) {
  return mongoose.Types.ObjectId.isValid(id);
}

// ✅ detect platform from header (Android/Web)
function getClient(req) {
  const x = (req.headers["x-client"] || "").toString().toLowerCase();
  return x === "android" ? "android" : "web";
}

// -----------------------------
// Helper: emit request events to correct rooms
// -----------------------------
function emitRequestEvent(req, eventName, requestDoc) {
  const io = req.app.get("io");
  if (!io) return;

  const providerType = String(requestDoc.providerType || "").trim().toLowerCase();
  const requestId = String(requestDoc._id || requestDoc.id || "");

  // providers by type (broadcast room)
  if (providerType) {
    io.to(`type:${providerType}`).emit(eventName, requestDoc);
  }

  // targeted provider room (only if exists)
  if (requestDoc.targetProviderId) {
    io.to(`provider:${String(requestDoc.targetProviderId)}`).emit(eventName, requestDoc);
  }

  // assigned provider room
  if (requestDoc.assignedProviderId) {
    io.to(`provider:${String(requestDoc.assignedProviderId)}`).emit(eventName, requestDoc);
  }

  // user tracking this request
  if (requestId) {
    io.to(`request:${requestId}`).emit(eventName, requestDoc);
  }
}

/**
 * POST /api/requests
 * Create request (user role)
 * body may include targetProviderId (optional)
 */
router.post("/", protect, async (req, res) => {
  try {
    const body = req.body || {};

    if (!body.providerType) return res.status(400).json({ message: "providerType is required" });
    if (!body.service) return res.status(400).json({ message: "service is required" });

    body.providerType = String(body.providerType).trim().toLowerCase();
    body.service = String(body.service).trim().toLowerCase();

    // normalize targetProviderId
    const rawTarget = body.targetProviderId;
    if (rawTarget === "" || rawTarget === null || rawTarget === undefined) {
      delete body.targetProviderId;
    } else {
      const targetStr = String(rawTarget).trim();
      if (!ensureObjectId(targetStr)) {
        return res.status(400).json({ message: "Invalid targetProviderId" });
      }
      body.targetProviderId = targetStr;
    }

    const requestedFrom = getClient(req);

    const doc = await Request.create({
      ...body,
      userId: req.user._id,
      status: "pending",
      createdAt: new Date(),
      requestedFrom, // "android" | "web"
      createdByRole: req.user?.role || "user",
    });

    emitRequestEvent(req, "request_created", doc);
    return res.status(201).json(doc);
  } catch (err) {
    console.error("Create request error:", err);
    return res.status(500).json({ message: "Server error" });
  }
});

/**
 * ✅ UPDATED
 * GET /api/requests/provider?providerType=towing
 *
 * returns:
 * - pending: requests provider can accept
 * - ongoing: assigned/arrived/in_progress for THIS provider
 * - completed: completed/cancelled for THIS provider
 */
router.get("/provider", protect, async (req, res) => {
  try {
    const { providerType } = req.query;

    if (!providerType) return res.status(400).json({ message: "providerType required" });

    const pt = String(providerType).trim().toLowerCase();
    const providerObjectId = new mongoose.Types.ObjectId(req.user._id);

    // 1) Pending (broadcast or targeted to me, not declined)
    const pendingFilter = {
      providerType: pt,
      status: "pending",
      declinedBy: { $ne: providerObjectId },
      $or: [
        { targetProviderId: { $exists: false } },
        { targetProviderId: null },
        { targetProviderId: providerObjectId },
      ],
    };

    // 2) Ongoing (assigned to me)
    const ongoingFilter = {
      providerType: pt,
      assignedProviderId: providerObjectId,
      status: { $in: ["assigned", "arrived", "in_progress"] },
    };

    // 3) Completed/Cancelled (history)
    const completedFilter = {
      providerType: pt,
      assignedProviderId: providerObjectId,
      status: { $in: ["completed", "cancelled"] },
    };

    const [pending, ongoing, completed] = await Promise.all([
      Request.find(pendingFilter).sort({ createdAt: -1 }).limit(200),
      Request.find(ongoingFilter).sort({ updatedAt: -1 }).limit(50),
      Request.find(completedFilter).sort({ updatedAt: -1 }).limit(200),
    ]);

    return res.json({
      pending,
      ongoing,
      completed,

      // optional backward compatibility if your old app used `requests`
      requests: pending,
    });
  } catch (err) {
    console.error("Get provider requests error:", err);
    return res.status(500).json({ message: "Server error" });
  }
});

/**
 * POST /api/requests/:id/decline
 */
router.post("/:id/decline", protect, async (req, res) => {
  try {
    const { id } = req.params;

    if (!ensureObjectId(id)) {
      return res.status(400).json({ message: "Invalid request id" });
    }

    const providerId = String(req.user._id);
    const providerObjectId = new mongoose.Types.ObjectId(providerId);

    const request = await Request.findById(id);
    if (!request) return res.status(404).json({ message: "Request not found" });

    if (String(request.status).toLowerCase() !== "pending") {
      return res.status(400).json({ message: "Only pending requests can be declined" });
    }

    const target = request.targetProviderId ? String(request.targetProviderId) : null;
    if (target && target !== providerId) {
      return res.status(403).json({ message: "Not allowed to decline this request" });
    }

    await Request.updateOne({ _id: id }, { $addToSet: { declinedBy: providerObjectId } });

    const updated = await Request.findById(id);
    emitRequestEvent(req, "request_updated", updated);

    return res.json({ message: "Request declined" });
  } catch (err) {
    console.error("Decline request error:", err);
    return res.status(500).json({ message: "Server error" });
  }
});

/**
 * GET /api/requests/:id
 */
router.get("/:id", protect, async (req, res) => {
  try {
    const { id } = req.params;

    if (!ensureObjectId(id)) return res.status(400).json({ message: "Invalid request id" });

    const request = await Request.findById(id);
    if (!request) return res.status(404).json({ message: "Request not found" });

    return res.json(request);
  } catch (err) {
    console.error("Get request by id error:", err);
    return res.status(500).json({ message: "Server error" });
  }
});

/**
 * POST /api/requests/:id/assign
 * provider accepts
 */
router.post("/:id/assign", protect, async (req, res) => {
  try {
    const { id } = req.params;

    if (!ensureObjectId(id)) return res.status(400).json({ message: "Invalid request id" });

    const providerId = String(req.user._id);

    const request = await Request.findById(id);
    if (!request) return res.status(404).json({ message: "Request not found" });

    if (String(request.status).toLowerCase() !== "pending") {
      return res.status(400).json({ message: "Request already taken" });
    }

    const target = request.targetProviderId ? String(request.targetProviderId) : null;
    if (target && target !== providerId) {
      return res.status(403).json({ message: "This request is assigned to a different provider" });
    }

    const provider = await User.findById(providerId).select("-password");
    if (!provider) return res.status(404).json({ message: "Provider not found" });

    request.status = "assigned";

    // ✅ IMPORTANT: store as ObjectId (matches schema + queries)
    request.assignedProviderId = new mongoose.Types.ObjectId(providerId);

    request.assignedProviderName = provider.name || "";
    request.assignedProviderPhone = provider.phone || "";
    request.assignedProviderRating = provider.rating || 0.0;

    await request.save();

    emitRequestEvent(req, "request_updated", request);
    return res.json(request);
  } catch (err) {
    console.error("Assign request error:", err);
    return res.status(500).json({ message: "Server error" });
  }
});

/**
 * PATCH /api/requests/:id/status
 */
router.patch("/:id/status", protect, async (req, res) => {
  try {
    const { id } = req.params;

    if (!ensureObjectId(id)) return res.status(400).json({ message: "Invalid request id" });

    const { status } = req.body;
    if (!status) return res.status(400).json({ message: "status required" });

    const request = await Request.findById(id);
    if (!request) return res.status(404).json({ message: "Request not found" });

    request.status = String(status).trim().toLowerCase();
    await request.save();

    emitRequestEvent(req, "request_updated", request);
    return res.json(request);
  } catch (err) {
    console.error("Update status error:", err);
    return res.status(500).json({ message: "Server error" });
  }
});

/**
 * GET /api/requests/:id/location
 */
router.get("/:id/location", protect, async (req, res) => {
  try {
    const { id } = req.params;

    if (!ensureObjectId(id)) return res.status(400).json({ message: "Invalid request id" });

    const request = await Request.findById(id);
    if (!request) return res.status(404).json({ message: "Request not found" });

    const lat = request.userLocation?.lat ?? 0;
    const lng = request.userLocation?.lng ?? 0;

    return res.json({ lat, lng });
  } catch (err) {
    console.error("Get user location error:", err);
    return res.status(500).json({ message: "Server error" });
  }
});

export default router;