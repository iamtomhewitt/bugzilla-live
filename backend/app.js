var express = require('express');
var app = express();
var bodyParser = require('body-parser')

var bugRoutes = require('./routes/bug-routes')
var listRoutes = require('./routes/list-routes')
var configRoutes = require('./routes/config-routes')

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', function (req, res) {
    res.status(200).send('SERVER OK');
});

app.use('/bugs', bugRoutes);
app.use('/list', listRoutes);
app.use('/config', configRoutes);

var port = 3001;
app.listen(port, function () { });

module.exports = app;