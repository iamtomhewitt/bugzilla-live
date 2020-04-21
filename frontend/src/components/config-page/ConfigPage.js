import React, { Component } from 'react';
import { Button } from 'react-bootstrap';
import * as api from '../../api/api';

import './ConfigPage.css'

export default class ConfigPage extends Component {
	constructor(props) {
		super(props);
		this.state = {
			bugzillaUrl: ''
		};
	}

	async componentDidMount() {
		this.getBugzillaUrl();
		this.getLists();
	}

	async getBugzillaUrl() {
		const config = await api.getConfig();
		this.setState({ bugzillaUrl: config.bugzillaUrl })
	}

	async getLists() {
		const lists = await api.getLists();
		const currentList = await api.getCurrentList();
		this.setState({
			lists,
			currentList
		})
	}

	async saveConfig() {
		await api.saveConfig(this.state.bugzillaUrl, this.state.lists, this.state.currentList);
	}

	handleContentChange = (e) => {
		this.setState({ bugzillaUrl: e.target.value })
	}

	render() {
		return (
			<div id="config-container">
				<h1 id="h1">Config</h1>
				{this.state.bugzillaUrl != null &&
					<div id="config">
						<div id="config-nameLabel">Bugzilla URL: </div>
						<div><input id="config-input" onChange={this.handleContentChange} name="content" type="text" value={this.state.bugzillaUrl} />
						</div>
					</div>
				}
				<div id="config-buttons">
					<Button
						id="config-button"
						variant="primary"
						onClick={(e) => this.saveConfig(e)}>
						Save
					</Button>
				</div>
				<div id="swagger">Backend API documentation can be found <a href="http://localhost:8080/swagger-ui.html">here</a>.</div>
			</div>
		)
	}
}