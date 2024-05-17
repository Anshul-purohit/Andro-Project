// Apkselection.js

import React from "react";
import "./Apkselection.css";

export default function ApkSelection({
  selectedApks,
  apkInfo,
  handleApkSelection,
  handleDropdownSelection,
  handleRunClick,
  isRunning,
  handleApkDelete,
}) {
  console.log("ApkInfo in frontend ", apkInfo);

  const handleDeleteSelectedApk = () => {
    if (selectedApks.length > 0) {
      handleApkDelete(selectedApks[0]); // Assuming you only want to delete the first selected APK
    }
  };

  return (
    <div className="apk-selection-container animate__animated animate__fadeIn">
      <div className="file-input-container">
        <label for="file-input" class="custom-file-upload">
          Choose File
        </label>
        <input
          type="file"
          accept=".apk"
          onChange={handleApkSelection}
          id="file-input"
          className="custom-file-input"
        />
      

      <button
        className={`btn1 btn-primary m-3 ${isRunning ? "disabled" : ""
          } animate__animated animate__fadeIn`}
        onClick={handleRunClick}
        disabled={isRunning || !apkInfo}
      >
        {isRunning ? "Running..." : "Run"}
      </button>
      </div>
       <div className="apk-container">
      {apkInfo && (
        <div className="apk-info">
          <h3>APK Information:</h3>
          <p>Package Name: {apkInfo.name}</p>
          {/* <p>Version Name: {apkInfo.versionName}</p>
          <p>
            Permissions:{" "}
            {apkInfo && apkInfo.permissions
              ? apkInfo.permissions.join(", ")
              : "N/A"}
          </p> */}
        </div>
      )}

      {selectedApks.length > 0 && (
        <div className="selected-apks-container">
          <h3>Selected APK:</h3>
          <select
            onChange={(e) => handleDropdownSelection(e.target.value)}
            value={selectedApks[0] ? selectedApks[0].name : "selectapk"}
          >
            <option value="selectapk" disabled>
              Select APK
            </option>
            {selectedApks.map((apk, index) => (
              <option key={index} value={apk.name}>
                {apk.name}
              </option>
            ))}
          </select>
          <button className="delete-button" onClick={handleDeleteSelectedApk}>
            Delete
          </button>
        </div>
      )}
      </div>
    </div>
  );
}





// import React from "react";
// import "./Apkselection.css";

// export default function ApkSelection({
//   selectedApks,
//   apkInfo,
//   handleApkSelection,
//   handleDropdownSelection,
//   handleRunClick,
//   isRunning,
//   handleApkDelete,
// }) {
//   console.log("ApkInfo in frontend ", apkInfo);

//   const handleDeleteSelectedApk = () => {
//     if (selectedApks.length > 0) {
//       handleApkDelete(selectedApks[0]); // Assuming you only want to delete the first selected APK
//     }
//   };

//   return (
//     <div className="file-input m-3 text-bar-container animate__animated animate__fadeIn">
//       <input
//         type="file"
//         accept=".apk"
//         onChange={handleApkSelection}
//         id="file-input"
//         className="custom-file-input"
//       />

// <button
//         className={`btn1 btn-primary m-3 ${isRunning ? "disabled" : ""
//           } animate__animated animate__fadeIn`}
//         onClick={handleRunClick}
//         disabled={isRunning || !apkInfo}
//       >
//         {isRunning ? "Running..." : "Run"}
//       </button>


//       {apkInfo && (
//         <div className="apk-info">
//           <h3>APK Information:</h3>
//           <p>Package Name: {apkInfo.name}</p>
//           <p>Version Name: {apkInfo.versionName}</p>
//           <p>
//             Permissions:{" "}
//             {apkInfo && apkInfo.permissions
//               ? apkInfo.permissions.join(", ")
//               : "N/A"}
//           </p>
//         </div>
//       )}

    
//       {selectedApks.length > 0 && (
//         <div className="selected-apks">
//           <h3>Selected APK:</h3>
//           <select
//             onChange={(e) => handleDropdownSelection(e.target.value)}
//             value={selectedApks[0] ? selectedApks[0].name : "selectapk"}
//           >
//             <option value="selectapk" disabled>
//               Select APK
//             </option>
//             {selectedApks.map((apk, index) => (
//               <option key={index} value={apk.name}>
//                 {apk.name}
//               </option>
//             ))}
//           </select>
//           <button className="delete-button" onClick={handleDeleteSelectedApk}>
//             Delete 
//           </button>
//         </div>
//       )}
//     </div>
//   );
// }