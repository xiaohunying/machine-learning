import React from 'react';
import {Navbar} from 'react-bootstrap';
import {Link, Route, withRouter} from 'react-router-dom';
import HomePage from './home/HomePage';
import AlgorithmPage from './algorithm/AlgorithmPage';

class App extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {};
  }

  // noop(e) {
  //   e.preventDefault();
  // }

  render() {
    return (
      <div>
        <Navbar className="navbar-fixed-top" fluid>
          <div className="navbar-header">
            <Link to="/" className="navbar-brand">
              <strong>Ayawala</strong>
            </Link>
          </div>
        </Navbar>
        <div>
          <Route exact path="/" component={HomePage} />
          <Route path="/algorithms/:id" component={AlgorithmPage} />
        </div>
      </div>
    );
  }
}

export default withRouter(App);
