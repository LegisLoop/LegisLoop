/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * App declaration.
 ****************************************************************
 * Last Updated: February 10, 2025.
 ***************************************************************/
import NavBar from "./components/NavBar/NavBar";
import Footer from "./components/Footer/Footer";

function App() {
  return (
    <div className="App">
        <NavBar/>
        <div style={{ marginTop: '4rem' }}>  {/* This pushes content below the navbar */}
            {/* Add your main content here */}
        </div>
        <Footer/>
    </div>
  );
}

export default App;
