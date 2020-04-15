import React, { Component } from 'react';
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
	}

	async componentDidMount() {
		this.getLists()
	}

	async getLists() {
		const lists = await api.getLists();
		const currentList = await api.getCurrentList();
		this.setState({
			lists: lists,
			currentList
		})
	}

	createRow(list) {
		return (
			<div key={list['name']} id="list">
				<div id="label">Name: </div>
				<div id="value">{list['name']}</div>
				<br />
				<div id="label">Content: </div>
				<div id="value">{list['content']}</div>
				<div>
					<button id="button">Edit</button>
					<button id="button">Use</button>
					<button id="button">Delete</button>
				</div>
			</div>
		)
	}

	render() {
		return (
			<div id="container">
				<h1>Lists</h1>
				<h2>Current List</h2>
				{this.createRow(this.state.currentList)}

				<h2>Other Lists</h2>
				{this.state.lists.map((list) => {
					return this.createRow(list)
				})}
			</div>
		)
	}
}