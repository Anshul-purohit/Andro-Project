import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './cwe.css'

const Cwe = ({cweData}) => {
  
  console.log('cwe data', cweData)
  return (
    <div className='cwe'>
      {
        cweData ? <>
        <div className='num'>
          <h7>CWE_No : </h7>
          {cweData.data.CWE_No}
          </div>
          <div className='name'>
          <h7>Name: </h7>
          {cweData.data.Name}
          </div>
        <div className='des'>
          <h7>Description of cwe_no : </h7>
          {cweData.data.description}
          </div>
          <div className='ed'>
          <h7>Extended_Description: </h7>
          {cweData.data.Extended_Description}
          </div>
        </> : <></>
      }
      {/* <label htmlFor="cweDropdown">Select CWE:</label> */}
      
      {/* {selectedCwe && <p>You selected: {selectedCwe}</p>} */}
    </div>
  );
};

export default Cwe;
