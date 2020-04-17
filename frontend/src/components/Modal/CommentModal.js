import React from "react";
import { Button, Modal } from 'react-bootstrap';

export default function CommentModal(props) {
	return (
		<>
			<Modal show={true} onHide={props.hideComment}>
				<Modal.Header closeButton>
					<Modal.Title>Bug {props.bug['id']}</Modal.Title>
				</Modal.Header>
				<Modal.Body>{props.bug['summary']}</Modal.Body>
				<Modal.Footer>
					<Button variant="secondary" onClick={props.hideComment}>
						Close
					</Button>
				</Modal.Footer>
			</Modal>
		</>
	);
}