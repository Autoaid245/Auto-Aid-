// server.js
// FULL UPDATED VERSION
// ANDROID + WEB FRIENDLY + REALTIME CHAT + NOTIFICATIONS + CORS FIX + MAINTENANCE MODE

import dotenv from "dotenv";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// ✅ Force load .env from same folder as server.js
dotenv.config({ path: path.join(__dirname, ".env") });

import express from "express";
import cors from "cors";
import mongoose from "mongoose";
import http from "http";
import { Server } from "socket.io";
import cookieParser from "cookie-parser";
import helmet from "helmet";
import rateLimit from "express-rate-limit";
import jwt from "jsonwebtoken";
import cookie from "cookie";

// ROUTES
import uploadsRoutes from "./routes/uploads.js";
import chatRoutes from "./routes/chat.js";
import authRoutes from "./routes/auth.js";
import adminRoutes from "./routes/admin.js";
import providersRoutes from "./routes/providers.js";
import fuelRoutes from "./routes/fuel.js";
import towingRoutes from "./routes/towing.js";
import ambulanceRoutes from "./routes/ambulance.js";
import garageRoutes from "./routes/garage.js";
import paymentsRoutes from "./routes/payments.js";
import requestsRoutes from "./routes/requests.js";

// MODELS
import User from "./models/User.js";
import ChatMessage from "./models/chat.js";
import Request from "./models/Request.js";
import Settings from "./models/Settings.js";

const app = express();
const server = http.createServer(app);

console.log("✅ ENV FILE LOADED. MONGO_URI =", process.env.MONGO_URI);

/* =================================================
   ✅ CORS HELPERS
================================================= */
const FRONTEND_ALLOWED = [
  "http://localhost:5173",
  "http://127.0.0.1:5173",
  "http://localhost:5174",
  "http://127.0.0.1:5174",
];

const LAN_REGEX = /^http:\/\/192\.168\.\d{1,3}\.\d{1,3}(:\d+)?$/;
const EMULATOR_REGEX = /^http:\/\/10\.0\.2\.2(:\d+)?$/;
const LOCALHOST_ANY_PORT = /^http:\/\/localhost:\d+$/;
const LOOPBACK_ANY_PORT = /^http:\/\/127\.0\.0\.1:\d+$/;

function isAllowedOrigin(origin) {
  if (!origin) return true; // mobile apps, curl, postman, same-device tools
  if (FRONTEND_ALLOWED.includes(origin)) return true;
  if (LAN_REGEX.test(origin)) return true;
  if (EMULATOR_REGEX.test(origin)) return true;
  if (LOCALHOST_ANY_PORT.test(origin)) return true;
  if (LOOPBACK_ANY_PORT.test(origin)) return true;
  return false;
}

function corsOrigin(origin, callback) {
  if (isAllowedOrigin(origin)) {
    return callback(null, true);
  }
  console.warn("❌ CORS blocked origin:", origin);
  return callback(new Error(`CORS blocked for origin: ${origin}`));
}

const corsOptions = {
  origin: corsOrigin,
  credentials: true,
  methods: ["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"],
  allowedHeaders: [
    "Content-Type",
    "Authorization",
    "X-Client",
    "x-client",
    "Accept",
    "Origin",
  ],
  optionsSuccessStatus: 204,
};

/* =================================================
   ✅ VERY IMPORTANT: CORS FIRST
================================================= */
app.use((req, res, next) => {
  const origin = req.headers.origin;

  if (isAllowedOrigin(origin)) {
    res.header("Access-Control-Allow-Origin", origin || "*");
  }

  res.header("Vary", "Origin");
  res.header("Access-Control-Allow-Credentials", "true");
  res.header(
    "Access-Control-Allow-Headers",
    "Content-Type, Authorization, X-Client, x-client, Accept, Origin"
  );
  res.header(
    "Access-Control-Allow-Methods",
    "GET, POST, PUT, PATCH, DELETE, OPTIONS"
  );

  if (req.method === "OPTIONS") {
    console.log("✅ PREFLIGHT:", req.method, req.originalUrl, "Origin:", origin || "none");
    return res.sendStatus(204);
  }

  next();
});

app.use(cors(corsOptions));

/* =================================================
   ✅ SECURITY
================================================= */
app.use(
  helmet({
    crossOriginResourcePolicy: false,
  })
);

app.use(cookieParser());

app.use(
  rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 100,
  })
);

/* =================================================
   ✅ BODY PARSING
================================================= */
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

/* =================================================
   ✅ REQUEST LOGGER
================================================= */
app.use((req, res, next) => {
  console.log(
    "📱 HIT:",
    req.method,
    req.originalUrl,
    "| origin:",
    req.headers.origin || "none",
    "| client:",
    req.headers["x-client"] || "none"
  );
  next();
});

