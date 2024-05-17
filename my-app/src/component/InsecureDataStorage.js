import React, { useState } from "react";
import "./DynamicToolPage.css";
import Instruction from "./Instruction";
 
const InsecureDataStorage = () => {
  const [selectedTool, setSelectedTool] = useState("");
 
  const handleToolChange = (event) => {
    setSelectedTool(event.target.value);
  };
 
  return (
    <div className="page-container">
      <Instruction />
      <h2>
        Select a Insecure Data Storage, Authentication and Authorization Tool
      </h2>
      <select
        className="tool-select"
        value={selectedTool}
        onChange={handleToolChange}
      >
        <option value="">Select a tool</option>
        <option value="objection">Objection</option>
        <option value="andriller">Andriller</option>
        <option value="faceniff">FaceNiff</option>
        <option value="magisk">Magisk</option>
        {/* Add more tool options here */}
      </select>
      {selectedTool && (
        <div className="tool-info">
          <h3>Selected Tool: {selectedTool}</h3>
          {/* Render tool-specific content here */}
        </div>
      )}
    </div>
  );
};
 
export default InsecureDataStorage;