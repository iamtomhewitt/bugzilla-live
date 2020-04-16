import React, { Component } from 'react';
import { Link } from "react-router-dom";
import * as api from '../../api/api';

import './Lists.css'

export default class Lists extends Component {
	constructor(props) {
		super(props);
		this.state = {
			currentList: {},
			lists: []
		};
		this.createRow = this.createRow.bind(this);
		this.updateCurrentList = this.updateCurrentList.bind(this);
		this.deleteList = this.deleteList.bind(this);
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

	async deleteList(name) {
		const response = await api.deleteList(name);
		this.setState({
			lists: response.lists
		})
	}

	createRow(list) {
		return (
			<div key={list['name']} id="list">
				<div id="name">Name: {list['name']}</div>
				<div id="content">Content: <input value={list['content']}/></div>
				
				<div id="buttons">
					<button id="button">Edit</button>
					<button id="button" onClick={(e) => this.updateCurrentList(list, e)}>Use</button>
					{list['name'] !== this.state.currentList['name'] &&
						<button id="button" onClick={(e) => this.deleteList(list['name'], e)}>Delete</button>
					}
				</div>
			</div>
		)
	}

	render() {
		return (
			<div id="container">
				<h1>Lists</h1>

				<h2>Current List</h2>
				<p id="note">This is the current list being used to display bugs in the table.</p>
				{this.createRow(this.state.currentList)}

				<h2>Available Lists</h2>
				<div id="lists">
					{this.state.lists.map((list) => {
						if (list['name'] !== this.state.currentList['name']) {
							return this.createRow(list)
						}
						return null;
					})}
				</div>

				<button id="createButton"><Link to="/createList" style={{ textDecoration: 'none', color: 'white' }}>Create List</Link></button>
			</div>
		)
	}
}