/* =================================================
   ✅ GLOBAL MAINTENANCE MODE
   Works for BOTH WEB + ANDROID
   - allows admin routes
   - blocks normal user/provider routes
================================================= */
app.use(async (req, res, next) => {
  try {
    const requestPath = req.path || req.originalUrl || "";

    // Always allow these even during maintenance
    const allowDuringMaintenance =
      requestPath === "/" ||
      requestPath === "/api/ping" ||
      requestPath.startsWith("/api/admin") ||
      requestPath.startsWith("/uploads");

    if (allowDuringMaintenance) {
      return next();
    }

    const settings = await Settings.findOne().lean();

    const maintenanceMode = !!settings?.maintenanceMode;
    const maintenanceMessage =
      settings?.maintenanceMessage ||
      "AutoAid is currently under maintenance. Please try again later.";

    if (!maintenanceMode) {
      return next();
    }

    return res.status(503).json({
      ok: false,
      maintenanceMode: true,
      message: maintenanceMessage,
      systemName: settings?.systemName || "AutoAid",
    });
  } catch (err) {
    console.error("❌ Maintenance middleware error:", err.message);
    next();
  }
});

/* =================================================
   ✅ HEALTH CHECK
================================================= */
app.get("/api/ping", (req, res) => {
  res.json({ ok: true, message: "pong", time: new Date().toISOString() });
});

app.get("/", (req, res) => {
  res.send("🚀 AutoAID backend running successfully!");
});

/* =================================================
   ✅ SOCKET.IO
================================================= */
const io = new Server(server, {
  cors: {
    origin: (origin, callback) => {
      if (isAllowedOrigin(origin)) return callback(null, true);
      return callback(new Error(`Socket CORS blocked for origin: ${origin}`));
    },
    methods: ["GET", "POST"],
    credentials: true,
  },
});

app.set("io", io);

/* =================================================
   ✅ SOCKET AUTH
================================================= */
io.use(async (socket, next) => {
  try {
    let token = null;

    // ✅ Android / mobile preferred
    if (socket.handshake.auth?.token) {
      token = socket.handshake.auth.token;
    }

    // ✅ Authorization header
    if (!token) {
      const authHeader =
        socket.handshake.headers.authorization ||
        socket.handshake.headers.Authorization;

      if (authHeader && authHeader.startsWith("Bearer ")) {
        token = authHeader.split(" ")[1];
      }
    }

    // ✅ Cookie for web
    if (!token) {
      const cookiesHeader = socket.handshake.headers.cookie;
      if (cookiesHeader) {
        const parsed = cookie.parse(cookiesHeader);
        token = parsed.token;
      }
    }

    if (!token) return next(new Error("Not authenticated"));
    if (!process.env.JWT_SECRET) return next(new Error("JWT_SECRET missing"));

    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    const user = await User.findById(decoded.id).select("-password");

    if (!user) return next(new Error("User not found"));

    socket.user = user;
    next();
  } catch (e) {
    console.error("❌ Socket auth error:", e.message);
    next(new Error("Authentication failed"));
  }
});

