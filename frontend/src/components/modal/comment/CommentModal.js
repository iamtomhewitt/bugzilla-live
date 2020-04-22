import React from 'react';
import { Button, Modal } from 'react-bootstrap';
import * as api from '../../../api/api';
import './CommentModal.css';

export default class CommentModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            comments: [],
        };
    }

    async componentDidMount() {
        const { bug } = this.props;
        const comments = await api.getBugComments(bug.id);
        this.setState({ comments });
    }

    render() {
        const { hideComment, bug } = this.props;
        const { comments } = this.state;
        return (
            <>
                <Modal show onHide={hideComment}>
                    <Modal.Header closeButton>
                        <Modal.Title id="bold">
                            Bug
                            {bug.id}
                            {' '}
                            Comments
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {comments.map((comment) => (
                            <div key={comment.time}>
                                <p id="name">
                                    {comment.creator}
                                    {' '}
                                    |
                                    {' '}
                                    {new Date(comment.time).toLocaleDateString()}
                                </p>
                                <p>{comment.text}</p>
                            </div>
                        ))}
                    </Modal.Body>
                    <Modal.Footer id="footer">
                        <Button
                            variant="primary"
                            onClick={hideComment}
                        >
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}
