const express = require("express");
const app = express();
const cors = require("cors");
const mongooseConnection = require("./db");
const multer = require("multer");
const mongoose = require("mongoose");
const upload = multer({ dest: "uploads/" });
const path = require("path");
const fs = require("fs");
const cweSchema = require("./CWEDB");
const AdmZip = require("adm-zip");
// const mainBackendComponent = require("./server");
const server = require("./server");
const bodyParser = require("body-parser");

const PORT = process.env.PORT || 5000;
// const stagingAreaDirectory = 'staging_area/';
const fileMapping = {}; // Maintain a mapping of original filenames to unique identifiers

mongooseConnection();
const maxFileCount = 10; // Maximum number of files to store
const uploadDirectory = "uploads/";
app.use(function (req, res, next) {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
  res.setHeader("Access-Control-Allow-Headers", "Content-Type");
  res.setHeader("Access-Control-Allow-Credentials", true);
  next();
});

app.use(cors());
app.use(express.json());

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Mount the main backend component alongside your Express app

// app.use(upload.single("file"));

// Set up other middleware and routes as needed

// Mount the main backend component alongside your Express app
// app.use("/", mainBackendComponent);
app.use("/api/apktool/run", server);

app.use("/api/:tool/malware", upload.single("apkFile"), async (req, res) => {
  const apkFilePath = req.file.path;
  if (!apkFilePath) {
    return res.status(400).json({ error: "No APK file uploaded" });
  }
  console.log('apk path : ', apkFilePath)
  let tool = req.params.tool
  if(tool == "SUPER Android Analyzer"){
    renameToApk(apkFilePath);
    let resultPath = "/home/lavkush/tools/super-analyzer/"
    let command = `super-analyzer ${apkFilePath} --results ${resultPath} --json`
    console.log(command)
    exec(command, { stdio: "pipe" }, (error, stdout, stderr) => {
      let filePath = resultPath + '/de.ecspride/results.json'
      if (fs.existsSync(filePath)) {
        console.log("in if block");
        // Set the appropriate headers for file download
        res.setHeader("Content-Type", "text/plain");
        res.setHeader(
          "Content-Disposition",
          'attachment; filename="downloaded_file.txt"'
        );
        // console.log('response :',res)
        // Stream the file to the client
        const fileStream = fs.createReadStream(filePath);
        fileStream.pipe(res);
      } else {
        console.log("in else block");
        res.status(404).json({ error: "File not found" });
      }
    })
  }
})

app.get("/", (req, res) => {
  res.send("Hello World!");
});

app.get("/getcwe/:cwe", async (req, res) => {
  // let cweNum = req.
  const cwe = req.params.cwe;
  const cweObj = await cweSchema.findOne({ CWE_No: cwe });
  if (cweObj == undefined) {
    res.json({ success: false });
    console.log("cwe object does not exist");
    return;
  }
  console.log("cwe object exists");
  res.json({ success: true, data: cweObj });
});

app.post("/makeCWE", async (req, res) => {
  // const cweNum = req.body
  const cweObj = req.body;

  cweSchema.create(cweObj).then(
    (res) => {
      console.log("some result", res);
    },
    (err) => {
      console.log("error ", err);
    }
  );
  res.json({ success: true, cweObj });
});

const { exec } = require("child_process");
const { CLIENT_RENEG_LIMIT } = require("tls");

function convertZipToApk(zipFilePath, apkFilePath) {
  try {
    const zip = new AdmZip(zipFilePath);
    const zipEntries = zip.getEntries();

    // Assuming there's only one file in the ZIP (the APK file)
    const apkEntry = zipEntries[0];
    const apkBuffer = apkEntry.getData();

    fs.writeFileSync(apkFilePath, apkBuffer);
    console.log("APK file created successfully:", apkFilePath);
  } catch (error) {
    console.error("Error converting ZIP to APK:", error);
  }
}

function renameToApk(filePath) {
  try {
    // Get the file extension of the original file
    const fileExt = path.extname(filePath);
    console.log(`-----\nExtension : ${fileExt}\n------`);
    // Check if the file extension is not already '.apk'
    if (fileExt !== ".apk") {
      // Create the new file path with '.apk' extension
      // const newFilePath = filePath.replace(fileExt, '.apk');
      const newFilePath = filePath + ".apk";
      // console.log(`-----\nExtension : ${fileExt}\n------`)
      // Rename the file
      fs.renameSync(filePath, newFilePath);

      console.log("File renamed successfully:", newFilePath);
    } else {
      console.log("File already has .apk extension:", filePath);
    }
  } catch (error) {
    console.error("Error renaming file:", error);
  }
}

