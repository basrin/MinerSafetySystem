var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var database = {};

mongoose.connect('mongodb://admin:miner-safety-password@ds039850.mongolab.com:39850/miner-safety-system');

var MoteSchema = new Schema({
      moteId: String,
      date: String,
      temperature: Number,
      orientation: Number,
    }, {
      collection: 'Measurements'
    }, {strict: true});

// This is the database object
var MoteModel = mongoose.model('Measurements', MoteSchema);


database.save = function (mongoData, success, failure) {
    var measurement = new MoteModel(mongoData);

    measurement.save(function (err) {
        if (err) {
            if (typeof failure === 'function') {
                failure(err);
            }
            console.log(err);
        } else {
            if (typeof success === 'function') {
                success();
            }
        }
    })
};

database.load = function (res, options) {
    if (options && options.by && options.whereValueIs) {
        var query = {};
        query[options.by] = options.whereValueIs;
        MoteModel.findOne(query, function(err, user) {
            console.log(err, user);
        });
    } else {
        MoteModel.find({}, function (err, data) {
            if (!err) {
                 res.json({'dbValueArray': data});
            } else {
                return err;
            }
        })
    }
};

module.exports = database;