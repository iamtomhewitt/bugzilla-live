import React from 'react';

import './InformationPane.css';

export default class InformationPane extends React.Component {
    constructor(props) {
        super(props);
        this.countStatuses = this.countStatuses.bind(this);
        this.countSeverities = this.countSeverities.bind(this);
    }

    capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    }

    countStatuses(bugs) {
        const map = {};

        for (let i = 0; i < bugs.length; i += 1) {
            const status = this.capitalizeFirstLetter(bugs[i].status);
            map[status] = map[status] ? map[status] + 1 : 1;
        }
        return map;
    }

    countSeverities(bugs) {
        const map = {};

        for (let i = 0; i < bugs.length; i += 1) {
            const status = this.capitalizeFirstLetter(bugs[i].severity);
            map[status] = map[status] ? map[status] + 1 : 1;
        }
        return map;
    }

    render() {
        const { currentList, bugs } = this.props;

        return (
            <div id="informationPane">
                {currentList != null && bugs != null
                    && (
                        <>
                            <h4 id="header">{currentList.name}</h4>
                            <ul id="informationPaneList">
                                {Object.entries(this.countStatuses(bugs)).map(([label, value]) => (
                                    <li key={label}>
                                        {label}
                                        :
                                        {' '}
                                        {value}
                                    </li>
                                ))}
                                <hr />
                                {Object.entries(this.countSeverities(bugs)).map(([label, value]) => (
                                    <li key={label}>
                                        {label}
                                        :
                                        {' '}
                                        {value}
                                    </li>
                                ))}
                            </ul>
                        </>
                    )}
            </div>
        );
    }
}
