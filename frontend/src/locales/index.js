import {NavbarFr} from "./francais/components/NavbarFr.js";
import {NavbarEn} from "./english/components/NavbarEn.js";

export const translations = {
    fr: {
        navbar: NavbarFr
    },
    en: {
        navbar: NavbarEn
    }
}
export const languages = [
    { code: 'fr', name: 'Français', flag: '/images/canadaFlag.png', shortCode: 'FR' },
    { code: 'en', name: 'English', flag: '/images/US_Flag.png', shortCode: 'EN' }
];