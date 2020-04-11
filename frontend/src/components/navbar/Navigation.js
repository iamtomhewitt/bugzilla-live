import React, { Component } from 'react';
import './Navigation.css'

export default class Navigation extends Component {

	render() {
		return (
			<ul>
				<li><a href="/home">Bugzilla Live</a></li>
				<li><a href="/lists">Lists</a></li>
				<li><a href="/config">Config</a></li>
			</ul>
		)
	}
}