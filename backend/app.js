var express = require('express');
var app = express();

app.get('/', function (req, res) {
    res.send('Bugzilla backend is running! :-)');
});

var port = 3000;
app.listen(port, function () { });

console.log("Bugzilla backend listening on port: " + port);