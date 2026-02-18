import React from "react";
import FeatureCard from "./FeatureCard";
import { FaShieldAlt, FaTags, FaUsers } from "react-icons/fa";

function WhyShopWithUsSection({ features }) {
    return (
        <section className="py-24 px-6 bg-gradient-to-b from-slate-900 to-slate-950">
            <div className="max-w-7xl mx-auto">
                <h2
                    className="text-5xl md:text-6xl font-black text-center mb-20 bg-gradient-to-r from-red-500 to-orange-500 text-transparent bg-clip-text"
                    style={{ fontFamily: "'Bebas Neue', 'Impact', sans-serif" }}
                >
                    {features.title}
                </h2>

                <div className="grid md:grid-cols-3 gap-12">
                    <FeatureCard icon={FaShieldAlt} title={features.quality.title} description={features.quality.description} color="red" />
                    <FeatureCard icon={FaTags} title={features.prices.title} description={features.prices.description} color="orange" />
                    <FeatureCard icon={FaUsers} title={features.service.title} description={features.service.description} color="yellow" />
                </div>
            </div>
        </section>
    );
}

export default WhyShopWithUsSection;
