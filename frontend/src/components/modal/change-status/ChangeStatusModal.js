import React from "react";
import { Button, Modal, DropdownButton, Dropdown } from 'react-bootstrap';
import * as api from '../../../api/api';
import './ChangeStatusModal.css'

export default class ChangeStatusModal extends React.Component {
	// Info: https://wiki.mozilla.org/BMO/UserGuide/BugStatuses
	constructor(props) {
		super(props);
		this.state = {
			status: [
				"-",
				"UNCONFIRMED",
				"RESOLVED"
			],
			resolutions: [
				"-",
				"INVALID",
				"WONTFIX",
				"INACTIVE",
				"DUPLICATE",
				"WORKSFORME",
				"MOVED"
			],
			statusButtonTitle: 'Status',
			resolutionButtonTitle: 'Resolution'
		}
		this.changeStatusButtonTitle = this.changeStatusButtonTitle.bind(this)
		this.changeResolutionButtonTitle = this.changeResolutionButtonTitle.bind(this)
	}

	async componentDidMount() {

	}

	changeStatusButtonTitle(name) {
		this.setState({
			statusButtonTitle: name
		})
	}

	changeResolutionButtonTitle(name) {
		this.setState({
			resolutionButtonTitle: name
		})
	}

	render() {
		return (
			<>
				<Modal show={true} onHide={this.props.hideChangeStatus}>
					<Modal.Header closeButton>
						<Modal.Title id="bold">Bug {this.props.bug['id']} Status</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						Current status: {this.props.bug['status']}
						<p />
						<div id="newStatus">
							<div id="label">New status: </div>
							<DropdownButton id="dropdownButton" title={this.state.statusButtonTitle}>
								{this.state.status.map((s) => {
									return <Dropdown.Item key={s} onClick={(e) => this.changeStatusButtonTitle(s, e)}>{s}</Dropdown.Item>
								})}
							</DropdownButton>

							{this.state.statusButtonTitle === "RESOLVED" &&
								<DropdownButton id="dropdownButton" title={this.state.resolutionButtonTitle}>
									{this.state.resolutions.map((r) => {
										return <Dropdown.Item key={r} onClick={(e) => this.changeResolutionButtonTitle(r, e)}>{r}</Dropdown.Item>
									})}
								</DropdownButton>
							}
						</div>
					</Modal.Body>

					<Modal.Footer id="footer">
						<Button
							variant="primary"
							onClick={this.props.hideChangeStatus}>
							Change
						</Button>
					</Modal.Footer>
				</Modal>
			</>
		);
	}
}