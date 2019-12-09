var express = require('express')
var router = express.Router();

router.get('/', function (req, res) {
    res.send('/config is working');
});

// Get existing config
router.get('/get', function (req, res) {
    res.send('/get config');
});

// Save config
router.get('/save', function (req, res) {
    res.send('/save config');
});

module.exports = router;