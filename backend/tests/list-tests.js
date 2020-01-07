const request = require('supertest');
const assert = require('assert');

describe('List route tests', () => {
    let server;

    before(() => {
        server = require('../app').listen(3002);
    });

    after(() => {
        server.close();
    });

    it('Should give 200 when adding a list', (done) => {
        request(server)
            .get('/list/add?name=unit-test&contents=12345,6789')
            .expect(200, done);
    });

    it('Should give 601 when adding a list with no parameters', (done) => {
        request(server)
            .get('/list/add?name=&contents=')
            .expect(601, done);
    });

    it('Should have correct error title and error message when adding incorrect list', (done) => {
        request(server)
            .get('/list/add?name=&contents=')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Missing parameters');
                assert.equal(error.message, 'File name or file contents are missing.');
                return done();
            });
    });

    it('Should give 200 when getting contents of a list', (done) => {
        request(server)
            .get('/list/unit-test/contents')
            .expect(200, done);
    });

    it('Should give 200 when modifying a list', (done) => {
        request(server)
            .get('/list/modify?name=unit-test&add=1234')
            .expect(200, done);
    });

    it('Should give 601 when modifying a list with no parameters', (done) => {
        request(server)
            .get('/list/modify')
            .expect(601, done);
    });

    it('Should have correct error title and error message when modifying incorrectly', (done) => {
        request(server)
            .get('/list/modify?name=&add=&remove=')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Missing parameters');
                assert.equal(error.message, 'File name, contents to remove or contents to add are missing.');
                return done();
            });
    });

    it('Should give 200 when deleting a list', (done) => {
        request(server)
            .get('/list/delete?name=unit-test')
            .expect(200, done);
    });

    it('Should give 601 when deleting a list with no parameteres', (done) => {
        request(server)
            .get('/list/delete')
            .expect(601, done);
    });

    it('Should have correct error title and error message when deleting incorrectly', (done) => {
        request(server)
            .get('/list/delete?name=')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Missing parameters');
                assert.equal(error.message, 'File name is missing.');
                return done();
            });
    });

    it('Should give 200 when listing all lists', (done) => {
        request(server)
            .get('/list/lists')
            .expect(200, done);
    });
});
