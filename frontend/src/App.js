import React from 'react';
import Routes from './Routes';
import Navigation from './components/navbar/Navigation';

export default class App extends React.Component {
	render() {
		return (
			<div>
				<Navigation/>
				<Routes/>
			</div>
		);
	}
}