import type { Metadata } from "next";
import Link from "next/link";
import SiteFooter from "../components/site-footer";
import { Command } from "lucide-react";

export const metadata: Metadata = {
  title: "Cookies",
  description: "Informações sobre cookies e tecnologias semelhantes no site da NYX Bot.",
};

export default function CookiesPage() {
  return (
    <main className="legal-page">
      <header className="legal-header">
        <Link className="brand" href="/"><span className="brand-mark"><Command size={17} /></span><span className="brand-name">NYX<span>BOT</span></span></Link>
        <Link className="legal-back" href="/">Voltar ao início ↗</Link>
      </header>
      <article className="legal-content">
        <div className="legal-eyebrow">Informações e transparência</div>
        <h1>Cookies</h1>
        <div className="legal-updated">Informações da configuração atual</div>

        <p>Cookies são pequenos arquivos ou identificadores que podem ser armazenados pelo navegador para permitir funções técnicas, lembrar preferências ou medir o uso de um site.</p>

        <h2>Como o site funciona atualmente</h2>
        <p>A versão atual da NYX Bot não possui login, carrinho, publicidade ativa ou ferramenta própria de análise de audiência configurada. Não criamos cookies para montar perfis publicitários dos visitantes.</p>
        <p>Isso não significa que toda a infraestrutura ou os serviços externos funcionem sem qualquer armazenamento técnico. A hospedagem e recursos de terceiros podem utilizar registros, identificadores ou tecnologias necessárias à segurança e à entrega dos serviços, conforme suas próprias configurações e políticas.</p>

        <h2>Fontes e serviços externos</h2>
        <p>O site carrega fontes pelo Google Fonts e oferece um link para contato pelo WhatsApp. Ao acessar esses recursos, o navegador pode se conectar diretamente aos respectivos serviços, que possuem suas próprias práticas de privacidade e tecnologias.</p>

        <h2>Controle pelo navegador</h2>
        <p>Você pode consultar as configurações do navegador para visualizar, bloquear ou apagar cookies. O bloqueio de determinados recursos pode afetar o funcionamento de serviços externos ou de funcionalidades que venham a ser adicionadas.</p>

        <h2>Mudanças futuras</h2>
        <p>Se forem adicionados cookies não essenciais, ferramentas de análise ou publicidade de parceiros, esta página será atualizada para explicar sua finalidade e os controles disponíveis. Um mecanismo de consentimento será avaliado quando a tecnologia adotada exigir isso.</p>

        <div className="legal-note"><p>Esta descrição é limitada à configuração conhecida do site e não equivale a uma varredura técnica de todos os cookies ou registros gerados pela infraestrutura de hospedagem.</p></div>
      </article>
      <SiteFooter />
    </main>
  );
}
