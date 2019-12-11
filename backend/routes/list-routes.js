var express = require('express')
var fs = require('fs');
var path = require('path')

var router = express.Router();
var listFolder = path.join(__dirname, '..', 'config', 'bug-lists', '/');
var errorCode = 601;
var successCode = 200;

router.get('/', function (req, res) {
    res.status(successCode).send('OK');
});

// Add a new list
router.get('/add', function (req, res) {
    var name 	 = req.query.name;
	var contents = req.query.contents;
	
	let error, response;

	if (!name || !contents) {
		error = createError("Missing parameters", "File name or file contents are missing.");
		response = failure(error)
		res.status(errorCode).send(response).json();
		return;
	}

	let filename = listFolder + name + '.bugList';

	fs.writeFile(filename, contents, function (err) {
        if (err) {
			error = createError("Error creating file", err.message);
			response = failure(error)
            res.status(successCode).send(response).json();
        }
        else {
			response = success("List created.")
            res.status(successCode).send(response).json();
        }
    })
});

// Modify an existing list
router.get('/modify', function (req, res) {
    var name 	= req.query.name;
    var remove 	= req.query.remove;
	var add 	= req.query.add;

	let error, response;
	
	if (!name || (!remove && !add)) {
		error = createError("Missing parameters", "File name, contents to remove or contents to add are missing.");
		response = failure(error);
		res.status(errorCode).send(response).json();
		return;
	}

	let filename = listFolder + name + '.bugList';

	if (!fs.existsSync(filename)) {
		error = createError("Error modifying file", `File ${filename} does not exist.`);
		response = failure(error);
		res.status(errorCode).send(response).json();
		return;
	}

	let contents = fs.readFileSync(filename, 'utf-8');
	let newContents = contents;
	
	if (remove) {
		newContents = contents.replace(new RegExp(remove), '');
	}

	if (add) {
		newContents += ',' + add + ',';
	}

	newContents = newContents.replace(new RegExp(',,'), ',');
	fs.writeFileSync(filename, newContents, 'utf-8');

	response = success("List modified");
    res.status(successCode).send(response).json();
});

// Delete a list
router.get('/delete', function (req, res) {
	var name = req.query.name;
	
	let error, response;

	if (!name) {
		error = createError("Missing parameters", "File name or file contents are missing.");
		response = failure(error)
		res.status(errorCode).send(response).json();
		return;
	}

	let filename = listFolder + name + '.bugList';

	fs.unlink(filename, function (err) {
        if (err) {
			error = createError("Error deleting file", err.message);
			response = failure(error)
			res.status(errorCode).send(response).json();
		}
		else {
			response = success("File deleted");
    		res.status(successCode).send(response);
		}
	});
});

// Get existing lists
router.get('/lists', function (req, res) {
	let error, response
	let lists = [];
	
	fs.readdir(listFolder, (err, files) => {
		if (err) {
			error = createError('Could not get lists', err.message);
			response = failure(error);
			res.status(errorCode).send(response);
		}

		files.forEach(file => {
			lists.push(file)
		});

		response = success('Lists retrieved');
		response['lists'] = lists;
		res.status(successCode).send(response);
	});
});

function success(message) {
	return response = {
		"type": "listResponse",
		"message": message,
		"status": successCode
	}
}

function failure(error) {
	return response = {
		"type": "listResponse",
		"operation": "notification",
		"status": errorCode,
		"error": error
	}
}

function createError(title, message) {
	return error = {
		"title": title,
		"message": message
	}
}

module.exports = router;