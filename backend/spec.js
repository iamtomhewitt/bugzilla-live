var request = require('supertest');

describe('Express app tests', function () {
	var server;

	before(function () {
		server = require('./app').listen(3002);
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

describe('/bugs/numbers tests', function () {
	var server;
	var timeout = 30000;

	before(function () {
		server = require('./app').listen(3002);
	});

	after(function () {
		server.close();
	});

	it('/bugs/numbers gives 200', function test(done) {
		request(server)
			.get('/bugs/numbers?numbers=12345')
			.expect(200, done);
	});

	it('/bugs/numbers with no parameters gives 601', function test(done) {
		request(server)
			.get('/bugs/numbers')
			.expect(601, done);
	});

	it('/bugs/email gives 200', function test(done) {
		request(server)
			.get('/bugs/email?email=test@gmail.com')
			.expect(200, done);
	}).timeout(timeout);

	it('/bugs/email with no parameters gives 601', function test(done) {
		request(server)
			.get('/bugs/email')
			.expect(601, done);
	});

	it('/bugs/:number/comments gives 200', function test(done) {
		request(server)
			.get('/bugs/12345/comments')
			.expect(200, done);
	}).timeout(timeout);

	it('/bugs/:number/comments with no parameters gives 404', function test(done) {
		request(server)
			.get('/bugs//comments')
			.expect(404, done);
	});
});

