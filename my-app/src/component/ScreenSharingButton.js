import React from 'react';

const ScreenSharingButton = ({ onStartSharing }) => {
  return (
    <button onClick={onStartSharing}>Start Screen Sharing</button>
  );
}

export default ScreenSharingButton;