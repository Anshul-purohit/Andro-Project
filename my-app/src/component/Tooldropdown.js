import React, { useState } from "react";
import {
  Routes,
  Route,
  BrowserRouter as Router,
  Outlet,
} from "react-router-dom";
import StaticToolpage from "./StaticToolpage";
import DynamicToolPage from "./DynamicToolPage";
import MalwareDetection from "./MalwareDetection";
import NetworkWeb from "./NetworkWeb";
import Pentesting from "./Pentesting";
import Reverse from "./Reverse";
import InsecureDataStorage from "./InsecureDataStorage";
import "./Tooldropdown.css";
// import Pic from './component/pen.jpeg';
import penImage from "./Pen.jpeg";
 
// export default function App() {
//   return (
//     <Router>
//       <Routes>
//         <Route path="/" element={<Tooldropdown />} />
//         <Route path="/static-tool" element={<StaticToolpage />} />
//         <Route path="/dynamic-tool" element={<DynamicToolPage />} />
//       </Routes>
//     </Router>
//   );
// }
 
export default function Tooldropdown(props) {
  console.log("selected tool", props);
 
  let toolSelected = props.selectedTool;
  let toolContent = <></>;
 
  if (toolSelected === "static") {
    toolContent = <StaticToolpage />;
  } else if (toolSelected === "dynamic") {
    toolContent = <DynamicToolPage />;
  } else if (toolSelected === "malware") {
    toolContent = <MalwareDetection />;
  } else if (toolSelected === "reverse") {
    toolContent = <Reverse />;
  } else if (toolSelected === "network-web") {
    toolContent = <NetworkWeb />;
  } else if (toolSelected === "pentesting-environments") {
    toolContent = <Pentesting />;
  } else if (toolSelected === "insecure-data-storage") {
    toolContent = <InsecureDataStorage />;
  }
 
  return (
    <div className="page-container">
      {/* <h1>APK Analysis Tool</h1> */}
 
      {/* <img
        src={penImage}
        alt="Top Image"
        className="top-image"
      /> */}
      <div className="top">
        <h3>How to Run the Tool</h3>
        <p>1. Select a tool from the dropdown menu</p>
        <p>2. Click the "Choose File" button to upload an APK file.</p>
        <p>3. After uploading, click the "Run" button to start the analysis.</p>
        <p>
          4. View the generated report and download it using the download report
          button
        </p>
      </div>
      <div className="content">
        {toolContent}
        <Outlet />
      </div>
    </div>
  );
}