// app.post('/run-command', (req, res) => {
//   const { tool, apkInfo } = req.body;
//   console.log('apkinfo:', apkInfo);
//   let toolPath = '';
//   let command = '';

//   if (tool === 'Androwarn') {
//     toolPath = '/home/lavkush/tools/androwarn/androwarn.py';
//     command = `python3 ${toolPath} -i "${apkInfo.filePath.replace(/\\/g, '\\\\')}" -r txt -v 1`;
//     // console.log(command)
//     // console.log(apkInfo.filePath); // Log the filePath for debugging purposes
//   } else if (tool === 'Androbugs') {
//     toolPath = '/home/lavkush/tools/androbugs2/androbugs.py';
//     command = `python3 "${toolPath}" -f "${apkInfo.filePath.replace(/\\/g, '\\\\')}"`;

//   } else if (tool === 'qark') {
//     renameToApk(apkInfo.filePath)
//     toolPath = '/home/lavkush/tools/qark/qark/qark.py';
//     command = `qark --apk "${apkInfo.filePath}.apk" --report-type json`;
//     console.log(command)
//   } else {
//     return res.status(400).json({ error: 'Invalid tool' });
//   }

//   console.log('Tool Path:', toolPath);
//   console.log('Command:', command);
//   // convertZipToApk(apkInfo.filePath, 'uploads' + apkInfo.fileServerName + '.apk')

//   exec(command, { stdio: 'pipe' }, (error, stdout, stderr) => {
//     // ...

//     // Generate a unique file name\
//     // console.log('std', stdout)
//     // console.log('Error message : ', error.message)
//     // console.log('--------')
//     // console.log(stderr)
//     // console.log('\n\n')
//     let str = "backend"
//     if (tool == 'qark'){
//       str = "Finish writing report to"
//     }
//     let ind = stdout.indexOf(str)+str.length+1
//     let fileName = stdout.slice(ind)
//     // let endIdx = fileName.indexOf('.csv') + 4
//     // fileName = fileName.slice(0, endIdx)
//     console.log(`File name : ->${fileName}<-`)
//     if(tool == 'Androwarn'){
//       var commaInd = fileName.indexOf('\'')
//     }else if(tool == 'Androbugs'){
//       var commaInd = fileName.indexOf(' >>>')
//     }else if(tool == 'qark'){
//       var commaInd = fileName.indexOf('.json') + 5
//     }
//     fileName = fileName.slice(0, commaInd)
//     console.log('filename length : ', fileName.length)
//     console.log('\' index : ', commaInd)
//     console.log('file name : ', fileName)
//     let filePath = path.join(__dirname, fileName)
//     if(tool == 'qark'){
//       filePath = fileName
//     }
//     console.log('file path : ', filePath)

//     if (fs.existsSync(filePath)) {
//       console.log('in if block')
//       // Set the appropriate headers for file download
//       res.setHeader('Content-Type', 'text/plain');
//       res.setHeader('Content-Disposition', 'attachment; filename="downloaded_file.txt"');
//       // console.log('response :',res)
//       // Stream the file to the client
//       const fileStream = fs.createReadStream(filePath);
//       fileStream.pipe(res);
//     } else {
//       console.log('in else block')
//       res.status(404).json({ error: 'File not found' });
//     }

// // Sanitize the generated file name
// // const sanitizedFileName = fileName.replace(/[^a-zA-Z0-9]/g, '_') + '.txt';

// // // Construct the full file path within the report directory
// // const reportFilePath = path.join(reportDirectory, sanitizedFileName);

// // console.log('Sanitized File Name:', sanitizedFileName);
// // console.log('Report File Path:', reportFilePath);

// // // ... (rest of the code)

// // if (fs.existsSync(reportFilePath)) {
// //   // Set the appropriate headers for file download
// //   res.setHeader('Content-Type', 'text/plain');
// //   res.setHeader('Content-Disposition', `attachment; filename="${sanitizedFileName}"`);

// //   // Stream the file to the client
// //   const fileStream = fs.createReadStream(reportFilePath);
// //   fileStream.pipe(res);
// // } else {
// //   res.status(404).json({ error: 'File not found' });
// // }

