var express = require('express')
var router = express.Router();

router.get('/', function (req, res) {
    res.send('/bugs is working');
});

// Get bugs by numbers
router.get('/numbers/:numbers', function (req, res) {
    var bugNumbers = req.params.numbers;
    res.send('Requested bugs with numbers: ' + bugNumbers);
    console.log('Bug numbers: ' + bugNumbers);
});

// Get bugs by username
router.get('/username/:username', function (req, res) {
    var username = req.params.username;
    res.send('Requested bugs with username: ' + username);
});

// Get comments for a bug
router.get('/:number/comments', function (req, res) {
    res.send('Requested comments for bug: ' + req.params.number);
});

// Change status for a bug
router.post('/:number/state/:state', function (req, res) {
    res.send('Requested to change ' + req.params.number + ' state to: ' + req.params.state);
});

module.exports = router;