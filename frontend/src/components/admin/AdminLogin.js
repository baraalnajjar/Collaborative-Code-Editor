// src/components/viewer/ViewerLogin.js
import React from 'react';
import LoginForm from './LoginForm'; 
import { request, setAuthHeader } from '../../helpers/axios_helper';
import { useNavigate } from 'react-router-dom'; 



const AdminLogin = () => {

  const onLogin = (e, username, password) => {
    e.preventDefault();

    request('POST', '/admin/login', { login: username, password: password })
      .then((response) => {
        const token = response.data.token;
        localStorage.setItem('authToken', token);
        localStorage.setItem('username', username);

        setAuthHeader(token);
        
        window.location.href = '/admin'; 
      })
      .catch(() => {
        localStorage.removeItem('authToken');
        setAuthHeader(null);
        alert('Login failed. Please try again.');
      });
  };

    const navigate = useNavigate(); 
  
    const handleBackToHome = () => {
      navigate('/home'); 
    };
  
  
  return (
    <div className="login-page">
      <h2>Admin Login</h2>
      <LoginForm onLogin={onLogin} />
      <button className="btn btn-secondary mt-3" onClick={handleBackToHome}>
        Back to Home
      </button>
    </div>
  );
};

export default AdminLogin;
