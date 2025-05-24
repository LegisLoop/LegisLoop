import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import App from '../App.js';
import TeamMembersList from './TeamMembersList.js';

function Root() {
    return (
        <Router>
            <Switch>
                <Route exact path="/" component={App} />
            </Switch>
        </Router>
    );
}

export default Root;