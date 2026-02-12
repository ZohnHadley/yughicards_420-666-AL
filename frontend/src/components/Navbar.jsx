import React, { useState, useRef, useEffect } from "react";
import { Link } from "react-router-dom";
import { translations, languages } from "../locales/index.js";
import { FaShoppingCart, FaUser, FaChevronDown } from "react-icons/fa";

export default function Navbar({ language, setLanguage }) {
    const t = translations[language]?.navbar;
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const dropdownRef = useRef(null);

    const currentLang = languages.find(lang => lang.code === language);

    // ✅ Ferme le dropdown si on clique ailleurs
    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsDropdownOpen(false);
            }
        }

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

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

                {/* Dropdown langue */}
                <li className="relative" ref={dropdownRef}>
                    <button
                        onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                        className="bg-gray-200 text-black font-semibold border border-gray-400 rounded px-3 py-1
                                   hover:bg-gray-300 transition flex items-center gap-2"
                    >
                        <img
                            src={currentLang?.flag}
                            alt={currentLang?.name}
                            className="w-6 h-4 object-cover rounded"
                        />
                        <span>{currentLang?.shortCode}</span>
                        <FaChevronDown className="text-xs" />
                    </button>

                    {isDropdownOpen && (
                        <div className="absolute top-full mt-1 bg-white border border-gray-300 rounded shadow-lg overflow-hidden min-w-full">
                            {languages.map(lang => (
                                <button
                                    key={lang.code}
                                    onClick={() => {
                                        setLanguage(lang.code);
                                        setIsDropdownOpen(false);
                                    }}
                                    className="w-full px-3 py-2 hover:bg-gray-100 flex items-center gap-2 text-left"
                                >
                                    <img
                                        src={lang.flag}
                                        alt={lang.name}
                                        className="w-6 h-4 object-cover rounded"
                                    />
                                    <span className="text-black">{lang.shortCode}</span>
                                </button>
                            ))}
                        </div>
                    )}
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
