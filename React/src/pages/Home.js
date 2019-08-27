import React from 'react';


export default class Home extends React.Component {

    render() {
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
                <div>TEST LIST SHARE</div>
            </form>


        );

    }
}