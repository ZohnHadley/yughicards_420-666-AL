import React from "react";
import { translations } from "../locales/index.js";
import { MapPin, Phone, Mail, Clock } from 'lucide-react';
import { FaFacebook, FaInstagram, FaXTwitter } from 'react-icons/fa6';

function Contact({ language }) {
    const t = translations[language]?.contactUs;

    const contactDetails = [
        {
            icon: Phone,
            label: t.contactInfo.phone.label,
            value: t.contactInfo.phone.value,
            link: `tel:${t.contactInfo.phone.value.replace(/[^0-9]/g, '')}`,
            color: 'bg-blue-600',
            isEmail: false
        },
        {
            icon: Mail,
            label: t.contactInfo.email.label,
            value: t.contactInfo.email.value,
            link: `https://mail.google.com/mail/?view=cm&fs=1&to=${t.contactInfo.email.value}`,
            color: 'bg-green-600',
            isEmail: true
        },
        {
            icon: MapPin,
            label: t.contactInfo.address.label,
            value: t.contactInfo.address.value,
            link: 'https://www.google.com/maps/search/?api=1&query=1111+Rue+Lapierre+LaSalle+QC+H8N+2J4',
            color: 'bg-red-600',
            isEmail: false
        }
    ];

    const socialMedia = [
        { icon: FaFacebook, color: 'hover:bg-blue-600', name: 'Facebook' },
        { icon: FaInstagram, color: 'hover:bg-pink-600', name: 'Instagram' },
        { icon: FaXTwitter, color: 'hover:bg-black', name: 'X (Twitter)' }
    ];

    return (
        <div className="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 pt-10 pb-16 px-6">
            <div className="max-w-6xl mx-auto">
                {/* En-tête */}
                <div className="text-center mb-12">
                    <h1 className="text-4xl md:text-5xl font-bold text-red-600 mb-4">
                        {t.title}
                    </h1>
                    <div className="w-24 h-1 bg-red-600 mx-auto mb-4"></div>
                    <p className="text-gray-600 text-lg">{t.subtitle}</p>
                </div>

                {/* Introduction */}
                <section className="bg-white rounded-lg shadow-lg p-8 mb-8">
                    <h2 className="text-2xl font-bold text-gray-800 mb-4">{t.getInTouch.title}</h2>
                    <p className="text-gray-700 leading-relaxed">{t.getInTouch.description}</p>
                </section>

                {/* Informations de contact & Carte */}
                <div className="grid lg:grid-cols-2 gap-8 mb-8">

                    {/* Informations de contact */}
                    <section className="bg-white rounded-lg shadow-lg p-8">
                        <h2 className="text-2xl font-bold text-gray-800 mb-6">{t.contactInfo.title}</h2>
                        <div className="space-y-6">
                            {contactDetails.map((detail, index) => {
                                const Icon = detail.icon;
                                return (
                                    <div
                                        key={index}
                                        onClick={() => {}}
                                        className="flex items-start space-x-4 group hover:bg-gray-50 p-3 rounded-lg transition-colors cursor-pointer"
                                    >
                                        <div className={`${detail.color} text-white rounded-full p-3 flex-shrink-0 group-hover:scale-110 transition-transform`}>
                                            <Icon className="w-6 h-6" />
                                        </div>
                                        <div>
                                            <h3 className="font-semibold text-gray-800 mb-1">{detail.label}</h3>
                                            <p className="text-gray-600 text-sm group-hover:text-red-600 transition-colors">{detail.value}</p>
                                        </div>
                                    </div>
                                );
                            })}

                        </div>

                        {/* Heures d'ouverture */}
                        <div className="mt-8 pt-6 border-t border-gray-200">
                            <div className="flex items-center space-x-3 mb-4 group">
                                <div className="bg-purple-600 text-white rounded-full p-3 flex-shrink-0 transition-transform group-hover:scale-110">
                                    <Clock className="w-6 h-6" />
                                </div>
                                <h3 className="text-xl font-semibold text-gray-800">{t.hours.title}</h3>
                            </div>
                            <div className="ml-14 space-y-2">
                                <div className="flex justify-between items-center py-2 border-b border-gray-100">
                                    <span className="text-gray-700 font-medium">{t.hours.weekdays}</span>
                                    <span className="text-gray-600">{t.hours.weekdaysHours}</span>
                                </div>
                                <div className="flex justify-between items-center py-2">
                                    <span className="text-gray-700 font-medium">{t.hours.weekend}</span>
                                    <span className="text-gray-600">{t.hours.weekendHours}</span>
                                </div>
                            </div>
                        </div>


                        {/* Réseaux sociaux */}
                        <div className="mt-8 pt-6 border-t border-gray-200">
                            <h3 className="text-xl font-semibold text-gray-800 mb-4">{t.socialTitle}</h3>
                            <div className="flex gap-4">
                                {socialMedia.map((social, index) => {
                                    const Icon = social.icon;
                                    return (
                                        <button
                                            key={index}
                                            onClick={() => {}}
                                            className={`bg-gray-200 text-gray-700 rounded-full p-3 ${social.color} hover:text-white transition-all duration-300 hover:scale-110 cursor-pointer`}
                                            aria-label={social.name}
                                        >
                                            <Icon className="w-6 h-6" />
                                        </button>
                                    );
                                })}
                            </div>
                        </div>
                    </section>

                    {/* Carte Google Maps */}
                    <section className="bg-white rounded-lg shadow-lg p-8">
                        <h2 className="text-2xl font-bold text-gray-800 mb-6">{t.visit.title}</h2>
                        <p className="text-gray-700 leading-relaxed mb-6">{t.visit.description}</p>
                        <div className="w-full h-[400px] rounded-lg overflow-hidden shadow-md">
                            <iframe
                                title="Google Map"
                                src="https://www.google.com/maps?q=1111+Rue+Lapierre,+LaSalle,+QC+H8N+2J4&output=embed"
                                className="w-full h-full border-0"
                                loading="lazy"
                                referrerPolicy="no-referrer-when-downgrade"
                            />
                        </div>
                    </section>
                </div>

                {/* Message d'encouragement */}
                <section className="bg-red-600 text-white rounded-lg shadow-lg p-8 text-center">
                    <h2 className="text-2xl font-bold mb-4">{t.encouragement.title}</h2>
                    <p className="leading-relaxed">{t.encouragement.description}</p>
                </section>
            </div>
        </div>
    );
}

export default Contact;