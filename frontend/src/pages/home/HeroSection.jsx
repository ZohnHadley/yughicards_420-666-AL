import React from "react";
import { Link } from "react-router-dom";

function HeroSection({ hero }) {
    return (
        <section className="relative h-screen flex items-center justify-center overflow-hidden">

            {/* Background Image */}
            <div
                className="absolute inset-0 bg-cover bg-center bg-no-repeat"
                style={{ backgroundImage: "url('/images/NEWS_TITLE-SECTION_V2.png')", filter: "brightness(0.4)" }}
            />

            {/* Animated Gradient Overlay */}
            <div className="absolute inset-0 bg-gradient-to-br from-red-900/40 via-transparent to-purple-900/40 animate-gradient" />

            {/* Decorative Elements */}
            <div className="absolute top-20 left-10 w-32 h-32 bg-red-500/10 rounded-full blur-3xl animate-pulse" />
            <div className="absolute bottom-20 right-10 w-40 h-40 bg-purple-500/10 rounded-full blur-3xl animate-pulse"
                 style={{ animationDelay: '1s' }} />

            {/* Hero Content */}
            <div className="relative z-10 text-center px-6 max-w-5xl mx-auto">

                <h1
                    className="text-7xl md:text-8xl font-black mb-6 tracking-tight"
                    style={{
                        fontFamily: "'Bebas Neue', 'Impact', sans-serif",
                        textShadow: "0 0 40px rgba(239, 68, 68, 0.5), 0 0 80px rgba(239, 68, 68, 0.3)",
                        animation: "fadeInUp 1s ease-out"
                    }}
                >
          <span className="bg-gradient-to-r from-red-500 via-orange-500 to-yellow-500 text-transparent bg-clip-text">
            {hero.title}
          </span>
                </h1>

                <h2
                    className="text-5xl md:text-6xl font-bold text-white mb-8"
                    style={{
                        fontFamily: "'Bebas Neue', 'Impact', sans-serif",
                        textShadow: "0 4px 20px rgba(0, 0, 0, 0.8)",
                        animation: "fadeInUp 1s ease-out 0.2s backwards"
                    }}
                >
                    {hero.subtitle}
                </h2>

                <p
                    className="text-xl md:text-2xl text-gray-200 mb-12 max-w-3xl mx-auto font-light"
                    style={{
                        textShadow: "0 2px 10px rgba(0, 0, 0, 0.8)",
                        animation: "fadeInUp 1s ease-out 0.4s backwards"
                    }}
                >
                    {hero.description}
                </p>

                <div className="flex flex-col sm:flex-row gap-6 justify-center items-center"
                     style={{ animation: "fadeInUp 1s ease-out 0.6s backwards" }}>
                    <Link
                        to="/inventaire"
                        className="group relative px-10 py-5 bg-gradient-to-r from-red-600 to-red-700 text-white font-bold text-lg rounded-lg
                       transform hover:scale-105 transition-all duration-300 shadow-2xl hover:shadow-red-500/50 overflow-hidden"
                    >
                        <span className="relative z-10">{hero.cta}</span>
                        <div className="absolute inset-0 bg-gradient-to-r from-red-500 to-orange-500 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
                    </Link>

                    <Link
                        to="/vendez-nous"
                        className="px-10 py-5 bg-white/10 backdrop-blur-md text-white font-bold text-lg rounded-lg border-2 border-white/30
                       transform hover:scale-105 hover:bg-white/20 transition-all duration-300 shadow-xl"
                    >
                        {hero.secondaryCta}
                    </Link>
                </div>
            </div>
        </section>
    );
}

export default HeroSection;