//     // res.download(filePath, 'downloaded_file.txt', (err) => {
//     //   if (err) {
//     //     console.error('Error downloading file:', err);
//     //     res.status(500).send('Error downloading file');
//     //   }
//     // });
//     // const timestamp = Date.now();
//     // const reportFileName = `report_${timestamp}.txt`;

//     // // Specify the directory where the report files will be stored
//     // const reportDirectory = 'C:/Users/Admin/Desktop/react/phone/backend'; // Replace with the actual directory path

//     // // Construct the full file path
//     // const reportFilePath = path.join(reportDirectory, reportFileName);

//     // // Generate the text file and write the content
//     // // console.log('reportFilePath', reportFilePath)
//     // res.json({ result: 'Command executed successfully', reportFilePath });
//   });

// });

function getFilePathForAPKLeaks(toolPath, stdout, tool, apkInfo){
  let str = "** Results saved into '"
  console.log(`Stdout : --${stdout}--`)
  // let ind = stdout.indexOf(str) + str.length + 1;
  let fileName = "results.txt"
  console.log('File name : ', fileName)
  let startIndex = toolPath.split("").reverse().join("").indexOf('/')
  let toolPathWithoutTool = toolPath.split("").reverse().join("").slice(startIndex).split("").reverse().join("")
  console.log('tool path : ', toolPathWithoutTool)
  filePath = path.join(toolPathWithoutTool, fileName);
  console.log("file path : ", filePath)
  return filePath
}

app.post("/run-command", (req, res) => {
  console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
  const { tool, apkInfo } = req.body;
  console.log("apkinfo:", apkInfo);
  let toolPath = "";
  let command = "";
  let filePath = ""
  if (tool === "Androwarn") {
    toolPath = "/home/lavkush/Desktop/thesis/androwarn/androwarn.py";
    command = `python3 ${toolPath} -i "${apkInfo.filePath.replace(
      /\\/g,
      "\\\\"
    )}" -r txt -v 1`;
    // console.log(command)
    // console.log(apkInfo.filePath); // Log the filePath for debugging purposes
  } else if (tool === "Androbugs") {
    toolPath = "/home/lavkush/Desktop/thesis/androbugs2/androbugs.py";
    command = `python3 "${toolPath}" -f "${apkInfo.filePath.replace(
      /\\/g,
      "\\\\"
    )}"`;
  } else if (tool === "qark") {
    renameToApk(apkInfo.filePath);
    toolPath = "/home/lavkush/Desktop/qark/qark/qark.py";
    command = `qark --apk "${apkInfo.filePath}.apk" --report-type json`;
    console.log(command);
  } else if(tool === "APKLeaks"){
    toolPath = "/home/lavkush/tools/apkleaks/apkleaks.py";
    command = `python3 ${toolPath} -f "${apkInfo.filePath.replace(/\\/g,"\\\\")}" -o results.txt`;
  }else {
    return res.status(400).json({ error: "Invalid tool" });
  }
  console.log("Tool : ", tool);
  console.log("Tool Path:", toolPath);
  console.log("Command:", command);
  // convertZipToApk(apkInfo.filePath, 'uploads' + apkInfo.fileServerName + '.apk')

  exec(command, { stdio: "pipe" }, (error, stdout, stderr) => {
    // ...

    // Generate a unique file name\
    // console.log('Error message : ', error.message)
    // console.log('--------')
    // console.log('error : ', stderr)
    // console.log('\n\n')
    let str = "backend";
    if(tool == "APKLeaks"){
      filePath = getFilePathForAPKLeaks(toolPath, stdout, tool, apkInfo)
      // str = "[LinkFinder]";
      // return;
    }else{
      if (tool == "qark") {
        str = "Finish writing report to";
      }
      let ind = stdout.indexOf(str) + str.length + 1;
      let fileName = stdout.slice(ind);
      // let endIdx = fileName.indexOf('.csv') + 4
      // fileName = fileName.slice(0, endIdx)
      console.log(`File name : ->${fileName}<-`);
      if (tool == "Androwarn") {
        var commaInd = fileName.indexOf("'");
      } else if (tool == "Androbugs") {
        var commaInd = fileName.indexOf(" >>>");
      } else if (tool == "qark") {
        var commaInd = fileName.indexOf(".json") + 5;
      }
      fileName = fileName.slice(0, commaInd);
      console.log("filename length : ", fileName.length);
      console.log("' index : ", commaInd);
      console.log("file name : ", fileName);
      filePath = path.join(__dirname, fileName);
      console.log("file path : ", filePath);
    }

    if (fs.existsSync(filePath)) {
      console.log("in if block");
      // Set the appropriate headers for file download
      res.setHeader("Content-Type", "text/plain");
      res.setHeader(
        "Content-Disposition",
        'attachment; filename="downloaded_file.txt"'
      );
      // console.log('response :',res)
      // Stream the file to the client
      const fileStream = fs.createReadStream(filePath);
      fileStream.pipe(res);
    } else {
      console.log("in else block");
      res.status(404).json({ error: "File not found" });
    }

    // Sanitize the generated file name
    // const sanitizedFileName = fileName.replace(/[^a-zA-Z0-9]/g, '_') + '.txt';

    // // Construct the full file path within the report directory
    // const reportFilePath = path.join(reportDirectory, sanitizedFileName);

    // console.log('Sanitized File Name:', sanitizedFileName);
    // console.log('Report File Path:', reportFilePath);

    // // ... (rest of the code)

    // if (fs.existsSync(reportFilePath)) {
    //   // Set the appropriate headers for file download
    //   res.setHeader('Content-Type', 'text/plain');
    //   res.setHeader('Content-Disposition', `attachment; filename="${sanitizedFileName}"`);

    //   // Stream the file to the client
    //   const fileStream = fs.createReadStream(reportFilePath);
    //   fileStream.pipe(res);
    // } else {
    //   res.status(404).json({ error: 'File not found' });
    // }

    // res.download(filePath, 'downloaded_file.txt', (err) => {
    //   if (err) {
    //     console.error('Error downloading file:', err);
    //     res.status(500).send('Error downloading file');
    //   }
    // });
    // const timestamp = Date.now();
    // const reportFileName = `report_${timestamp}.txt`;

    // // Specify the directory where the report files will be stored
    // const reportDirectory = 'C:/Users/Admin/Desktop/react/phone/backend'; // Replace with the actual directory path

    // // Construct the full file path
    // const reportFilePath = path.join(reportDirectory, reportFileName);

    // // Generate the text file and write the content
    // // console.log('reportFilePath', reportFilePath)
    // res.json({ result: 'Command executed successfully', reportFilePath });
  });
});

