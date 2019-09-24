import React from 'react';

export default class StockItemDisplay extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasStock:false,
            stock:[]
        };

    }
    updateStock(item){
        console.log(item);
        this.setState({/*hasStock:true,*/ stock:item.json()});
        console.log(this.state.stock);
    }
    componentDidMount() {
        
    }
    
    render() {
    
        if(this.state.hasStock==true){
            return(
                <div>
                <div id="stockItemH1">{this.state.item.company}</div>
                <div id="stockItemH1">{this.state.item.symbol}</div>
                <div id="stockItemH1">{this.state.item.uAmount}</div>
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