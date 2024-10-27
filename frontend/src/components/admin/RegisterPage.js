import React from 'react';
import RegisterForm from './RegisterForm';
import '../../CSS/RegisterPage.css';


const RegisterPage = ({ onRegister, onBack }) => {
  return (
    <div className="register-page">
      <h2>Register</h2>
      <RegisterForm onRegister={onRegister} />
      <button className="btn btn-secondary mt-3" onClick={onBack}>
        Back to Admin
      </button>
    </div>
  );
};

export default RegisterPage;
