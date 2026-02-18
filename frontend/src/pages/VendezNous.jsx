import React from "react";
import { Link } from "react-router-dom";
import { translations } from "../locales/index.js";

function VendezNous({ language }) {
    const t = translations[language]?.vendezNous || translations['fr'].vendezNous;
    const steps = [t.steps.step1, t.steps.step2, t.steps.step3, t.steps.step4];

    return (
        <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 pt-10 pb-16 px-6">
            <div className="max-w-4xl mx-auto">
                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-4">{t.intro.title}</h2>
                    <p className="text-gray-700 leading-relaxed">{t.intro.description}</p>
                </section>
                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">{t.steps.title}</h2>
                    <div className="grid md:grid-cols-2 gap-6">
                        {steps.map((step, index) => (
                            <div key={index} className="flex items-start space-x-4">
                                <div
                                    className="bg-red-600 text-white rounded-full w-10 h-10 flex items-center justify-center flex-shrink-0 font-bold text-lg">
                                    {index + 1}
                                </div>
                                <div>
                                    <h3 className="font-semibold text-gray-800 mb-2">{step.title.slice(3)}</h3>
                                    <p className="text-gray-600 text-sm">{step.description}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </section>
            </div>
        </div>
    );
}

export default VendezNous;
