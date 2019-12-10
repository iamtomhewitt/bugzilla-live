var express = require('express')
var router = express.Router();
var fs = require('fs');
var path = require('path')

var configFile = path.join(__dirname, '..', 'config', 'config.json');

router.get('/', function (req, res) {
    res.status(200).send('OK');
});

// Get existing config
router.get('/get', function (req, res) {
	let contents = fs.readFileSync(configFile, 'utf-8');
    res.send(contents);
});

// Save config
router.get('/save', function (req, res) {
    res.send('/save config');
});

module.exports = router;