// function deleteOldFiles() {
//   fs.readdir(uploadDirectory, (err, files) => {
//     if (err) {
//       console.error('Error reading upload directory:', err);
//       return;
//     }

//     // Sort the files by modified date in ascending order
//     files.sort((a, b) => {
//       return fs.statSync(uploadDirectory + a).mtime.getTime() - fs.statSync(uploadDirectory + b).mtime.getTime();
//     });

//     // Calculate the number of files to delconst express = require('express');
// const app = express();
// const cors = require('cors');
// const mongooseConnection = require('./db');
// const multer = require('multer');
// const mongoose = require('mongoose')
// const upload = multer({ dest: 'uploads/' });
// const path = require('path');
// const fs = require('fs');
// const cweSchema = require('./CWEDB')
// const AdmZip = require('adm-zip')
// // const reportDirectory = path.join(__dirname, 'reports');

// // const stagingAreaDirectory = 'staging_area/';
// const fileMapping = {}; // Maintain a mapping of original filenames to unique identifiers

// mongooseConnection();
// const maxFileCount = 10; // Maximum number of files to store
// const uploadDirectory = 'uploads/';
// app.use(function(req, res, next) {
//   res.setHeader('Access-Control-Allow-Origin', '*');
//   res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
//   res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
//   res.setHeader('Access-Control-Allow-Credentials', true);
//   next();
// });

// app.use(cors());
// app.use(express.json());

// app.get('/', (req, res) => {
//   res.send('Hello World!');
// });

// app.get('/getcwe/:cwe',async (req, res)=>{
//   // let cweNum = req.
//   const cwe = req.params.cwe
//   const cweObj = await cweSchema.findOne({"CWE_No" : cwe})
//   if(cweObj == undefined) {
//     res.json({success : false})
//     console.log('cwe object does not exist')
//     return;
//   }
//   console.log('cwe object exists')
//   res.json({success : true, data : cweObj})
// })

// app.post('/makeCWE', async (req, res) => {
//   // const cweNum = req.body
//   const cweObj = req.body;

//   cweSchema.create(cweObj).then((res) => {
//     console.log('some result', res)
//   }, (err) => {
//     console.log('error ', err)
//   })
//   res.json({'success' : true, cweObj})
// })

