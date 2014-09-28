var app      = require('../app'),
    database = require('../data');

var routes = {};

// GET home page
routes.index = function (req, res) {
    res.render('index.ejs', {ejsData:'EJS is working!'});
};

// GET normalize.css
routes.normalizecss = function (req, res) {
    res.sendFile(app.get('node_modules') + '/normalize.css/normalize.css');
};

// GET 404 page
routes.fileNotFound = function (req, res) {
    res.render('status/404.ejs');
};

routes.databaseAPI = function (req, res) {

    console.log("request body: ", req.body);

    // Empty response
    if (!req.body) {
        res.json({message: 'Invalid request body'});
        return;
    }

    // With data
    if (!isEmpty(req.body)) {
        // saves everything idk why
        database.save(req.body, function () {
            res.json({message: 'Success!'});
        }, function (error) {
            res.json({message: 'Failure!', err: error});
        }, req);
    } else {
    // Without data (nedd to retrieve from db)
        // res needs to be passed in because of callback hell
        database.load(res)
    }

    // Check if an object has properties
    function isEmpty(map) {
       for(var key in map) {
          if (map.hasOwnProperty(key)) {
             return false;
          }
       }
       return true;
    }
}

module.exports = routes;