import React, { Component } from 'react';
import * as api from '../../api/api';
import CommentModal from '../modal/comment/CommentModal';
import './BugTable.css';
import AttachmentModal from '../modal/attachment/AttachmentModal';

export default class BugTable extends Component {
    constructor(props) {
        super(props);
        this.state = {
            bugs: [],
            bugzillaUrl: '',
            currentList: null,
            loading: true,
        };
        this.createRow = this.createRow.bind(this);
        this.removeBug = this.removeBug.bind(this);
        this.showComment = this.showComment.bind(this);
        this.hideComment = this.hideComment.bind(this);
        this.showAttachments = this.showAttachments.bind(this);
        this.hideAttachments = this.hideAttachments.bind(this);
    }

    async componentDidMount() {
        this.getBugzillaUrl();
        this.refreshBugs();
        this.interval = setInterval(() => this.refreshBugs(), 300000);
    }

    async componentWillReceiveProps() {
        this.refreshBugs();
        this.getBugzillaUrl();
    }

    async getBugzillaUrl() {
        const config = await api.getConfig();
        this.setState({
            bugzillaUrl: config.bugzillaUrl,
        });
    }

    async refreshBugs() {
        this.setState({ loading: true });
        const currentList = await api.getCurrentList();
        const bugs = await api.getBugs(currentList.content);
        this.setState({
            bugs,
            currentList,
            loading: false,
        });
    }

    async removeBug(number) {
        const { bugs, currentList } = this.state;

        if (bugs.length <= 1) {
            return;
        }

        const currentListContent = currentList.content;
        const newContent = currentListContent.replace(number, '');

        const list = {
            name: currentList.name,
            content: newContent,
        };

        await api.updateCurrentList(list);
        this.refreshBugs();
    }

    showComment(bug) {
        this.setState({
            showCommentModal: true,
            modalData: bug,
        });
    }

    hideComment() {
        this.setState({ showCommentModal: false });
    }

    showAttachments(bug) {
        this.setState({
            showAttachmentModal: true,
            modalData: bug,
        });
    }

    hideAttachments() {
        this.setState({ showAttachmentModal: false });
    }

    truncate(str) {
        const length = 40;
        let newStr = str;
        if (str.length >= length) {
            newStr = `${str.substring(0, length)}...`;
        }
        return newStr;
    }

    createRow(bug) {
        const { bugzillaUrl } = this.state;
        return (
            <tr key={bug.id} id={bug.severity}>
                <td><a href={`${bugzillaUrl}/show_bug.cgi?id=${bug.id}`}>{bug.id}</a></td>
                <td>{this.truncate(bug.summary)}</td>
                <td>{bug.status}</td>
                <td>{bug.severity}</td>
                <td>{bug.product}</td>
                <td>{bug.component}</td>
                <td>{bug.assignedTo}</td>
                <td>{new Date(bug.lastUpdated).toLocaleDateString()}</td>
                <td>
                    <button type="button" onClick={(e) => this.removeBug(bug.id, e)}><span role="img" aria-label="cross">❌</span></button>
                    <button type="button" onClick={(e) => this.showComment(bug, e)}><span role="img" aria-label="speech balloon">💬</span></button>
                    <button type="button" onClick={(e) => this.showAttachments(bug, e)}><span role="img" aria-label="paperclip">📎</span></button>
                </td>
            </tr>
        );
    }

    render() {
        const {
            bugs, loading, showCommentModal, showAttachmentModal, modalData,
        } = this.state;
        return (
            <div id="bugTable">
                <table cellSpacing="0" cellPadding="0">
                    {bugs != null && !loading
                        && (
                            <tbody>
                                <tr>
                                    <th>Number</th>
                                    <th>Summary</th>
                                    <th>Status</th>
                                    <th>Severity</th>
                                    <th>Product</th>
                                    <th>Component</th>
                                    <th>Assignee</th>
                                    <th>Last Updated</th>
                                    <th aria-label="none" />
                                </tr>
                                {bugs.map((bug) => this.createRow(bug))}
                            </tbody>
                        )}
                    {bugs == null && !loading
                        && (
                            <tbody>
                                <tr>
                                    <td>No bugs! :-D</td>
                                </tr>
                            </tbody>
                        )}
                </table>
                {showCommentModal
                    && <CommentModal bug={modalData} hideComment={this.hideComment} />}
                {showAttachmentModal
                    && <AttachmentModal bug={modalData} hideAttachments={this.hideAttachments} />}
                {loading
                    && <p id="loading">Loading...</p>}
            </div>
        );
    }
}
