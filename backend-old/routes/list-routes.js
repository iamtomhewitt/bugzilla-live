const express = require('express');
const fs = require('fs');
const path = require('path');

const router = express.Router();
const listFolder = path.join(__dirname, '..', 'config', 'bug-lists', '/');
const errorCode = 601;
const successCode = 200;

function success(message) {
    return {
        type: 'listResponse',
        message,
        status: successCode,
    };
}

function failure(error) {
    return {
        type: 'listResponse',
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

router.get('/', (req, res) => {
    res.status(successCode).send('OK');
});

// Add a new list
router.get('/add', (req, res) => {
    const name = req.query.name.replace('+', ' ');
    const { contents } = req.query;

    let error; let
        response;

    if (!name || !contents) {
        error = createError('Missing parameters', 'File name or file contents are missing.');
        response = failure(error);
        res.status(errorCode).send(response).json();
        return;
    }

    const filename = `${listFolder + name}.bugList`;

    fs.writeFile(filename, contents, (err) => {
        if (err) {
            error = createError('Error creating file', err.message);
            response = failure(error);
            res.status(successCode).send(response).json();
        } else {
            response = success('List created.');
            res.status(successCode).send(response).json();
        }
    });
});

// Modify an existing list
router.get('/modify', (req, res) => {
    const { name } = req.query;
    const { remove } = req.query;
    const { add } = req.query;

    let error; let
        response;

    if (!name || (!remove && !add)) {
        error = createError('Missing parameters', 'File name, contents to remove or contents to add are missing.');
        response = failure(error);
        res.status(errorCode).send(response).json();
        return;
    }

    const filename = `${listFolder + name}.bugList`;

    if (!fs.existsSync(filename)) {
        error = createError('Error modifying file', `File ${filename} does not exist.`);
        response = failure(error);
        res.status(errorCode).send(response).json();
        return;
    }

    const contents = fs.readFileSync(filename, 'utf-8');
    let newContents = contents;

    if (remove) {
        const array = remove.split(',');
        array.forEach((number, index) => {
            console.log(number);
            newContents = newContents.replace(new RegExp(number), '');
        });
    }

    if (add) {
        newContents += `,${add},`;
    }

    newContents = newContents.replace(new RegExp(',,'), ',');
    fs.writeFileSync(filename, newContents, 'utf-8');

    response = success('List modified');
    res.status(successCode).send(response).json();
});

// Delete a list
router.get('/delete', (req, res) => {
    const { name } = req.query;

    let error; let
        response;

    if (!name) {
        error = createError('Missing parameters', 'File name is missing.');
        response = failure(error);
        res.status(errorCode).send(response).json();
        return;
    }

    const filename = `${listFolder + name}.bugList`;

    fs.unlink(filename, (err) => {
        if (err) {
            error = createError('Error deleting file', err.message);
            response = failure(error);
            res.status(errorCode).send(response).json();
        } else {
            response = success('File deleted');
            res.status(successCode).send(response);
        }
    });
});

// Get existing lists
router.get('/lists', (req, res) => {
    let error; let
        response;
    const lists = [];

    fs.readdir(listFolder, (err, files) => {
        if (err) {
            error = createError('Could not get lists', err.message);
            response = failure(error);
            res.status(errorCode).send(response);
        }

        files.forEach((file) => {
            lists.push(file);
        });

        response = success('Lists retrieved');
        response.lists = lists;
        res.status(successCode).send(response);
    });
});

// Get existing lists
router.get('/:listName/contents', (req, res) => {
    const filename = `${listFolder + req.params.listName.replace('+', ' ')}.bugList`;
    const contents = fs.readFileSync(filename, 'utf-8');
    const response = success('Retrieved contents');

    response.contents = contents;
    res.status(successCode).send(response);
});

module.exports = router;
