import React, { Component } from 'react';
import { HashRouter as Router, Route, Link, NavLink } from 'react-router-dom';
import SignUpForm from './pages/SignUpForm';
import SignInForm from './pages/SignInForm';
import Home from './pages/Home';

import './App.css';
import stockItemDisplay from './pages/stockItemDisplay';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
        listDataFromChild: null
    };    
}
  stockData = (dataFromChild) => {
    //[...we will use the dataFromChild here...]
  }
  render() {
    return (
      <Router basename="/react-auth-ui/">
        <div className="App">
          <div className="App__Aside">
          <stockItemDisplay/>
          </div>
          <div className="App__Form">
            <div className="PageSwitcher">
              <NavLink to="/home" activeClassName="PageSwitcher__Item--Active" className="PageSwitcher__Item">HomeTest</NavLink>
              <NavLink exact to="/" activeClassName="PageSwitcher__Item--Active" className="PageSwitcher__Item">Sign Up</NavLink>
            </div>

            {/* <div className="FormTitle"> }
              <NavLink to="/sign-in" activeClassName="FormTitle__Link--Active" className="FormTitle__Link">Sign In</NavLink> or <NavLink exact to="/" activeClassName="FormTitle__Link--Active" className="FormTitle__Link">Sign Up</NavLink>
            </div> */}

            <Route exact path="/" component={SignUpForm}>
            </Route>
            <Route path="/sign-in" component={SignInForm}>
            </Route>
            <Route exact path="/home"render={ () => <Home callbackFromParent={this.stockData}/> } />
          </div>

        </div>
      </Router>
    );
  }
}

export default App;
