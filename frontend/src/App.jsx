import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Navbar from "./components/Navbar";
import {translations} from "./locales/index.js";


function App() {
    const [language, setLanguage] = useState('fr');

    return (
        <Router>
            <Navbar
                translations={translations}
                language={language}
                setLanguage={setLanguage}
            />

            <Routes>
                <Route path="/" element={<Home language={language} />} />
            </Routes>
        </Router>
    );
}

export default App;
