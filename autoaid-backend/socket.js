// socket.js
import { Server } from "socket.io";
import jwt from "jsonwebtoken";
import cookie from "cookie";
import mongoose from "mongoose";

import User from "./models/User.js";
import Request from "./models/Request.js";
import ChatMessage from "./models/chat.js";

export function initSocket(server, allowOrigin) {
  const io = new Server(server, {
    cors: {
      origin: allowOrigin,
      methods: ["GET", "POST"],
      credentials: true,
    },
  });

  /* =================================================
     AUTH
  ================================================= */
  io.use(async (socket, next) => {
    try {
      let token = null;

      // ✅ Best for Android / mobile
      if (socket.handshake.auth && socket.handshake.auth.token) {
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
      next(new Error("Authentication failed"));
    }
  });

  /* =================================================
     SOCKET EVENTS
  ================================================= */
  io.on("connection", (socket) => {
    console.log("⚡ Socket connected:", socket.id, "role:", socket.user?.role);

    const room = (requestId) => `chat_${requestId}`;

    async function assertParticipant(requestId) {
      if (!mongoose.Types.ObjectId.isValid(requestId)) {
        throw new Error("Invalid request id");
      }

      const reqDoc = await Request.findById(requestId);
      if (!reqDoc) throw new Error("Request not found");

      const me = String(socket.user._id);
      const userId = String(reqDoc.userId || "");
      const providerId = String(reqDoc.assignedProviderId || reqDoc.assignedTo || "");

      if (me !== userId && me !== providerId) {
        throw new Error("Not allowed");
      }

      return reqDoc;
    }

    /* -----------------------------
       JOIN CHAT
    ----------------------------- */
    socket.on("joinChat", async ({ requestId }) => {
      try {
        if (!requestId) return;

        await assertParticipant(requestId);
        socket.join(room(requestId));

        const rid = new mongoose.Types.ObjectId(requestId);

        const messages = await ChatMessage.find({ requestId: rid })
          .sort({ createdAt: 1 })
          .limit(200);

        socket.emit("chat_history", { requestId, messages });
        socket.emit("chat_joined", { requestId });

        console.log("✅ Joined chat:", requestId);
      } catch (e) {
        socket.emit("chat_error", {
          requestId,
          message: e.message || "Join failed",
        });
      }
    });

    /* -----------------------------
       LEAVE CHAT
    ----------------------------- */
    socket.on("leaveChat", ({ requestId }) => {
      if (!requestId) return;
      socket.leave(room(requestId));
      socket.emit("chat_left", { requestId });
    });

    /* -----------------------------
       SEND MESSAGE
    ----------------------------- */
    socket.on("sendMessage", async ({ requestId, text, meta }) => {
      try {
        const clean = String(text || "").trim();
        if (!clean || !requestId) return;

        const reqDoc = await assertParticipant(requestId);

        const status = String(reqDoc.status || "").toLowerCase();
        const canChat = ["assigned", "arrived", "in_progress", "completed"].includes(status);

        if (!canChat) {
          throw new Error("Chat is only available after a provider is assigned.");
        }

        const role = String(socket.user.role || "user").toLowerCase();
        const sender =
          role === "provider"
            ? "provider"
            : role === "admin"
            ? "admin"
            : "user";

        const rid = new mongoose.Types.ObjectId(requestId);

        const message = await ChatMessage.create({
          requestId: rid,
          sender,
          senderId: socket.user._id,
          text: clean,
          meta: meta || {},
          readBy: [socket.user._id],
        });

        io.to(room(requestId)).emit("new_message", { requestId, message });

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

    /* -----------------------------
       MARK READ
    ----------------------------- */
    socket.on("markRead", async ({ requestId }) => {
      try {
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

    /* -----------------------------
       GET NOTIFICATIONS
    ----------------------------- */
    socket.on("get_notifications", async () => {
      try {
        const myId = socket.user._id;

        const requestFilter =
          String(socket.user.role).toLowerCase() === "provider"
            ? { $or: [{ assignedProviderId: myId }, { assignedTo: myId }] }
            : { userId: myId };

        const myRequests = await Request.find(requestFilter)
          .select("_id status createdAt")
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
              sender: 1,
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
          read: false,
        }));

        socket.emit("notifications", { notifications });
      } catch (e) {
        socket.emit("notifications_error", {
          message: e.message || "Failed to load notifications",
        });
      }
    });

    socket.on("disconnect", () => {
      console.log("❌ Socket disconnected:", socket.id);
    });
  });

  return io;
}