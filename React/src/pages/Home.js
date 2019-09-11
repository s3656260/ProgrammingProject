import React from 'react';

function searchingFor(term) {
    return function (x) {
        return x.symbol.toLowerCase().includes(term.toLowerCase());
    }
}

export default class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            items: [],
            term: ''
        };
        this.searchHandler = this.searchHandler.bind(this);
    }

    searchHandler(event) {
        this.setState({ term: event.target.value })
    }
    getApi() {
        //fetch("http://localhost:4567/test/top", {mode: 'no-cors'}).then(res => res.text())          // convert to plain text
        //.then(text => console.log(text))
        fetch("http://localhost:4567/test/top").then(res => res.json()).then(
            (result) => { this.setState({ isLoaded: true, items: result }); },
            (error) => { this.setState({ isLoaded: true, error }); })
    }
    componentDidMount() {
        this.getApi();
        console.log("items");
        console.log(this.state.items);
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
                                Symbol
            </label>
                            <input
                                type="text"

                                //value={this.state.term}
                                onchange={this.searchHandler}
                                id="name"
                                className="FormField__Input"
                                placeholder="Enter Share name"
                                name="name"


                            />

                        </div>
                    </form>
                    <div>TEST LIST SHARE</div>

                    <ul>
                        {this.state.items.filter(searchingFor(this.state.term)).map(item => (
                            <li key={item.symbol}>
                                {item.symbol} {item.company} {item.price}
                            </li>
                        ))}
                    </ul>
                </div>

            );


        }
    }
}