// src/components/ViewerManagement.js
import React from 'react';
import '../../CSS/UserManagement.css';

const UserManagement = ({ onAddViewer, onRemoveViewer, onViewViewers, onBack }) => (
  <div className="viewer-management-container">
    <h2 className="management-title">Manage Users</h2>
    <div className="button-group">
      <button className="management-button add-user" onClick={onAddViewer}>
        Add User
      </button>
      <button className="management-button remove-user" onClick={onRemoveViewer}>
        Remove User
      </button>
      <button className="management-button view-users" onClick={onViewViewers}>
        View Users
      </button>
      <button className="management-button back-to-admin" onClick={onBack}>
        Back to Admin Options
      </button>
    </div>
  </div>
);

export default UserManagement;
