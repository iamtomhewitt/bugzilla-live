const request = require('supertest');

describe('Express tests', () => {
    let server;

    before(() => {
        server = require('../app').listen(3002);
    });

    after(() => {
        server.close();
    });

    it('/ gives 200', (done) => {
        request(server)
            .get('/')
            .expect(200, done);
    });

    it('/bugs gives 200', (done) => {
        request(server)
            .get('/bugs')
            .expect(200, done);
    });

    it('/list gives 200', (done) => {
        request(server)
            .get('/list')
            .expect(200, done);
    });

    it('/config gives 200', (done) => {
        request(server)
            .get('/config')
            .expect(200, done);
    });
});
