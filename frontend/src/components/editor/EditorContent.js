// src/components/editor/EditorContent.js
import React from 'react';
import { Link } from 'react-router-dom';
import '../../CSS/EditorContent.css';

const EditorContent = () => {
  const username = localStorage.getItem('username'); 

  return (
    <div className="editor-content">
      <h1 className="editor-header">Welcome {username || "User"}</h1> 
      <div className="options-card">
        <div className="editor-options">
          <Link to="/create-session">
            <button className="editor-button">Create New Session</button>
          </Link>
          <Link to="/join-session">
            <button className="editor-button">Join a Session</button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default EditorContent;
