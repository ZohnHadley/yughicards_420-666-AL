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
        <nav className="bg-red-600 shadow-md fixed top-0 left-0 w-full z-50">
            <div className="max-w-7xl mx-auto px-6 py-7">
                <div className="flex items-center justify-between">

                    {/* Logo / Nom du site */}
                    <Link
                        to="/"
                        className="text-3xl font-bold text-white hover:text-red-300 transition-colors duration-200"
                    >
                        Yughi-Cards
                    </Link>

                    {/* Navigation principale */}
                    <ul className="flex items-center gap-8">

                        <li>
                            <Link
                                to="/inventaire"
                                className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base"
                            >
                                {t.yugioh}
                            </Link>
                        </li>

                        <li>
                            <Link
                                to="/inventaire"
                                className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base"
                            >
                                {t.inventory}
                            </Link>
                        </li>

                        <li>
                            <Link
                                to="/vendez-nous"
                                className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base"
                            >
                                {t.sell}
                            </Link>
                        </li>

                        <li>
                            <Link
                                to="/about"
                                className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base"
                            >
                                {t.about}
                            </Link>
                        </li>

                        <li>
                            <Link
                                to="/contact"
                                className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base"
                            >
                                {t.contact}
                            </Link>
                        </li>

                        {/* Séparateur visuel */}
                        <li className="h-6 w-px bg-white/30"></li>

                        {/* Dropdown langue */}
                        <li className="relative" ref={dropdownRef}>
                            <button
                                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                                className="bg-white text-red-600 font-semibold border border-white rounded-lg px-4 py-2
                                           hover:bg-gray-100 transition-all duration-200 flex items-center gap-2 min-w-[100px]"
                            >
                                <img
                                    src={currentLang?.flag}
                                    alt={currentLang?.name}
                                    className="w-6 h-4 object-cover rounded shadow-sm"
                                />
                                <span>{currentLang?.shortCode}</span>
                                <FaChevronDown className={`text-xs ml-auto transition-transform duration-200 ${isDropdownOpen ? 'rotate-180' : ''}`} />
                            </button>

                            {isDropdownOpen && (
                                <div className="absolute top-full right-0 mt-2 bg-white border border-gray-200 rounded-lg shadow-xl overflow-hidden min-w-[140px] animate-fadeIn">
                                    {languages.map(lang => (
                                        <button
                                            key={lang.code}
                                            onClick={() => {
                                                setLanguage(lang.code);
                                                setIsDropdownOpen(false);
                                            }}
                                            className={`w-full px-4 py-3 hover:bg-red-50 flex items-center gap-3 text-left transition-colors duration-150
                                                       ${language === lang.code ? 'bg-red-100' : ''}`}
                                        >
                                            <img
                                                src={lang.flag}
                                                alt={lang.name}
                                                className="w-6 h-4 object-cover rounded shadow-sm"
                                            />
                                            <span className="text-gray-800 font-medium">{lang.name}</span>
                                        </button>
                                    ))}
                                </div>
                            )}
                        </li>

                        {/* Icônes panier et utilisateur */}
                        <li>
                            <Link
                                to="/cart"
                                className="text-white hover:text-red-300 text-xl transition-all duration-200 hover:scale-110 block"
                            >
                                <FaShoppingCart />
                            </Link>
                        </li>

                        <li>
                            <Link
                                to="/login"
                                className="text-white hover:text-red-300 text-xl transition-all duration-200 hover:scale-110 block"
                            >
                                <FaUser />
                            </Link>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    );
}