// const { exec } = require('child_process');

// function convertZipToApk(zipFilePath, apkFilePath) {
//   try {
//       const zip = new AdmZip(zipFilePath);
//       const zipEntries = zip.getEntries();

//       // Assuming there's only one file in the ZIP (the APK file)
//       const apkEntry = zipEntries[0];
//       const apkBuffer = apkEntry.getData();

//       fs.writeFileSync(apkFilePath, apkBuffer);
//       console.log('APK file created successfully:', apkFilePath);
//   } catch (error) {
//       console.error('Error converting ZIP to APK:', error);
//   }
// }

// function renameToApk(filePath) {
//   try {
//       // Get the file extension of the original file
//       const fileExt = path.extname(filePath);
//       console.log(`-----\nExtension : ${fileExt}\n------`)
//       // Check if the file extension is not already '.apk'
//       if (fileExt !== '.apk') {
//           // Create the new file path with '.apk' extension
//           // const newFilePath = filePath.replace(fileExt, '.apk');
//           const newFilePath = filePath + '.apk'
//           // console.log(`-----\nExtension : ${fileExt}\n------`)
//           // Rename the file
//           fs.renameSync(filePath, newFilePath);

//           console.log('File renamed successfully:', newFilePath);
//       } else {
//           console.log('File already has .apk extension:', filePath);
//       }
//   } catch (error) {
//       console.error('Error renaming file:', error);
//   }
// }

function deleteOldFiles() {
  fs.readdir(uploadDirectory, (err, files) => {
    if (err) {
      console.error("Error reading upload directory:", err);
      return;
    }

    // Sort the files by modified date in ascending order
    files.sort((a, b) => {
      return (
        fs.statSync(uploadDirectory + a).mtime.getTime() -
        fs.statSync(uploadDirectory + b).mtime.getTime()
      );
    });

    // Calculate the number of files to delete
    const filesToDeleteCount = files.length - maxFileCount;

    // Delete older files if the count exceeds the limit
    if (filesToDeleteCount > 0) {
      const filesToDelete = files.slice(0, filesToDeleteCount);

      filesToDelete.forEach((file) => {
        fs.unlink(uploadDirectory + file, (err) => {
          if (err) {
            console.error("Error deleting file:", file, err);
          } else {
            console.log("Deleted file:", file);
          }
        });
      });
    }
  });
}

// app.post('/upload-apk', upload.single('apkFile'), (req, res) => {
//   if (req.file) {
//     // File uploaded successfully
//     const { originalname, path, mimetype } = req.file;
//     console.log(path)
//     // Process the uploaded APK file and extract information
//     // Implement your APK analysis logic here to extract the necessary information
//     // You can use libraries like APKTool or AndroGuard to extract APK information

//     // Example implementation using mock APK information
//     const apkInfo = {
//       packageName: 'com.example.app',
//       versionName: '1.0',
//       permissions: ['android.permission.CAMERA', 'android.permission.ACCESS_FINE_LOCATION'],
//       filePath: path, // Assign the file path to the filePath property
//       // Add more relevant APK information
//     };

//     res.json({ apkInfo });
//   } else {
//     // No file uploaded
//     res.status(400).json({ error: 'No file uploaded' });
//   }
//   deleteOldFiles();
// });

// app.post('/delete-apk', (req, res) => {
//   const { apkIdentifier } = req.body;

//   if (!apkIdentifier) {
//     return res.status(400).json({ error: 'APK identifier is missing' });
//   }
//   const pathTillNow =  process.cwd()
//   // Construct the file path based on the unique identifier
//   const filePath = path.join(pathTillNow, uploadDirectory, apkIdentifier);
//   console.log('File path 208 : ', filePath)
//   console.log('Upload directory : ', uploadDirectory)
//   console.log('Deleting file:', filePath);
//   fs.unlink(filePath, (err) => {
//     if (err) {
//       console.error('Error deleting file:', filePath, err);
//       return res.status(500).json({ success : false, error: 'Error deleting file' });
//     }

//     console.log('Deleted file:', filePath);

//     // You can perform additional tasks here if needed
//     // ...

//     res.json({ success : true, message: 'APK deleted successfully' });
//   });
// });

// // app.post('/upload-apk', upload.single('apkFile'), (req, res) => {
// //   if (req.file) {
// //     // File uploaded successfully
// //     const { originalname, path: uploadedFilePath, mimetype } = req.file;

