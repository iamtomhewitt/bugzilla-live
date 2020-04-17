import React, { Component } from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import './Navigation.css'

export default class Navigation extends Component {
	render() {
		return (
			<Navbar bg="primary" variant="dark">
				<Navbar.Brand href="/">Bugzilla LIVE</Navbar.Brand>
				<Nav className="mr-auto">
					<Nav.Link href="/lists">Lists</Nav.Link>
					<Nav.Link href="/config">Config</Nav.Link>
				</Nav>
			</Navbar>
		)
	}
}