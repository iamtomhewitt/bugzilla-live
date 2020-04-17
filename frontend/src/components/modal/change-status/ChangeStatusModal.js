import React from "react";
import { Button, Modal } from 'react-bootstrap';
import * as api from '../../../api/api';
import './ChangeStatusModal.css'

export default class ChangeStatusModal extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
		}
	}

	async componentDidMount() {

	}

	render() {
		return (
			<>
				<Modal show={true} onHide={this.props.hideChangeStatus}>
					<Modal.Header closeButton>
						<Modal.Title id="bold">Status</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						Status
					</Modal.Body>
					<Modal.Footer id="footer">
						<Button
							variant="primary"
							onClick={this.props.hideChangeStatus}>
							Close
						</Button>
					</Modal.Footer>
				</Modal>
			</>
		);
	}
}