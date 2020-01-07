const express = require('express');

const app = express();
const bodyParser = require('body-parser');

const bugRoutes = require('./routes/bug-routes');
const listRoutes = require('./routes/list-routes');
const configRoutes = require('./routes/config-routes');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (req, res) => {
    res.status(200).send('SERVER OK');
});

app.use('/bugs', bugRoutes);
app.use('/list', listRoutes);
app.use('/config', configRoutes);

const port = 3001;
app.listen(port, () => { });

console.log(`Listening on port: ${port}`);

module.exports = app;
