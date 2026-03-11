import mongoose from "mongoose";

const { Schema } = mongoose;

const LocationSchema = new Schema(
  {
    lat: { type: Number, default: 0 },
    lng: { type: Number, default: 0 },
  },
  { _id: false }
);

const LastMessageSchema = new Schema(
  {
    text: { type: String, default: "" },
    sender: {
      type: String,
      enum: ["user", "provider", "admin", "system"],
      default: "user",
      lowercase: true,
      trim: true,
    },
    createdAt: { type: Date, default: null },
  },
  { _id: false }
);

const RequestSchema = new Schema(
  {
    // -------------------------
    // USER
    // -------------------------
    userId: {
      type: Schema.Types.ObjectId,
      ref: "User",
      required: true,
      index: true,
    },
    userName: { type: String, default: "" },
    userPhone: { type: String, default: "" },

    // -------------------------
    // SOURCE
    // -------------------------
    requestedFrom: {
      type: String,
      enum: ["android", "web"],
      default: "web",
      index: true,
    },

    createdByRole: {
      type: String,
      enum: ["user", "provider", "admin"],
      default: "user",
      index: true,
    },

    // -------------------------
    // TYPE
    // -------------------------
    providerType: {
      type: String,
      enum: ["towing", "fuel", "ambulance", "garage"],
      required: true,
      index: true,
      lowercase: true,
      trim: true,
    },

    service: {
      type: String,
      enum: ["towing", "fuel", "ambulance", "garage"],
      required: true,
      lowercase: true,
      trim: true,
    },

    // -------------------------
    // TARGETING
    // -------------------------
    targetProviderId: {
      type: Schema.Types.ObjectId,
      ref: "User",
      default: null,
      index: true,
    },

    declinedBy: {
      type: [Schema.Types.ObjectId],
      ref: "User",
      default: [],
      index: true,
    },

    // -------------------------
    // STATUS / ASSIGNMENT
    // -------------------------
    status: {
      type: String,
      enum: ["pending", "assigned", "arrived", "in_progress", "completed", "cancelled"],
      default: "pending",
      index: true,
      lowercase: true,
      trim: true,
    },

    assignedProviderId: {
      type: Schema.Types.ObjectId,
      ref: "User",
      default: null,
      index: true,
    },
    assignedProviderName: { type: String, default: "" },
    assignedProviderPhone: { type: String, default: "" },
    assignedProviderRating: { type: Number, default: 0 },

    // -------------------------
    // LOCATION
    // -------------------------
    userLocation: {
      type: LocationSchema,
      default: () => ({ lat: 0, lng: 0 }),
    },
    providerLocation: {
      type: LocationSchema,
      default: () => ({ lat: 0, lng: 0 }),
    },

    // -------------------------
    // DETAILS
    // -------------------------
    vehicleInfo: { type: String, default: "" },
    problem: { type: String, default: "" },

    urgency: {
      type: String,
      enum: ["normal", "urgent"],
      default: "normal",
      lowercase: true,
      trim: true,
    },

    towType: { type: String, default: "" },
    note: { type: String, default: "" },

    // -------------------------
    // CHAT SUMMARY
    // -------------------------
    lastMessage: {
      type: LastMessageSchema,
      default: () => ({
        text: "",
        sender: "user",
        createdAt: null,
      }),
    },
  },
  { timestamps: true }
);

RequestSchema.pre("validate", function (next) {
  if (this.targetProviderId === "") this.targetProviderId = null;
  next();
});

/* =================================================
   INDEXES
================================================= */
RequestSchema.index({ providerType: 1, status: 1, createdAt: -1 });
RequestSchema.index({ assignedProviderId: 1, status: 1, updatedAt: -1 });
RequestSchema.index({ userId: 1, createdAt: -1 });

export default mongoose.models.Request || mongoose.model("Request", RequestSchema);