var request = require('supertest');

describe('Express tests', function () {
	var server;

	before(function () {
		server = require('../app').listen(3002);
	});

	after(function () {
		server.close();
	});

	it('/ gives 200', function test(done) {
		request(server)
			.get('/')
			.expect(200, done);
	});

	it('/bugs gives 200', function test(done) {
		request(server)
			.get('/bugs')
			.expect(200, done);
	});

	it('/list gives 200', function test(done) {
		request(server)
			.get('/list')
			.expect(200, done);
	});

	it('/config gives 200', function test(done) {
		request(server)
			.get('/config')
			.expect(200, done);
	});
});