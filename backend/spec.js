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

describe('/bugs tests', function () {
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

describe('/config tests', function () {
	var server;

	before(function () {
		server = require('./app').listen(3002);
	});

	after(function () {
		server.close();
	});

	it('/config/get gives 200', function test(done) {
		request(server)
			.get('/config/get')
			.expect(200, done);
	});

	it('/config/save with no parameters gives 601', function test(done) {
		request(server)
			.get('/config/save')
			.expect(601, done);
	});

	it('/config/save gives 200', function test(done) {
		request(server)
			.get('/config/save?key=test&value=myvalue')
			.expect(200, done);
	});
});

describe('/list tests', function () {
	var server;
	const fs = require('fs');
	const path = require('path');

	const directory = path.join(__dirname, '/', 'config', 'bug-lists', '/');

	before(function () {
		server = require('./app').listen(3002);
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

	it('/list/add gives 200', function test(done) {
		request(server)
			.get('/list/add?name=unit-test&contents=12345,6789')
			.expect(200, done);
	});

	it('/list/add with no parameters gives 601', function test(done) {
		request(server)
			.get('/list/add')
			.expect(601, done);
	});

	it('/list/modify gives 200', function test(done) {
		request(server)
			.get('/list/modify?name=unit-test&add=1234')
			.expect(200, done);
	});

	it('/list/modify with no parameters gives 601', function test(done) {
		request(server)
			.get('/list/modify')
			.expect(601, done);
	});

	it('/list/delete gives 200', function test(done) {
		request(server)
			.get('/list/delete?name=unit-test')
			.expect(200, done);
	});

	it('/list/delete with no parameters gives 601', function test(done) {
		request(server)
			.get('/list/delete')
			.expect(601, done);
	});

	it('/list/lists gives 200', function test(done) {
		request(server)
			.get('/list/lists')
			.expect(200, done);
	});
});