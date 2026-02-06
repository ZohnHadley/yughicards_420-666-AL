import React from "react";
import { translations } from "../locales/index.js";

function About({ language }) {
    const t = translations[language]?.about || translations['fr'].about;

    return (
        <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 pt-32 pb-16 px-6">
            <div className="max-w-4xl mx-auto">
                {/* En-tête */}
                <div className="text-center mb-12">
                    <h1 className="text-4xl md:text-5xl font-bold text-red-600 mb-4">
                        {t.title}
                    </h1>
                    <div className="w-24 h-1 bg-red-600 mx-auto"></div>
                </div>

                {/* Notre Histoire */}
                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-4">{t.ourStory.title}</h2>
                    <p className="text-gray-700 leading-relaxed mb-4">
                        {t.ourStory.paragraph1}
                    </p>
                    <p className="text-gray-700 leading-relaxed">
                        {t.ourStory.paragraph2}
                    </p>
                </section>

                {/* Ce qui nous rend unique */}
                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-6">{t.whatMakesUsUnique.title}</h2>
                    <div className="grid md:grid-cols-2 gap-6">
                        <div className="flex items-start space-x-4">
                            <div className="bg-red-600 text-white rounded-full p-3 flex-shrink-0">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                </svg>
                            </div>
                            <div>
                                <h3 className="font-semibold text-gray-800 mb-2">{t.whatMakesUsUnique.authenticity.title}</h3>
                                <p className="text-gray-600 text-sm">
                                    {t.whatMakesUsUnique.authenticity.description}
                                </p>
                            </div>
                        </div>

                        <div className="flex items-start space-x-4">
                            <div className="bg-red-600 text-white rounded-full p-3 flex-shrink-0">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                                </svg>
                            </div>
                            <div>
                                <h3 className="font-semibold text-gray-800 mb-2">{t.whatMakesUsUnique.pricing.title}</h3>
                                <p className="text-gray-600 text-sm">
                                    {t.whatMakesUsUnique.pricing.description}
                                </p>
                            </div>
                        </div>

                        <div className="flex items-start space-x-4">
                            <div className="bg-red-600 text-white rounded-full p-3 flex-shrink-0">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                                </svg>
                            </div>
                            <div>
                                <h3 className="font-semibold text-gray-800 mb-2">{t.whatMakesUsUnique.service.title}</h3>
                                <p className="text-gray-600 text-sm">
                                    {t.whatMakesUsUnique.service.description}
                                </p>
                            </div>
                        </div>

                        <div className="flex items-start space-x-4">
                            <div className="bg-red-600 text-white rounded-full p-3 flex-shrink-0">
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                                </svg>
                            </div>
                            <div>
                                <h3 className="font-semibold text-gray-800 mb-2">{t.whatMakesUsUnique.inventory.title}</h3>
                                <p className="text-gray-600 text-sm">
                                    {t.whatMakesUsUnique.inventory.description}
                                </p>
                            </div>
                        </div>
                    </div>
                </section>

                {/* Notre Engagement */}
                <section className="bg-red-600 text-white rounded-lg shadow-lg p-8">
                    <h2 className="text-2xl font-bold mb-4">{t.ourCommitment.title}</h2>
                    <p className="leading-relaxed mb-4">
                        {t.ourCommitment.paragraph1}
                    </p>
                    <p className="leading-relaxed">
                        {t.ourCommitment.paragraph2}
                    </p>
                </section>
            </div>
        </div>
    );
}

export default About;