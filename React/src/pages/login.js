import React from 'react';

class login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        };
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);

        this.handleLogin = this.handleLogin.bind(this);
    }


    handleUsernameChange(event) {
        this.setState({ username: event.target.value })

    }

    handlePasswordChange(event) {
        this.setState({ password: event.target.value })

    }

    async handleLogin(event) {
        var id = 0;
        console.log("signing in");
        var url = "http://localhost:4567/test/login/" + this.state.username + "/" + this.state.password;
        const fetchResult = fetch(url, {method: 'GET'})
        var res = await fetchResult;
        var token = await res.text();
        console.log(token);
        //jump to new page here
        event.preventDefault();

    }

    render() {
        return (
            <div className="container">
                <form className="white" onSubmit={this.handleLogin}>
                    <h5 className="grey-text text-darken-3">Sign In</h5>
                    <div className="input-field">
                        <label htmlFor="input">UsertestName</label>
                        <input type="input" id='input' onChange={this.handleUsernameChange} />
                    </div>
                    <div className="input-field">
                        <label htmlFor="password">Password</label>
                        <input type="password" id='password' onChange={this.handlePasswordChange} />
                    </div>
                    <div className="input-field">
                        <button className="btn pink lighten-1 z-depth-0">Login</button>
                    </div>
                </form>
            </div>
        )
    }
}

export default login
