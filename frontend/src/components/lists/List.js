import React from 'react';
import { Button } from 'react-bootstrap';
import './Lists.css';

export default class List extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			name: props.name,
			content: props.content,
			currentListName: props.currentListName,
		};
	}

	handleContentChange = (e) => {
		this.setState({ content: e.target.value });
	}

	updateCurrentList(name, content) {
		const list = {
			name,
			content,
		};
		this.props.updateCurrentList(list);
	}

	updateList(name, content) {
		const list = {
			name,
			content,
		};
		this.props.updateList(list);
	}

	deleteList(name) {
		this.props.deleteList(name);
	}

	render() {
		const isCurrentList = this.state.name === this.state.currentListName;
		let buttons;

		if (isCurrentList) {
			buttons = (
				<div id="buttons">
					<Button
						id="button"
						variant="primary"
						onClick={(e) => this.updateCurrentList(this.state.name, this.state.content, e)}
					>
						Save
        </Button>
				</div>
			);
		} else {
			buttons = (
				<div id="buttons">
					<Button
						id="button"
						variant="primary"
						onClick={(e) => this.updateList(this.state.name, this.state.content, e)}
					>
						Save
	                </Button>
					<Button
						id="button"
						variant="primary"
						onClick={(e) => this.updateCurrentList(this.state.name, this.state.content, e)}
					>
						Use
	                </Button>
					<Button
						id="button"
						variant="danger"
						onClick={(e) => this.deleteList(this.state.name, e)}
					>
						Delete
        </Button>
				</div>
			);
		}

		return (
			<div id="list">
				<div id="name">{this.state.name}</div>
				<div id="content">
					Content: 	<input id="input" onChange={this.handleContentChange} name="content" type="text" value={this.state.content} />
				</div>
				{buttons}
			</div>
		);
	}
}
