import * as React from 'react';
import classNames from 'classnames';
import '../../CSS/RegisterForm.css'; 


export default class RegisterForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            active: "register",
            firstName: "",
            lastName: "",
            login: "",
            password: "",
            onRegister: props.onRegister
        };
    }

    onChangeHandler = (event) => {
        const { name, value } = event.target;
        this.setState({ [name]: value });
    };

    onSubmitRegister = (e) => {
        e.preventDefault();
        this.state.onRegister(e, this.state.firstName, this.state.lastName, this.state.login, this.state.password);
    };

    render() {
        return (
                        <div className={classNames("tab-pane", "fade", this.state.active === "register" ? "show active" : "")} id="pills-register">
                            <form onSubmit={this.onSubmitRegister}>
                                <div className="form-outline mb-4">
                                    <input 
                                        type="text" 
                                        id="firstName" 
                                        name="firstName" 
                                        className="form-control" 
                                        onChange={this.onChangeHandler} 
                                        placeholder=" " 
                                        required 
                                    />
                                    <label className="form-label" htmlFor="firstName">First name</label>
                                </div>

                                <div className="form-outline mb-4">
                                    <input 
                                        type="text" 
                                        id="lastName" 
                                        name="lastName" 
                                        className="form-control" 
                                        onChange={this.onChangeHandler} 
                                        placeholder=" " 
                                        required 
                                    />
                                    <label className="form-label" htmlFor="lastName">Last name</label>
                                </div>

                                <div className="form-outline mb-4">
                                    <input 
                                        type="text" 
                                        id="login" 
                                        name="login" 
                                        className="form-control" 
                                        onChange={this.onChangeHandler} 
                                        placeholder=" " 
                                        required 
                                    />
                                    <label className="form-label" htmlFor="login">Username</label>
                                </div>

                                <div className="form-outline mb-4">
                                    <input 
                                        type="password" 
                                        id="registerPassword" 
                                        name="password" 
                                        className="form-control" 
                                        onChange={this.onChangeHandler} 
                                        placeholder=" " 
                                        required 
                                    />
                                    <label className="form-label" htmlFor="registerPassword">Password</label>
                                </div>

                                <div className="text-center">
                                    <button type="submit" className="btn btn-primary mb-3">Sign up</button>
                                </div>
                            </form>
                        </div>
        );
    }
}
