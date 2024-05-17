import React from 'react';
import './Toolinfo.css';

export default function ToolInfo({ toolInfo }) {
  return (
    <div className="tool-info m-3">
      {toolInfo && (
        <div className='tool'>
          <h3>Tool Information:</h3>
          <p>{toolInfo}</p>
        </div>
      )}
    </div>
  );
}
