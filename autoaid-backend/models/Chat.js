// models/chat.js
import mongoose from "mongoose";

const { Schema } = mongoose;

const ChatMessageSchema = new Schema(
  {
    // Request the chat belongs to
    requestId: {
      type: Schema.Types.ObjectId,
      ref: "Request",
      required: true,
      index: true,
    },

    // Who sent the message
    sender: {
      type: String,
      enum: ["user", "provider", "admin", "system"],
      required: true,
      lowercase: true,
      trim: true,
      index: true,
    },

    // Sender user ID
    senderId: {
      type: Schema.Types.ObjectId,
      ref: "User",
      required: false,
      index: true,
    },

    // Message text
    text: {
      type: String,
      required: true,
      trim: true,
      maxlength: 2000,
    },

    // Extra data (location, attachments etc)
    meta: {
      type: Schema.Types.Mixed,
      default: null,
    },

    // Users who have read the message
    readBy: [
      {
        type: Schema.Types.ObjectId,
        ref: "User",
        index: true,
      },
    ],
  },
  {
    timestamps: true,
  }
);

/* =================================================
   INDEXES (important for performance)
================================================= */

// Fast chat history lookup
ChatMessageSchema.index({ requestId: 1, createdAt: 1 });

// Fast unread notification lookup
ChatMessageSchema.index({ requestId: 1, readBy: 1 });

// Prevent model overwrite in dev / hot reload
export default mongoose.models.ChatMessage ||
  mongoose.model("ChatMessage", ChatMessageSchema);