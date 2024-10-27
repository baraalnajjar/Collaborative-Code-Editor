// src/components/viewer/ViewerLoginForm.js
import React from 'react';
import '../../CSS/LoginForm.css'; 

export default class LoginForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      onLogin: props.onLogin
    };
  }

  onChangeHandler = (event) => {
    const { name, value } = event.target;
    this.setState({ [name]: value });
  };

  onSubmitLogin = (e) => {
    e.preventDefault();
    this.state.onLogin(e, this.state.username, this.state.password);
  };

  render() {
    return (
      <div className="login-box">
        <form onSubmit={this.onSubmitLogin}>
          <div className="form-outline mb-4">
            <input 
              type="text" 
              id="username" 
              name="username" 
              className="form-control" 
              onChange={this.onChangeHandler} 
              placeholder=" " 
              required 
            />
            <label className="form-label" htmlFor="username">Username</label>
          </div>

          <div className="form-outline mb-4">
            <input 
              type="password" 
              id="password" 
              name="password" 
              className="form-control" 
              onChange={this.onChangeHandler} 
              placeholder=" " 
              required 
            />
            <label className="form-label" htmlFor="password">Password</label>
          </div>

          <div className="text-center">
            <button type="submit" className="btn btn-primary mb-3">Login</button>
          </div>
        </form>
      </div>
    );
  }
}
