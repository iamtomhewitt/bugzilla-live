var request = require('supertest');
var assert = require('assert');

describe('List route tests', function () {
	var server;
	const fs = require('fs');
	const path = require('path');

	const directory = path.join(__dirname, '/', 'config', 'bug-lists', '/');

	before(function () {
		server = require('../app').listen(3002);
	});

	after(function () {
		// Remove test files
		fs.readdir(directory, (err, files) => {
			if (err) throw err;
		  
			for (const file of files) {
				fs.unlink(path.join(directory, file), err => {
					if (err) throw err;
				});
			}
		});

		server.close();
	});

	it('Should give 200 when adding a list', function test(done) {
		request(server)
			.get('/list/add?name=unit-test&contents=12345,6789')
			.expect(200, done);
	});

	it('Should give 601 when adding a list with no parameters', function test(done) {
		request(server)
			.get('/list/add?name=&contents=')
			.expect(601, done);
	});

	it('Should have correct error title and error message when adding incorrect list', function test(done) {
		request(server)
			.get('/list/add?name=&contents=')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Missing parameters');
				assert.equal(error.message, 'File name or file contents are missing.');
				done();
			});
	});

	it('Should give 200 when getting contents of a list', function test(done) {
		request(server)
			.get('/list/unit-test/contents')
			.expect(200, done);
	});

	it('Should give 200 when modifying a list', function test(done) {
		request(server)
			.get('/list/modify?name=unit-test&add=1234')
			.expect(200, done);
	});

	it('Should give 601 when modifying a list with no parameters', function test(done) {
		request(server)
			.get('/list/modify')
			.expect(601, done);
	});
	
	it('Should have correct error title and error message when modifying incorrectly', function test(done) {
		request(server)
			.get('/list/modify?name=&add=&remove=')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Missing parameters');
				assert.equal(error.message, 'File name, contents to remove or contents to add are missing.');
				done();
			});
	});

	it('Should give 200 when deleting a list', function test(done) {
		request(server)
			.get('/list/delete?name=unit-test')
			.expect(200, done);
	});

	it('Should give 601 when deleting a list with no parameteres', function test(done) {
		request(server)
			.get('/list/delete')
			.expect(601, done);
	});

	it('Should have correct error title and error message when deleting incorrectly', function test(done) {
		request(server)
			.get('/list/delete?name=')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Missing parameters');
				assert.equal(error.message, 'File name is missing.');
				done();
			});
	});

	it('Should give 200 when listing all lists', function test(done) {
		request(server)
			.get('/list/lists')
			.expect(200, done);
	});
});