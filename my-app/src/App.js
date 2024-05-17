import "./App.css";
import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom"; // Import BrowserRouter and Link
import Navbar from "./component/Navbar";
import StaticToolpage from "./component/StaticToolpage";
import DynamicToolPage from "./component/DynamicToolPage";
import MalwareDetection from "./component/MalwareDetection";
import Reverse from "./component/Reverse";
import NetworkWeb from "./component/NetworkWeb";
import Pentesting from "./component/Pentesting";
import InsecureDataStorage from "./component/InsecureDataStorage";
import Adb from "./component/Adb";
import CombinedToolsRun from "./component/CombinedToolsRun";

import heroImage from "./images/hero.jpg"; // Import the hero image
import image2 from "./images/hero2.jpg"; // Import additional images
// import image3 from "./images/image3.jpg";

function Home() {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const images = [heroImage, image2]; // Add your additional images here
  const texts = [
    "Explore the world of cybersecurity", // Text corresponding to heroImage
    "Stay ahead with our innovative solutions",
    // "Protect your digital assets with us"
  ]; // Add corresponding text for each image

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentImageIndex((prevIndex) =>
        prevIndex === images.length - 1 ? 0 : prevIndex + 1
      );
    }, 3000); // Change image every 3 seconds

    return () => clearInterval(intervalId);
  }, []);

  return (
    <section className="hero">
      <div className="hero-image">
        {/* Render current image */}
        <img src={images[currentImageIndex]} alt="Hero Image" />
        {/* Overlay text */}
        <div className="hero-text">
          <h1>{texts[currentImageIndex]}</h1>
          <p>Discover the latest advancements in technology and innovation.</p>
        </div>
      </div>
      <div className="hero-content">
        {/* <h1>Welcome to Our Website</h1>
        <p>Discover the latest advancements in technology and innovation.</p>
        <Link to="/about" className="btn btn-primary">
          Learn More
        </Link> */}
      </div>
      <Adb />
    </section>
  );
}

function App() {
  return (
    <Router>
      <section className="section11">
        <header>
          <Navbar />
        </header>
        <main className="main">
          {/* <div>
            <h1>this is our android pentesting Website</h1>
          </div> */}
          <Routes>
            <Route exact path="/" element={<Home />} />
            <Route path="/static" element={<StaticToolpage />} />
            <Route path="/dynamic" element={<DynamicToolPage />} />
            <Route path="/malware" element={<MalwareDetection />} />
            <Route path="/reverse" element={<Reverse />} />
            <Route path="/network-web" element={<NetworkWeb />} />
            <Route path="/pentesting-environments" element={<Pentesting />} />
            <Route
              path="/insecure-data-storage"
              element={<InsecureDataStorage />}
            />
            <Route
              path="/combined-tools-run"
              element={<CombinedToolsRun />}
            />
            {/* Add routes for other tools */}
          </Routes>
          ``{" "}
        </main>
        <footer>
          <div class="footer">
            <div class="sb_footer section_padding">
              <div class="sb_footer-links">
                <div class="sb_footer-links-div">
                  <h4>Quick Links</h4>
                  <ul>
                    <li>
                      <a href="/products">Products</a>
                    </li>
                    <li>
                      <a href="/services">Services</a>
                    </li>
                    <li>
                      <a href="/about">About Us</a>
                    </li>
                    <li>
                      <a href="/contact">Contact Us</a>
                    </li>
                  </ul>
                </div>
                <div class="sb_footer-links_div">
                  <h4>Follow Us</h4>
                  <ul class="social-icons">
                    <li>
                      <a href="#" class="fa fa-facebook"></a>
                    </li>
                    <li>
                      <a href="#" class="fa fa-twitter"></a>
                    </li>
                    <li>
                      <a href="#" class="fa fa-instagram"></a>
                    </li>
                    <li>
                      <a href="#" class="fa fa-linkedin"></a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <div class="footer-bottom">
            <p>&copy; 2024 Your Company. All rights reserved.</p>
          </div>
        </footer>
      </section>
    </Router>
  );
}

// const Home = () => (
//   <section className="hero">
//     <div className="hero-image">
//       <img src={heroImage} alt="Hero Image" />
//     </div>
//     <div className="hero-content">
//       <h1>Welcome to Our Website</h1>
//       <p>Discover the latest advancements in technology and innovation.</p>
//       {/* <a href="/about" className="btn btn-primary">
//       Learn More
//     </a> */}
//     </div>
//     <Adb />
//   </section>
// );

export default App;
