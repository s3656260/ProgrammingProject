import React from 'react';
//import getTop from 'D:/Development files/ProgrammingProject/React/src/Client.js';
import { JsonToTable } from "react-json-to-table";




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
        fetch("http://localhost:4567/test/top").then(res => res.json()).then(
        (result) => {this.setState({isLoaded: true, items: result.items});},
        (error) => {this.setState({isLoaded: true,error});})
        
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
                    <li key={item.name}>
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