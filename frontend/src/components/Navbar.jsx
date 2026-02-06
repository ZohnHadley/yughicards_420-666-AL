import React from "react";
import { Link } from "react-router-dom";
import { translations, languages } from "../locales/index.js";
import { FaShoppingCart, FaUser } from "react-icons/fa";

export default function Navbar({ language, setLanguage }) {
    const t = translations[language]?.navbar

    return (
        <nav className="bg-red-600 shadow-md py-6 px-6 flex items-center justify-between fixed top-0 left-0 w-full z-50">

            {/* Nom du site */}
            <Link to="/" className="text-3xl font-bold text-white hover:text-red-300">
                Yughi-Cards
            </Link>

            <ul className="flex space-x-6 items-center">

                <li>
                    <Link to="/inventaire" className="hover:text-red-300 text-white">
                        {t.yugioh}
                    </Link>
                </li>

                <li>
                    <Link to="/inventaire" className="hover:text-red-300 text-white">
                        {t.inventory}
                    </Link>
                </li>

                <li>
                    <Link to="/vendez-nous" className="hover:text-red-300 text-white">
                        {t.sell}
                    </Link>
                </li>

                <li>
                    <Link to="/about" className="hover:text-red-300 text-white">
                        {t.about}
                    </Link>
                </li>

                <li>
                    <Link to="/contact" className="hover:text-red-300 text-white">
                        {t.contact}
                    </Link>
                </li>

                {/* Sélecteur de langue - maintenant dans un <li> */}
                <li>
                    <select
                        value={language}
                        onChange={(e) => setLanguage(e.target.value)}
                        className="bg-gray-200 text-black font-semibold border border-gray-400 rounded px-3 py-1
                                   hover:bg-gray-300 transition"
                    >
                        {languages.map(lang => (
                            <option key={lang.code} value={lang.code}>
                                {lang.shortCode}
                            </option>
                        ))}
                    </select>
                </li>

                {/* Icônes */}
                <li>
                    <Link to="/cart" className="text-white hover:text-red-300 text-xl">
                        <FaShoppingCart />
                    </Link>
                </li>

                <li>
                    <Link to="/login" className="text-white hover:text-red-300 text-xl">
                        <FaUser />
                    </Link>
                </li>
            </ul>
        </nav>
    );
}