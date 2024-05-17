import React from "react";
import "./Apkselection.css";
import "./CombinedToolsRun.css";

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

  const handleClick = () => {
    console.log('Button clicked!');
  };

  return (
    <>
    <div className="apk2">
        <div className="file-input-container">
            <label htmlFor="file-input" className="custom-file-upload">
            Choose File
            </label>
            <input type="file" accept=".apk" onChange={handleApkSelection} id="file-input" className="custom-file-input"/>
        </div>
    </div>
    <div className="container">
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Static Anlysis</h4>
          <input type="checkbox" id="Androbug"/>
          <label htmlFor="Androbug" style={{ marginRight: '20px' }}>Androbug</label>
          <input type="checkbox" id="Androwarn"/>
          <label htmlFor="Androwarn" style={{ marginRight: '20px' }}>Androwarn</label>
          <input type="checkbox" id="Qark"/>
          <label htmlFor="Qark" style={{ marginRight: '20px' }}>Qark</label>
          <input type="checkbox" id="ApkLeaks"/>
          <label htmlFor="ApkLeaks" style={{ marginRight: '20px' }}>ApkLeaks</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Dynamic Anlysis</h4>
          <input type="checkbox" id="Mobsf"/>
          <label htmlFor="Mobsf" style={{ marginRight: '20px' }}>Mobsf</label>
          <input type="checkbox" id="Drozer"/>
          <label htmlFor="Drozer" style={{ marginRight: '20px' }}>Drozer</label>
          <input type="checkbox" id="Quark-Engine"/>
          <label htmlFor="Quark-Engine" style={{ marginRight: '20px' }}>Quark-Engine</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Malware Detection</h4>
          <input type="checkbox" id="SUPER Android Analyzer"/>
          <label htmlFor="SUPER Android Analyzer" style={{ marginRight: '20px' }}>SUPER Android Analyzer</label>
          <input type="checkbox" id="Virus Totals"/>
          <label htmlFor="Virus Totals" style={{ marginRight: '20px' }}>Virus Totals</label>
          <input type="checkbox" id="Fortify"/>
          <label htmlFor="Fortify" style={{ marginRight: '20px' }}>Fortify</label>
          <input type="checkbox" id="Vezir Project"/>
          <label htmlFor="Vezir Project" style={{ marginRight: '20px' }}>Vezir Project</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Reverse Engineering</h4>
          <input type="checkbox" id="Apk Tool"/>
          <label htmlFor="Apk Tool" style={{ marginRight: '20px' }}>Apk Tool</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Network/web</h4>
          <input type="checkbox" id="nmap"/>
          <label htmlFor="nmap" style={{ marginRight: '20px' }}>nmap</label>
          <input type="checkbox" id="BurpSuite"/>
          <label htmlFor="BurpSuite" style={{ marginRight: '20px' }}>BurpSuite</label>
          <input type="checkbox" id="OWASP ZAP"/>
          <label htmlFor="OWASP ZAP" style={{ marginRight: '20px' }}>OWASP ZAP</label>
          <input type="checkbox" id="Bettercap"/>
          <label htmlFor="Bettercap" style={{ marginRight: '20px' }}>Bettercap</label>
          <input type="checkbox" id="Immuniweb"/>
          <label htmlFor="Immuniweb" style={{ marginRight: '20px' }}>Immuniweb</label>
          <input type="checkbox" id="zANTI"/>
          <label htmlFor="zANTI" style={{ marginRight: '20px' }}>zANTI</label>
          <input type="checkbox" id="MWR LAbs Mercury"/>
          <label htmlFor="MWR LAbs Mercury" style={{ marginRight: '20px' }}>MWR LAbs Mercury</label>
          <input type="checkbox" id="Mallory"/>
          <label htmlFor="Mallory" style={{ marginRight: '20px' }}>Mallory</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Prenetration Testing</h4>
          <input type="checkbox" id="Appie"/>
          <label htmlFor="Appie" style={{ marginRight: '20px' }}>Appie</label>
          <input type="checkbox" id="Android Tamer"/>
          <label htmlFor="Android Tamer" style={{ marginRight: '20px' }}>Android Tamer</label>
          <input type="checkbox" id="Androl4b"/>
          <label htmlFor="Androl4b" style={{ marginRight: '20px' }}>Androl4b</label>
          <input type="checkbox" id="Movexler"/>
          <label htmlFor="Movexler" style={{ marginRight: '20px' }}>Movexler</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Insecure Data Storage</h4>
          <input type="checkbox" id="Objection"/>
          <label htmlFor="Objection" style={{ marginRight: '20px' }}>Objection</label>
          <input type="checkbox" id="Andriller"/>
          <label htmlFor="Andriller" style={{ marginRight: '20px' }}>Andriller</label>
          <input type="checkbox" id="FaceNiff"/>
          <label htmlFor="FaceNiff" style={{ marginRight: '20px' }}>FaceNiff</label>
          <input type="checkbox" id="Magisk"/>
          <label htmlFor="Magisk" style={{ marginRight: '20px' }}>Magisk</label>
        </div>
      </div>
      <div>
        <button type="button" className="btn btn-primary" onClick={handleClick}>Run</button>
      </div>
    </div>
  </>
  );
}


