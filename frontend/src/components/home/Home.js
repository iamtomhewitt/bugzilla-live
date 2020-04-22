import React from 'react';
import AddBugInput from '../add-bug-input/AddBugInput';
import BugTable from '../bug-table/BugTable';
import * as api from '../../api/api';
import InformationPane from '../information-pane/InformationPane';

import './Home.css';

export default class Home extends React.Component {
    constructor() {
        super();
        this.state = {
            bugs: null,
            currentList: null,
        };
        this.updateBugs = this.updateBugs.bind(this);
    }

    async componentDidMount() {
        this.updateBugs();
    }

    async updateBugs() {
        const currentList = await api.getCurrentList();
        const bugs = await api.getBugs(currentList.content);
        this.setState({
            bugs,
            currentList,
        });
    }

    render() {
        const { currentList, bugs } = this.state;
        return (
            <div>
                <AddBugInput updateBugs={this.updateBugs} />
                <div id="main">
                    <InformationPane currentList={currentList} bugs={bugs} />
                    <BugTable />
                </div>
            </div>
        );
    }
}
