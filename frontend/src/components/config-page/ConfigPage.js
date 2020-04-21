import React, { Component } from 'react';
import * as api from '../../api/api';

import './ConfigPage.css'

export default class ConfigPage extends Component {
	constructor(props) {
		super(props);
		this.state = {
			bugzillaUrl: {}
		};
	}

	async componentDidMount() {
		this.getBugzillaUrl()
	}

	async getBugzillaUrl() {
		const config = await api.getConfig();
		this.setState({
			bugzillaUrl: config.bugzillaUrl
		})
	}

	render() {
		return (
			<div id="config-container">
				<h1 id="h1">Config</h1>
				<div id="config">
					<p>This is the current config.</p>
				</div>
			</div>
		)
	}
}