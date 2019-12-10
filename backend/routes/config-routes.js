var express = require('express')
var fs = require('fs');
var path = require('path')

var router = express.Router();
var configFilename = path.join(__dirname, '..', 'config', 'config.json');
var configFile = require(configFilename)

router.get('/', function (req, res) {
    res.status(200).send('OK');
});

// Get existing config
router.get('/get', function (req, res) {
	let contents = fs.readFileSync(configFilename, 'utf-8');
	let response = success(contents)
    res.send(response);
});

// Save config
router.get('/save', function (req, res) {
	let key 	= req.query.key;
	let value 	= req.query.value;
	let error;
	let response;

	if (!key || !value)	{
		error = createError("Missing parameters", "A key or value was missing.")
		response = failure(error);
		res.send(response);
		return;
	}

	configFile[key] = value;

	fs.writeFile(configFilename, JSON.stringify(configFile, null, 4), function(err){
		if (err) {
			error = createError("Error saving config", err.message);
			response = failure(error);
			res.send(response);
			return;
		}
	});

	response = success("Config saved");
    res.send(response);
});

function success(message) {
	return response = {
		"type": "configResponse",
		"operation": "notification",
		"message": message
	}
}

function success(config) {
	return response = {
		"type": "configResponse",
		"operation": "notification",
		"config": config
	}
}

function failure(error) {
	return response = {
		"type": "configResponse",
		"operation": "notification",
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