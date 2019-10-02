import React from 'react';
function roundStr(val){
    var i = Math.round(val* 1000)/1000;
    return(i);
}
export default class StockItemDisplay extends React.Component {

    
    constructor(props) {
        super(props);
        this.state = {
            hasStock:false,
            stock:[],
            sellVal:0,
            buyVal:0
        };
        this.handleSellChange = this.handleSellChange.bind(this);
        this.handleSellSubmit = this.handleSellSubmit.bind(this);
        
        this.handleBuyChange = this.handleBuyChange.bind(this);
        this.handleBuySubmit = this.handleBuySubmit.bind(this);
        
    }
    updateStock(item){
        this.setState({hasStock:true,stock:item})
    }
    handleBuyChange(event){
        this.setState({buyVal:event.target.value})
    }
    handleSellChange(event){
        this.setState({sellVal:event.target.value})
    }

    handleBuySubmit(event){
        var id = 0;
        console.log("doing purchase");
        var url = "http://localhost:4567/test/userPurchase/";
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
        })
        event.preventDefault();
        //need to check if user can afford
        this.props.updateUserAmount(this.state.buyVal,true,this.state.stock.symbol);
       
    }
    
    handleSellSubmit(event){
        var id = 0;
        console.log("doing Sale");
        var url = "http://localhost:4567/test/userSellShare/";
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
        event.preventDefault();
        //need to check if user can afford
        this.props.updateUserAmount(this.state.sellVal,true,this.state.stock.symbol);
    }
    render() {
        var price = this.state.stock.price;
        var userVal = (this.state.stock.uAmount)*price;
        if(this.state.hasStock==true){
            return(
                <div id = "stockDisplay">
                <div class="stockItemH1">Company : {this.state.stock.company}</div>
                <div class="stockItemH1">Symbol : {this.state.stock.symbol}</div>
                <div class="stockItemH1">User stocks held : {this.state.stock.uAmount}</div>
                <div class="stockItemH1">Price : ${roundStr(price)}</div>
                <div class="stockItemH1">Current Value of held stock : ${roundStr(userVal)}</div>

                <form onSubmit={this.handleSellSubmit}>
                <div class="stockItemH1"><input type="number" name="quantity" min="1" value={this.state.sellVal} onChange={this.handleSellChange}/>
                <input type="submit" value="Sell"/>Value : ${roundStr(this.state.sellVal*price)}</div>
                </form>
                
                <form onSubmit={this.handleBuySubmit}>
                <div class="stockItemH1"><input type="number" name="quantity" min="1" value={this.state.buyVal} onChange={this.handleBuyChange}/>
                <input type="submit" value="buy"/>Value : ${roundStr(this.state.buyVal*price)}</div>
                </form>

                </div>
                
            );
        }else{
            return(
                <div>
                    please select a stock to display details
                </div>
            )
        }
    }
}