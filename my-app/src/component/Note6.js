import React, { useState, useEffect } from 'react';
import axios from 'axios';
// import './action.css';
import 'animate.css';
import { Accordion, Card, Button } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS
// import cwedata from '../cwe.json';

export default function Dropdown() {
  const [selectedItem, setSelectedItem] = useState('');
  const [result, setResult] = useState('');
  const [isRunning, setIsRunning] = useState(false);
  const [toolInfo, setToolInfo] = useState('');
  const [reportFilePath, setReportFilePath] = useState('');
  const [apkInfo, setApkInfo] = useState(null); // State to store APK information
  const [reportContent, setReportContent] = useState('');
  const [reportSections, setReportSections] = useState([]);
  const [expandedSections, setExpandedSections] = useState([true]);
  const [selectedApks, setSelectedApks] = useState([]);
  const [selectedDropdownItem, setSelectedDropdownItem] = useState('');
  const [selectedApk, setSelectedApk] = useState(null);
  

  // const [reportAlreadyCached, setReportAlreadyCached] = useState(false);
  // const [apkUploaded, setApkUploaded] = useState(false);



  const handleItemClick = (item) => {
    setSelectedItem(item);
    setSelectedDropdownItem(item);
    setToolInfo(getToolInfo(item));
    setReportSections([]);
    setReportContent('');

    setExpandedSections((prevExpandedSections) => {
      const resetExpandedSections = prevExpandedSections.map(() => false);
      resetExpandedSections[0] = true; // Expand the first section
      return resetExpandedSections;
    });

    if (item === 'Androwarn') {
      setToolInfo(
        <>
            <h3>Androwarn</h3>
            <p>
              Androwarn is an open-source static code analyzer designed specifically for detecting and assessing the security risks in Android applications. It is commonly used by security researchers, application developers, and penetration testers to identify potential vulnerabilities and weaknesses in Android apps.
            </p>
            <p>
              Key Features:
            </p>
            <ul>
              <li>Detects insecure data storage</li>
              <li>Identifies permission misuse</li>
              <li>Finds potential component exposure</li>
              <li>Identifies other security vulnerabilities</li>
            </ul>
          </>
      );
    } else if (item === 'Androbugs') {
      setToolInfo(
        <>
        <h3>AndroBugs</h3>
        <p>
          AndroBugs uses static analysis techniques to inspect an Android app's code and identify issues such as insecure data storage, permissions misuse, component exposure, and other security vulnerabilities. By identifying these weaknesses, developers can take appropriate measures to secure their applications and protect user data.
        </p>
        <p>
          Key Features:
        </p>
        <ul>
          <li>Detects insecure data storage</li>
          <li>Identifies permission misuse</li>
          <li>Finds potential component exposure</li>
          <li>Identifies other security vulnerabilities</li>
        </ul>
      </>
      );
    } else if (item === 'Mobsf') {
      setToolInfo(
        'MOBSF (Mobile Security Framework) is an open-source framework and automated tool designed for mobile app security testing. It assists in identifying potential security vulnerabilities and weaknesses in mobile applications. MOBSF is primarily focused on Android and iOS platforms.'
      );
    } else {
      setToolInfo('');
    }
  };

  // ################ on change function for choose file button #######################
  const handleApkSelection = async (event) => {
    const selectedApk = event.target.files[0];
    console.log('apk selected in handle apk selection:', selectedApk);
  
    if (selectedApk) {
      // Check if the APK with the same name is already selected
      const apkAlreadySelected = selectedApks.some((apk) => apk.name === selectedApk.name);
  
      if (!apkAlreadySelected) {
        setSelectedApks((prevSelectedApks) => {
          console.log('prev apks:', prevSelectedApks);
  
          // Check if the limit has been reached
          if (prevSelectedApks.length >= 5) {
            // Remove the oldest APK (first item in the array)
            const newSelectedApks = prevSelectedApks.slice(1);
  
            // Add the new selected APK to the end of the array
            return [...newSelectedApks, selectedApk];
          } else {
            // Add the new selected APK to the existing array
            return [...prevSelectedApks, selectedApk];
          }
        });
      } else {
        console.log('APK with the same name already selected.');
      }
    }
  };
  


  // ############################## Changing dropdown item ############################## 
  const handleDropdownSelection = async (event) => {
    // console.log('Dropdown value changed !!!!!!!!!!!!')
    const selectedApkIndex = event.target.selectedIndex - 1;
    console.log('index : ', selectedApkIndex)
    const apkSelected = selectedApks[selectedApkIndex]
    console.log('apk selected : ', apkSelected)
    // console.log(event.target.selectedIndex)
    // console.log('apk selected in handle dropdown :', selectedApk);
    setSelectedApk(apkSelected);
    const formData = new FormData();
    formData.append('apkFile', apkSelected);

    try {
      const response = await axios.post('http://localhost:4000/upload-apk', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      console.log('File uploaded successfully:', response.data);
      const apkInfo = response.data.apkInfo;
      console.log('APK Info:', apkInfo);
      setApkInfo(apkInfo);


    } catch (error) {
      console.error('Error uploading file:', error);
    }
    setSelectedDropdownItem(apkSelected);
  }

  const handleDownload = () => {
    const blob = new Blob([result], { type: 'text/plain' });

    // Create a temporary URL for the Blob
    const blobUrl = URL.createObjectURL(blob);

    // Create an anchor element
    const link = document.createElement('a');
    link.href = blobUrl;
    link.download = 'downloaded_file.txt'; // Specify the filename

    // Programmatically click the link to start the download
    link.click();

    // Clean up the temporary URL and anchor element
    URL.revokeObjectURL(blobUrl);
    link.remove();
  };
  // Assuming you have the reportContent from the server response
// const reportContent = response.data.reportContent;

// Store the reportContent in the cache using the selectedDropdownItem.name as the key

  // Function to check if the report is already cached for the selected APK
  // const checkReportCache = () => {
  //   const cachedReport = localStorage.getItem(selectedDropdownItem.name);
  //   return cachedReport !== null;
  // };





  const handleRunClick = () => {
    if (!apkInfo) {
      console.error('APK file is not uploaded yet.');
      return;
    }

    // const cachedReport = localStorage.getItem(selectedDropdownItem.name);

    // if (cachedReport) {
    //   // If the report is already cached, display it and show a notification
    //   setReportContent(cachedReport);
    //   setReportAlreadyCached(true);
    //   alert('The report is already generated and available for download.');
    //   return;
    // }

    // const sampleCachedReport = "   ";
   
    // const selectedApkInfo = selectedApks.find((apk) => apk.name === selectedItem);
    const selectedApk = selectedApks.find((apk) => apk === selectedDropdownItem);

    setIsRunning(true);
    axios
      .post('http://localhost:4000/run-command', {
        tool: selectedItem,
        apkInfo: apkInfo,
      })
      .then((response) => {
        // console.log(response.data);
        setResult(response.data || 'No result available');
        // console.log('file path : ', response.data.reportFilePath)
        // console.log(response.data)
        generateSections(response.data); // Generate sections from the report content
        // if (response.data.reportFilePath) {
        //   axios
        //     .get(`http://localhost:4000/${response.data.reportFilePath}`)
        //     .then((response) => {
        //       console.log('Report Content:', response.data);
        //       setReportContent(response.data);
        //     })
        //     .catch((error) => {
        //       console.error('Error fetching report content:', error);
        //     });
        // }
        // if (response.data.reportContent) {
        //   localStorage.setItem(selectedDropdownItem.name, response.data.reportContent);
        //   setReportAlreadyCached(true);

        // }

      })
      .catch((error) => {
        console.error(error);
      })
      .finally(() => {
        setIsRunning(false);
      });
      // localStorage.setItem(selectedDropdownItem.name, sampleCachedReport);
  };
  // useEffect(() => {
  //   const cachedReport = localStorage.getItem(selectedDropdownItem.name);

  //   if (cachedReport) {
  //     setReportContent(cachedReport);
  //     setReportSections([]); // Reset sections since they may not match the cached report
  //     setReportAlreadyCached(true);
  //   } else {
  //     setReportContent('');
  //     setReportAlreadyCached(false);
  //   }
  // }, [selectedDropdownItem.name]);


  // const isReportCached = () => {
  //   // Check if there is a cached report for the selected APK
  //   const cachedReport = localStorage.getItem(selectedDropdownItem.name);
  //   return cachedReport !== null;
  // };


  const generateSectionsForAndrowarn = (reportContent) => {
    const delimiter = '[+]';
    const subSectionDelimiter = '[.]';
    const sections = reportContent.split(delimiter);
    const parsedSections = [];
  
    for (let i = 1; i < sections.length; i++) {
      const subsections = sections[i].split(subSectionDelimiter);
      const title = subsections[0].trim();
      const content = subsections.slice(1).map(subsection => subsection.trim()).join('\n');
  
      parsedSections.push({ title, content, subSections: subsections.slice(1) });
    }
  
    return parsedSections;
  };


  const generateSectionsForAndrobugs = (reportContent) => {
    const delimiter = /(\[Critical\]|\[Warning\]|\[Notice\]|\[Info\])/;
    const sections = reportContent.split(delimiter);
    const parsedSections = [];

    let currentTitle = '';
    let combinedContent = '';
    // console.log('sections : ', sections)

    for (let i = 1; i < sections.length; i += 2) {
      let title = sections[i].trim();
      let content = sections[i + 1].trim();
      title = title.slice(1, title.length-1)
      
      // if(cwedata[title]){ 
      //   console.log('title', title)
      //   // console.log(`content :${content}`)
        // console.log(JSON.stringify(content))
      //   // console.log('json', cwedata[title])
      //   // console.log('json', cwedata[title][content])
      //   // console.log('json : ', cwedata[title][content]["CWE_No"] )
      // }
      // console.log('json', cwedata[title])
      // if (currentTitle === title) {
      //   // If the title is the same as the previous section, append the content
      //   if(cwedata[title] && cwedata[title][content]){
      //     console.log('Number : ', cwedata[title][content]["CWE_No"])
      //     content = cwedata[title][content]["CWE_No"] + " " + content
      //   }
      //   combinedContent += '\n' + content;
      // } else {
      //   // If the title is different, push the previous section and start a new one
      //   if (currentTitle) {
      //     if(cwedata[title] && cwedata[title][content]){
      //       console.log('Number : ', cwedata[title][content]["CWE_No"])
      //       content = cwedata[title][content]["CWE_No"] + " " + content
      //     }
      //     parsedSections.push({ title: currentTitle, content: combinedContent });
      //   }
      //   currentTitle = title;
      //   if(cwedata[title] && cwedata[title][content]){
      //     console.log('Number : ', cwedata[title][content]["CWE_No"])
      //     content = cwedata[title][content]["CWE_No"] + " " + content
      //   }
      //   combinedContent = content;
      // }
      // if(cwedata[title]){
      //   content = cwedata[title][content] + " --- " + content
      // }
    }

    // Push the last section after the loop
    if (currentTitle) {
      parsedSections.push({ title: currentTitle, content: combinedContent });
    }

    return parsedSections;
  };


  const generateSections = (reportContent) => {
    let delimiter;
    let titleRegex;
    let parsedSections;
  
    if (selectedItem === 'Androwarn') {
      parsedSections = generateSectionsForAndrowarn(reportContent);
    } else if (selectedItem === 'Androbugs') {
      parsedSections = generateSectionsForAndrobugs(reportContent);
    } else {
      delimiter = '[+]'; // Default delimiter
      titleRegex = /^(.+)/;
    }

    // const cweNumbers = cwedata[selectedItem]; // This will give you the CWE numbers for critical and warning points

    // // Add CWE numbers to each section
    // parsedSections.forEach((section) => {
    //   const cweNumber = cweNumbers[section.title];
    //   if (cweNumber) {
    //     section.cweNumber = cweNumber;
    //   }
    // });
  
    // ...
  
    setReportSections(parsedSections);
    setExpandedSections(new Array(parsedSections.length).fill(false));
    return parsedSections;
  };
  


  const getToolInfo = () => {
    // Return the corresponding tool information based on the selected item
    // switch (selectedItem) {
    //   case 'Androwarn':
    //     return (
    //       <>
    //         <h3>Androwarn</h3>
    //         <p>
    //           Androwarn is an open-source static code analyzer designed specifically for detecting and assessing the security risks in Android applications. It is commonly used by security researchers, application developers, and penetration testers to identify potential vulnerabilities and weaknesses in Android apps.
    //         </p>
    //         <p>
    //           Key Features:
    //         </p>
    //         <ul>
    //           <li>Detects insecure data storage</li>
    //           <li>Identifies permission misuse</li>
    //           <li>Finds potential component exposure</li>
    //           <li>Identifies other security vulnerabilities</li>
    //         </ul>
    //       </>
    //     );

    //   case 'Androbugs':
    //     return (
    //       <>
    //         <h3>AndroBugs</h3>
    //         <p>
    //           AndroBugs uses static analysis techniques to inspect an Android app's code and identify issues such as insecure data storage, permissions misuse, component exposure, and other security vulnerabilities. By identifying these weaknesses, developers can take appropriate measures to secure their applications and protect user data.
    //         </p>
    //         <p>
    //           Key Features:
    //         </p>
    //         <ul>
    //           <li>Detects insecure data storage</li>
    //           <li>Identifies permission misuse</li>
    //           <li>Finds potential component exposure</li>
    //           <li>Identifies other security vulnerabilities</li>
    //         </ul>
    //       </>
    //     );

    //   case 'Mobsf':
    //     return (
    //       <>
    //         <h3>MOBSF (Mobile Security Framework)</h3>
    //         <p>
    //           MOBSF is an open-source framework and automated tool designed for mobile app security testing. It assists in identifying potential security vulnerabilities and weaknesses in mobile applications, primarily focused on Android and iOS platforms.
    //         </p>
    //         <p>
    //           Key Features:
    //         </p>
    //         <ul>
    //           <li>Comprehensive security testing for mobile apps</li>
    //           <li>Supports both Android and iOS platforms</li>
    //           <li>Identifies security vulnerabilities and weaknesses</li>
    //           <li>Generates detailed security reports</li>
    //         </ul>
    //       </>
    //     );

    //   default:
    //     return '';
    // }
  };


  return (

    <div className="dropdown-container">
      {/* <div className="instructions">
        <h3>Step-by-Step Guide: How to Run the Tool</h3>
        <p>1. Select a tool from the dropdown menu.</p>
        <p>2. Click the "Choose File" button to upload an APK file.</p>
        <p>3. After uploading, click the "Run" button to start the analysis.</p>
        <p>4. View the generated report and download it if needed.</p>
      </div> */}

      <div className="dropdown m-3">
        <button
          className={`btn btn-primary dropdown-toggle ${isRunning ? 'disabled' : ''}`}
          type="button"
          id="dropdownMenuButton1"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          {selectedItem ? selectedItem : 'Select Tool '}
          {/* <i className="fas fa-chevron-down"></i> Font Awesome dropdown icon */}
        </button>
        {toolInfo && (
          <div className="tool-info m-3">
            <h3>Tool Information:</h3>
            <p>{toolInfo}</p>
          </div>
        )}
        <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton1">
          <li>
            <button className="dropdown-item" onClick={() => handleItemClick('Androwarn')}>
              Androwarn
            </button>
          </li>
          <li>
            <button className="dropdown-item" onClick={() => handleItemClick('Androbugs')}>
              Androbugs
            </button>
          </li>
          <li>
            <button className="dropdown-item" onClick={() => handleItemClick('Mobsf')}>
              Mobsf
            </button>
          </li>
        </ul>
      </div>

      {/* {toolInfo && (
        <div className="tool-info m-3">
          <h3>Tool Information:</h3>
          <p>{toolInfo}</p>
        </div>

      )} */}


 



      {selectedItem && (
        <div className="file-input m-3 text-bar-container animate__animated animate__fadeIn">
          <input type="file" accept=".apk" onChange={handleApkSelection} id="file-input" className="custom-file-input" />

          {/* Display APK information here */}
          {apkInfo && (
            <div className="apk-info ">
              <h3>APK Information:</h3>
              <p>Package Name: {apkInfo.name}</p>
              <p>Version Name: {apkInfo.versionName}</p>
              <p>Permissions: {apkInfo && apkInfo.permissions ? apkInfo.permissions.join(', ') : 'N/A'}</p>
              {/* Add more relevant APK information */}
            </div>
          )}
          <button
            className={`btn1 btn-primary m-3 ${isRunning ? 'disabled' : ''} animate__animated animate__fadeIn`}
            onClick={handleRunClick}
            disabled={isRunning || !apkInfo}
          >
            {isRunning ? 'Running...' : 'Run'}
          </button>
        </div>
      )}


      {selectedApks.length > 0 && (
        <div className="selected-apks">
          <h3>Selected APKs:</h3>
          <select value={selectedDropdownItem.name} onChange={handleDropdownSelection}>
            <option key={-1} value="selectapk">Select APK</option>
            {selectedApks.map((apk, index) => (
              <option key={index} value={apk.name}>
                {apk.name}
              </option>
            ))}
          </select>


          <ul className="apk-list">
            {selectedApks.map((apk, index) => (
              <li key={index}>{apk.name}</li>
            ))}
          </ul>
        </div>
      )}

 {/* Display a message if the report is already cached */}
 


      {/* {result && (
        <div className="result-container animate__animated animate__fadeIn">
          <h2>Result:</h2>
          <pre>{result}</pre>
        </div>
      )} */}
    {reportSections.length > 0 && (
  <div className="report-sections">
    {reportSections.map((section, index) => (
      <div className="section" key={index}>
        <div
          className="section-header"
          onClick={() => {
            setExpandedSections((prevExpandedSections) => {
              const updatedExpandedSections = [...prevExpandedSections];
              updatedExpandedSections[index] = !prevExpandedSections[index];
              return updatedExpandedSections;
            });
          }}
        >
          {expandedSections[index] ? '[-] ' : '[+] '}
          {section.title}
          {section.cweNumber && <span className="cwe-number">CWE No: {section.cweNumber}</span>}
        </div>
        {expandedSections[index] && (
          <div className="section-content">
            <pre>{section.content}</pre>
            {section.subSections > 0 && (
              <div className="subsection-header" onClick={(e) => e.stopPropagation()}>
                {section.subSections} Subsection{section.subSections > 1 ? 's' : ''}:
                <div
                  className="toggle-icon"
                  onClick={() => {
                    setExpandedSections((prevExpandedSections) => {
                      const updatedExpandedSections = [...prevExpandedSections];
                      updatedExpandedSections[index] = !prevExpandedSections[index];
                      return updatedExpandedSections;
                    });
                  }}
                >
                  {expandedSections[index] ? '-' : '+'}
                </div>
              </div>
            )}
            {expandedSections[index] && section.subSections > 0 && (
              <div className="subsection-content">
                {section.subSections > 0 && (
                  <pre>{section.subSections.join('\n')}</pre>
                )}
              </div>
            )}
          </div>
        )}
      </div>
    ))}
  </div>
)}



      {selectedItem && (
        <div className="report-container m-3">
          <button className="btnn btn-primary" onClick={handleDownload}>
            Download Report
          </button>
        </div>
      )}
      {reportContent && (
        <div className=" report-content-container m-3">
          {/* <h2>Report Content:</h2>
          <pre>{reportContent}</pre> */}
        </div>
      )}
      {reportContent && (
        <div className="report-content-container m-3">
          {/* <h2>Report Content:</h2>
          <pre>{reportContent}</pre> */}
        </div>
      )}
      {/* Display a message if the report is already cached and the user has uploaded an APK */}
 {/* {reportAlreadyCached && apkUploaded && selectedItem && (
        <div className="report-cache-msg-container m-3">
          <p className="report-cache-msg">The report is already generated and available for download.</p>
        </div>
      )} */}


      {/* Report Sections */}
      {/* {console.log(reportSections)} */}

    </div>
  );
}