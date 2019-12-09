var express = require('express');
var app = express();
var bodyParser = require('body-parser')

var bugRoutes = require('./routes/bug-routes')

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', function (req, res) {
    res.send('Bugzilla backend is running! :-)');
});

app.use('/bugs', bugRoutes);

var port = 3000;
app.listen(port, function () { });

console.log("Bugzilla backend listening on port: " + port);