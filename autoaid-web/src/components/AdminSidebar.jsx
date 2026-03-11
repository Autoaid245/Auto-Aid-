import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  FiMenu,
  FiHome,
  FiUsers,
  FiClipboard,
  FiBarChart2,
  FiSettings,
} from "react-icons/fi";
import "./AdminSidebar.css";

export default function AdminSidebar() {
  const [open, setOpen] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  // ✅ SHOW SIDEBAR ONLY ON DASHBOARD PAGE
  const isDashboard = location.pathname === "/admin";
  if (!isDashboard) return null;

  const menu = [
    { label: "Dashboard", icon: <FiHome />, path: "/admin" },
    { label: "Users", icon: <FiUsers />, path: "/admin/users" },
    { label: "Providers", icon: <FiUsers />, path: "/admin/providers" },
    { label: "Requests", icon: <FiClipboard />, path: "/admin/requests" },
    { label: "Reports", icon: <FiBarChart2 />, path: "/admin/reports" },
    { label: "Settings", icon: <FiSettings />, path: "/admin/settings" },
  ];

  return (
    <div className={`admin-sidebar ${open ? "open" : "closed"}`}>
      <div className="sidebar-top">
        <button className="menu-btn" onClick={() => setOpen(!open)}>
          <FiMenu size={20} />
        </button>
        {open && <h2 className="title">Admin</h2>}
      </div>

      {open && (
        <div className="sidebar-menu">
          {menu.map((item, i) => (
            <div
              key={i}
              className="sidebar-item"
              onClick={() => navigate(item.path)}
            >
              <span className="icon">{item.icon}</span>
              <span className="text">{item.label}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}