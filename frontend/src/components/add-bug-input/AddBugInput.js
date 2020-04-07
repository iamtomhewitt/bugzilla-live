import React from 'react'

import './AddBugInput.css'

export default class AddBugInput extends React.Component {
    constructor(props) {
        super(props);
        
        this.updateBugNumbers = this.updateBugNumbers.bind(this)
        this.handleChange = this.handleChange.bind(this)
    }

    handleChange = event => {
        this.setState({
            input: event.target.value
        });
    }

    updateBugNumbers() {
        this.props.updateBugNumbers(this.state.input)
    }

    render() {
        return (
            <div className='AddBugInput'>
                <input onChange={this.handleChange}/>
				&nbsp;
				&nbsp;
                <button onClick={this.updateBugNumbers}>Add</button>
            </div>
        );
    }
}