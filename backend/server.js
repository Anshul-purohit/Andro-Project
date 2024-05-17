const fs = require("fs");
const path = require("path");
const express = require("express");
const bodyParser = require("body-parser");
const multer = require("multer");
const { exec } = require("child_process");
const AdmZip = require("adm-zip");

const app = express();
const router = express.Router();
const PORT = process.env.PORT || 4000;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

const upload = multer({ dest: "uploads/" });

router.post("/", upload.single("apkFile"), (req, res) => {
  console.log("--------------------------------");
  console.log(req.file);
  const apkFilePath = req.file.path;

  if (!apkFilePath) {
    return res.status(400).json({ error: "No APK file uploaded" });
  }

  exec(`apktool d ${apkFilePath}`, (error, stdout, stderr) => {
    if (error) {
      console.error("Error running apktool:", error);
      return res.status(500).json({ error: "Error running apktool" });
    }

    // Extract the directory path from the output
    const outputName = stdout
      .split("\n")
      .find((line) => line.startsWith("I: Using Apktool"))
      .split(" ")[5];
    const outputPath = outputName + ".out";
    if (!outputPath) {
      console.error("Error extracting output path");
      return res.status(500).json({ error: "Error extracting output path" });
    }
    console.log("output path : ", outputPath);
    const reportFolder = path.join(__dirname, outputPath);
    const reportZip = `${outputName}.zip`;
    console.log(`reportFolder :${reportFolder}`);
    const zipPath = path.join(__dirname, reportZip);
    // Create a zip file containing the report folder
    const zip = new AdmZip();
    zip.addLocalFolder(reportFolder);
    zip.writeZip(zipPath);
    console.log("Full path : ", zipPath);
    // Send the path of the zip file to the frontend

    if (fs.existsSync(zipPath)) {
      console.log("in if block");
      // Set the appropriate headers for file download
      res.setHeader("Content-Type", "application/zip");
      res.setHeader(
        "Content-Disposition",
        'attachment; filename="downloaded_file.zip"'
      );

      // Stream the zip file to the client
      const fileStream = fs.createReadStream(zipPath);
      fileStream.pipe(res);
    } else {
      console.log("in else block");
      res.status(404).json({ error: "File not found" });
    }
  });
});

module.exports = router;
