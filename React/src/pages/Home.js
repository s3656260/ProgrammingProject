import React from 'react';

function searchingFor(term) {
    return function (x) {
        return x.symbol.toLowerCase().includes(term.toLowerCase()) || x.company.toLowerCase().includes(term.toLowerCase());
    }
}
function roundStr(val){
    var i = parseInt(val);
    i = i.toFixed(2);
    return(i);
}
export default class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            items: [],
            term: '',
            userMoney: null
        };
        this.searchHandler = this.searchHandler.bind(this);
    }

    searchHandler(event) {
        this.setState({ term: event.target.value })
    }
    getApi() {
        fetch("http://localhost:4567/test/top").then(res => res.json()).then(
            (result) => { this.setState({ isLoaded: true, items: result }); },
            (error) => { this.setState({ isLoaded: true, error }); })
            fetch("http://localhost:4567/test/userCash/1").then(res=>res.json().then(
                (result) => { this.setState({ userMoney: result }); }
            ));
    }
    componentDidMount() {
        this.getApi();
        console.log("user money");
        console.log(this.state.userMoney);
    }
    
    render() {

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
                    <div>TEST LIST SHARE user money : {this.userMoney}</div><div className="TableData">
        
                    <div class="row">
                    <b class ="cell">Share Symbol</b><b class ="cell">Company Name</b><b class ="cell">Price</b><b class ="cell">User Amount</b>
                    </div>

                    <ul id="shareTable">
                        {this.state.items.filter(searchingFor(this.state.term)).map(item => (
                            <li key={item.symbol} class="row" id="shareItem">
                                <div class ="cell">{item.symbol}</div><div class ="cell">{item.company}</div><div class ="cell">${roundStr(item.price)}</div><div class ="cell">{item.uAmount}</div>
                            </li>
                        ))}
                    </ul>
                    </div>
                </div>

            );


        }
    }
}