import React, { useState } from 'react';
import './DynamicToolPage.css';
import Instruction from './Instruction.js';
import axios from 'axios';
import './NetworkWeb.css'

const Reverse = () => {
  const [selectedTool, setSelectedTool] = useState('');
  const [selectedFunctionality, setSelectedFunctionality] = useState('');
  const [ipAddress, setIpAddress] = useState('');
  const [report, setReport] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleToolChange = (event) => {
    setSelectedTool(event.target.value);
    setSelectedFunctionality('');
    setIpAddress('');
  };

  const handleFunctionalityChange = (event) => {
    setSelectedFunctionality(event.target.value);
  };

  const handleIpAddressChange = (event) => {
    setIpAddress(event.target.value);
  };

  const runTool = () => {
    if (!selectedTool || !selectedFunctionality || !ipAddress) {
      setError('Please select a tool, functionality, and provide an IP address.');
      return;
    }

    setLoading(true);
    setError('');

    axios.post('http://localhost:4000/run-tool', {
      tool: selectedTool,
      nmapFunctionality: selectedFunctionality,
      ipAddress: ipAddress
    })
    .then(response => {
      setReport(response.data.report);
    })
    .catch(error => {
      setError('Error running tool: ' + error.message);
    })
    .finally(() => {
      setLoading(false);
    });
  };

  return (
    <div className="page-container">
      <Instruction/>
      <h2>Select a Network Web Tool</h2>
      <select className="tool-select" value={selectedTool} onChange={handleToolChange}>
        <option value="">Select a tool</option>
        <option value="nmap">nmap</option>
        <option value="BurpSuite">BurpSuite</option>
        <option value="OWASP ZAP">OWASP ZAP</option>
        <option value="bettercap">bettercap</option>
        <option value="Immuniweb">Immuniweb</option>
        <option value="zANTI">zANTI</option>
        <option value="MWR Labs Mercury">MWR Labs Mercury</option>
        <option value="Mallory">Mallory</option>
        {/* Add more tool options here */}
      </select>
      {selectedTool === 'nmap' && (
        <div>
          <select className="functionality-select" value={selectedFunctionality} onChange={handleFunctionalityChange}>
            <option value="">Select a functionality</option>
            <option value="version">Version</option>
            <option value="TCP SYN Scan">TCP SYN Scan</option>
            <option value="TCP Connect Scan">TCP Connect Scan</option>
            <option value="UDP Scan">UDP Scan</option>
            <option value="HOST Scan">HOST Scan</option>
            {/* Add more nmap functionalities here */}
          </select>
          <label>IP Address:</label>
          <input type="text" value={ipAddress} onChange={handleIpAddressChange} />
        </div>
      )}
      <button onClick={runTool} disabled={loading}>Run {selectedTool}</button>
      {loading && <p>Loading...</p>}
      {error && <p>Error: {error}</p>}
      {report && (
        <div>
          <h3>Report</h3>
          <pre>{report}</pre>
        </div>
      )}
    </div>
  );
};

export default Reverse;
