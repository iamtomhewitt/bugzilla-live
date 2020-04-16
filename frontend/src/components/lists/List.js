import React from 'react';
import './Lists.css';

export default class List extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			name: props.name,
			content: props.content,
			currentListName: props.currentListName
		};
	}

	handleNameChange = (e) => {
		this.setState({ name: e.target.value })
	}

	handleContentChange = (e) => {
		this.setState({ content: e.target.value })
	}

	updateCurrentList(name, content) {
		let list = {
			name,
			content
		}
		this.props.updateCurrentList(list);
	}

	updateList(name, content) {
		let list = {
			name,
			content
		}
		this.props.updateList(list);
	}

	deleteList(name) {
		this.props.deleteList(name);
	}

	render() {
		const isCurrentList = this.state.name === this.state.currentListName;
		let buttons;

		if (isCurrentList) {
			buttons =
				<div id="buttons">
					<button id="button" onClick={(e) => this.updateList(this.state.name, this.state.content, e)}>Save</button>
				</div>
		}
		else {
			buttons =
				<div id="buttons">
					<button id="button" onClick={(e) => this.updateList(this.state.name, this.state.content, e)}>Save</button>
					<button id="button" onClick={(e) => this.updateCurrentList(this.state.name, this.state.content, e)}>Use</button>
					<button id="button" onClick={(e) => this.deleteList(this.state.name, e)}>Delete</button>
				</div>
		}

		return (
			<div id="list">
				<div id="name">Name: 		<input onChange={this.handleNameChange} name="name" type="text" value={this.state.name} /></div>
				<div id="content">Content: 	<input onChange={this.handleContentChange} name="content" type="text" value={this.state.content} /></div>
				{buttons}
			</div>
		)
	}
}