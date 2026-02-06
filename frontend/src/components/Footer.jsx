import React from "react";
import { Link } from "react-router-dom";
import {
    FaFacebook,
    FaInstagram,
    FaPhoneAlt,
    FaMapMarkerAlt,
    FaClock
} from "react-icons/fa";
import { FaXTwitter } from "react-icons/fa6";
import { translations } from "../locales";

export default function Footer({ language }) {
    const t = translations[language]?.footer

    return (
        <footer className="bg-red-600 shadow-md mt-20">
            <div className="max-w-7xl mx-auto px-6 py-10">

                {/* Contenu principal */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-8">

                    {/* Colonne 1 */}
                    <div className="space-y-4">
                        <Link to="/" className="text-3xl font-bold text-white hover:text-red-300 inline-block">
                            Yughi-Cards
                        </Link>

                        <p className="text-white/90 text-sm">
                            {t.description}
                        </p>

                        <div className="flex space-x-4 pt-2">
                            <a href="#" className="text-white hover:text-red-300 text-2xl">
                                <FaFacebook />
                            </a>
                            <a href="#" className="text-white hover:text-red-300 text-2xl">
                                <FaInstagram />
                            </a>
                            <a href="#" className="text-white hover:text-red-300 text-2xl">
                                <FaXTwitter />
                            </a>
                        </div>
                    </div>

                    {/* Colonne 2 */}
                    <div className="space-y-4">
                        <h3 className="text-xl font-bold text-white">
                            {t.quickLinks}
                        </h3>

                        <ul className="space-y-3">
                            <li>
                                <Link to="/inventaire" className="text-white/90 hover:text-red-300 flex items-center">
                                    <span className="mr-2">›</span> {t.yugioh}
                                </Link>
                            </li>

                            <li>
                                <Link to="/vendez-nous" className="text-white/90 hover:text-red-300 flex items-center">
                                    <span className="mr-2">›</span> {t.sell}
                                </Link>
                            </li>

                            <li>
                                <Link to="/about" className="text-white/90 hover:text-red-300 flex items-center">
                                    <span className="mr-2">›</span> {t.about}
                                </Link>
                            </li>

                            <li>
                                <Link to="/contact" className="text-white/90 hover:text-red-300 flex items-center">
                                    <span className="mr-2">›</span> {t.contact}
                                </Link>
                            </li>
                        </ul>
                    </div>

                    {/* Colonne 3 */}
                    <div className="space-y-4">
                        <h3 className="text-xl font-bold text-white">
                            {t.contactUs}
                        </h3>

                        <div className="text-white/90 text-sm space-y-3">
                            <p className="flex gap-3">
                                <FaPhoneAlt className="text-red-300 mt-1" />
                                {t.phone}
                            </p>

                            <p className="flex gap-3">
                                <FaMapMarkerAlt className="text-red-300 mt-1" />
                                {t.address}
                            </p>

                            <div className="pt-2 border-t border-white/20">
                                <p className="flex gap-3 font-semibold">
                                    <FaClock className="text-red-300 mt-1" />
                                    {t.hours}
                                </p>
                                <p className="ml-8">{t.hoursWeek}</p>
                                <p className="ml-8">{t.hoursWeekend}</p>
                            </div>
                        </div>
                    </div>

                </div>

                {/* Séparateur */}
                <div className="border-t border-white/20"></div>

                {/* Copyright */}
                <div className="text-center text-sm text-white/80 pt-6">
                    © {new Date().getFullYear()} Yughi-Cards. {t.rights}
                </div>

            </div>
        </footer>
    );
}
