import React from "react";
import { translations } from "../../locales/index.js";
import HeroSection from "./HeroSection";
import WhyShopWithUsSection from "./WhyShopWithUsSection.jsx";

function Home({ language }) {
    const t = translations[language]?.home;

    return (
        <div className="min-h-screen bg-gradient-to-b from-slate-900 via-slate-800 to-slate-900">
            <HeroSection hero={t.hero} />
            <WhyShopWithUsSection features={t.features} />
        </div>
    );
}

export default Home;