// //     // Process the uploaded APK file and extract information
// //     // ...

// //     // Example implementation using mock APK information
// //     const apkInfo = {
// //       packageName: 'com.example.app',
// //       versionName: '1.0',
// //       permissions: ['android.permission.CAMERA', 'android.permission.ACCESS_FINE_LOCATION'],
// //       filePath: uploadedFilePath, // Assign the uploaded file path to the filePath property
// //       // Add more relevant APK information
// //     };

// //     // Move the uploaded file to the staging area directory
// //     const newFilePath = path.join(__dirname, stagingAreaDirectory, originalname);
// //     fs.renameSync(uploadedFilePath, newFilePath); // Move the file

// //     res.json({ apkInfo });
// //   } else {
// //     // No file uploaded
// //     res.status(400).json({ error: 'No file uploaded' });
// //   }
// //   deleteOldFiles();
// // });

// // app.post('/upload-apk', upload.single('apkFile'), (req, res) => {
// //   if (req.file) {
// //     // File uploaded successfully
// //     const { originalname, path: uploadedFilePath, mimetype } = req.file;

// //     // Process the uploaded APK file and extract information
// //     // ...
// //     const apkInfo = {
// //             packageName: 'com.example.app',
// //             versionName: '1.0',
// //             permissions: ['android.permission.CAMERA', 'android.permission.ACCESS_FINE_LOCATION'],
// //             filePath: uploadedFilePath, // Assign the uploaded file path to the filePath property
// //             // Add more relevant APK information
// //           };

// //     // Generate a sanitized file name
// //     const sanitizedFileName = apkInfo.packageName.replace(/\s+/g, '_') + '_report.txt';

// //     // Construct the file path within the report directory
// //     const filePath = path.join(reportDirectory, sanitizedFileName);

// //     // Move the uploaded file to the staging area directory
// //     const newFilePath = path.join(__dirname, stagingAreaDirectory, originalname);
// //     fs.renameSync(uploadedFilePath, newFilePath); // Move the file

// //     // Update the filePath property in the apkInfo object
// //     apkInfo.filePath = filePath; // Use the sanitized file path

// //     res.json({ apkInfo });
// //   } else {
// //     // No file uploaded
// //     res.status(400).json({ error: 'No file uploaded' });
// //   }
// //   deleteOldFiles();
// // });

// app.get('/download-report', (req, res) => {
//   const { fileName } = req.query;

//   if (!fileName) {
//     return res.status(400).json({ error: 'File name is missing' });
//   }

//   // Specify the directory where the report files are stored
//   const reportDirectory = 'C:/Users/HP/Desktop/react/phone/backend'; // Replace with the actual directory path

//   // Construct the full file path
//   const filePath = path.join(reportDirectory, fileName);

//   // Check if the file exists
//   if (fs.existsSync(filePath)) {
//     // Set the appropriate headers and trigger file download
//     res.download(filePath, fileName);
//   } else {
//     res.status(404).json({ error: 'File not found' });
//   }
// });

// // ADB api
// // Endpoint to handle the ADB command execution
// // Endpoint to handle the ADB command execution
// app.get('/run-adb-command', (req, res) => {
//   const adbDevicesCommand = '/usr/bin/adb devices'; // Correct path to the adb executable

//   exec(adbDevicesCommand, (error, stdout, stderr) => {
//     if (error) {
//       console.error('Error executing ADB devices command:', error);
//       res.status(500).json({ error: 'Error executing ADB devices command' });
//       return;
//     }
//     console.log('ADB devices command output:', stdout);
//     const devicesOutput = stdout;
//     let commands = [];
//     if (devicesOutput.includes('List of devices attached')) {
//       commands = [
//         "adb shell",
//         "adb install <path_to_apk>",
//         "adb uninstall <package_name>",
//         "adb push <local_path> <remote_path>",
//         "adb pull <remote_path> <local_path>",
//         "adb reboot",
//         "adb shell dumpsys battery",
//         "adb shell pm list packages",
//         "adb shell wm size",
//         "adb shell getprop",
//         "adb shell df",
//         "adb shell top",
//         "adb shell ip address show",
//         "adb shell dumpsys sensorservice"
//       ];
//     }
//     res.json({ output: devicesOutput, commands });
//   });
// });

// // Endpoint to execute ADB commands
// app.get('/execute-adb-command/:command', (req, res) => {
//   const adbCommand = req.params.command;

