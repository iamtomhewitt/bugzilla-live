var express = require('express')
var router = express.Router();

router.get('/', function (req, res) {
    res.send('/list is working');
});

// Add a new list
router.get('/add', function (req, res) {
    var name = req.query.name;
    var contents = req.query.contents;

    console.log(name);
    console.log(contents);
    res.send('/add with name ' + name + ' and contents ' + contents);
});

// Modify an existing list
router.get('/modify', function (req, res) {
    var name = req.query.name;
    var remove = req.query.remove;
    var add = req.query.add

    console.log(name);
    console.log(remove);
    console.log(add);
    res.send('/modify with name ' + name + ' and add ' + add + ' and remove ' + remove);
});

// Delete a list
router.get('/delete', function (req, res) {
    res.send('/delete with name ' + req.query.name);
});

module.exports = router;