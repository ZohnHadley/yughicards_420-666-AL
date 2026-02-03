import { Link } from 'react-router-dom'
import { ShoppingCart, Menu, X } from 'lucide-react'
import { useState } from 'react'

function Layout({ children }) {
    const [isMenuOpen, setIsMenuOpen] = useState(false)

    return (
        <div className="min-h-screen bg-gradient-to-br from-purple-900 via-indigo-900 to-blue-900">
            {/* Header */}
            <header className="bg-black/40 backdrop-blur-md border-b border-purple-500/30 sticky top-0 z-50">
                <nav className="container mx-auto px-4 py-4">
                    <div className="flex items-center justify-between">
                        {/* Logo */}
                        <Link to="/" className="flex items-center space-x-2">
                            <div className="w-12 h-12 bg-gradient-to-br from-yellow-400 to-orange-500 rounded-lg flex items-center justify-center font-bold text-2xl text-white shadow-lg shadow-yellow-500/50">
                                YG
                            </div>
                            <span className="text-2xl font-bold text-white">
                Yu-Gi-Oh! <span className="text-yellow-400">Cards</span>
              </span>
                        </Link>

                        {/* Desktop Navigation */}
                        <div className="hidden md:flex items-center space-x-8">
                            <Link to="/" className="text-white hover:text-yellow-400 transition-colors font-medium">
                                Accueil
                            </Link>
                            <Link to="/shop" className="text-white hover:text-yellow-400 transition-colors font-medium">
                                Boutique
                            </Link>
                            <Link to="/cart" className="relative">
                                <ShoppingCart className="w-6 h-6 text-white hover:text-yellow-400 transition-colors" />
                                <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                  0
                </span>
                            </Link>
                        </div>

                        {/* Mobile Menu Button */}
                        <button
                            onClick={() => setIsMenuOpen(!isMenuOpen)}
                            className="md:hidden text-white"
                        >
                            {isMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
                        </button>
                    </div>

                    {/* Mobile Navigation */}
                    {isMenuOpen && (
                        <div className="md:hidden mt-4 pb-4 space-y-4">
                            <Link to="/" className="block text-white hover:text-yellow-400 transition-colors font-medium" onClick={() => setIsMenuOpen(false)}>Accueil</Link>
                            <Link to="/shop" className="block text-white hover:text-yellow-400 transition-colors font-medium" onClick={() => setIsMenuOpen(false)}>Boutique</Link>
                            <Link to="/cart" className="block text-white hover:text-yellow-400 transition-colors font-medium" onClick={() => setIsMenuOpen(false)}>Panier</Link>
                        </div>
                    )}
                </nav>
            </header>

            {/* Main Content */}
            <main className="min-h-[calc(100vh-200px)]">
                {children}
            </main>

            {/* Footer */}
            <footer className="bg-black/60 backdrop-blur-md border-t border-purple-500/30 mt-20">
                <div className="container mx-auto px-4 py-8">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                        <div>
                            <h3 className="text-yellow-400 font-bold text-lg mb-4">Yu-Gi-Oh! Cards</h3>
                            <p className="text-gray-300 text-sm">
                                Votre destination ultime pour les cartes Yu-Gi-Oh! authentiques et rares.
                            </p>
                        </div>
                        <div>
                            <h3 className="text-yellow-400 font-bold text-lg mb-4">Liens rapides</h3>
                            <ul className="space-y-2 text-sm">
                                <li><Link to="/" className="text-gray-300 hover:text-yellow-400 transition-colors">Accueil</Link></li>
                                <li><Link to="/shop" className="text-gray-300 hover:text-yellow-400 transition-colors">Boutique</Link></li>
                            </ul>
                        </div>
                        <div>
                            <h3 className="text-yellow-400 font-bold text-lg mb-4">Contact</h3>
                            <p className="text-gray-300 text-sm">
                                Email: contact@yugiohcards.com<br />
                                Tél: +33 1 23 45 67 89
                            </p>
                        </div>
                    </div>
                    <div className="border-t border-purple-500/30 mt-8 pt-8 text-center">
                        <p className="text-gray-400 text-sm">© 2026 Yu-Gi-Oh! Cards. Tous droits réservés.</p>
                    </div>
                </div>
            </footer>
        </div>
    )
}

export default Layout
