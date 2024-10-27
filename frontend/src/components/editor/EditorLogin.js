import React from 'react';
import LoginForm from './LoginForm'; 
import { request, setAuthHeader } from '../../helpers/axios_helper';
import { useNavigate } from 'react-router-dom';

const EditorLogin = ({ onBack }) => {
  const navigate = useNavigate(); 

  const onLogin = (e, username, password) => {
    e.preventDefault();

    request('POST', '/editor/login', { login: username, password: password })
        .then((response) => {
            const token = response.data.token;
            localStorage.setItem('authToken', token); 
            localStorage.setItem('username', username); 
            setAuthHeader(token);
            
            navigate('/editorContent'); 
        })
        .catch(() => {
            localStorage.removeItem('authToken'); 
            
            alert('Login failed. Please try again.'); 
        });
};


  const handleBackToHome = () => {
    navigate('/home'); 
  };
  
  return (
    <div className="login-page">
      <h2>Editor Login</h2>
      <LoginForm onLogin={onLogin} />
      <button className="btn btn-secondary mt-3" onClick={handleBackToHome}>
        Back to Home
      </button>
    </div>
  );
};

export default EditorLogin;
