// src/components/AppContent.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../CSS/AppContent.css'; 

const AppContent = () => {
  const navigate = useNavigate(); 

  const onRoleSelect = (role) => {
    if (role === 'admin') {
      navigate('/adminLogin');  
    } 

    if (role === 'viewer') {
      navigate('/viewerLogin'); 
    } 
    if (role === 'editor') {
      navigate('/editorLogin'); 
    } 
  };

  return (
    <div>
      <div className="jumbotron">
        <h1 className="display-4">Welcome Collaborative Code Editor</h1>
        <h2 className="lead">Choose your role:</h2>
        <div className="mt-4">
          <button className="btn btn-primary mx-2" onClick={() => onRoleSelect('admin')}>
            Admin
          </button>
          <button className="btn btn-secondary mx-2" onClick={() => onRoleSelect('editor')}>
            Editor
          </button>
          <button className="btn btn-success mx-2" onClick={() => onRoleSelect('viewer')}>
            Viewer
          </button>
        </div>
      </div>
    </div>
  );
};

export default AppContent;
