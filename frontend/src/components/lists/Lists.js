import React, { Component } from 'react';
import List from './List';
import * as api from '../../api/api';

import './Lists.css'
import CreateList from './CreateList';

export default class Lists extends Component {
	constructor(props) {
		super(props);
		this.state = {
			currentList: {},
			lists: []
		};
		this.createTile = this.createTile.bind(this);
		this.updateList = this.updateList.bind(this);
		this.updateCurrentList = this.updateCurrentList.bind(this);
		this.deleteList = this.deleteList.bind(this);
		this.getLists = this.getLists.bind(this);
	}

	async componentDidMount() {
		this.getLists()
	}

	async getLists() {
		const lists = await api.getLists();
		const currentList = await api.getCurrentList();
		this.setState({
			lists,
			currentList
		})
	}

	async updateCurrentList(list) {
		const response = await api.updateCurrentList(list);
		this.setState({ currentList: response.currentList })
	}

	async updateList(list) {
		const response = await api.updateList(list);
		this.setState({ lists: response.lists })
	}

	async deleteList(name) {
		const response = await api.deleteList(name);
		this.setState({ lists: response.lists })
	}

	createTile(list) {
		return (
			<List
				key={list['name']}
				name={list['name']}
				content={list['content']}
				currentListName={this.state.currentList.name}
				deleteList={this.deleteList}
				updateList={this.updateList}
				updateCurrentList={this.updateCurrentList}
			/>
		)
	}

	render() {
		return (
			<div id="container">
				<h1>Lists</h1>

				<div id="currentAndCreate">
					<div id="current">
						<h2>Current List</h2>
						<p id="note">This is the current list being used to display bugs in the table.</p>
						{this.createTile(this.state.currentList)}
					</div>

					<div id="create">
						<h2>Create List</h2>
						<p id="note">Create a new list here.</p>
						<CreateList getLists={this.getLists}/>
					</div>
				</div>

				<br></br>

				<h2>Available Lists</h2>
				<div id="lists">
					{this.state.lists.map((list) => {
						if (list['name'] !== this.state.currentList['name']) {
							return this.createTile(list)
						}
						return null;
					})}
				</div>
			</div>
		)
	}
}