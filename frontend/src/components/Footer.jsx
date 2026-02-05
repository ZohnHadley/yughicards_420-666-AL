import React from "react";
import { Link } from "react-router-dom";
import { FaFacebook, FaInstagram, FaTwitter, FaPhoneAlt, FaMapMarkerAlt, FaClock } from "react-icons/fa";
import { translations } from "../locales";
import {FaXTwitter} from "react-icons/fa6";

export default function Footer({ language }) {
    const t = translations[language]?.footer;

    return (
        <footer className="bg-red-600 shadow-md py-6 px-6 mt-20">
            <div className="max-w-7xl mx-auto flex flex-col md:flex-row items-center justify-between gap-6">

                {/* Nom du site */}
                <Link to="/" className="text-3xl font-bold text-white hover:text-red-300">
                    Yughi-Cards
                </Link>

                {/* Infos magasin */}
                <div className="text-white text-sm space-y-1 text-center md:text-left">
                    <p className="flex items-center gap-2">
                        <FaPhoneAlt className="text-red-300"/> {t.phone}
                    </p>
                    <p className="flex items-center gap-2">
                        <FaMapMarkerAlt className="text-red-300"/> {t.address}
                    </p>
                    <p className="flex items-center gap-2">
                        <FaClock className="text-red-300"/> {t.hoursWeek}
                    </p>
                    <p className="flex items-center gap-2">
                        <FaClock className="text-red-300"/> {t.hoursWeekend}
                    </p>
                </div>


                {/* Liens et réseaux sociaux */}
                <ul className="flex space-x-6 items-center">
                    <li>
                        <Link to="/sellToUs" className="hover:text-red-300 text-white">
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

                    <li className="text-white">|</li>

                    <li>
                        <a href="#" className="text-white hover:text-red-300 text-xl">
                            <FaFacebook />
                        </a>
                    </li>
                    <li>
                        <a href="#" className="text-white hover:text-red-300 text-xl">
                            <FaInstagram />
                        </a>
                    </li>
                    <li>
                        <a href="#" className="text-white hover:text-red-300 text-xl">
                            <FaXTwitter/>
                        </a>
                    </li>
                </ul>
            </div>

            {/* Copyright */}
            <div className="text-center text-sm text-white mt-4">
                © {new Date().getFullYear()} Yughi-Cards. {t.rights}
            </div>
        </footer>
    );
}
