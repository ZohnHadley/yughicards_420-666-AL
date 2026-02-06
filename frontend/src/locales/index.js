import {NavbarFr} from "./francais/components/NavbarFr.js";
import {NavbarEn} from "./english/components/NavbarEn.js";
import {FooterFr} from "./francais/components/FooterFr.js";
import {FooterEn} from "./english/components/FooterEn.js";
import {AboutFr} from "./francais/AboutFr.js";
import {AboutEn} from "./english/AboutEn.js";

export const translations = {
    fr: {
        navbar: NavbarFr,
        footer: FooterFr,
        about: AboutFr
    },
    en: {
        navbar: NavbarEn,
        footer: FooterEn,
        about: AboutEn
    }
}
export const languages = [
    { code: 'fr', name: 'Français', flag: '/images/canadaFlag.png', shortCode: 'FR' },
    { code: 'en', name: 'English', flag: '/images/US_Flag.png', shortCode: 'EN' }
];