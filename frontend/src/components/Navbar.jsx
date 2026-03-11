import React, { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { translations, languages } from "../locales/index.js";
import { FaShoppingCart, FaUser, FaChevronDown } from "react-icons/fa";
import { useAuthStore } from "../store/useAuthStore.js";

export default function Navbar({ language, setLanguage }) {
    const t = translations[language]?.navbar;
    const navigate = useNavigate();

    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [userDropdownOpen, setUserDropdownOpen] = useState(false);

    const dropdownRef = useRef(null);
    const userDropdownRef = useRef(null);

    const currentLang = languages.find(lang => lang.code === language);
    const { user, isAuthenticated, logout } = useAuthStore();

    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsDropdownOpen(false);
            }
            if (userDropdownRef.current && !userDropdownRef.current.contains(event.target)) {
                setUserDropdownOpen(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    return (
        <nav className="bg-red-600 shadow-md fixed top-0 left-0 w-full z-50">
            <div className="max-w-7xl mx-auto px-6 py-7">
                <div className="flex items-center justify-between">

                    <Link
                        to="/"
                        className="text-3xl font-bold text-white hover:text-red-300 transition-colors duration-200"
                    >
                        Yughi-Cards
                    </Link>

                    <ul className="flex items-center gap-8">

                        <li>
                            <Link to="/inventaire" className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base">
                                {t.yugioh}
                            </Link>
                        </li>

                        <li>
                            <Link to="/inventaire" className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base">
                                {t.inventory}
                            </Link>
                        </li>

                        <li>
                            <Link to="/vendez-nous" className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base">
                                {t.sell}
                            </Link>
                        </li>

                        <li>
                            <Link to="/about" className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base">
                                {t.about}
                            </Link>
                        </li>

                        <li>
                            <Link to="/contact" className="text-white hover:text-red-300 transition-colors duration-200 font-medium text-base">
                                {t.contact}
                            </Link>
                        </li>

                        <li className="h-6 w-px bg-white/30"></li>

                        {/* Dropdown langue */}
                        <li className="relative" ref={dropdownRef}>
                            <button
                                onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                                className="bg-white text-red-600 font-semibold border border-white rounded-lg px-4 py-2
                                           hover:bg-gray-100 transition-all duration-200 flex items-center gap-2 min-w-[100px]"
                            >
                                <img src={currentLang?.flag} alt={currentLang?.name} className="w-6 h-4 object-cover rounded shadow-sm" />
                                <span>{currentLang?.shortCode}</span>
                                <FaChevronDown className={`text-xs ml-auto transition-transform duration-200 ${isDropdownOpen ? 'rotate-180' : ''}`} />
                            </button>

                            {isDropdownOpen && (
                                <div className="absolute top-full right-0 mt-2 bg-white border border-gray-200 rounded-lg shadow-xl overflow-hidden min-w-[140px]">
                                    {languages.map(lang => (
                                        <button
                                            key={lang.code}
                                            onClick={() => { setLanguage(lang.code); setIsDropdownOpen(false); }}
                                            className={`w-full px-4 py-3 hover:bg-red-50 flex items-center gap-3 text-left transition-colors duration-150
                                                       ${language === lang.code ? 'bg-red-100' : ''}`}
                                        >
                                            <img src={lang.flag} alt={lang.name} className="w-6 h-4 object-cover rounded shadow-sm" />
                                            <span className="text-gray-800 font-medium">{lang.name}</span>
                                        </button>
                                    ))}
                                </div>
                            )}
                        </li>

                        {/* User */}
                        <li className="relative" ref={userDropdownRef}>
                            {isAuthenticated() ? (
                                <>
                                    <button
                                        onClick={() => setUserDropdownOpen(!userDropdownOpen)}
                                        className="bg-white text-red-600 font-semibold border border-white rounded-lg px-4 py-2
                           hover:bg-gray-100 transition-all duration-200 flex items-center gap-2 min-w-[100px]"
                                    >
                                        <FaUser className="text-red-600" />
                                        <span className="font-medium text-sm">{user?.userName}</span>
                                        <FaChevronDown className={`text-xs ml-auto transition-transform duration-200 ${userDropdownOpen ? 'rotate-180' : ''}`} />
                                    </button>

                                    {userDropdownOpen && (
                                        <div className="absolute top-full right-0 mt-2 bg-white border border-gray-200 rounded-lg shadow-xl overflow-hidden min-w-[140px]">
                                            <button
                                                onClick={() => {
                                                    logout();
                                                    setUserDropdownOpen(false);
                                                    navigate("/");
                                                }}
                                                className="w-full px-4 py-3 hover:bg-red-50 flex items-center gap-2
                                   text-gray-800 font-medium transition-colors duration-150"
                                            >
                                                <span className="text-red-600 text-sm">✦</span>
                                                {t.logout}
                                            </button>
                                        </div>
                                    )}
                                </>
                            ) : (
                                <Link
                                    to="/login"
                                    className="text-white hover:text-red-300 text-xl transition-all duration-200 hover:scale-110 block"
                                >
                                    <FaUser />
                                </Link>
                            )}
                        </li>

                        {/* Panier — toujours visible */}
                        <li>
                            <Link
                                to="/shoppingCard"
                                className="text-white hover:text-red-300 text-xl transition-all duration-200 hover:scale-110 block"
                            >
                                <FaShoppingCart />
                            </Link>
                        </li>


                    </ul>
                </div>
            </div>
        </nav>
    );
}