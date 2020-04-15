import React from "react";
import { Route, Switch } from 'react-router-dom';
import Home from "./components/home/Home";
import PageNotFound from "./components/page-not-found/PageNotFound";

export default () =>
	<Switch>
		<Route path="/" exact component={Home} />

		{ /* Catch all unmatched routes */}
		<Route component={PageNotFound} />
	</Switch>;