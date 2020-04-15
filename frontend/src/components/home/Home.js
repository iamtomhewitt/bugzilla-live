import React from "react";
import AddBugInput from "../add-bug-input/AddBugInput";
import BugTable from "../bug-table/BugTable";

export default class Home extends React.Component {
	constructor() {
		super();
		this.state = {
			bugNumbers: [1605238, 12345, 23456]
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
			<div>
				<AddBugInput bugNumbers={this.state.bugNumbers} updateBugNumbers={this.updateBugNumbers} />
				<BugTable bugNumbers={this.state.bugNumbers} />
			</div >
		);
	}
}