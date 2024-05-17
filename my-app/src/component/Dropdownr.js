import React from "react";
import "./Dropdown.css"; // Import the CSS file

export default function Dropdownr({
  selectedItem,
  handleItemClick,
  isRunning,
}) {
  return (
    <div>
      <div className="dropdown m-3">
        <button
          className={`btn btn-primary dropdown-toggle ${
            isRunning ? "disabled" : ""
          }`}
          type="button"
          id="dropdownMenuButton1"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          {selectedItem ? selectedItem : "Select Tool "}
          {/* <i className="fas fa-chevron-down"></i> Font Awesome dropdown icon */}
        </button>

        <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton1">
          <li>
            <button
              className="dropdown-item"
              onClick={() => handleItemClick("Apk Tool")}
            >
              Apk Tool
            </button>
          </li>
          <li>
            <button
              className="dropdown-item"
              onClick={() => handleItemClick("Frida")}
            >
              Frida
            </button>
          </li>
          <li>
            <button
              className="dropdown-item"
              onClick={() => handleItemClick("Hackode")}
            >
              Hackode
            </button>
          </li>
        </ul>
      </div>
    </div>
  );
}
