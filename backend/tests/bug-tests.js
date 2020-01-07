const request = require('supertest');
const assert = require('assert');

describe('Bug route tests', () => {
    let server;
    const timeout = 30000;

    before(() => {
        server = require('../app').listen(3002);
    });

    after(() => {
        server.close();
    });

    it('Should give 200 when querying for a bug', (done) => {
        request(server)
            .get('/bugs/numbers?numbers=12345')
            .expect(200, done);
    }).timeout(timeout);

    it('Should give 601 when no parameters are passed for bug numbers', (done) => {
        request(server)
            .get('/bugs/numbers')
            .expect(601, done);
    });

    it('Should have correct error title and message when no parameters are passed for bug numbers', (done) => {
        request(server)
            .get('/bugs/numbers')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Invalid bugs');
                assert.equal(error.message, 'No numbers specified in query.');
                return done();
            });
    });

    it('Should give 200 when getting bugs by username', (done) => {
        request(server)
            .get('/bugs/username?username=test')
            .expect(200, done);
    }).timeout(timeout);

    it('Should give 601 when no parameters are passed for bugs with username', (done) => {
        request(server)
            .get('/bugs/username')
            .expect(601, done);
    });

    it('Should have correct error title and message when no parameters are passed for username', (done) => {
        request(server)
            .get('/bugs/username')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Invalid username');
                assert.equal(error.message, 'No username specified in query.');
                return done();
            });
    });

    it('Should give 200 when getting comments for a bug', (done) => {
        request(server)
            .get('/bugs/12345/comments')
            .expect(200, done);
    }).timeout(timeout);

    it('Should give 404 when getting comments with no number parameter', (done) => {
        request(server)
            .get('/bugs//comments')
            .expect(404, done);
    });

    it('Should give 404 when adding a comment with no number parameter', (done) => {
        request(server)
            .post('/bugs//comments/add')
            .expect(404, done);
    });

    it('Should give 601 when adding a comment with no query parameters', (done) => {
        request(server)
            .post('/bugs/1605238/comments/add')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Could not add comment');
                assert.equal(error.message, 'There is a missing parameter.');
                return done();
            });
    });

    it('Should give 404 when changing a status with no number parameter', (done) => {
        request(server)
            .put('/bugs//status/change')
            .expect(404, done);
    });

    it('Should give 601 when changing a status with no query parameters', (done) => {
        request(server)
            .put('/bugs/1605238/status/change')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Could not change bug status');
                assert.equal(error.message, 'There is a missing parameter.');
                return done();
            });
    });

    it('Should give 200 when getting attachments for a bug', (done) => {
        request(server)
            .get('/bugs/12345/attachments')
            .expect(200, done);
    }).timeout(timeout);

    it('Should give 404 when getting attachments with no number parameter', (done) => {
        request(server)
            .get('/bugs//attachments')
            .expect(404, done);
    });
});