//   exec(adbCommand, (error, stdout, stderr) => {
//     if (error) {
//       console.error('Error executing ADB command:', error);
//       res.status(500).json({ error: 'Error executing ADB command' });
//       return;
//     }
//     console.log('ADB command output:', stdout);
//     res.json({ output: stdout });
//   });
// });

// const port = 4000;
// app.listen(port, () => {
//   console.log(`Server is running on port ${port}`);
// });ete
//     const filesToDeleteCount = files.length - maxFileCount;

//     // Delete older files if the count exceeds the limit
//     if (filesToDeleteCount > 0) {
//       const filesToDelete = files.slice(0, filesToDeleteCount);

//       filesToDelete.forEach((file) => {
//         fs.unlink(uploadDirectory + file, (err) => {
//           if (err) {
//             console.error('Error deleting file:', file, err);
//           } else {
//             console.log('Deleted file:', file);
//           }
//         });
//       });
//     }
//   });
// }

app.post("/upload-apk", upload.single("apkFile"), (req, res) => {
  if (req.file) {
    // File uploaded successfully
    const { originalname, path, mimetype } = req.file;
    console.log(path);
    // Process the uploaded APK file and extract information
    // Implement your APK analysis logic here to extract the necessary information
    // You can use libraries like APKTool or AndroGuard to extract APK information

    // Example implementation using mock APK information
    const apkInfo = {
      packageName: "com.example.app",
      versionName: "1.0",
      permissions: [
        "android.permission.CAMERA",
        "android.permission.ACCESS_FINE_LOCATION",
      ],
      filePath: path, // Assign the file path to the filePath property
      // Add more relevant APK information
    };

    res.json({ apkInfo });
  } else {
    // No file uploaded
    res.status(400).json({ error: "No file uploaded" });
  }
  deleteOldFiles();
});

app.post("/delete-apk", (req, res) => {
  const { apkIdentifier } = req.body;

  if (!apkIdentifier) {
    return res.status(400).json({ error: "APK identifier is missing" });
  }
  const pathTillNow = process.cwd();
  // Construct the file path based on the unique identifier
  const filePath = path.join(pathTillNow, uploadDirectory, apkIdentifier);
  console.log("File path 208 : ", filePath);
  console.log("Upload directory : ", uploadDirectory);
  console.log("Deleting file:", filePath);
  fs.unlink(filePath, (err) => {
    if (err) {
      console.error("Error deleting file:", filePath, err);
      return res
        .status(500)
        .json({ success: false, error: "Error deleting file" });
    }

    console.log("Deleted file:", filePath);

    // You can perform additional tasks here if needed
    // ...

    res.json({ success: true, message: "APK deleted successfully" });
  });
});

// app.post('/upload-apk', upload.single('apkFile'), (req, res) => {
//   if (req.file) {
//     // File uploaded successfully
//     const { originalname, path: uploadedFilePath, mimetype } = req.file;

//     // Process the uploaded APK file and extract information
//     // ...

//     // Example implementation using mock APK information
//     const apkInfo = {
//       packageName: 'com.example.app',
//       versionName: '1.0',
//       permissions: ['android.permission.CAMERA', 'android.permission.ACCESS_FINE_LOCATION'],
//       filePath: uploadedFilePath, // Assign the uploaded file path to the filePath property
//       // Add more relevant APK information
//     };

//     // Move the uploaded file to the staging area directory
//     const newFilePath = path.join(__dirname, stagingAreaDirectory, originalname);
//     fs.renameSync(uploadedFilePath, newFilePath); // Move the file

//     res.json({ apkInfo });
//   } else {
//     // No file uploaded
//     res.status(400).json({ error: 'No file uploaded' });
//   }
//   deleteOldFiles();
// });

// app.post('/upload-apk', upload.single('apkFile'), (req, res) => {
//   if (req.file) {
//     // File uploaded successfully
//     const { originalname, path: uploadedFilePath, mimetype } = req.file;

//     // Process the uploaded APK file and extract information
//     // ...
//     const apkInfo = {
//             packageName: 'com.example.app',
//             versionName: '1.0',
//             permissions: ['android.permission.CAMERA', 'android.permission.ACCESS_FINE_LOCATION'],
//             filePath: uploadedFilePath, // Assign the uploaded file path to the filePath property
//             // Add more relevant APK information
//           };

//     // Generate a sanitized file name
//     const sanitizedFileName = apkInfo.packageName.replace(/\s+/g, '_') + '_report.txt';

