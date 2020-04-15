import React, { Component } from 'react';
import * as api from '../../api/api';
import { Link } from "react-router-dom";

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
					<button id="button"><Link to="/editList" style={{ textDecoration: 'none', color: 'white' }}>Edit</Link></button>
					<button id="button"><Link to="/useList" style={{ textDecoration: 'none', color: 'white' }}>Use</Link></button>
					<button id="button"><Link to="/deleteList" style={{ textDecoration: 'none', color: 'white' }}>Delete</Link></button>
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
				{this.state.lists.map((list) => {
					return this.createRow(list)
				})}

				<button>Create List</button>
			</div>
		)
	}
}