/* =================================================
   ✅ SOCKET CHAT + NOTIFICATIONS
================================================= */
io.on("connection", (socket) => {
  console.log("⚡ Secure socket connected:", socket.id, "role:", socket.user?.role);

  const room = (requestId) => `chat_${requestId}`;

  async function assertParticipant(requestId) {
    if (!mongoose.Types.ObjectId.isValid(requestId)) {
      const err = new Error("Invalid request id");
      err.status = 400;
      throw err;
    }

    const reqDoc = await Request.findById(requestId);

    if (!reqDoc) {
      const err = new Error("Request not found");
      err.status = 404;
      throw err;
    }

    const me = String(socket.user?._id);
    const userId = String(reqDoc.userId || "");
    const providerId = String(reqDoc.assignedProviderId || reqDoc.assignedTo || "");

    const allowed = me === userId || (providerId && me === providerId);
    if (!allowed) {
      const err = new Error("Not allowed");
      err.status = 403;
      throw err;
    }

    return reqDoc;
  }

  socket.on("joinChat", async ({ requestId }) => {
    try {
      if (!requestId) return;

      // optional: block chat during maintenance for non-admin users
      const settings = await Settings.findOne().lean();
      if (settings?.maintenanceMode && String(socket.user?.role).toLowerCase() !== "admin") {
        socket.emit("chat_error", {
          requestId,
          message:
            settings?.maintenanceMessage ||
            "AutoAid is currently under maintenance. Please try again later.",
          maintenanceMode: true,
        });
        return;
      }

      await assertParticipant(requestId);
      socket.join(room(requestId));

      const rid = new mongoose.Types.ObjectId(requestId);

      const messages = await ChatMessage.find({ requestId: rid })
        .sort({ createdAt: 1 })
        .limit(200);

      socket.emit("chat_history", { requestId, messages });
      socket.emit("chat_joined", { requestId });
    } catch (e) {
      socket.emit("chat_error", {
        requestId,
        message: e.message || "Join failed",
      });
    }
  });

  socket.on("leaveChat", ({ requestId }) => {
    if (!requestId) return;
    socket.leave(room(requestId));
    socket.emit("chat_left", { requestId });
  });

  socket.on("sendMessage", async ({ requestId, text, meta }) => {
    try {
      const settings = await Settings.findOne().lean();
      if (settings?.maintenanceMode && String(socket.user?.role).toLowerCase() !== "admin") {
        socket.emit("chat_error", {
          requestId,
          message:
            settings?.maintenanceMessage ||
            "AutoAid is currently under maintenance. Please try again later.",
          maintenanceMode: true,
        });
        return;
      }

      const clean = String(text || "").trim();
      if (!requestId || !clean) return;

      const reqDoc = await assertParticipant(requestId);

      const status = String(reqDoc.status || "").toLowerCase();
      const canChat = [
        "assigned",
        "driver_assigned",
        "driver_on_the_way",
        "ambulance_on_the_way",
        "arrived",
        "in_progress",
        "patient_picked",
        "at_hospital",
        "completed",
      ].includes(status);

      if (!canChat) {
        socket.emit("chat_error", {
          requestId,
          message: "Chat is only available after a provider is assigned.",
        });
        return;
      }

      const role = String(socket.user.role || "user").toLowerCase();
      const sender =
        role === "provider"
          ? "provider"
          : role === "admin"
          ? "admin"
          : "user";

      const rid = new mongoose.Types.ObjectId(requestId);

      const msg = await ChatMessage.create({
        requestId: rid,
        sender,
        senderId: socket.user._id,
        text: clean,
        meta: meta || {},
        readBy: [socket.user._id],
      });

      io.to(room(requestId)).emit("new_message", { requestId, message: msg });

      socket.to(room(requestId)).emit("notify", {
        type: "message",
        title: "New message",
        body: clean,
        requestId,
        createdAt: new Date().toISOString(),
      });
    } catch (e) {
      socket.emit("chat_error", {
        requestId,
        message: e.message || "Send failed",
      });
    }
  });

  socket.on("markRead", async ({ requestId }) => {
    try {
      const settings = await Settings.findOne().lean();
      if (settings?.maintenanceMode && String(socket.user?.role).toLowerCase() !== "admin") {
        socket.emit("chat_error", {
          requestId,
          message:
            settings?.maintenanceMessage ||
            "AutoAid is currently under maintenance. Please try again later.",
          maintenanceMode: true,
        });
        return;
      }

      if (!requestId) return;

      await assertParticipant(requestId);

      const rid = new mongoose.Types.ObjectId(requestId);

      await ChatMessage.updateMany(
        { requestId: rid, readBy: { $ne: socket.user._id } },
        { $addToSet: { readBy: socket.user._id } }
      );

      socket.emit("read_ok", { requestId });
    } catch (e) {
      socket.emit("chat_error", {
        requestId,
        message: e.message || "Read update failed",
      });
    }
  });

  socket.on("get_notifications", async () => {
    try {
      const settings = await Settings.findOne().lean();
      if (settings?.maintenanceMode && String(socket.user?.role).toLowerCase() !== "admin") {
        socket.emit("notifications_error", {
          maintenanceMode: true,
          message:
            settings?.maintenanceMessage ||
            "AutoAid is currently under maintenance. Please try again later.",
        });
        return;
      }

      console.log("📩 get_notifications from:", String(socket.user._id), socket.user.role);

      const myId = socket.user._id;

      const requestFilter =
        String(socket.user.role).toLowerCase() === "provider"
          ? { $or: [{ assignedProviderId: myId }, { assignedTo: myId }] }
          : { userId: myId };

      const myRequests = await Request.find(requestFilter)
        .select("_id createdAt status")
        .sort({ createdAt: -1 })
        .limit(50);

      const requestIds = myRequests.map((r) => r._id);

      const unread = await ChatMessage.aggregate([
        {
          $match: {
            requestId: { $in: requestIds },
            readBy: { $ne: myId },
          },
        },
        { $sort: { createdAt: -1 } },
        { $limit: 100 },
        {
          $lookup: {
            from: "users",
            localField: "senderId",
            foreignField: "_id",
            as: "senderUser",
          },
        },
        { $unwind: { path: "$senderUser", preserveNullAndEmptyArrays: true } },
        {
          $project: {
            _id: 1,
            requestId: 1,
            text: 1,
            createdAt: 1,
            senderName: "$senderUser.name",
          },
        },
      ]);

      const notifications = unread.map((m) => ({
        id: String(m._id),
        type: "message",
        title: "New message",
        body: (m.senderName ? `${m.senderName}: ` : "") + String(m.text || ""),
        requestId: String(m.requestId),
        createdAt: m.createdAt,
      }));

      socket.emit("notifications", { notifications });
    } catch (e) {
      console.error("❌ notifications_error:", e);
      socket.emit("notifications_error", {
        message: e.message || "Failed to load notifications",
      });
    }
  });

  socket.on("disconnect", () => {
    console.log("❌ Socket disconnected:", socket.id);
  });
});

