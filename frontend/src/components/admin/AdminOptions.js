// src/components/AdminOptions.js
import React from 'react';
import '../../CSS/AdminOptions.css';

const AdminOptions = ({ onManageEditors, onManageViewers, onLogout }) => (
  <div className="admin-options-container">
    <h2 className="admin-title">Admin Options</h2>
    <ul className="admin-options-list">
      <li>
        <button className="admin-button manage-editors" onClick={onManageEditors}>
          Manage Editors
        </button>
      </li>
      <li>
        <button className="admin-button manage-viewers" onClick={onManageViewers}>
          Manage Viewers
        </button>
      </li>
      <li>
        <button className="admin-button logout" onClick={onLogout}>
          Logout
        </button>
      </li>
    </ul>
  </div>
);

export default AdminOptions;
