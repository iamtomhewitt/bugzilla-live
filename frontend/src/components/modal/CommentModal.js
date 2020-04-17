import React from "react";
import { Button, Modal } from 'react-bootstrap';
import * as api from '../../api/api';
import './CommentModal.css'

export default class CommentModal extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			comments: []
		}
	}

	async componentDidMount() {
		const comments = await api.getBugComments(this.props.bug['id']);
		this.setState({comments})
	}

	render() {
		return (
			<>
				<Modal show={true} onHide={this.props.hideComment}>
					<Modal.Header closeButton>
						<Modal.Title id="bold">Bug {this.props.bug['id']} Comments</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						{this.state.comments.map((comment) => {
							return <div key={comment['time']}>
								<p id="name">{comment['creator']} | {new Date(comment['time']).toLocaleDateString()}</p>
								<p>{comment['text']}</p>
							</div>
						})}
					</Modal.Body>
					<Modal.Footer id="footer">
						<Button 
							variant="primary" 
							onClick={this.props.hideComment}>
							Close
						</Button>
					</Modal.Footer>
				</Modal>
			</>
		);
	}
}