/* =================================================
   ✅ DATABASE
================================================= */
const PORT = process.env.PORT || 5001;
const MONGO_URI = process.env.MONGO_URI;

async function ensureDefaultSettings() {
  try {
    const settings = await Settings.findOne();

    if (!settings) {
      await Settings.create({
        systemName: "AutoAid",
        supportEmail: "",
        notificationsEnabled: true,
        maintenanceMode: false,
        maintenanceMessage:
          "AutoAid is currently under maintenance. Please try again later.",
      });
      console.log("✅ Default settings created");
    }
  } catch (err) {
    console.error("❌ Default settings creation failed:", err.message);
  }
}

async function connectDb() {
  try {
    if (!MONGO_URI) {
      console.error("❌ MONGO_URI missing in .env");
      process.exit(1);
    }

    await mongoose.connect(MONGO_URI);
    console.log("✅ MongoDB connected");

    await ensureDefaultSettings();
  } catch (err) {
    console.error("❌ DB connection failed:", err);
    process.exit(1);
  }
}

/* =================================================
   ✅ SUBSCRIPTION CHECK
================================================= */
async function checkAndExpireSubscriptions() {
  try {
    const now = new Date();

    const providers = await User.find({
      role: "provider",
      "subscription.active": true,
      "subscription.expiryDate": { $ne: null },
    });

    for (const p of providers) {
      if (p.subscription?.expiryDate && p.subscription.expiryDate < now) {
        p.subscription.active = false;
        await p.save();
      }
    }
  } catch (err) {
    console.error("Subscription expiry error:", err);
  }
}

setInterval(checkAndExpireSubscriptions, 60 * 1000);

/* =================================================
   ✅ ROUTES
================================================= */
app.use("/api/payments", paymentsRoutes);
app.use("/api/auth", authRoutes);
app.use("/api/admin", adminRoutes);
app.use("/api/providers", providersRoutes);
app.use("/api/fuel", fuelRoutes);
app.use("/api/towing", towingRoutes);
app.use("/api/ambulance", ambulanceRoutes);
app.use("/api/garage", garageRoutes);
app.use("/api/uploads", uploadsRoutes);
app.use("/api/requests", requestsRoutes);
app.use("/api/chat", chatRoutes);

app.use("/uploads", express.static(path.join(__dirname, "uploads")));

/* =================================================
   ✅ 404 HANDLER
================================================= */
app.use((req, res) => {
  res.status(404).json({
    message: `Route not found: ${req.method} ${req.originalUrl}`,
  });
});

/* =================================================
   ✅ ERROR HANDLER
================================================= */
app.use((err, req, res, next) => {
  console.error("❌ Express error:", err);

  const origin = req.headers.origin;
  if (isAllowedOrigin(origin)) {
    res.header("Access-Control-Allow-Origin", origin || "*");
    res.header("Vary", "Origin");
    res.header("Access-Control-Allow-Credentials", "true");
  }

  if (String(err.message || "").includes("CORS")) {
    return res.status(403).json({ message: err.message });
  }

  return res.status(err.status || 500).json({
    message: err.message || "Server error",
  });
});

/* =================================================
   ✅ BOOT
================================================= */
(async function boot() {
  await connectDb();

  try {
    const adminEmail = "admin@autoaid.com";
    const exists = await User.findOne({ email: adminEmail });

    if (!exists) {
      if (!process.env.ADMIN_PASSWORD) {
        console.warn("⚠️ ADMIN_PASSWORD missing in .env (admin not created)");
      } else {
        await new User({
          name: "System Admin",
          email: adminEmail,
          password: process.env.ADMIN_PASSWORD,
          role: "admin",
          status: "approved",
          subscription: {
            plan: "monthly",
            active: false,
            startDate: null,
            expiryDate: null,
            paymentMethod: null,
            price: 0,
          },
        }).save();

        console.log("✅ Admin created:", adminEmail);
      }
    }
  } catch (err) {
    console.error("Admin creation error:", err);
  }

  server.listen(PORT, "0.0.0.0", () => {
    console.log(`🚀 Server listening on http://0.0.0.0:${PORT}`);
    console.log(`✅ Test ping: http://<YOUR_PC_IP>:${PORT}/api/ping`);
  });
})();