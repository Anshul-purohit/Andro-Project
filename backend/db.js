const mongoose = require('mongoose');

// Database Connection
const connectDB = async () => {
  mongoose.connection.on('connected', () => {
    console.log('Connected to MongoDB');
  });

  mongoose.connection.on('error', (err) => {
    console.error('Error connecting to MongoDB', err);
  });

  try {
    await mongoose.connect('mongodb+srv://akku:akku@cluster0.osapnyy.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0', {
      useNewUrlParser: true,
      useUnifiedTopology: true
    });
    console.log('MongoDB connected');
  } catch (error) {
    console.error('Error connecting to MongoDB:', error);
  }
};

// CWE Schema and Model
const CWESchema = new mongoose.Schema({
  CWE_No: {
    type: String,
    required: true
  },
  Name:{
    type : String,
    required: true
  },
  description: {
    type: String,
    required: true
  },
  Extended_Description: {
    type : String,
  }
});

const CWE = mongoose.model('CWE', CWESchema, 'cwe');

// Insert Data
const insertData = async () => {
  const newCWE = new CWE({
    CWE_No: '499',
    description: '<SSL_Security> SSL Connection Checking: ... [rest of the description]'
  });

  try {
    await newCWE.save();
    console.log('Data inserted successfully');
  } catch (error) {
    console.error('Error inserting data:', error);
  }
};

// Function to Connect and Insert Data
const connectAndInsertData = async () => {
  await connectDB();
  // await insertData();
};

// Execute
connectAndInsertData();
module.exports = connectDB;


