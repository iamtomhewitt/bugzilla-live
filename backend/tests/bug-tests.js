var request = require('supertest');
var assert = require('assert');

describe('Bug route tests', function () {
	var server;
	var timeout = 30000;

	before(function () {
		server = require('../app').listen(3002);
	});

	after(function () {
		server.close();
	});

	it('Should give 200 when querying for a bug', function test(done) {
		request(server)
			.get('/bugs/numbers?numbers=12345')
			.expect(200, done);
	}).timeout(timeout);

	it('Should give 601 when no parameters are passed for bug numbers', function test(done) {
		request(server)
			.get('/bugs/numbers')
			.expect(601, done);
	});

	it('Should have correct error title and message when no parameters are passed for bug numbers', function test(done) {
		request(server)
			.get('/bugs/numbers')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Invalid bugs');
				assert.equal(error.message, 'No numbers specified in query.');
				done();
			});
	});

	it('Should give 200 when getting bugs by email', function test(done) {
		request(server)
			.get('/bugs/email?email=test@gmail.com')
			.expect(200, done);
	}).timeout(timeout);

	it('Should give 601 when no parameters are passed for bugs with email', function test(done) {
		request(server)
			.get('/bugs/email')
			.expect(601, done);
	});

	it('Should have correct error title and message when no parameters are passed for email', function test(done) {
		request(server)
			.get('/bugs/email')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Invalid email');
				assert.equal(error.message, 'No email specified in query.');
				done();
			});
	});

	it('Should give 200 when getting comments for a bug', function test(done) {
		request(server)
			.get('/bugs/12345/comments')
			.expect(200, done);
	}).timeout(timeout);

	it('Should give 404 when getting comments with no number parameter', function test(done) {
		request(server)
			.get('/bugs//comments')
			.expect(404, done);
	});

	it('Should give 404 when adding a comment with no number parameter', function test(done) {
		request(server)
			.post('/bugs//comments/add')
			.expect(404, done);
	});

	it('Should give 601 when adding a comment with no query parameters', function test(done) {
		request(server)
			.post('/bugs/1605238/comments/add')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Could not add comment');
				assert.equal(error.message, 'There is a missing parameter.');
				done();
			});
	});

	it('Should give 404 when changing a status with no number parameter', function test(done) {
		request(server)
			.put('/bugs//status/change')
			.expect(404, done);
	});

	it('Should give 601 when changing a status with no query parameters', function test(done) {
		request(server)
			.put('/bugs/1605238/status/change')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Could not change bug status');
				assert.equal(error.message, 'There is a missing parameter.');
				done();
			});
	});

	it('Should give 200 when getting attachments for a bug', function test(done) {
		request(server)
			.get('/bugs/12345/attachments')
			.expect(200, done);
	}).timeout(timeout);

	it('Should give 404 when getting attachments with no number parameter', function test(done) {
		request(server)
			.get('/bugs//attachments')
			.expect(404, done);
	});
});