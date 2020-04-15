import React from 'react';
import Routes from './Routes';
import Navigation from './components/navbar/Navigation';

import './App.css';

export default class App extends React.Component {

	render() {
		return (
			<div className='App'>
				<Navigation/>
				<Routes/>
			</div>
		);
	}
}