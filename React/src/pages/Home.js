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
    componentDidMount() {
        //fetch("http://localhost:4567/test/top", {mode: 'no-cors'}).then(res => res.text())          // convert to plain text
        //.then(text => console.log(text))
        /*fetch("http://localhost:4567/test/top", {mode: 'no-cors'}).then(res => res.json()).then(
        (result) => {this.setState({isLoaded: true, items: result.items});},
        (error) => {this.setState({isLoaded: true,error});})
        console.log("items");
        console.log(this.items);*/
        const myJson = [
            {"symbol":"GE","company":"General Electric Co.","price":"7.93"},
            {"symbol":"MO","company":"Altria Group, Inc.","price":"45.25"},
            {"symbol":"CHK","company":"Chesapeake Energy Corp.","price":"1.39"},
            {"symbol":"AMD","company":"Advanced Micro Devices, Inc.","price":"30.2"},
            {"symbol":"BAC","company":"Bank of America Corp.","price":"26.47"},
            {"symbol":"PM","company":"Philip Morris International, Inc.","price":"71.7"},
            {"symbol":"T","company":"AT&T, Inc.","price":"34.72"},
            {"symbol":"SNAP","company":"Snap, Inc.","price":"15.51"},
            {"symbol":"NLY","company":"Annaly Capital Management, Inc.","price":"8.42"},
            {"symbol":"ECA","company":"Encana Corp.","price":"4.31"}];
        this.setState({isLoaded: true, items:myJson});
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
                <ul>
                    {items.map(item => (
                    <li key={item.symbol}>
                    {item.symbol} {item.company} {item.price}
                    </li>
                    ))}
                </ul> 
                </div>
                </form>
            );
        }
    }
}