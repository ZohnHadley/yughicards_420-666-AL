import React from "react";
import {Link} from "react-router-dom";
import {translations} from "../locales/index.js";

function VendezNous({language}) {
    const t = translations[language]?.vendezNous || translations['fr'].vendezNous;
    const steps = [t.steps.step1, t.steps.step2, t.steps.step3, t.steps.step4];
    const conditions = [t.conditions.item1, t.conditions.item2, t.conditions.item3, t.conditions.item4];

    return (
        <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 pt-10 pb-16 px-6">
            <div className="max-w-4xl mx-auto">

                <div className="text-center mb-12">
                    <h1 className="text-4xl md:text-5xl font-bold text-red-600 mb-4">
                        {t.title}
                    </h1>
                    <div className="w-24 h-1 bg-red-600 mx-auto mb-4"></div>
                    <p className="text-gray-600 text-lg">{t.subtitle}</p>
                </div>

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
                                    <h3 className="font-semibold text-gray-800 mb-2">{step.title}</h3>
                                    <p className="text-gray-600 text-sm">{step.description}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </section>

                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-4">{t.conditions.title}</h2>
                    <p className="text-gray-700 mb-4">{t.conditions.description}</p>
                    <ul className="space-y-3">
                        {conditions.map((item, index) => (
                            <li key={index} className="flex items-center space-x-3">
                                <div className="bg-red-600 text-white rounded-full p-1 flex-shrink-0">
                                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                                              d="M5 13l4 4L19 7"/>
                                    </svg>
                                </div>
                                <span className="text-gray-700">{item}</span>
                            </li>
                        ))}
                    </ul>
                </section>
            </div>
        </div>
    );
}

export default VendezNous;
