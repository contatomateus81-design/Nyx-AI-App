import Link from "next/link";
import { Command } from "lucide-react";

export default function SiteFooter() {
  return (
    <footer className="site-footer section-wrap">
      <div className="footer-main">
        <div className="footer-identity">
          <Link className="brand footer-brand" href="/" aria-label="NYX Bot — início">
            <span className="brand-mark"><Command size={17} /></span>
            <span className="brand-name">NYX<span>BOT</span></span>
          </Link>
          <p className="footer-tagline">Bots, automação e tecnologia.</p>
          <p className="footer-author">Criação e desenvolvimento: <strong>Yato</strong></p>
        </div>

        <nav className="footer-links" aria-label="Informações e transparência">
          <span className="footer-links-title">Informações e transparência</span>
          <div className="footer-link-list">
            <Link href="/privacidade">Privacidade</Link>
            <Link href="/termos">Termos de uso</Link>
            <Link href="/cookies">Cookies</Link>
            <Link href="/acessibilidade">Acessibilidade</Link>
          </div>
          <div className="footer-contact">
            <span className="footer-links-title">Contato</span>
            <a href="mailto:contatonyx.ai@gmail.com">contatonyx.ai@gmail.com</a>
          </div>
        </nav>
      </div>

      <div className="footer-bottom">
        <span>© {new Date().getFullYear()} NYX Bot. Feito com intenção.</span>
        <span>Projeto independente.</span>
      </div>
    </footer>
  );
}
