import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Navbar from "./components/Navbar";
import {translations} from "./locales/index.js";
import Footer from "./components/Footer.jsx";


function App() {
    const [language, setLanguage] = useState("fr");

    return (
        <Router>
            <div className="min-h-screen flex flex-col">
                <Navbar
                    language={language}
                    setLanguage={setLanguage}
                />

                {/* Contenu principal */}
                <main className="flex-grow pt-24">
                    <Routes>
                        <Route path="/" element={<Home language={language} />} />
                    </Routes>
                </main>

                <Footer language={language} />
            </div>
        </Router>
    );
}


export default App;
