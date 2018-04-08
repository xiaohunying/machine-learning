import React from 'react';
import FetchApi from '../../api/FetchApi';
import MathJax from 'react-mathjax-preview';
import Prism from 'prismjs';
import 'prismjs/plugins/line-numbers/prism-line-numbers.js';
import 'prismjs/plugins/remove-initial-line-feed/prism-remove-initial-line-feed.js';
import 'prismjs/plugins/line-numbers/prism-line-numbers.css';
import 'prismjs/plugins/line-highlight/prism-line-highlight.js';
import 'prismjs/themes/prism-solarizedlight.css';
import 'prismjs/components/prism-java.js';
import 'prismjs/components/prism-yaml.js';
import 'prismjs/components/prism-bash.js';
import 'prismjs/components/prism-properties.js';
import 'prismjs/components/prism-clike.js';
import 'prismjs/components/prism-batch.js';
import 'prismjs/components/prism-css.js';
import 'prismjs/components/prism-javascript.js';
import 'prismjs/components/prism-markdown.js';
import 'prismjs/components/prism-markup.js';
import 'prismjs/components/prism-nginx.js';
import 'prismjs/components/prism-python.js';
import 'prismjs/components/prism-sass.js';
import 'prismjs/components/prism-sql.js';
import 'prismjs/components/prism-typescript.js';
import 'prismjs/components/prism-json.js';
import 'prismjs/components/prism-jsx.js';
import 'prismjs/components/prism-groovy.js';

export default class AlgorithmPage extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      algorithmId: props.match.params.id,
      algorithm: {}
    };
  }

  componentDidMount() {
    this.getAlgorithm();
  }

  getAlgorithm() {
    FetchApi.getAlgorithm(this.state.algorithmId).then(response => {
      this.setState({algorithm: response});
      setTimeout(() => {
        Prism.highlightAll();
        if (window.location.hash) {
          document.querySelector(`${window.location.hash}`).scrollIntoView();
        }
      }, 250);
    });
  }

  render() {
    return (
      <div className="center">
        <div className="markdown">
          <MathJax math={this.state.algorithm.htmlContent} />
        </div>
      </div>
    );
  }
}
