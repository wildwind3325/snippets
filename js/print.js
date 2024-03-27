var html_to_pdf = require('html-pdf-node');
var ipp = require('ipp');

let options = { format: 'A4' };
let file = { content: '<h1>Welcome to html-pdf-node</h1>' };
html_to_pdf.generatePdf(file, options).then(data => {
  let printer = ipp.Printer('http://192.168.201.250:631/ipp/printer');
  var msg = {
    'operation-attributes-tag': {
      'requesting-user-name': 'administrator',
      'job-name': "Printing Job",
      'document-format': 'application/pdf'
    },
    data: data
  };
  printer.execute('Print-Job', msg, function (err, res) {
    console.log(res);
  });
});