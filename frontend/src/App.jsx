import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "./pages/Home";
import VendezNous from "./pages/VendezNous";
import About from "./pages/About";
import YughiohInventory from "./pages/YughiohInventory";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer.jsx";
import Contact from "./pages/Contact.jsx";

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
                        <Route path="/vendez-nous" element={<VendezNous />} />
                        <Route path="/inventaire" element={<YughiohInventory />} />
                        <Route path="/about" element={<About />} />
                        <Route path="/contact" element={<Contact />} />
                    </Routes>
                </main>

                <Footer language={language} />
            </div>
        </Router>
    );
}

export default App;
