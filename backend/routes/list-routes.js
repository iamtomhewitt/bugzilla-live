var express = require('express')
var router = express.Router();
var fs = require('fs');
var path = require('path')

var listFolder = path.join(__dirname, '..', 'config', 'bug-lists', '/');

router.get('/', function (req, res) {
    res.send('/list is working');
});

// Add a new list
router.get('/add', function (req, res) {
    var name = req.query.name;
	var contents = req.query.contents;
	let response;

	if (!name || !contents) {
		let error = {
			"title": "Missing parameters",
			"message": "File name or file contents are missing."
		}
		response = failure(error)
		res.send(response).json();
		return;
	}

	let filename = listFolder + name + '.bugList';

	fs.writeFile(filename, contents, function (err) {
        if (err) {
			let error = {
				"title": "Error creating file",
				"message": err.message
			}
			response = failure(error)
            res.send(response).json();
        }
        else {
			response = success("List created.")
            res.send(response).json();
        }
    })
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
	var name = req.query.name;
	let response;

	if (!name) {
		let error = {
			"title": "Missing parameters",
			"message": "File name or file contents are missing."
		}
		response = failure(error)
		res.send(response).json();
		return;
	}

	let filename = listFolder + name + '.bugList';

	fs.unlink(filename, function (err) {
        if (err) {
			let error = {
				"title": "Error deleting file",
				"message": err.message
			}
			response = failure(error)
			res.send(response).json();
			return;
		}
		else {
			response = success("File deleted");
    		res.send(response);
		}
	});
});

function success(message) {
	return response = {
		"type": "listResponse",
		"operation": "notification",
		"message": message
	}
}

function failure(error) {
	return response = {
		"type": "listResponse",
		"operation": "notification",
		"error": {
			"title": error.title,
			"message": error.message
		}
	}
}

module.exports = router;