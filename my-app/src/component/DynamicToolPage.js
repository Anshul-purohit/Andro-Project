import React, { useState } from 'react';
import './DynamicToolPage.css';
import Instruction from './Instruction.js';


const DynamicToolPage= () => {
  const [selectedTool, setSelectedTool] = useState('');

  const handleToolChange = (event) => {
    setSelectedTool(event.target.value);
  };

  return (
    <div className="page-container">
      <Instruction/>

      <h2>Select a Dynamic Tool</h2>
      <select  className="tool-select" value={selectedTool} onChange={handleToolChange}>
        <option value="">Select a tool</option>
        <option value="mobsf">Mobsf</option>
        <option value="drozer">Drozer</option>
        <option value="drozer">Quark-Engine</option>
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

export default DynamicToolPage;

