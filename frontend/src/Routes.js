import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Home from './components/home/Home';
import PageNotFound from './components/page-not-found/PageNotFound';
import Lists from './components/lists/Lists';
import ConfigPage from './components/config-page/ConfigPage';

export default () => (
    <Switch>
        <Route path="/" exact component={Home} />
        <Route path="/lists" exact component={Lists} />
        <Route path="/config" exact component={ConfigPage} />

        { /* Catch all unmatched routes */}
        <Route component={PageNotFound} />
    </Switch>
);
