var express = require('express')
var router = express.Router();
var request = require('request');

var bugzillaUrl = 'https://bugzilla.mozilla.org'

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

	let url = bugzillaUrl + '/rest/bug?id='

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

// Get bugs by username
router.get('/username', function (req, res) {
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


module.exports = router;