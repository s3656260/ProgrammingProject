import React, { Component } from 'react';
import api from './api';

function searchingFor(term) {
    return function (x) {
        return x.symbol.toLowerCase().includes(term.toLowerCase()) || x.company.toLowerCase().includes(term.toLowerCase());
    }
}
function roundStr(val) {
    //var i = parseInt(val);
    //i = i.toFixed(2);
    var i = Math.round(val * 1000) / 1000;
    return (i);
}

// function returnApi() {
//     return fetch("http://localhost:4567/test/top");
// }
export default class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            items: [],
            term: '',
            userMoney: 0,
            haveItems: false,
            needUpdate: false
        };
        this.searchHandler = this.searchHandler.bind(this);
        this.stockUChange = this.stockUChange.bind(this);
    }

    stockUChange = (amount, type, symbol) => {//type is wether its buy or sell, true for but, false for sell
        console.log("home is re rendering");
        console.log(amount + "/" + type + "/" + symbol)
        this.setState({ haveItems: false });
        this.getApi();
        this.render();
    }

    searchHandler(event) {
        this.setState({ term: event.target.value })
    }
    getStockFromParents(stocks, loaded, err, money) {
        console.log(stocks);
        this.setState({ items: stocks, error: err, isLoaded: loaded, userMoney: money });
    }


    async getApi() {
        console.log("getting api");
        const fetchResult = fetch("http://localhost:4567/test/userCash/1")
        var res = await fetchResult;
        var json = await res.json();
        this.setState({ userMoney: json.userMoney });
        console.log(json.userMoney);

        api.all().then(res => res.json()).then(
            (result) => { this.setState({ isLoaded: true, items: result }); },
            (error) => { this.setState({ isLoaded: true, error }); })
        //console.log(this.state.userMoney);
        this.setState({ haveItems: true });

    }
    doStockPurchase = (item) => {
        this.props.currentStock(item)
    }

    componentDidMount() {
        this.getApi();
    }

    render() {
        console.log("rendering home");
        const { error, isLoaded, items, term } = this.state;
        if (error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>;
        } else {
            return (
                <div className="TableData">
                    <form>
                        <div className="FormField">
                            <label className="FormField__Label" htmlFor="name">
                                testing
            </label>

                            <input
                                type="text"
                                onChange={this.searchHandler}
                                value={term}

                                id="name"
                                className="FormField__Input"
                                placeholder="Enter Share name"
                                name="name"
                            />

                        </div>
                    </form>
                    <div>TEST LIST SHARE user money : {roundStr(this.state.userMoney)}</div><div className="TableData">

                        <div class="row">
                            <b class="cell">Share Symbol</b><b class="cell">Company Name</b><b class="cell">Price</b><b class="cell">User Amount</b>
                        </div>

                        <ul classname="shareTable">
                            {}
                            {this.state.items.filter(searchingFor(this.state.term)).map(item => (
                                <li key={item.symbol} class="row" id="shareItem" onClick={() => this.doStockPurchase(item)}>
                                    <div class="cell">{item.symbol}</div><div class="cell">{item.company}</div><div class="cell">${roundStr(item.price)}</div><div class="cell">{item.uAmount}</div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>

            );


        }
    }

}
