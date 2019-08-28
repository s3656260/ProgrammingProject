import React from 'react';
export class TableData extends React.Component {	
    constructor () {
        super();
        this.state = { data: null };
      }
      
      componentDidMount () {
        this.data().then(data => {
          const self = this;
          this.setState({data: data});
        });
      }
      
      columns () {
        return [
            {key: 'symbol', label: 'Symbol'},
            {key: 'company', label: 'Company'},
            {key: 'price', label: 'Price', cell: (obj, key) => {
                return <span>{ obj[key] }</span>;
            }}
        ];
      }
    
      data () {
        return new Promise((resolve, reject) => {
          //var list = numbers();
          resolve([
            { symbol: 'Louise', company: 27, price: 'red' }
          ]);
       });
      }
    
      render () {
        if (this.state.data !== null) {
          return <JsonTable rows={this.state.data} columns={this.columns()} />;
        }
        return <div>Loading...</div>;
      }
}