import React from "react";

import './InformationPane.css';

export default class InformationPane extends React.Component {
	constructor(props) {
		super(props);
		this.countStatuses = this.countStatuses.bind(this)
		this.countSeverities = this.countSeverities.bind(this)
	}

	capitalizeFirstLetter(string) {
		return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
	}

	countStatuses(bugs) {
		let map = {}

		for (let i = 0; i < bugs.length; i++) {
			let status = this.capitalizeFirstLetter(bugs[i]['status']);
			map[status] = map[status] ? map[status] + 1 : 1;
		}
		return map;
	}

	countSeverities(bugs) {
		let map = {}

		for (let i = 0; i < bugs.length; i++) {
			let status = this.capitalizeFirstLetter(bugs[i]['severity']);
			map[status] = map[status] ? map[status] + 1 : 1;
		}
		return map;
	}

	render() {
		let currentList = this.props.currentList;
		let bugs = this.props.bugs;

		return (
			<div id="informationPane">
				{currentList != null && bugs != null &&
					<>
						<h4 id="header">{currentList.name}</h4>
						<ul id="informationPaneList">
							{Object.entries(this.countStatuses(bugs)).map(([label, value]) => {
								return <li key={label}>{label}: {value}</li>
							})}
							<hr></hr>
							{Object.entries(this.countSeverities(bugs)).map(([label, value]) => {
								return <li key={label}>{label}: {value}</li>
							})}
						</ul>
					</>
				}
			</div >
		);
	}
}