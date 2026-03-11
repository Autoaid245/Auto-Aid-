// ✅ FILE NAME: routes/providers.js
import express from "express";
import User from "../models/User.js";
import Request from "../models/Request.js"; // ✅ CHANGED: use Request model
import { protect, authorize } from "../middleware/authMiddleware.js";

const router = express.Router();

/* =================================================
   1️⃣ PUBLIC ROUTES (NO AUTH REQUIRED)
================================================= */

router.get("/garages/approved", async (req, res) => {
  try {
    const garages = await User.find(
      {
        role: "provider",
        businessType: "garage",
        "subscription.active": true,
        status: "approved",
      },
      "-password"
    );
    res.json({ garages });
  } catch (err) {
    console.error("Get approved garages error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

router.get("/public/:id", async (req, res) => {
  try {
    const provider = await User.findById(req.params.id).select(
      "name businessName businessType servicesOffered address phone logoUrl subscription"
    );

    if (!provider) return res.status(404).json({ message: "Provider not found" });

    // decrypt before returning
    res.json(provider.getDecrypted());
  } catch (err) {
    console.error("Public provider profile error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

router.get("/:id/subscription", async (req, res) => {
  try {
    const { id } = req.params;

    const provider = await User.findById(id).select("subscription role status");
    if (!provider) return res.status(404).json({ message: "Provider not found" });

    if (provider.role !== "provider") {
      return res.status(400).json({ message: "Not a provider account" });
    }

    res.json({
      subscription: provider.subscription || {},
      status: provider.subscription?.active ? "active" : "inactive",
    });
  } catch (err) {
    console.error("Provider subscription error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

/* -----------------------------------------
   AVAILABLE PROVIDERS (PUBLIC)
   GET /api/providers/available?providerType=towing&isOnline=true

   ⚠️ Your User schema currently DOES NOT include isOnline.
   So this endpoint will always return empty if you filter by isOnline.
   Fix: remove isOnline filter OR add isOnline in schema.
------------------------------------------ */
router.get("/available", async (req, res) => {
  try {
    const { providerType } = req.query;

    const query = {
      role: "provider",
      status: "approved",
    };

    if (providerType) query.businessType = providerType;

    const providers = await User.find(query)
      .select("name phone businessType rating profileImageUrl logoUrl")
      .sort({ rating: -1 });

    // decrypt phone/businessName/address if present
    const list = providers.map((p) => p.getDecrypted());
    res.json(list);
  } catch (err) {
    console.error("Get available providers error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

/* =================================================
   🔒 PROVIDER-ONLY ROUTES (AUTH REQUIRED)
================================================= */
router.use(protect, authorize("provider"));

/* -----------------------------------------
   ✅ GET LOGGED-IN PROVIDER PROFILE
   GET /api/providers/me
------------------------------------------ */
router.get("/me", async (req, res) => {
  try {
    const providerDoc = await User.findById(req.user._id).select("-password");
    if (!providerDoc) return res.status(404).json({ message: "Provider not found" });
    res.json(providerDoc.getDecrypted());
  } catch (err) {
    console.error("Provider me error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

/* -----------------------------------------
   ✅ UPDATE LOGGED-IN PROVIDER PROFILE
   PATCH /api/providers/me
------------------------------------------ */
router.patch("/me", async (req, res) => {
  try {
    const updates = req.body;

    const providerDoc = await User.findByIdAndUpdate(req.user._id, updates, {
      new: true,
      runValidators: true,
    }).select("-password");

    if (!providerDoc) return res.status(404).json({ message: "Provider not found" });

    res.json(providerDoc.getDecrypted());
  } catch (err) {
    console.error("Provider update me error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

/* -----------------------------------------
   LEGACY SETTINGS ROUTE
------------------------------------------ */
router.put("/provider/:id/settings", async (req, res) => {
  try {
    const { id } = req.params;

    if (req.user._id.toString() !== id) {
      return res.status(403).json({ message: "Access denied" });
    }

    const providerDoc = await User.findByIdAndUpdate(id, req.body, {
      new: true,
      runValidators: true,
    }).select("-password");

    if (!providerDoc) return res.status(404).json({ message: "Provider not found" });

    res.json(providerDoc.getDecrypted());
  } catch (err) {
    console.error("Provider settings update error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

/* -----------------------------------------
   ✅ PROVIDER REQUESTS (UPDATED TO USE Request MODEL)
   OLD ROUTE KEPT:
     GET /api/providers/garageRequests/byProvider/:providerId

   ✅ ALSO ADDED ALIAS (cleaner):
     GET /api/providers/requests/byProvider/:providerId
------------------------------------------ */

// ✅ Cleaner alias (recommended)
router.get("/requests/byProvider/:providerId", async (req, res) => {
  try {
    const { providerId } = req.params;

    if (req.user._id.toString() !== providerId) {
      return res.status(403).json({ message: "Access denied" });
    }

    const provider = await User.findById(providerId);
    if (!provider) return res.status(404).json({ message: "Provider not found" });

    // ✅ Find requests for this provider:
    // 1) broadcast requests matching providerType (if you want that)
    // 2) requests assigned specifically to this provider
    const requests = await Request.find({
      $or: [
        // requests matching provider type (example: garage, towing, fuel...)
        { providerType: String(provider.businessType || "").toLowerCase() },

        // requests assigned to this provider
        { assignedProviderId: provider._id },
      ],
    }).sort({ createdAt: -1 });

    res.json(requests);
  } catch (err) {
    console.error("Provider requests error:", err);
    res.status(500).json({ message: "Server error" });
  }
});

// ✅ OLD route kept (points to the same handler)
router.get("/garageRequests/byProvider/:providerId", async (req, res) => {
  // forward to the same logic
  req.url = `/requests/byProvider/${req.params.providerId}`;
  router.handle(req, res);
});

export default router;