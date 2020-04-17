import React, { Component } from 'react';
import * as api from '../../api/api';
import CommentModal from '../Modal/CommentModal';
import './BugTable.css';

export default class BugTable extends Component {

	constructor(props) {
		super(props);
		this.state = {
			bugs: [],
			bugzillaUrl: '',
			currentList: null
		};
		this.createRow = this.createRow.bind(this)
		this.removeBug = this.removeBug.bind(this)
		this.showComment = this.showComment.bind(this)
		this.hideComment = this.hideComment.bind(this)
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
		const currentList = await api.getCurrentList();
		const bugs = await api.getBugs(currentList.content);
		this.setState({
			bugs,
			currentList
		})
	}

	async getBugzillaUrl() {
		const config = await api.getConfig();
		this.setState({
			bugzillaUrl: config.bugzillaUrl
		})
	}

	async removeBug(number) {
		if (this.state.bugs.length <= 1) {
			return;
		}

		let currentListContent = this.state.currentList.content;
		let newContent = currentListContent.replace(number, '');

		let list = {
			name: this.state.currentList.name,
			content: newContent
		}

		await api.updateCurrentList(list);
		this.refreshBugs();
	}

	showComment(bug) {
		this.setState({
			showModal: true,
			modalData: bug
		})
	}

	hideComment() {
		this.setState({showModal: false})
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
				<td>
					<button onClick={(e) => this.removeBug(bug['id'], e)}><span role="img" aria-label="cross">❌</span></button>
					<button onClick={(e) => this.showComment(bug, e)}><span role="img" aria-label="speech balloon">💬</span></button>
				</td>
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
								<th></th>
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
				{this.state.showModal &&
					<CommentModal bug={this.state.modalData} hideComment={this.hideComment}/>
				}
			</div>
		);
	}
}