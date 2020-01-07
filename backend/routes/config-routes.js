const express = require('express');
const fs = require('fs');
const path = require('path');

const router = express.Router();
const configFilename = path.join(__dirname, '..', 'config', 'config.json');
const configFile = require(configFilename);
const errorCode = 601;
const successCode = 200;

function success() {
    return {
        type: 'configResponse',
        operation: 'notification',
        status: successCode,
    };
}

function failure(error) {
    return {
        type: 'configResponse',
        operation: 'notification',
        status: errorCode,
        error,
    };
}

function createError(title, message) {
    return {
        title,
        message,
    };
}

function IsJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

router.get('/', (req, res) => {
    res.status(200).send('OK');
});

// Get existing config
router.get('/get', (req, res) => {
    const contents = fs.readFileSync(configFilename, 'utf-8');
    const response = success(contents);
    response.config = contents;
    res.status(successCode).send(response);
});

// Save config
router.get('/save', (req, res) => {
    const key = req.query.key;
    let value = req.query.value;
    let error;
    let response;

    if (!key || !value) {
        error = createError('Missing parameters', 'A key or value was missing.');
        response = failure(error);
        res.status(errorCode).send(response);
        return;
    }

    value = IsJsonString(value) ? JSON.parse(value) : value;
    configFile[key] = value;

    fs.writeFile(configFilename, JSON.stringify(configFile, null, 4), (err) => {
        if (err) {
            error = createError('Error saving config', err.message);
            response = failure(error);
            res.status(errorCode).send(response);
        }
    });

    response = success();
    response.message = 'Config saved';
    res.status(successCode).send(response);
});


module.exports = router;
