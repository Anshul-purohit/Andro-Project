import React from "react";
import "./Apkselection.css";
import "./CombinedToolsRun.css";
import { useState } from "react";

export default function ApkSelection({
  selectedApks,
  apkInfo,
  handleApkSelection,
  handleDropdownSelection,
  handleRunClick,
  isRunning,
  handleApkDelete,
}) {

  // Defining State variable for each tool.
  const [selectedTools, setSelectedTools] = useState({
    Androbug: false,
    Androwarn: false,
    Qark: false,
    ApkLeaks: false,
    Mobsf: false,
    Drozer: false,
    QuarkEngine: false,
    SUPERAndroidAnalyzer: false,
    VirusTotals: false,
    Fortify: false,
    VezirProject: false,
    ApkTool: false,
    nmap: false,
    BurpSuite: false,
    OWASPZAP: false,
    Bettercap: false,
    Immuniweb: false,
    zANTI: false,
    MWRLabsMercury: false,
    Mallory: false,
    Appie: false,
    AndroidTamer: false,
    Androl4b: false,
    Movexler: false,
    Objection: false,
    Andriller: false,
    FaceNiff: false,
    Magisk: false,
  });

  const handleCheckboxChange = (event) => {
    const { id, checked } = event.target;
    setSelectedTools((prevState) => ({
      ...prevState,
      [id]: checked,
    }));
  };


  // console.log("ApkInfo in frontend ", apkInfo);

  const handleDeleteSelectedApk = () => {
    if (selectedApks.length > 0) {
      handleApkDelete(selectedApks[0]); // Assuming you only want to delete the first selected APK
    }
  };

  const handleClick = () => {
    const selected = Object.keys(selectedTools).filter((key) => selectedTools[key]);
    console.log("Selected tools:", selected);
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
          <input type="checkbox" id="Androbug" onChange={handleCheckboxChange}/>
          <label htmlFor="Androbug" style={{ marginRight: '20px' }}>Androbug</label>
          <input type="checkbox" id="Androwarn" onChange={handleCheckboxChange}/>
          <label htmlFor="Androwarn" style={{ marginRight: '20px' }}>Androwarn</label>
          <input type="checkbox" id="Qark" onChange={handleCheckboxChange}/>
          <label htmlFor="Qark" style={{ marginRight: '20px' }}>Qark</label>
          <input type="checkbox" id="ApkLeaks" onChange={handleCheckboxChange}/>
          <label htmlFor="ApkLeaks" style={{ marginRight: '20px' }}>ApkLeaks</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Dynamic Anlysis</h4>
          <input type="checkbox" id="Mobsf" onChange={handleCheckboxChange}/>
          <label htmlFor="Mobsf" style={{ marginRight: '20px' }}>Mobsf</label>
          <input type="checkbox" id="Drozer" onChange={handleCheckboxChange}/>
          <label htmlFor="Drozer" style={{ marginRight: '20px' }}>Drozer</label>
          <input type="checkbox" id="Quark-Engine" onChange={handleCheckboxChange}/>
          <label htmlFor="Quark-Engine" style={{ marginRight: '20px' }}>Quark-Engine</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Malware Detection</h4>
          <input type="checkbox" id="SUPER Android Analyzer" onChange={handleCheckboxChange}/>
          <label htmlFor="SUPER Android Analyzer" style={{ marginRight: '20px' }}>SUPER Android Analyzer</label>
          <input type="checkbox" id="Virus Totals" onChange={handleCheckboxChange}/>
          <label htmlFor="Virus Totals" style={{ marginRight: '20px' }}>Virus Totals</label>
          <input type="checkbox" id="Fortify" onChange={handleCheckboxChange}/>
          <label htmlFor="Fortify" style={{ marginRight: '20px' }}>Fortify</label>
          <input type="checkbox" id="Vezir Project" onChange={handleCheckboxChange}/>
          <label htmlFor="Vezir Project" style={{ marginRight: '20px' }}>Vezir Project</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Reverse Engineering</h4>
          <input type="checkbox" id="Apk Tool" onChange={handleCheckboxChange}/>
          <label htmlFor="Apk Tool" style={{ marginRight: '20px' }}>Apk Tool</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Network/web</h4>
          <input type="checkbox" id="nmap" onChange={handleCheckboxChange}/>
          <label htmlFor="nmap" style={{ marginRight: '20px' }}>nmap</label>
          <input type="checkbox" id="BurpSuite" onChange={handleCheckboxChange}/>
          <label htmlFor="BurpSuite" style={{ marginRight: '20px' }}>BurpSuite</label>
          <input type="checkbox" id="OWASP ZAP" onChange={handleCheckboxChange}/>
          <label htmlFor="OWASP ZAP" style={{ marginRight: '20px' }}>OWASP ZAP</label>
          <input type="checkbox" id="Bettercap" onChange={handleCheckboxChange}/>
          <label htmlFor="Bettercap" style={{ marginRight: '20px' }}>Bettercap</label>
          <input type="checkbox" id="Immuniweb" onChange={handleCheckboxChange}/>
          <label htmlFor="Immuniweb" style={{ marginRight: '20px' }}>Immuniweb</label>
          <input type="checkbox" id="zANTI" onChange={handleCheckboxChange}/>
          <label htmlFor="zANTI" style={{ marginRight: '20px' }}>zANTI</label>
          <input type="checkbox" id="MWR LAbs Mercury" onChange={handleCheckboxChange}/>
          <label htmlFor="MWR LAbs Mercury" style={{ marginRight: '20px' }}>MWR LAbs Mercury</label>
          <input type="checkbox" id="Mallory" onChange={handleCheckboxChange}/>
          <label htmlFor="Mallory" style={{ marginRight: '20px' }}>Mallory</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Prenetration Testing</h4>
          <input type="checkbox" id="Appie" onChange={handleCheckboxChange}/>
          <label htmlFor="Appie" style={{ marginRight: '20px' }}>Appie</label>
          <input type="checkbox" id="Android Tamer" onChange={handleCheckboxChange}/>
          <label htmlFor="Android Tamer" style={{ marginRight: '20px' }}>Android Tamer</label>
          <input type="checkbox" id="Androl4b" onChange={handleCheckboxChange}/>
          <label htmlFor="Androl4b" style={{ marginRight: '20px' }}>Androl4b</label>
          <input type="checkbox" id="Movexler" onChange={handleCheckboxChange}/>
          <label htmlFor="Movexler" style={{ marginRight: '20px' }}>Movexler</label>
        </div>
      </div>
      <div class="card bg-light mb-3">
        <div class="card-body">
          <h4>Insecure Data Storage</h4>
          <input type="checkbox" id="Objection" onChange={handleCheckboxChange}/>
          <label htmlFor="Objection" style={{ marginRight: '20px' }}>Objection</label>
          <input type="checkbox" id="Andriller" onChange={handleCheckboxChange}/>
          <label htmlFor="Andriller" style={{ marginRight: '20px' }}>Andriller</label>
          <input type="checkbox" id="FaceNiff" onChange={handleCheckboxChange}/>
          <label htmlFor="FaceNiff" style={{ marginRight: '20px' }}>FaceNiff</label>
          <input type="checkbox" id="Magisk" onChange={handleCheckboxChange}/>
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
