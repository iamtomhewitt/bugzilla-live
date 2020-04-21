import React from "react";

import './InformationPane.css';

export default class InformationPane extends React.Component {
	constructor(props) {
		super(props);
		this.countOccurences = this.countOccurences.bind(this)
	}

	countOccurences(bugs) {
		let map = {}

		for (let i = 0; i < bugs.length; i++) {
			let status = bugs[i]['status'];
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
						<ul>
							{Object.entries(this.countOccurences(bugs)).map(([label, value]) => {
								return <li key={label}>{label}: {value}</li>
							})}
						</ul>
					</>
				}
			</div >
		);
	}
}