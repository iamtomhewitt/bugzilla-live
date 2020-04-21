import React from "react";

import './InformationPane.css';

export default class InformationPane extends React.Component {

	render() {
		let currentList = this.props.currentList;
		return (
			<div id="informationPane">
				{currentList != null &&
					<>
						<h4>{currentList.name}</h4>
						<ul>
							<li>1</li>
							<li>2</li>
							<li>3</li>
							<li>4</li>
							<li>5</li>
						</ul>
					</>
				}
			</div >
		);
	}
}