import React from 'react';

export default class Home extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
          error: null,
          isLoaded: false,
          items: []
        };
      }
    getApi(){
        //fetch("http://localhost:4567/test/top", {mode: 'no-cors'}).then(res => res.text())          // convert to plain text
        //.then(text => console.log(text))
        fetch("http://localhost:4567/test/top").then(res => res.json()).then(
        (result) => {this.setState({isLoaded: true, items: result});},
        (error) => {this.setState({isLoaded: true,error});})
    }
    componentDidMount() {
        this.getApi();
        console.log("items");
        console.log(this.state.items);
    }
    render() {
        
        const { error, isLoaded, items } = this.state;
        if (error) {
        return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
        return <div>Loading...</div>;
        } else {
        return (
            <form>
                <div className="FormField">
                    <label className="FormField__Label" htmlFor="name">
                        Symbol
            </label>
                    <input
                        type="text"
                        id="name"
                        className="FormField__Input"
                        placeholder="Enter Share name"
                        name="name"

                        onChange={this.handleChange}
                    />
                </div>
                <div>TEST LIST SHARE</div><div className="TableData">
                <ul id="shareTable">
                    <div class="row">
                    <b class ="cell">Share Symbol</b><b class ="cell">Company name</b><b class ="cell">Price</b><b class ="cell">User Amount</b>
                    </div>
                    {items.map(item => (
                    <li key={item.symbol} class="row" id="shareItem">
                    <div class ="cell">{item.symbol}</div><div class ="cell">{item.company}</div><div class ="cell">{item.price}</div><div class ="cell">{item.uAmount}</div>
                    </li>
                    ))}
                </ul> 
                </div>
                </form>
            );
        }
    }
}