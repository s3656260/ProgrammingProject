import React from 'react';
import Button from 'react-bootstrap/Button';

function roundStr(val) {
    var i = Math.round(val * 1000) / 1000;
    return (i);
}
export default class StockItemDisplay extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            hasStock: false,
            stock: [],
            sellVal: 0,
            buyVal: 0,
            isLoading:false
        };
        this.handleSellChange = this.handleSellChange.bind(this);
        this.handleSellSubmit = this.handleSellSubmit.bind(this);

        this.handleBuyChange = this.handleBuyChange.bind(this);
        this.handleBuySubmit = this.handleBuySubmit.bind(this);
        this.endLoading = this.endLoading.bind(this);
    }
    endLoading(){
        this.setState({isLoading:false});
        this.render();
    }
    updateStock(item) {
        this.setState({ hasStock: true, stock: item })
    }
    handleBuyChange(event) {
        this.setState({ buyVal: event.target.value })
    }
    handleSellChange(event) {
        this.setState({ sellVal: event.target.value })
    }

    handleBuySubmit(event) {
        var id = 0;
        console.log("doing purchase");
        this.setState({isLoading:true});
        var url = "http://34.70.170.35:8080/test/userPurchase/IEZPLSTQRPH2NFJHN";
        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                sym: this.state.stock.symbol,
                userId: id,
                amount: this.state.buyVal
            })
        }).then(this.props.updateUserAmount(this.state.buyVal, true, this.state.stock.symbol));
        
        event.preventDefault();
        //need to check if user can afford
        

    }

    handleSellSubmit(event) {
        var id = 0;
        console.log("doing Sale");
        this.setState({isLoading:true});
        var url = "http://34.70.170.35:8080/test/userSellShare/IEZPLSTQRPH2NFJHN";
        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                sym: this.state.stock.symbol,
                userId: id,
                amount: this.state.sellVal
            })
        })
        .then(this.props.updateUserAmount(this.state.sellVal, true, this.state.stock.symbol));
        
        event.preventDefault();
        //need to check if user can afford
        
    }
    render() {
        var price = this.state.stock.price;
        var userVal = (this.state.stock.uAmount) * price;
        console.log("rendering details");
        console.log(this.state);
        if(this.state.isLoading == true){
            return(<div>loading purchase</div>)
        }
        else if (this.state.hasStock == true) {
            return (
                <div id="stockDisplay">
                    <div class="stockItemH1" >Company : {this.state.stock.company}</div>
                    <div class="stockItemH1">Symbol : {this.state.stock.symbol}</div>
                    <div class="stockItemH1">User stocks held : {this.state.stock.uAmount}</div>
                    <div class="stockItemH1">Price : ${roundStr(price)}</div>
                    <div class="stockItemH1">Current Value of held stock : ${roundStr(userVal)}</div>



                    <form onSubmit={this.handleBuySubmit}>
                        <div class="stockItemH1"><input type="number" name="quantity" min="0" value={this.state.buyVal} onChange={this.handleBuyChange} />
                            {/* <input type="submit" value="buy" />Value : ${roundStr(this.state.buyVal * price)} */}
                            <Button variant="outline-success" type="submit" value="buy" >BUY : ${roundStr(this.state.buyVal * price)}</Button>
                        </div>
                    </form>

                    <form onSubmit={this.handleSellSubmit}>
                        <div class="stockItemH1"><input type="number" name="quantity" min="0" value={this.state.sellVal} onChange={this.handleSellChange} />
                            {/* <input type="submit" value="Sell" />Value : ${roundStr(this.state.sellVal * price)} */}
                            <Button variant="outline-danger" type="submit" value="Sell" >SELL : ${roundStr(this.state.sellVal * price)}</Button>
                        </div>

                    </form>

                </div>

            );
        } else {
            return (
                <div>
                    <div class="dropdown">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Menu
  </button>
                        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                            <a class="dropdown-item" href="#">Action</a>
                            <a class="dropdown-item" href="#">Another action</a>
                            <a class="dropdown-item" href="#">Something else here</a>
                        </div>
                    </div>
                    <div>
                        please select a stock to display details
                </div></div>
            )
        }
    }
}