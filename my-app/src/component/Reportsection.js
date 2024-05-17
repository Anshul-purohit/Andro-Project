import React, { useState } from "react";
import "./Reportsection.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faAngleDown,
  faAngleUp,
  faFileDownload,
} from "@fortawesome/free-solid-svg-icons";

export default function Reportsections({
  reportsections,
  expandedSections,
  handleDownload,
  setExpandedSections,
}) {
  console.log("reportsections prop:", reportsections);
  // const [expandedSubSections, setExpandedSubSections] = useState([]);
  const [expandedSubSections, setExpandedSubSections] = useState(
    new Array(reportsections.length).fill([])
  );

  const toggleSection = (index) => {
    setExpandedSections((prevExpandedSections) => {
      const updatedExpandedSections = [...prevExpandedSections];
      updatedExpandedSections[index] = !prevExpandedSections[index];
      return updatedExpandedSections;
    });
  };

  const toggleSubSection = (sectionIndex, subSectionIndex) => {
    setExpandedSubSections((prevExpandedSubSections) => {
      const updatedExpandedSubSections = [...prevExpandedSubSections];
      updatedExpandedSubSections[sectionIndex] = [
        ...(updatedExpandedSubSections[sectionIndex] || []),
      ];
      updatedExpandedSubSections[sectionIndex][subSectionIndex] =
        !updatedExpandedSubSections[sectionIndex][subSectionIndex];
      return updatedExpandedSubSections;
    });
  };

  console.log("report ---->", reportsections);

  return (
    <div className="report-container">
      {reportsections.map((section, index) => (
        <div className="section" key={index}>
          <div
            className={`section-header ${
              expandedSections[index] ? "open" : ""
            }`}
            onClick={() => {
              toggleSection(index);
            }}
          >
            <div className="title">{section.title}</div>
            <div className="toggle-icon">
              {expandedSections[index] ? "-" : "+"}
            </div>
          </div>
          {Boolean(expandedSections[index]) ? (
            <div
              className={`section-content ${
                expandedSections[index] ? "open" : ""
              }`}
            >
              {/* {
                section.map((s, idx) => {
                  return (<div>
                    <div>{s.c}</div>
                    <pre>{s.content}</pre>
                  </div>)
                })
              } */}
              <pre>{section.content}</pre>
              {section.subSections && section.subSections.length > 0 && (
                <div
                  className="subsection-header"
                  onClick={(e) => e.stopPropagation()}
                >
                  {/* {section.subSections.length} Subsection */}
                  {/* {section.subSections.length > 1 ? "s" : ""}: */}
                  {/* <div
                    className="toggle-icon"
                    onClick={() => {
                      toggleSection(index);
                    }}
                  >
                    {expandedSections[index] ? "-" : "+"}
                  </div> */}
                </div>
              )}
              {expandedSections[index] &&
                section.subSections &&
                section.subSections.length > 0 && (
                  <div className="subsection-content">
                    {section.subSections.map((subsection, subIndex) => (
                      <div key={subIndex}>
                        <div
                          className={`subsection-title ${
                            expandedSubSections[index] &&
                            expandedSubSections[index][subIndex]
                              ? "open"
                              : ""
                          }`}
                          onClick={() => {
                            toggleSubSection(index, subIndex);
                            console.log("Section Title:", section.title);
                            console.log("Subsection Title:", subsection.title);
                            console.log(
                              "Subsection Content:",
                              subsection.content
                            );
                            console.log(
                              "Expanded Subsections State:",
                              expandedSubSections
                            );
                          }}
                        >
                          <div className="title-container">
                            <span className="title">{subsection.title}</span>
                            <span className="cwe1"> {subsection.cwe}</span>
                          </div>
                          <div className="toggle-icon">
                            {expandedSubSections[index] &&
                            expandedSubSections[index][subIndex]
                              ? "-"
                              : "+"}
                          </div>
                        </div>
                        <div
                          className={`subsection-content ${
                            expandedSubSections[index] &&
                            expandedSubSections[index][subIndex]
                              ? "open"
                              : ""
                          }`}
                        >
                          {expandedSubSections[index] &&
                          expandedSubSections[index][subIndex] ? (
                            <pre>{subsection.content}</pre>
                          ) : null}
                        </div>
                      </div>
                    ))}
                  </div>
                )}
            </div>
          ) : (
            <></>
          )}
        </div>
      ))}
      <button className="btnn btn-primary" onClick={handleDownload}>
        <FontAwesomeIcon icon={faFileDownload} /> Download Report
      </button>
    </div>
  );
}

////////////1111111
//////another part//////
// import React from 'react';
// import "./Reportsection.css";
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { faAngleDown, faAngleUp, faFileDownload } from '@fortawesome/free-solid-svg-icons';

// export default function Reportsections({ reportsections, expandedSections, handleDownload, setExpandedSections }) {
//   console.log('Reportsections Data:', reportsections);
//   return (
//     <div className="report-sections">
//       {reportsections.map((section, index) => (
//         <div className="section" key={index}>
//           <div
//             className={`section-header ${expandedSections[index] ? 'open' : ''}`}
//             onClick={() => {
//               setExpandedSections((prevExpandedSections) => {
//                 const updatedExpandedSections = [...prevExpandedSections];
//                 updatedExpandedSections[index] = !prevExpandedSections[index];
//                 return updatedExpandedSections;
//               });
//               console.log(`Section ${index} clicked. expandedSections:`, expandedSections);
//             }}
//           >
//             <div className="title">{section.title}</div>
//             <div className="toggle-icon">{expandedSections[index] ? '-' : '+'}</div>
//             {section.cweNumber && <span className="cwe-number">CWE No: {section.cweNumber}</span>}
//           </div>
//           {expandedSections[index] && (
//             <div className={`section-content ${expandedSections[index] ? 'open' : ''}`}>
//               <pre>{section.content}</pre>
//               {section.subSections && section.subSections.length > 0 && (
//                 <div className="subsection-header" onClick={(e) => e.stopPropagation()}>
//                   {section.subSections.length} Subsection{section.subSections.length > 1 ? 's' : ''}:
//                   <div
//                     className="toggle-icon"
//                     onClick={() => {
//                       setExpandedSections((prevExpandedSections) => {
//                         const updatedExpandedSections = [...prevExpandedSections];
//                         updatedExpandedSections[index] = !prevExpandedSections[index];
//                         return updatedExpandedSections;
//                       });
//                     }}
//                   >
//                     {expandedSections[index] ? '-' : '+'}
//                   </div>
//                 </div>
//               )}
//               {expandedSections[index] && section.subSections && section.subSections.length > 0 && (
//                 <div className="subsection-content">
//                   {console.log(`Section ${index} Subsections:`, section.subSections)} {/* Add this line */}
//                   {section.subSections.map((subsection, subIndex) => (
//                     <div key={subIndex}>{subsection}</div>
//                   ))}
//                 </div>
//               )}
//             </div>
//           )}
//         </div>
//       ))}
//       <button className="btnn btn-primary" onClick={handleDownload}>
//         <FontAwesomeIcon icon={faFileDownload} /> Download Report
//       </button>
//     </div>
//   );
// }

///////22222222
