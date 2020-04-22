import React from 'react';
import { Button, Modal } from 'react-bootstrap';
import * as api from '../../../api/api';

import './AttachmentModal.css';

export default class AttachmentModal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            attachments: [],
            loading: true,
        };
    }

    async componentDidMount() {
        const { bug } = this.props;
        const attachments = await api.getBugAttachments(bug.id);
        const config = await api.getConfig();
        this.setState({
            attachments,
            bugzillaUrl: config.bugzillaUrl,
            loading: false,
        });
    }

    render() {
        const { hideAttachments, bug } = this.props;
        const { attachments, bugzillaUrl, loading } = this.state;
        return (
            <>
                <Modal show onHide={hideAttachments}>
                    <Modal.Header closeButton>
                        <Modal.Title id="bold">
                            Bug
                            {bug.id}
                            {' '}
                            Attachments
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {attachments.map((attachment) => (
                            <div key={attachment.filename}>
                                <a target="_blank" rel="noopener noreferrer" href={`${bugzillaUrl}attachment.cgi?id=${attachment.id}`}>{attachment.filename}</a>
                            </div>
                        ))}
                        {attachments.length === 0 && loading === false
                            && <div id="noAttachments"><p>There are no attachments for this bug.</p></div>}
                        {loading === true
                            && <div id="loading"><p>Loading...</p></div>}
                    </Modal.Body>
                    <Modal.Footer id="footer">
                        <Button
                            variant="primary"
                            onClick={hideAttachments}
                        >
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}
