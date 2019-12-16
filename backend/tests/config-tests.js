var request = require('supertest');
var assert = require('assert');

describe('Config route tests', function () {
	var server;

	before(function () {
		server = require('../app').listen(3002);
	});

	after(function () {
		server.close();
	});

	it('Should give 200 when asking for config', function test(done) {
		request(server)
			.get('/config/get')
			.expect(200, done);
	});

	it('Should give 601 when no parameters are set when saving config', function test(done) {
		request(server)
			.get('/config/save')
			.expect(601, done);
	});

	it('Should give 200 when saving config', function test(done) {
		request(server)
			.get('/config/save?key=test&value=myvalue')
			.expect(200, done);
	});

	it('Should have "test": "myvalue" in the config of the response message', function test(done) {
		request(server)
			.get('/config/get')
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let config = JSON.parse(response.body['config']);
				assert.equal(config.test, 'myvalue');
				done();
			});
	});

	it('Should have correct title and message when sending an error', function test(done) {
		request(server)
			.get('/config/save')
			.expect(601)
			.end(function(err, response) {
				if (err) {
					return done(err);
				}

				let error = response.body["error"];
				assert.equal(error.title, 'Missing parameters');
				assert.equal(error.message, 'A key or value was missing.');
				done();
			});
	});
});