import React from "react";
import {Link} from "react-router-dom";
import {translations} from "../locales/index.js";

function VendezNous({language}) {
    const t = translations[language]?.vendezNous || translations['fr'].vendezNous;
    const steps = [t.steps.step1, t.steps.step2, t.steps.step3, t.steps.step4];
    const conditions = [t.conditions.item1, t.conditions.item2, t.conditions.item3, t.conditions.item4];
    const whyUs = [
        {
            key: 'fair',
            icon: <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
        },
        {
            key: 'fast',
            icon: <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
        },
        {
            key: 'expert',
            icon: <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.347.347A3.375 3.375 0 0112 18.75a3.375 3.375 0 01-2.388-.988l-.347-.347z" />
        },
        {
            key: 'trust',
            icon: <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
        }
    ];

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

                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">{t.whyUs.title}</h2>
                    <div className="grid md:grid-cols-2 gap-6">
                        {whyUs.map(({key, icon}) => (
                            <div key={key} className="flex items-start space-x-4">
                                <div className="bg-red-600 text-white rounded-full p-3 flex-shrink-0">
                                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        {icon}
                                    </svg>
                                </div>
                                <div>
                                    <h3 className="font-semibold text-gray-800 mb-2">{t.whyUs[key].title}</h3>
                                    <p className="text-gray-600 text-sm">{t.whyUs[key].description}</p>
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
