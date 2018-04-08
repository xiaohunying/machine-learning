import React from 'react';
import {Link} from 'react-router-dom';
import FetchApi from '../../api/FetchApi';

export default class HomePage extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      algorithmSummaries: []
    };
  }

  componentDidMount() {
    this.getAlgorithmSummaries();
  }

  getAlgorithmSummaries() {
    FetchApi.getAlgorithmSummaries().then(response => {
      console.info(response);
      this.setState({algorithmSummaries: response});
    });
  }

  renderCards() {
    return (
      <div className="card-catalog">
        {this.state.algorithmSummaries.map((algorithmSummary, index) => (
          <div className="card" key={algorithmSummary.id}>
            <Link className="card-title" to={'/algorithms/' + algorithmSummary.id}>
              {algorithmSummary.metadata.title}
            </Link>
            <div className="card-body">
              <p>{algorithmSummary.metadata.summary}</p>
              <div className="tags">
                {algorithmSummary.metadata.tags.map((tag, index) => (
                  <Link key={index} to="/" className="btn btn-default btn-xs tag">{tag}</Link>
                ))}
              </div>
            </div>
          </div>
        ))}
      </div>
    );
  }

  render() {
    return (
      <div>
        <section className="banner text-center">
          <div className="center-block">
            <h1 className="font-weight-light margin-bottom">Machine Learning Algorithms</h1>
            <p>A catalog of machine learning and deep learning algorithms.</p>
          </div>
        </section>

        <section>
          <div className="center-block">
            {this.renderCards()}
          </div>
        </section>
      </div>
    );
  };
}
