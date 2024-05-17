import React from "react";
import Note from "./Note";
import Tooldropdown from "./Tooldropdown";
import { Outlet } from "react-router-dom";
 
// export default function StaticToolpage() {
//   return (
//     // <></><>
//     <>
//       {/* <h1>dsafdsaf</h1> */}
//       {/* <Tooldropdown selectedTool='static' /> */}
//       <Note />
//     </>
//   );
// }
 
export default function StaticToolpage() {
  console.log("something");
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
        <Note />
        <Outlet />
      </div>
    </div>
  );
}
 