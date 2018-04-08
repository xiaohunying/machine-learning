export default class FetchApi {
  static getAlgorithmSummaries() {
    return fetch('/api/algorithms').then(response => {
      if (response.status === 200) {
        return response.json();
      }
      throw Error('Invalid return code: ' + response.status);
    });
  }

  static getAlgorithm(id) {
    return fetch('/api/algorithms/' + id).then(response => {
      if (response.status === 200) {
        return response.json();
      }
      throw Error('Invalid return code: ' + response.status);
    });
  }

  get(url) {
    return fetch(url, {credentials: 'same-origin'}).then(response => {
      console.log(response);
      return response.json();
    }).catch(function(error) {
      window.location.reload();
      console.log('There is a problem with your session: ' + error.message);
    });
  }

  post(url, payload) {
    return this.withPayload(url, 'POST', payload);
  }

  put(url, payload) {
    return this.withPayload(url, 'PUT', payload);
  }

  withPayload(url, method, payload) {
    return fetch(url, {
      credentials: 'same-origin',
      method: method,
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    }).then(response => {
      return response.json();
    }).catch(function(error) {
      window.location.reload();
      console.log('There is a problem with your session: ' + error.message);
    });
  }
}
