var express = require('express');
var app = express();
var bodyParser = require('body-parser')

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Home
app.get('/', function (req, res) {
    res.send('Bugzilla backend is running! :-)');
});

// BUGS
// Get bugs by numbers
app.get('/bugs/numbers/:numbers', function (req, res) {
    var bugNumbers = req.params.numbers;
    res.send('Requested bugs with numbers: ' + bugNumbers);
    console.log('Bug numbers: ' + bugNumbers);
});

// Get bugs by username
app.get('/bugs/username/:username', function (req, res) {
    var username = req.params.username;
    res.send('Requested bugs with username: ' + username);
});

// Get comments for a bug
app.get('/bug/:number/comments', function (req, res) {
    res.send('Requested comments for bug: ' + req.params.number);
});

// Change status for a bug
app.post('/bug/:number/state/:state', function (req, res) {
    res.send('Requested to change ' + req.params.number + ' state to: ' + req.params.state);
});


var port = 3000;
app.listen(port, function () { });

console.log("Bugzilla backend listening on port: " + port);