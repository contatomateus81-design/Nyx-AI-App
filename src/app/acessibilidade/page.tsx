import type { Metadata } from "next";
import Link from "next/link";
import SiteFooter from "../components/site-footer";
import { Command } from "lucide-react";

export const metadata: Metadata = {
  title: "Acessibilidade",
  description: "Recursos de acessibilidade e canal para comunicar dificuldades no site da NYX Bot.",
};

export default function AcessibilidadePage() {
  return (
    <main className="legal-page">
      <header className="legal-header">
        <Link className="brand" href="/"><span className="brand-mark"><Command size={17} /></span><span className="brand-name">NYX<span>BOT</span></span></Link>
        <Link className="legal-back" href="/">Voltar ao início ↗</Link>
      </header>
      <article className="legal-content">
        <div className="legal-eyebrow">Informações e transparência</div>
        <h1>Acessibilidade</h1>
        <div className="legal-updated">Compromisso de melhoria contínua</div>

        <p>A NYX Bot busca manter uma experiência de navegação clara e utilizável em diferentes tamanhos de tela e dispositivos.</p>

        <h2>Recursos implementados</h2>
        <ul>
          <li>Estrutura de página com regiões e elementos semânticos para facilitar a compreensão do conteúdo.</li>
          <li>Layout responsivo para telas de celulares e computadores.</li>
          <li>Indicação visual de foco em links navegáveis pelo teclado.</li>
          <li>Respeito à preferência do dispositivo por redução de movimento em animações.</li>
        </ul>

        <h2>Limitações</h2>
        <p>Os recursos listados não significam que o site tenha passado por uma auditoria independente ou que exista garantia de conformidade integral com todas as diretrizes de acessibilidade. A experiência pode variar conforme o navegador, dispositivo e tecnologia assistiva utilizados.</p>

        <h2>Relatar uma dificuldade</h2>
        <p>Se encontrar uma barreira de navegação, dificuldade de leitura ou problema de compatibilidade, envie uma descrição para <a href="mailto:contatonyx.ai@gmail.com">contatonyx.ai@gmail.com</a> ou pelo <Link href="/#contato">canal de WhatsApp</Link>. Se possível, informe qual página e dispositivo apresentaram o problema, sem compartilhar dados pessoais desnecessários.</p>
      </article>
      <SiteFooter />
    </main>
  );
}
