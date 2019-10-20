import React from 'react';
import Button from 'react-bootstrap/Button'
import Table from 'react-bootstrap/Table'
import ButtonToolbar from 'react-bootstrap/ButtonToolbar'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'
import Navbar from 'react-bootstrap/Navbar'
import Form from 'react-bootstrap/Form'
import FormControl from 'react-bootstrap/FormControl'




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
function NavDropdownExample() {
    const handleSelect = eventKey => alert(`selected ${eventKey}`);
}
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

        fetch("http://localhost:4567/test/top").then(res => res.json()).then(
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
                <div>
                    {/* <Nav variant="pills" activeKey="1" onSelect={handleSelect}> */}
                    <Navbar bg="dark" variant="dark">
                     <Navbar.Brand id = "navMoney" href="#home">$ {roundStr(this.state.userMoney)}</Navbar.Brand>
                        <Nav className="mr-auto">

                            <Nav.Link >Home</Nav.Link>
                            <Nav.Link href="#features">My Stocks</Nav.Link>
                            <NavDropdown title="Transactions" id="basic-nav-dropdown">
                                <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                                <NavDropdown.Item href="#action/3.2">Another action</NavDropdown.Item>
                                <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item href="#action/3.4">Separated link</NavDropdown.Item>
                            </NavDropdown>
                        </Nav>

                        <Form inline>
                            <FormControl type="text" placeholder="Search" className="mr-sm-2" onChange={this.searchHandler}
                                value={term} />
                            <Button variant="outline-info">Search</Button>
                        </Form>
                    </Navbar>
                    <div className="TableData">
                        <form>
                            <div className="FormField">
                                <label className="FormField__Label" htmlFor="name">
                                    
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

                        <div>   .... user money : {roundStr(this.state.userMoney)}</div>
                        
                        <div id="TableData">



                            {/* <ul id="shareTable">
                            {}
                            {this.state.items.filter(searchingFor(this.state.term)).map(item => (
                                <li key={item.symbol} class="row" id="shareItem" onClick={() => this.doStockPurchase(item)}>
                                    <div class="cell">{item.symbol}</div><div class="cell">{item.company}</div><div class="cell">${roundStr(item.price)}</div><div class="cell">{item.uAmount}</div>
                                </li>
                            ))}
                        </ul> */}
                            <div class="table-wrapper-scroll-y my-custom-scrollbar">
                                <Table striped borderless hover variant="dark" responsive>
                                    <thead>
                                        <tr>
                                            <th>Symbol</th>
                                            <th>Company Name</th>
                                            <th>Price</th>
                                            <th>User Amount</th>
                                        </tr>
                                    </thead>
                                    <tbody id="scroll">


                                        {this.state.items.filter(searchingFor(this.state.term)).map(item => (
                                            <tr key={item.symbol} onClick={() => this.doStockPurchase(item)}>
                                                <td >{item.symbol}</td><td >{item.company}</td><td >${roundStr(item.price)}</td><td >{item.uAmount}</td>
                                            </tr>

                                        ))}


                                    </tbody>
                                </Table>
                            </div>                  </div>
                    </div>
                </div>

            );


        }
    }
}