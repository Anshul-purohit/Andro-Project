const mongoose = require('mongoose');
 
const CWESchema = new mongoose.Schema({
    CWE_No : Number,
    description : String
});
 
module.exports = mongoose.model('CWESchema', CWESchema);