import React, { useState } from "react";
// import "./DynamicToolPage.css";
import "./Reverse.css";
import Instruction from "./Instruction.js";
const axios = require("axios");

const Reverse = () => {
  const [selectedTool, setSelectedTool] = useState("");
  const [apkFile, setApkFile] = useState(null);
  const [reportPath, setReportPath] = useState("");
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState();

  const handleToolChange = (event) => {
    setSelectedTool(event.target.value);
  };

  const handleFileChange = (event) => {
    setApkFile(event.target.files[0]);
  };

  const handleRun = () => {
    if (!selectedTool || !apkFile) return;
    setLoading(true);

    const formData = new FormData();
    formData.append("apkFile", apkFile);

    fetch(`http://localhost:4000/api/${selectedTool}/run`, {
      method: "POST",
      body: formData,
      responseType: "arraybuffer",
    })
      .then((res) => res.arrayBuffer())
      .then((data) => {
        // setReportPath(data.reportPath); // Save the report path received from backend
        setResult(data);
        console.log(data);
        setReportPath(true);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error running tool:", error);
        setLoading(false);
      });
  };

  const handleDownload = () => {
    // Assuming result contains the binary data of the zip file
    const blob = new Blob([result], { type: "application/zip" }); // Set MIME type to application/zip

    // Create a temporary URL for the Blob
    const blobUrl = URL.createObjectURL(blob);

    // Create an anchor element
    const link = document.createElement("a");
    link.href = blobUrl;
    link.download = "downloaded_file.zip"; // Specify the filename with .zip extension

    // Programmatically click the link to start the download
    link.click();

    // Clean up the temporary URL and anchor element
    URL.revokeObjectURL(blobUrl);
    link.remove();
  };

  // const handleDownload = () => {
  //   console.log(result);
  //   const blob = new Blob([result], { type: "application/zip" }); // Set MIME type to application/zip

  //   // Create a temporary URL for the Blob
  //   const blobUrl = URL.createObjectURL(blob);

  //   // Create an anchor element
  //   const link = document.createElement("a");
  //   link.href = blobUrl;
  //   link.download = "downloaded_file.zip"; // Specify the filename with .zip extension

  //   // Programmatically click the link to start the download
  //   link.click();

  //   // Clean up the temporary URL and anchor element
  //   URL.revokeObjectURL(blobUrl);
  //   link.remove();
  // };

  // const handleDownload = () => {
  //   if (!reportPath) return;
  //   window.location.href = `http://localhost:4000/${reportPath}`; // Download the report file
  // };

  return (
    <div className="page-containerr">
      <Instruction />
      <h2>Select a Reverse Tool</h2>
      <select
        className="tool-selectt"
        value={selectedTool}
        onChange={handleToolChange}
      >
        <option value="">Select a tool</option>
        <option value="apktool">Apk Tool</option>
        {/* Add other tool options here */}
      </select>
      <h2>Upload an APK File</h2>
      <input type="file" accept=".apk" onChange={handleFileChange} />
      {selectedTool && apkFile && (
        <div className="tool-infoo">
          <h3>Selected Tool: {selectedTool}</h3>
          <h3>Uploaded APK File: {apkFile.name}</h3>
          <button onClick={handleRun} disabled={loading}>
            {loading ? "Running..." : "Run"}
          </button>
          {reportPath && (
            <div className="report">
              <h4>Report:</h4>
              <button onClick={handleDownload}>Download Report</button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Reverse;
