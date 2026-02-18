import React from "react";

function FeatureCard({ icon: Icon, title, description, color }) {
    return (
        <div className={`group relative bg-gradient-to-br from-slate-800/50 to-slate-900/50 backdrop-blur-sm p-8 rounded-2xl border border-${color}-500/20
                    hover:border-${color}-500/50 transition-all duration-500 hover:transform hover:scale-105`}>
            <div className={`absolute inset-0 bg-gradient-to-br from-${color}-500/0 to-${color}-500/5 rounded-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-500`} />
            <div className="relative">
                <div className={`w-16 h-16 bg-gradient-to-br from-${color}-500 to-${color}-400 rounded-xl flex items-center justify-center mb-6
                        shadow-lg shadow-${color}-500/50 group-hover:shadow-${color}-500/80 transition-shadow duration-500`}>
                    <Icon className="text-3xl text-white" />
                </div>
                <h3 className="text-2xl font-bold text-white mb-4" style={{ fontFamily: "'Bebas Neue', sans-serif" }}>{title}</h3>
                <p className="text-gray-400 leading-relaxed">{description}</p>
            </div>
        </div>
    );
}

export default FeatureCard;
