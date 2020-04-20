import React from "react";
import { Button, Modal } from 'react-bootstrap';
import * as api from '../../../api/api';

import './AttachmentModal.css'

export default class AttachmentModal extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			attachments: [],
			loading: true
		}
	}

	async componentDidMount() {
		const attachments = await api.getBugAttachments(this.props.bug['id']);
		const config = await api.getConfig();
		this.setState({
			attachments,
			bugzillaUrl: config.bugzillaUrl,
			loading: false
		})
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
								<a target="_blank" rel="noopener noreferrer" href={this.state.bugzillaUrl + "attachment.cgi?id=" + attachment['id']}>{attachment['filename']}</a>
							</div>
						})}
						{this.state.attachments.length === 0 && this.state.loading === false &&
							<div id="noAttachments"><p>There are no attachments for this bug.</p></div>
						}
						{this.state.loading === true &&
							<div id="loading"><p>Loading...</p></div>
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