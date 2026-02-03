import React from "react";
import { Link } from "react-router-dom";
import { translations, languages } from "../locales/index.js";

export default function Navbar({ language, setLanguage }) {
    const t = translations[language]?.navbar || {
        yugioh: "Yu-Gi-Oh",
        sell: "Vendre",
        about: "À propos",
        contact: "Contact",
    };

    return (
        <nav className="bg-red-600 shadow-md p-4 flex items-center justify-between fixed top-0 left-0 w-full z-50">
            <div className="text-2xl font-bold text-white">Yughi-Cards</div>

            <ul className="flex space-x-6 items-center">
                <li>
                    <Link to="/" className="hover:text-red-300 text-white">{t.yugioh}</Link>
                </li>
                <li>
                    <Link to="/sellToUs" className="hover:text-red-300 text-white">{t.sell}</Link>
                </li>
                <li>
                    <Link to="/about" className="hover:text-red-300 text-white">{t.about}</Link>
                </li>
                <li>
                    <Link to="/contact" className="hover:text-red-300 text-white">{t.contact}</Link>
                </li>
                <li>
                    <select
                        value={language}
                        onChange={(e) => setLanguage(e.target.value)}
                        className="border border-gray-300 rounded px-2 py-1"
                    >
                        {languages.map(lang => (
                            <option key={lang.code} value={lang.code}>
                                {lang.shortCode}
                            </option>
                        ))}
                    </select>
                </li>
            </ul>
        </nav>
    );
}
