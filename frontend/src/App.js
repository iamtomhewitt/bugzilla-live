import React from 'react';
import BugTable from './components/bug-table/BugTable';
import AddBugInput from './components/add-bug-input/AddBugInput';
import Navigation from './components/navbar/Navigation';

import './App.css';

export default class App extends React.Component {

	constructor() {
		super();
		this.state = { 
			bugNumbers: [1605238,12345,23456] 
		}
		this.updateBugNumbers = this.updateBugNumbers.bind(this)
	}

	updateBugNumbers(numbers) {
		let currentNumbers = this.state.bugNumbers
		currentNumbers.push(numbers)

		this.setState({
			bugNumbers: currentNumbers
		})
	}

	render() {
		return (
			<div className='App'>
				<Navigation/>
				<AddBugInput bugNumbers={this.state.bugNumbers} updateBugNumbers={this.updateBugNumbers} />
				<BugTable bugNumbers={this.state.bugNumbers} />
			</div>
		);
	}
}