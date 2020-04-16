import React from 'react';
import * as api from '../../api/api';

import './Lists.css';

export default class CreateList extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			name: 'Your list name',
			content: 'Your content'
		};
		this.save = this.save.bind(this);
	}

	handleNameChange = (e) => {
		this.setState({ name: e.target.value })
	}

	handleContentChange = (e) => {
		this.setState({ content: e.target.value })
	}

	async save() {
		let list = {
			name: this.state.name,
			content: this.state.content,
			current: false
		}
		await api.saveList(list);
		this.setState({
			name: 'Your list name',
			content: 'Your content'
		});
		this.props.getLists();
	}

	render() {
		return (
			<div id="createList">
				<div id="name"><input id="input" onChange={this.handleNameChange} name="content" type="text" value={this.state.name} /></div>
				<div id="content"><input id="input" onChange={this.handleContentChange} name="content" type="text" value={this.state.content} /></div>
				<div id="createListSaveButton">
					<button id="button" onClick={(e) => this.save()}>Save</button>
				</div>
			</div>
		)
	}
}