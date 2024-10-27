// src/components/JoinSession.js
import React, { useState } from 'react';
import '../../CSS/JoinSession.css'; 
import { useNavigate } from 'react-router-dom';

const JoinSession = () => {
  const [sessionCode, setSessionCode] = useState('');  
  const [projectName, setProjectName] = useState('');  
  const [password, setPassword] = useState('');        
  const [language, setlanguage] = useState('');          
  const username = localStorage.getItem('username');    
  const navigate = useNavigate(); 

  const handleJoinSession = () => {
    if (!sessionCode || !projectName || !password) {
      alert('Please fill in all fields.');
      return;
    }

    console.log('Joined session:', sessionCode);
    console.log('Password:', password);
    console.log('Project Name:', projectName);
    console.log('Username:', username);

    localStorage.setItem('projectName', projectName);
    localStorage.setItem('password', password);
    localStorage.setItem('sessionCode', sessionCode);
    localStorage.setItem('language', language);

    navigate('/collaboratice-room'); 
  };

  return (
    <div className="join-session-container">
      <h2>Join a Room</h2>
      <input
        type="text"
        placeholder="Enter room ID"
        value={sessionCode}
        onChange={(e) => setSessionCode(e.target.value)}
        required 
      />
      <input
        type="password"
        placeholder="Enter room password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required 
      />
      <input
        type="text"
        placeholder="Enter project name"
        value={projectName}
        onChange={(e) => setProjectName(e.target.value)}
        required 
      />
            <input
        type="text"
        placeholder="Enter language"
        value={language}
        onChange={(e) => setlanguage(e.target.value)}
        required 
      />
      <button onClick={handleJoinSession}>
        Join Session
      </button>
    </div>
  );
};

export default JoinSession;
