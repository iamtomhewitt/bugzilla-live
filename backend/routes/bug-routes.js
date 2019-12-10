var express = require('express')
var request = require('request');
var fs = require('fs');
var path = require('path')
var router = express.Router();

var bugzillaUrl;

router.get('/', function (req, res) {
    res.send('/bugs is working');
});

// Get bugs by numbers
router.get('/numbers', function (req, res) {
	let bugNumbers = req.query.numbers;
	let error, response, bugs;
	
	if (!bugNumbers) {
		error = {
			"title": "Invalid bugs",
			"message": "No numbers specified in query."
		}
		response = failure(error);
		res.send(response);
		return;
	}

	let url = getBugzillaUrl() + '/rest/bug?id='

	for (const number of bugNumbers.split(',')) {
        url += number + ','
	}

	request(url, function (err, response, body) {
		if (err) {
			error = {
				"title": "Could not get bugs from Bugzilla",
				"message": err.message
			}
			response = failure(error)
			res.send(response);
			return;
		}
		
		bugs = JSON.parse(body, null, 4).bugs;	
		response = success(bugs);
		res.send(response);
	});
});

// Get bugs by email
router.get('/email', function (req, res) {
	let email = req.query.email;
	let error, response, bugs;

	if (!email) {
		error = {
			"title": "Invalid email",
			"message": "No email specified in query."
		}
		response = failure(error);
		res.send(response);
		return;
	}

	let url = getBugzillaUrl() + '/rest/bug?assigned_to=' + email;
	
	request(url, function (err, response, body) {
		if (err) {
			error = {
				"title": "Could not get bugs from Bugzilla",
				"message": err.message
			}
			response = failure(error)
			res.send(response);
			return;
		}
		
		bugs = JSON.parse(body, null, 4).bugs;	
		response = success(bugs);
		res.send(response);
	});
});

// Get comments for a bug
router.get('/:number/comments', function (req, res) {
	let bugNumber = req.params.number;
	let error, response;

	if (!bugNumber) {
		error = {
			"title" : "Invalid bug number",
			"message": "No bug number specified"
		}
		response = failure(error);
		res.send(response);
		return ;
	}

	let url = getBugzillaUrl() + '/rest/bug/' + bugNumber + '/comment'

	request(url, function (err, response, body) {
		if (err) {
			error = {
				"title": "Could not get comments from Bugzilla",
				"message": err.message
			}
			response = failure(error)
			res.send(response);
			return;
		}
		
		comments = JSON.parse(body, null, 4)['bugs'][bugNumber];	
		response = success(comments);
		res.send(response);
	});
});

// Change status for a bug
router.post('/:number/state/:state', function (req, res) {
    res.send('Requested to change ' + req.params.number + ' state to: ' + req.params.state);
});

function success(bugs) {
	return response = {
		"type": "bugResponse",
		"operation": "bugs",
		bugs
	}
}

function failure(error) {
	return response = {
		"type": "bugResponse",
		"operation": "notification",
		"error": {
			"title": error.title,
			"message": error.message
		}
	}
}

function getBugzillaUrl() {
	if (!bugzillaUrl) {
		let configFilename = path.join(__dirname, '..', 'config', 'config.json');
		let contents = JSON.parse(fs.readFileSync(configFilename, 'utf-8'));
		bugzillaUrl = contents.bugzillaUrl;
	}
	return bugzillaUrl;
}


module.exports = router;