//     // Construct the file path within the report directory
//     const filePath = path.join(reportDirectory, sanitizedFileName);

//     // Move the uploaded file to the staging area directory
//     const newFilePath = path.join(__dirname, stagingAreaDirectory, originalname);
//     fs.renameSync(uploadedFilePath, newFilePath); // Move the file

//     // Update the filePath property in the apkInfo object
//     apkInfo.filePath = filePath; // Use the sanitized file path

//     res.json({ apkInfo });
//   } else {
//     // No file uploaded
//     res.status(400).json({ error: 'No file uploaded' });
//   }
//   deleteOldFiles();
// });

app.get("/download-report", (req, res) => {
  const { fileName } = req.query;

  if (!fileName) {
    return res.status(400).json({ error: "File name is missing" });
  }

  // Specify the directory where the report files are stored
  const reportDirectory = "C:/Users/HP/Desktop/react/phone/backend"; // Replace with the actual directory path

  // Construct the full file path
  const filePath = path.join(reportDirectory, fileName);

  // Check if the file exists
  if (fs.existsSync(filePath)) {
    // Set the appropriate headers and trigger file download
    res.download(filePath, fileName);
  } else {
    res.status(404).json({ error: "File not found" });
  }
});

// ADB api
// Endpoint to handle the ADB command execution
// Endpoint to handle the ADB command execution

// Endpoint to get connected devices and available commands
app.get("/run-adb-command", (req, res) => {
  const adbDevicesCommand = "/usr/bin/adb devices"; // Correct path to the adb executable

  exec(adbDevicesCommand, (error, stdout, stderr) => {
    if (error) {
      console.error("Error executing ADB devices command:", error);
      res.status(500).json({ error: "Error executing ADB devices command" });
      return;
    }
    console.log("ADB devices command output:", stdout);

    // Parse stdout to extract the list of devices
    const devicesOutput = stdout
      .split("\n")
      .filter(
        (line) =>
          line.trim().length > 0 && !line.includes("List of devices attached")
      )
      .map((line) => line.split("\t")[0]);

    let commands = [];
    if (devicesOutput.length > 0) {
      commands = [
        // "shell",
        // "install <path_to_apk>",
        // "uninstall <package_name>",
        // "push <local_path> <remote_path>",
        // "pull <remote_path> <local_path>",
        "reboot",
        "shell dumpsys battery",
        "shell pm list packages",
        "shell wm size",
        "shell getprop",
        "shell df",
        "shell top",
        "shell ip address show",
        "shell dumpsys sensorservice",
      ];
    }
    res.json({ devices: devicesOutput, commands });
  });
});
// Endpoint to execute ADB commands
app.get("/execute-adb-command/:deviceID/:command", (req, res) => {
  const adbDeviceID = req.params.deviceID; // Get the selected device's serial number
  const adbCommand = req.params.command;

  // Construct the full ADB command with the device serial number and the provided command
  const fullAdbCommand = `adb -s ${adbDeviceID} ${adbCommand}`;

  exec(fullAdbCommand, (error, stdout, stderr) => {
    if (error) {
      console.error("Error executing ADB command:", error);
      res.status(500).json({ error: "Error executing ADB command" });
      return;
    }
    console.log("ADB command output:", stdout);
    res.json({ output: stdout });
  });
});

// nmap endpoint #########################################

const nmapCommands = {
  version: "nmap --version",
  "TCP SYN Scan": "nmap -sS",
  "TCP Connect Scan": "nmap -sV",
  "UDP Scan": "nmap -sU",
  "HOST Scan": "nmap -sn",
  // Add more nmap functionalities here
};

app.post("/run-tool", (req, res) => {
  const { tool, nmapFunctionality, ipAddress } = req.body;

  if (
    tool === "nmap" &&
    nmapFunctionality &&
    nmapCommands[nmapFunctionality] &&
    ipAddress
  ) {
    const command = `${nmapCommands[nmapFunctionality]} ${ipAddress}`;
    exec(command, (error, stdout, stderr) => {
      if (error) {
        console.error(`Error executing nmap command: ${error.message}`);
        return res
          .status(500)
          .json({ error: "An error occurred while running the tool" });
      }
      if (stderr) {
        console.error(`nmap command stderr: ${stderr}`);
      }

      res.json({ report: stdout });
    });
  } else {
    res.status(400).json({ error: "Invalid request" });
  }
});

// ###########################
const port = 4000;
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
