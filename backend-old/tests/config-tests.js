const request = require('supertest');
const assert = require('assert');

describe('Config route tests', () => {
    let server;

    before(() => {
        server = require('../app').listen(3002);
    });

    after(() => {
        server.close();
    });

    it('Should give 200 when asking for config', (done) => {
        request(server)
            .get('/config/get')
            .expect(200, done);
    });

    it('Should give 601 when no parameters are set when saving config', (done) => {
        request(server)
            .get('/config/save')
            .expect(601, done);
    });

    it('Should give 200 when saving string config', (done) => {
        request(server)
            .get('/config/save?key=test&value=myvalue')
            .expect(200, done);
    });

    it('Should have "test": "myvalue" in the config of the response message', (done) => {
        request(server)
            .get('/config/get')
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const config = JSON.parse(response.body.config);
                assert.equal(config.test, 'myvalue');
                return done();
            });
    });

    it('Should give 200 when saving JSON config', (done) => {
        request(server)
            .get('/config/save?key=json&value={"mykey": "myvalue"}')
            .expect(200, done);
    });

    it('Should have correct title and message when sending an error', (done) => {
        request(server)
            .get('/config/save')
            .expect(601)
            .end((err, response) => {
                if (err) {
                    return done(err);
                }

                const { error } = response.body;
                assert.equal(error.title, 'Missing parameters');
                assert.equal(error.message, 'A key or value was missing.');
                return done();
            });
    });
});
