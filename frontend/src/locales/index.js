import {NavbarFr} from "./francais/components/NavbarFr.js";
import {NavbarEn} from "./english/components/NavbarEn.js";
import {FooterFr} from "./francais/components/FooterFr.js";
import {FooterEn} from "./english/components/FooterEn.js";

export const translations = {
    fr: {
        navbar: NavbarFr,
        footer: FooterFr
    },
    en: {
        navbar: NavbarEn,
        footer: FooterEn
    }
}
export const languages = [
    { code: 'fr', name: 'Français', flag: '/images/canadaFlag.png', shortCode: 'FR' },
    { code: 'en', name: 'English', flag: '/images/US_Flag.png', shortCode: 'EN' }
];