import type { Metadata } from "next";
import Link from "next/link";
import SiteFooter from "../components/site-footer";
import { Command } from "lucide-react";

export const metadata: Metadata = {
  title: "Termos de uso",
  description: "Condições gerais de utilização do site institucional da NYX Bot.",
};

export default function TermosPage() {
  return (
    <main className="legal-page">
      <header className="legal-header">
        <Link className="brand" href="/"><span className="brand-mark"><Command size={17} /></span><span className="brand-name">NYX<span>BOT</span></span></Link>
        <Link className="legal-back" href="/">Voltar ao início ↗</Link>
      </header>
      <article className="legal-content">
        <div className="legal-eyebrow">Informações e transparência</div>
        <h1>Termos de uso</h1>
        <div className="legal-updated">Condições gerais de navegação</div>

        <p>Este site apresenta a NYX Bot e outros projetos independentes de tecnologia. A criação e o desenvolvimento do projeto são de Yato.</p>

        <h2>Finalidade do site</h2>
        <p>O site funciona como uma vitrine informativa. Na versão atual, não possui carrinho, checkout, sistema de pagamentos, contratação de serviços ou venda de produtos. As informações e os recursos apresentados podem mudar conforme os projetos evoluem.</p>

        <h2>Uso adequado</h2>
        <p>Ao navegar, você concorda em não utilizar o site para tentar prejudicar sua disponibilidade, contornar medidas de segurança, distribuir conteúdo ilegal ou violar direitos de outras pessoas.</p>

        <h2>Conteúdo e disponibilidade</h2>
        <p>As descrições são fornecidas para apresentar os projetos e podem ser atualizadas, corrigidas ou removidas. Embora exista cuidado na manutenção do conteúdo, não é garantido que o site permaneça disponível sem interrupções ou que todas as informações estejam sempre atualizadas.</p>
        <p>Marcas, plataformas e serviços de terceiros mencionados ou acessados por links permanecem sujeitos às condições de seus respectivos responsáveis. A NYX Bot não controla páginas externas.</p>

        <h2>Contato</h2>
        <p>Dúvidas sobre o site podem ser encaminhadas pelo e-mail <a href="mailto:contatonyx.ai@gmail.com">contatonyx.ai@gmail.com</a> ou pelo <Link href="/#contato">canal de WhatsApp</Link>. O contato não representa, por si só, contratação de serviço ou compromisso comercial.</p>

        <div className="legal-note"><p>Estes termos descrevem o funcionamento informativo atual do site. Se forem adicionadas vendas, serviços contratáveis, contas ou novas funcionalidades, os termos deverão ser revistos antes dessas mudanças.</p></div>
      </article>
      <SiteFooter />
    </main>
  );
}
