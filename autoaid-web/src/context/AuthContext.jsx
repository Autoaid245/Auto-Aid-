import React, { createContext, useContext, useEffect, useState } from "react";
import axios from "axios";

const AuthContext = createContext(null);
export const useAuth = () => useContext(AuthContext);

const API = (import.meta.env.VITE_API_URL || "http://localhost:5001") + "/api/auth";

// allow cookies globally
axios.defaults.withCredentials = true;

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [authLoading, setAuthLoading] = useState(true);

  // ✅ Optional: check session on reload
  const checkAuth = async () => {
    try {
      const res = await axios.get(`${API}/me`, { withCredentials: true });
      setUser(res.data.user || null);
    } catch {
      setUser(null);
    } finally {
      setAuthLoading(false);
    }
  };

  useEffect(() => {
    checkAuth();
  }, []);

  const login = async (email, password) => {
    try {
      const res = await axios.post(
        `${API}/login`,
        {
          email: (email || "").trim().toLowerCase(), // ✅ match backend normalizeEmail
          password: password || "",
        },
        { withCredentials: true }
      );

      setUser(res.data.user);
      return res.data.user;
    } catch (err) {
      // ✅ show exact backend message instead of generic axios error
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        "Login failed";
      throw new Error(msg);
    }
  };

  const signup = async (formData) => {
    try {
      const res = await axios.post(`${API}/signup`, formData, {
        withCredentials: true,
      });
      return res.data;
    } catch (err) {
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        "Signup failed";
      throw new Error(msg);
    }
  };

  const logout = async () => {
    try {
      await axios.post(`${API}/logout`, {}, { withCredentials: true });
    } catch {}
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, setUser, login, signup, logout, authLoading }}>
      {children}
    </AuthContext.Provider>
  );
};