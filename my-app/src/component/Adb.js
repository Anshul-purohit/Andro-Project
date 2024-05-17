import React, { useState } from "react";
import "./Adb.css"; // Import CSS file

function Adb() {
  const [output, setOutput] = useState("");
  const [loading, setLoading] = useState(false);
  const [commands, setCommands] = useState([]);
  const [devices, setDevices] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState("");

  const handleClick = async () => {
    setLoading(true);
    try {
      const response = await fetch("http://localhost:4000/run-adb-command");
      const data = await response.json();
      console.log("Response from backend:", data);
      if (data.devices && Array.isArray(data.devices)) {
        setDevices(data.devices);
      } else {
        setDevices([]); // Set devices to empty array if data.devices is not valid
      }
      setCommands(data.commands);
    } catch (error) {
      console.error("Error:", error);
    }
    setLoading(false);
  };

  const handleDeviceSelect = (device) => {
    setSelectedDevice(device);
  };

  const handleCommandClick = async (command) => {
    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:4000/execute-adb-command/${selectedDevice}/${command}`
      );
      const data = await response.json();
      setOutput(data.output);
    } catch (error) {
      console.error("Error:", error);
    }
    setLoading(false);
  };

  console.log("Devices:", devices); // Add this line to check devices array

  return (
    <div className="container1">
      <button onClick={handleClick} disabled={loading}>
        View Connected devices
      </button>
      {loading && <p className="loading">Loading...</p>}
      <div className="devices-container">
        {devices.length > 0 &&
          devices.map((device, index) => (
            <button
              key={index}
              onClick={() => handleDeviceSelect(device)}
              className={selectedDevice === device ? "selected" : ""}
            >
              {device} {/* Render the device name here */}
            </button>
          ))}
      </div>

      <div className="button-container">
        {commands.map((command, index) => (
          <button key={index} onClick={() => handleCommandClick(command)}>
            {getCommandLabel(command)}
          </button>
        ))}
      </div>
      <div className="output-container">
        {output && (
          <div className="output">
            {/* <h3>ADB Command Output:</h3> */}
            <pre>{output}</pre>
          </div>
        )}
      </div>
    </div>
  );
}

function getCommandLabel(command) {
  switch (command) {
    case "shell":
      return "Shell";
    case "install <path_to_apk>":
      return "Install APK";
    case "uninstall <package_name>":
      return "Uninstall Package";
    case "push <local_path> <remote_path>":
      return "Push File";
    case "pull <remote_path> <local_path>":
      return "Pull File";
    case "reboot":
      return "Reboot";
    case "shell dumpsys battery":
      return "Dump Battery";
    case "shell pm list packages":
      return "Installed Packages";
    case "shell wm size":
      return "Window Size";
    case "shell getprop":
      return "Properties Of Device";
    case "shell df":
      return "Device's storage partitions";
    case "shell top":
      return "Real-time CPU and Memory Usage";
    case "shell ip address show":
      return "Show IP Address";
    case "shell dumpsys sensorservice":
      return "Sensors Available";
    default:
      return command;
  }
}

export default Adb;
