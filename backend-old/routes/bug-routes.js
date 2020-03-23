const express = require('express');
const request = require('request');
const fs = require('fs');
const path = require('path');

const router = express.Router();

const errorCode = 601;
const successCode = 200;
let bugzillaUrl;

function success(bugs) {
    return {
        type: 'bugResponse',
        operation: 'bugs',
        status: successCode,
        bugs,
    };
}

function failure(error) {
    return {
        type: 'bugResponse',
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

function getBugzillaUrl() {
    if (!bugzillaUrl) {
        const configFilename = path.join(__dirname, '..', 'config', 'config.json');
        const contents = JSON.parse(fs.readFileSync(configFilename, 'utf-8'));
        bugzillaUrl = contents.bugzillaUrl;
    }
    return bugzillaUrl;
}

router.get('/', (req, res) => {
    res.status(successCode).send('OK');
});

// Get bugs by numbers
router.get('/numbers', (req, res) => {
    const bugNumbers = req.query.numbers;
    let error;
    let response;
    let bugs;

    if (!bugNumbers) {
        error = createError('Invalid bugs', 'No numbers specified in query.');
        response = failure(error);
        res.status(errorCode).send(response);
        return;
    }

    let url = `${getBugzillaUrl()}/rest/bug?id=`;
    const array = bugNumbers.split(',');

    for (let i = 0; i < array.length; i++) {
        url += `${array[i]},`;
    }

    request(url, (err, resp, body) => {
        if (err) {
            error = createError('Could not get bugs from Bugzilla', err.message);
            response = failure(error);
            res.status(errorCode).send(response);
            return;
        }

        bugs = JSON.parse(body, null, 4).bugs;
        response = success(bugs);
        res.status(successCode).send(response);
    });
});

// Get bugs by username
router.get('/username', (req, res) => {
    const { username } = req.query;
    let error;
    let response;
    let bugs;

    if (!username) {
        error = createError('Invalid username', 'No username specified in query.');
        response = failure(error);
        res.status(errorCode).send(response);
        return;
    }

    const url = `${getBugzillaUrl()}/rest/bug?assigned_to=${username}`;

    request(url, (err, resp, body) => {
        if (err) {
            error = createError('Error querying Bugzilla', err.message);
            response = failure(error);
            res.send(response);
            return;
        }

        bugs = JSON.parse(body, null, 4).bugs;
        response = success(bugs);
        res.status(successCode).send(response);
    });
});

// Get comments for a bug
router.get('/:number/comments', (req, res) => {
    const bugNumber = req.params.number;
    let error;
    let response;

    if (!bugNumber) {
        error = createError('Invalid bug number', 'No bug number specified.');
        response = failure(error);
        res.status(errorCode).send(response);
        return;
    }

    const url = `${getBugzillaUrl()}/rest/bug/${bugNumber}/comment`;

    request(url, (err, resp, body) => {
        if (err) {
            error = createError('Could not get comments from Bugzilla', err.message);
            response = failure(error);
            res.status(errorCode).send(response);
            return;
        }

        const comments = JSON.parse(body, null, 4).bugs;
        response = success(comments);
        res.status(successCode).send(response);
    });
});

// Add comments for a bug
router.post('/:number/comments/add', (req, res) => {
    const bugNumber = req.params.number;
    const { comment } = req.query;
    const { apiKey } = req.query;
    let error;
    let response;

    if (!bugNumber || !comment || !apiKey) {
        error = createError('Could not add comment', 'There is a missing parameter.');
        response = failure(error);
        res.status(errorCode).send(response);
        return;
    }

    const url = `${getBugzillaUrl()}/rest/bug/${bugNumber}/comment`;
    const commentData = {
        comment,
    };

    request({
        headers: {
            'Content-Type': 'application/json',
            'X-BUGZILLA-API-KEY': apiKey,
        },
        url,
        body: JSON.stringify(commentData),
        method: 'POST',
    }, (err, resp, body) => {
        if (err) {
            error = createError('Could not add comment to Bugzilla', err.message);
            response = failure(error);
            res.status(errorCode).send(response);
            return;
        }

        response = success();
        res.status(successCode).send(response);
    });
});

// Get attachments for a bug
router.get('/:number/attachments', (req, res) => {
    const bugNumber = req.params.number;
    let error; let
        response;

    if (!bugNumber) {
        error = createError('Invalid bug number', 'No bug number specified.');
        response = failure(error);
        res.status(errorCode).send(response);
        return;
    }

    const url = `${getBugzillaUrl()}/rest/bug/${bugNumber}/attachment`;

    request(url, (err, resp, body) => {
        if (err) {
            error = createError('Could not get attachments from Bugzilla', err.message);
            response = failure(error);
            res.status(errorCode).send(response);
            return;
        }

        const attachments = JSON.parse(body, null, 4).bugs;
        response = success(attachments);
        res.status(successCode).send(response);
    });
});

// Change bug status
router.put('/:number/status/change', (req, res) => {
    const bugNumber = req.params.number;
    const { status } = req.query;
    const { resolution } = req.query;
    const { apiKey } = req.query;
    const { comment } = req.query;
    let error; let
        response;

    if (!bugNumber || !status || !apiKey) {
        error = createError('Could not change bug status', 'There is a missing parameter.');
        response = failure(error);
        res.status(errorCode).send(response);
        return;
    }

    const url = `${getBugzillaUrl()}/rest/bug/${bugNumber}`;
    const data = {
        status,
        resolution,
        comment: {
            body: comment,
        },
    };

    request({
        headers: {
            'Content-Type': 'application/json',
            'X-BUGZILLA-API-KEY': apiKey,
        },
        url,
        body: JSON.stringify(data),
        method: 'PUT',
    },
    (err, resp, body) => {
        if (err) {
            error = createError('Could not add comment to Bugzilla', err.message);
            response = failure(error);
            res.status(errorCode).send(response);
            return;
        }

        response = success();
        res.status(successCode).send(response);
    });
});


module.exports = router;
