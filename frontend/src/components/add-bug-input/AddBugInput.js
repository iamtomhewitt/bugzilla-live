import React from 'react'
import * as api from '../../api/api';
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

    async updateBugNumbers() {
		const currentList = await api.getCurrentList();
		const newContent = currentList.content + ',' + this.state.input;
		await api.updateCurrentList({name: currentList.name, content: newContent})
        this.props.updateBugs()
    }

    render() {
        return (
            <div id='addBugContainer'>
                <input id="addBugInput" type="number" onChange={this.handleChange}/>
                <button id="addBugButton" onClick={this.updateBugNumbers}>Add</button>
            </div>
        );
    }
}