const builder = require('api-console-builder');

builder({
  api: '../tarrie-api.raml',
  apiType: 'RAML 1.0',
  tagName: '5.0.0-preview-1',
  destination: './api-console-bundles'
})
.then(() => console.log('Build complete <3'))
.catch((cause) => console.log('Build error <\\3', cause.message));