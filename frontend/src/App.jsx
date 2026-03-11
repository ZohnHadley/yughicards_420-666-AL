import {useState} from "react";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Home from "./pages/home/Home.jsx";
import VendezNous from "./pages/VendezNous";
import About from "./pages/About";
import YughiohInventory from "./pages/YughiohInventory.jsx";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer.jsx";
import Contact from "./pages/Contact.jsx";
import ScrollToTop from "./components/ScrollToTop.jsx";
import YughiohCardDetails from "./pages/YughiohCardDetails.jsx";
import ShoppingCart from "./pages/ShoppingCart.jsx";
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";


function App() {
    const [language, setLanguage] = useState("fr");

    return (
        <Router>
            <ScrollToTop/>
            <div className="min-h-screen flex flex-col">
                <Navbar
                    language={language}
                    setLanguage={setLanguage}
                />

                {/* Contenu principal */}
                <main className="flex-grow pt-24">
                    <Routes>
                        <Route path="/" element={<Home language={language}/>}/>
                        <Route path="/vendez-nous" element={<VendezNous language={language}/>}/>
                        <Route path="/inventaire" element={<YughiohInventory language={language}/>}/>
                        <Route path="/about" element={<About language={language}/>}/>
                        <Route path="/contact" element={<Contact language={language}/>}/>

                        {/* Page pour login et register */}
                        <Route path="/login" element={<Login language={language}/>}/>
                        <Route path="/register" element={<Register language={language}/>}/>

                        {/* Page details des cartes */}
                        <Route path="/cardDetails" element={<YughiohCardDetails language={language}/>} />

                        {/* Page du shoppingCart*/}
                        <Route path="/shoppingCard" element={<ShoppingCart language={language}/>} />

                        <Route path="/cardDetails" element={<YughiohCardDetails language={language}/>}/>

                    </Routes>
                </main>


                <Footer language={language}/>
            </div>
        </Router>
    );
}

export default App;
