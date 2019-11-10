import React, { Component } from 'react';
import { HashRouter as Router, Route, Link, NavLink } from 'react-router-dom';
import SignUpForm from './pages/SignUpForm';
import SignInForm from './pages/SignInForm';
import Home from './pages/Home';

import './App.css';
import StockItemDisplay from './pages/stockItemDisplay';

class App extends Component {
  constructor(props) {
    
    super(props);
    this.state = {
      homeR: null,
      listDataFromChild: null,
      id:"TEST ID HERE"
    };
    
  }
  
  updateUserAmount = (amount, type, symbol) => {//type is wether its buy or sell, true for but, false for sell
    this.refs.stockPage.endLoading();
    console.log("refs:");
    console.log(this.refs);
    this.refs.Home.stockUChange(amount, type, symbol);
  }
  stockData = (dataFromChild,id) => {
    this.refs.stockPage.updateStock(dataFromChild,id);
  }
  render() {
    return (
      <Router basename="/react-auth-ui/">
        <div className="App">
          <div className="App__Aside">
            <StockItemDisplay ref="stockPage" updateUserAmount={this.updateUserAmount} />
          </div>
          <div className="App__Form">
            <Home ref="Home" currentStock={this.stockData} id={this.state.id}/>

            {/*
          <div className="PageSwitcher">
            <NavLink to="/home" activeClassName="PageSwitcher__Item--Active" className="PageSwitcher__Item">HomeTest</NavLink>
            <NavLink exact to="/" activeClassName="PageSwitcher__Item--Active" className="PageSwitcher__Item">Sign Up</NavLink>
          </div>
          <div className="FormTitle"> }
            <NavLink to="/sign-in" activeClassName="FormTitle__Link--Active" className="FormTitle__Link">Sign In</NavLink> or <NavLink exact to="/" activeClassName="FormTitle__Link--Active" className="FormTitle__Link">Sign Up</NavLink>
          </div> */}
            {/*<Route exact path="/" component={SignUpForm}>
          </Route>
          <Route path="/sign-in" component={SignInForm}>
          </Route>
        <Route exact path="/home"render={ () =>  } />*/}

          </div>
        </div>
      </Router>
    );
  }
}

//ref="Home" component={ () => <Home ref="test" currentStock={this.stockData} />}
/*var arr = [];
  var newStock = {
    company: this.state.nAmount,
    price: null,
    symbol: this.state.nSymbol,
    uAmount: null
  }
  var amnt = this.state.nAmount;
  var type = this.state.nType;
  var sym = this.state.nSymbol;
  
  this.state.items.map((item)=>{
    console.log(sym);

    if(sym.equals(item.symbol)==true){
      console.log(item);
    }
  }); */
export default App;
