import type { Metadata } from "next";
import Link from "next/link";
import SiteFooter from "../components/site-footer";
import { Command } from "lucide-react";

export const metadata: Metadata = {
  title: "Privacidade",
  description: "Informações sobre privacidade e tratamento de dados no site da NYX Bot.",
};

export default function PrivacidadePage() {
  return (
    <main className="legal-page">
      <header className="legal-header">
        <Link className="brand" href="/"><span className="brand-mark"><Command size={17} /></span><span className="brand-name">NYX<span>BOT</span></span></Link>
        <Link className="legal-back" href="/">Voltar ao início ↗</Link>
      </header>
      <article className="legal-content">
        <div className="legal-eyebrow">Informações e transparência</div>
        <h1>Privacidade</h1>
        <div className="legal-updated">Informações da versão atual do site</div>

        <p>Esta página explica como a NYX Bot trata questões de privacidade no site institucional. A NYX Bot é um projeto independente, criado e desenvolvido por Yato.</p>

        <h2>Quais dados o site solicita?</h2>
        <p>Na versão atual, o site não oferece cadastro, formulário de contato, conta de usuário, carrinho ou área de vendas. A NYX Bot não solicita nome completo, CPF, e-mail, telefone, senhas ou códigos de acesso para navegar pelo site.</p>
        <p>Se você entrar em contato voluntariamente pelo WhatsApp, as informações que decidir enviar serão tratadas também pelos sistemas e pelas políticas do próprio WhatsApp/Meta.</p>

        <h2>Dados técnicos e hospedagem</h2>
        <p>Mesmo sem formulários, a infraestrutura necessária para disponibilizar um site pode processar dados técnicos de conexão, como endereço IP, tipo de navegador, data e hora das requisições e registros de segurança ou diagnóstico. O provedor de hospedagem (Vercel) pode tratar essas informações conforme seus próprios serviços, configurações e políticas. A NYX Bot não opera uma coleta própria de IP para criar perfis de visitantes e não controla todos os registros técnicos mantidos pelo provedor.</p>
        <p>O site também carrega fontes disponibilizadas pelo Google Fonts. Ao solicitar esses arquivos, o navegador pode se comunicar com servidores do Google, sujeitos às políticas do respectivo serviço.</p>

        <h2>Publicidade e compartilhamento</h2>
        <p>Atualmente, o site não exibe anúncios. Também não configuramos ferramentas próprias de publicidade comportamental ou análise de audiência nesta versão. Se futuramente forem incluídos anúncios da própria NYX Bot ou de parceiros, as informações desta página serão revisadas para explicar as tecnologias e os dados envolvidos antes ou no momento da implementação.</p>
        <p>A NYX Bot não vende dados pessoais. Serviços externos necessários à hospedagem, ao carregamento de recursos ou à abertura de canais de contato podem tratar informações conforme suas próprias condições.</p>

        <h2>Seus pedidos e contato</h2>
        <p>Para enviar uma dúvida sobre esta página ou uma solicitação relacionada à privacidade, escreva para <a href="mailto:contatonyx.ai@gmail.com">contatonyx.ai@gmail.com</a>. Você também pode utilizar o canal de WhatsApp disponibilizado no <Link href="/#contato">site</Link>. Evite enviar documentos, senhas ou informações pessoais desnecessárias na mensagem.</p>

        <h2>Atualizações</h2>
        <p>Esta página poderá ser atualizada quando o site, seus recursos, integrações ou práticas de tratamento de dados mudarem. A versão publicada nesta página descreve as informações gerais conhecidas sobre a configuração atual, sem substituir uma avaliação jurídica específica.</p>

        <div className="legal-note"><p><strong>Transparência:</strong> criação e desenvolvimento da NYX Bot: Yato. A identificação de autoria não deve ser confundida, por si só, com a definição formal de controlador de dados ou encarregado de proteção de dados.</p></div>
      </article>
      <SiteFooter />
    </main>
  );
}
