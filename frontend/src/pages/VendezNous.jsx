import React from "react";
import { Link } from "react-router-dom";
import { translations } from "../locales/index.js";

function VendezNous({ language }) {
    const t = translations[language]?.vendezNous || translations['fr'].vendezNous;

    return (
        <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 pt-10 pb-16 px-6">
            <div className="max-w-4xl mx-auto">
                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-4">{t.intro.title}</h2>
                    <p className="text-gray-700 leading-relaxed">{t.intro.description}</p>
                </section>
            </div>
        </div>
    );
}

export default VendezNous;
