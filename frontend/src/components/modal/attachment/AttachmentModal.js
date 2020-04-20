import React from "react";
import { Button, Modal } from 'react-bootstrap';
import * as api from '../../../api/api';
import './AttachmentModal.css'

export default class AttachmentModal extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			attachments: []
		}
	}

	async componentDidMount() {
		const attachments = await api.getBugAttachments(this.props.bug['id']);
		this.setState({ attachments })
	}

	render() {
		return (
			<>
				<Modal show={true} onHide={this.props.hideAttachments}>
					<Modal.Header closeButton>
						<Modal.Title id="bold">Bug {this.props.bug['id']} Attachments</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						{this.state.attachments.map((attachment) => {
							return <div key={attachment['filename']}>
								<p id="name">{attachment['filename']}</p>
								<p>{attachment['contentType']}</p>
							</div>
						})}
						{this.state.attachments.length === 0 &&
							<div id="name"><p>There are no attachments for this bug.</p></div>
						}
					</Modal.Body>
					<Modal.Footer id="footer">
						<Button
							variant="primary"
							onClick={this.props.hideAttachments}>
							Close
						</Button>
					</Modal.Footer>
				</Modal>
			</>
		);
	}
}