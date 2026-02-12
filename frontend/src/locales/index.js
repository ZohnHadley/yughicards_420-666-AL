import {NavbarFr} from "./francais/components/NavbarFr.js";
import {NavbarEn} from "./english/components/NavbarEn.js";
import {FooterFr} from "./francais/components/FooterFr.js";
import {FooterEn} from "./english/components/FooterEn.js";
import {AboutFr} from "./francais/AboutFr.js";
import {AboutEn} from "./english/AboutEn.js";
import {ContactUsFr} from "./francais/ContactUsFr.js";
import {ContactUsEn} from "./english/ContactUsEn.js";
import {HomeFr} from "./francais/HomeFr.js";
import {HomeEn} from "./english/HomeEn.js";

export const translations = {
    fr: {
        navbar: NavbarFr,
        footer: FooterFr,
        about: AboutFr,
        contactUs: ContactUsFr,
        home: HomeFr
    },
    en: {
        navbar: NavbarEn,
        footer: FooterEn,
        about: AboutEn,
        contactUs: ContactUsEn,
        home: HomeEn
    }
}
export const languages = [
    { code: 'fr', name: 'Français', flag: '/images/canadaFlag.png', shortCode: 'FR' },
    { code: 'en', name: 'English', flag: '/images/US_Flag.png', shortCode: 'EN' }
];