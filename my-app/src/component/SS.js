import React from 'react'
import ScreenSharingButton from './ScreenSharingButton';

export default function SS() {
    const handleStartSharing = () => {
        // Add your logic here to initiate screen sharing
        // This is where you would establish socket connections or trigger other actions
        console.log("Starting screen sharing...");
      }
  return (
    <div className="App">
    <header className="App-header">
      <h1>Screen Sharing App</h1>
      <ScreenSharingButton onStartSharing={handleStartSharing} />
    </header>
  </div>
  )
}
