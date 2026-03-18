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
import {VendezNousEn} from "./english/VendezNous.En.js";
import {VendezNousFr} from "./francais/VendezNousFr.js";
import {YughiohInventoryFr} from "./francais/YughioInventoryFr.js";
import {YughiohInventoryEn} from "./english/YughiohInventoryEn.js";
import {YughiohCardDetailsFr} from "./francais/YughiohCardDetailsFr.js";
import {YughiohCardDetailsEn} from "./english/YughiohCardDetailsEn.js";
import {LoginFr} from "./francais/LoginFr.js";
import {RegisterFr} from "./francais/RegistrerFr.js";
import {LoginEn} from "./english/LoginEn.js";
import {RegisterEn} from "./english/RegisterEn.js";
import {ShoppingCartFr} from "./francais/ShoppingCartFr.js";
import {ShoppingCartEn} from "./english/ShoppingCartEn.js";
import {ThankYouFr} from "./francais/ThankYouFr.js";
import {ThankYouEn} from "./english/ThankYouEn.js";
import {AiChatBoxEn} from "./english/components/Ai/AiChatBoxEn.js";
import {AiChatBoxFr} from "./francais/components/Ai/AiChatBoxFr.js";
import {OrderHistoryEn} from "./english/OrderHistoryEn.js";
import {OrderHistoryFr} from "./francais/OrderHistoryFr.js";

export const translations = {
    fr: {
        navbar: NavbarFr,
        footer: FooterFr,
        about: AboutFr,
        contactUs: ContactUsFr,
        home: HomeFr,
        vendezNous: VendezNousFr,
        yughiohInventory: YughiohInventoryFr,
        yughiohCardDetails: YughiohCardDetailsFr,
        shoppingCart: ShoppingCartFr,
        login: LoginFr,
        register: RegisterFr,
        thankYou: ThankYouFr,
        aiChatBox: AiChatBoxFr,
        orderHistory: OrderHistoryFr,
    },
    en: {
        navbar: NavbarEn,
        footer: FooterEn,
        about: AboutEn,
        contactUs: ContactUsEn,
        home: HomeEn,
        vendezNous: VendezNousEn,
        yughiohInventory: YughiohInventoryEn,
        yughiohCardDetails: YughiohCardDetailsEn,
        shoppingCart: ShoppingCartEn,
        login: LoginEn,
        register: RegisterEn,
        thankYou: ThankYouEn,
        aiChatBox: AiChatBoxEn,
        orderHistory: OrderHistoryEn,
    }
}
export const languages = [
    { code: 'fr', name: 'Français', flag: '/images/canadaFlag.png', shortCode: 'FR' },
    { code: 'en', name: 'English', flag: '/images/US_Flag.png', shortCode: 'EN' }
];