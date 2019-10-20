import React from 'react';


class signup extends React.Component {


  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: ''
    };
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);

    this.handleSubmit = this.handleSubmit.bind(this);
  }


  handleUsernameChange(event) {
    this.setState({ username: event.target.value })

  }

  handlePasswordChange(event) {
    this.setState({ password: event.target.value })

  }

  handleSubmit(event) {
    var id = 0;
    console.log("signing in");
    var url = "http://localhost:4567/test/register/:" + this.state.username + "/:" + this.state.password;
    fetch(url, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }

    })
    event.preventDefault();
    //need to check if user can afford

  }



  render() {
    return (
      <div className="container">
        <form className="white" onSubmit={this.handleSubmit}>
          <h5 className="grey-text text-darken-3">Sign Up</h5>
          <div className="input-field">
            <label htmlFor="firstName">username</label>
            <input type="text" id='username' onChange={this.handleUsernameChange} />
          </div>
          <div className="input-field">
            <label htmlFor="password">Password</label>
            <input type="password" id='password' onChange={this.handlePasswordChange} />
          </div>


          <div className="input-field">
            <button className="btn pink lighten-1 z-depth-0">Sign Up</button>
          </div>
        </form>
      </div>
    )
  }
}

export default signup
