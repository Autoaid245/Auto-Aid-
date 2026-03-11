import mongoose from "mongoose";
import bcrypt from "bcryptjs";
import { encrypt, decrypt } from "../utils/crypto.js";

/* -------------------------------
   SUBSCRIPTION SCHEMA
--------------------------------*/
const SubscriptionSchema = new mongoose.Schema({
  plan: {
    type: String,
    enum: ["monthly", "quarterly", "yearly"],
    default: null,
  },
  active: { type: Boolean, default: false },
  startDate: { type: Date, default: null },
  expiryDate: { type: Date, default: null },
  paymentMethod: { type: String, default: null },
  price: { type: Number, default: 0 },
});

/* -------------------------------
   USER SCHEMA
--------------------------------*/
const UserSchema = new mongoose.Schema(
  {
    name: { type: String, required: true },

    email: {
      type: String,
      required: true,
      unique: true,
      lowercase: true,
      trim: true,
    },

    password: { type: String, required: true },

    phone: { type: String, default: "" },

    role: {
      type: String,
      enum: ["user", "provider", "admin"],
      default: "user",
    },

    status: {
      type: String,
      enum: ["active", "inactive", "pending", "approved"],
      default: "active",
    },

    /* ✅ NEW: PLATFORM TRACKING */
    registeredFrom: {
      type: String,
      enum: ["android", "web"],
      default: "web",
    },

    lastLoginFrom: {
      type: String,
      enum: ["android", "web"],
      default: null,
    },

    businessName: { type: String, default: "" },
    businessType: { type: String, default: "" },
    servicesOffered: { type: [String], default: [] },

    address: { type: String, default: "" },
    lat: { type: Number, default: 0 },
    lng: { type: Number, default: 0 },

    subscription: {
      type: SubscriptionSchema,
      default: () => ({
        plan: null,
        active: false,
        startDate: null,
        expiryDate: null,
        paymentMethod: null,
        price: 0,
      }),
    },
  },
  { timestamps: true }
);

/* -------------------------------
   FIELD ENCRYPTION
--------------------------------*/
UserSchema.pre("save", function (next) {
  if (this.isModified("phone") && this.phone) this.phone = encrypt(this.phone);
  if (this.isModified("address") && this.address) this.address = encrypt(this.address);
  if (this.isModified("businessName") && this.businessName)
    this.businessName = encrypt(this.businessName);
  next();
});

/* -------------------------------
   PASSWORD HASH
--------------------------------*/
UserSchema.pre("save", async function (next) {
  if (!this.isModified("password")) return next();
  this.password = await bcrypt.hash(this.password, 10);
  next();
});

/* -------------------------------
   PASSWORD COMPARE
--------------------------------*/
UserSchema.methods.comparePassword = async function (password) {
  return bcrypt.compare(password, this.password);
};

/* -------------------------------
   DECRYPT METHOD
--------------------------------*/
UserSchema.methods.getDecrypted = function () {
  const obj = this.toObject();
  if (obj.phone) obj.phone = decrypt(obj.phone);
  if (obj.address) obj.address = decrypt(obj.address);
  if (obj.businessName) obj.businessName = decrypt(obj.businessName);
  return obj;
};

export default mongoose.model("User", UserSchema);