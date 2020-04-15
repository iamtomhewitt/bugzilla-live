import React, { Component } from 'react';
import * as api from '../../api/api';
import './BugTable.css';

export default class BugTable extends Component {

	constructor(props) {
		super(props);
		this.state = {
			bugs: [],
			bugzillaUrl: ''
		};
		this.createRow = this.createRow.bind(this)
	}

	async componentDidMount() {
		this.getBugzillaUrl()
		this.refreshBugs()
		this.interval = setInterval(() => this.refreshBugs(), 60000);
	}

	async componentWillReceiveProps() {
		this.refreshBugs()
		this.getBugzillaUrl()
	}

	async refreshBugs() {
		const list = await api.getCurrentList();
		const bugs = await api.getBugs(list.content);
		this.setState({
			bugs: bugs
		})
	}

	async getBugzillaUrl() {
		const config = await api.getConfig();
		this.setState({
			bugzillaUrl: config.bugzillaUrl
		})
	}

	truncate(str) {
		let length = 40;
		if (str.length >= length) {
			str = str.substring(0, length) + '...';
		}
		return str;
	}

	createRow(bug) {
		return (
			<tr key={bug['id']} id={bug['priority']}>
				<td><a href={this.state.bugzillaUrl + "/show_bug.cgi?id=" + bug['id']}>{bug['id']}</a></td>
				<td>{this.truncate(bug['summary'])}</td>
				<td>{bug['status']}</td>
				<td>{bug['severity']}</td>
				<td>{bug['product']}</td>
				<td>{bug['component']}</td>
				<td>{bug['assignedTo']}</td>
				<td>{new Date(bug['lastUpdated']).toLocaleDateString()}</td>
			</tr>
		)
	}

	render() {
		return (
			<div>
				<table cellSpacing="0" cellPadding="0">
					{this.state.bugs != null &&
						<tbody>
							<tr>
								<th>Number</th>
								<th>Summary</th>
								<th>Status</th>
								<th>Severity</th>
								<th>Product</th>
								<th>Component</th>
								<th>Assignee</th>
								<th>Last Updated</th>
							</tr>
							{this.state.bugs.map((bug) => {
								return this.createRow(bug)
							})}
						</tbody>
					}
					{this.state.bugs == null &&
						<tbody>
							<tr>
								<td>No bugs! :-D</td>
							</tr>
						</tbody>
					}
				</table>
			</div>
		);
	}
}