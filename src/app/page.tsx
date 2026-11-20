"use client";

import SiteFooter from "./components/site-footer";
import ProjectAnnouncements from "./components/project-announcements";

import {
  ArrowDown,
  ArrowRight,
  ArrowUpRight,
  Bot,
  BookOpen,
  Braces,
  Code2,
  Command,
  Github,
  Layers3,
  MessageCircle,
  Sparkles,
  Workflow,
  Zap,
} from "lucide-react";

const projects = [
  {
    number: "01",
    icon: Bot,
    name: "NYX",
    category: "BOT PARA WHATSAPP",
    description:
      "Bot para WhatsApp com comandos e recursos úteis para tornar conversas e tarefas do dia a dia mais práticas.",
    status: "ACESSAR NYX",
    href: "https://nyx-ai-pearl.vercel.app",
  },
  {
    number: "02",
    icon: BookOpen,
    name: "Aulas",
    category: "AULAS E CONTEÚDOS",
    description:
      "Aulas e conteúdos para aprender, explorar ideias e desenvolver novas habilidades.",
    status: "ACESSAR AULAS",
    href: "https://nyx-classcode.vercel.app",
  },
  {
    number: "03",
    icon: Code2,
    name: "Experimentos web",
    category: "WEB & TECNOLOGIA",
    description:
      "Projetos independentes para testar conceitos, interfaces e novas possibilidades.",
    status: "VER NO GITHUB",
    href: "https://github.com/contatomateus81-design/Nyx-Bot-WA",
  },
];

export default function Home() {
  return (
    <main>
      <ProjectAnnouncements />
      <div className="ambient ambient-one" aria-hidden="true" />
      <div className="ambient ambient-two" aria-hidden="true" />

      <header className="site-header">
        <a className="brand" href="#inicio" aria-label="NYX Bot — início">
          <span className="brand-mark"><Command size={19} strokeWidth={2.1} /></span>
          <span className="brand-name">NYX<span>BOT</span></span>
        </a>

        <nav className="desktop-nav" aria-label="Navegação principal">
          <a href="#projetos">Projetos</a>
          <a href="#sobre">Sobre</a>
          <a href="#contato">Contato</a>
        </nav>

        <a className="header-cta" href="#projetos">
          Explorar projetos <ArrowUpRight size={15} />
        </a>
      </header>

      <section className="hero section-wrap" id="inicio">
        <div className="hero-backdrop" aria-hidden="true" />
        <div className="hero-copy">
          <div className="eyebrow"><span className="eyebrow-pulse" /> UM UNIVERSO DE POSSIBILIDADES</div>
          <h1>Ideias que viram<br /><span>tecnologia.</span></h1>
          <p className="hero-lede">Bots, automação e tecnologia.</p>
          <p className="hero-description">
            A NYX Bot reúne bots para WhatsApp, automação de tarefas e projetos web independentes — soluções digitais criadas para simplificar processos e transformar ideias em ferramentas úteis.
          </p>
          <div className="hero-actions">
            <a className="button button-primary" href="#projetos">Conhecer projetos <ArrowRight size={16} /></a>
            <a className="text-link" href="#sobre">Conheça a marca <ArrowDown size={15} /></a>
          </div>
          <div className="hero-footnote"><span className="mini-star"><Sparkles size={13} /></span> Criado com curiosidade. Construído com propósito.</div>
        </div>

        <div className="hero-art" aria-label="Elemento gráfico abstrato em tons violetas">
          <div className="orbit orbit-outer" />
          <div className="orbit orbit-middle" />
          <div className="orbit orbit-inner" />
          <div className="art-glow" />
          <div className="art-core"><Command size={57} strokeWidth={1.1} /></div>
          <div className="floating-chip chip-top"><span className="chip-dot" /> BOT DEVELOPMENT</div>
          <div className="floating-chip chip-bottom"><Braces size={14} /> AUTOMATION <span>•</span> WEB</div>
          <div className="art-index">Nº 001 <span>—</span> DIGITAL LAB</div>
        </div>
        <a className="scroll-cue" href="#projetos"><span /> ROLE PARA EXPLORAR <ArrowDown size={13} /></a>
      </section>

      <section className="intro-strip">
        <div className="section-wrap strip-inner">
          <div className="strip-label"><span /> O QUE MOVE A NYX BOT</div>
          <p>Curiosidade para imaginar. <span>Conhecimento para construir.</span></p>
          <div className="strip-symbol"><Zap size={19} /></div>
        </div>
      </section>

      <section className="projects section-wrap section-block" id="projetos">
        <div className="section-heading">
          <div>
            <div className="eyebrow"><span className="eyebrow-pulse" /> DO CONCEITO À PRÁTICA</div>
            <h2>Projetos em <span>movimento.</span></h2>
            <p>Uma seleção de ferramentas, ideias e experiências digitais.</p>
          </div>
          <span className="section-count">01 <i>/</i> 03</span>
        </div>

        <div className="project-grid">
          {projects.map((project) => {
            const Icon = project.icon;
            const cardContent = (
              <>
                <div className="card-topline"><span>{project.number} / PROJECT</span><span className="card-icon"><Icon size={18} /></span></div>
                <div className="project-visual"><div className="visual-grid" /><div className="visual-orb"><Icon size={35} strokeWidth={1.2} /></div><span className="visual-corner">{project.number}</span></div>
                <div className="project-category">{project.category}</div>
                <h3>{project.name}</h3>
                <p>{project.description}</p>
                <div className="card-bottom"><span className="project-status"><span />{project.status}</span><span className="card-arrow"><ArrowUpRight size={16} /></span></div>
              </>
            );

            return project.href ? (
              <a className="project-card project-card-anchor" href={project.href} key={project.number}>
                {cardContent}
              </a>
            ) : (
              <article className="project-card" key={project.number}>
                {cardContent}
              </article>
            );
          })}
        </div>
      </section>

      <section className="about-section" id="sobre">
        <div className="section-wrap about-grid">
          <div className="about-symbol"><div className="symbol-square"><Layers3 size={34} strokeWidth={1.2} /></div><span className="symbol-caption">IDEAS / BUILD / REPEAT</span></div>
          <div className="about-copy">
            <div className="eyebrow"><span className="eyebrow-pulse" /> POR TRÁS DOS PROJETOS</div>
            <h2>Tecnologia é melhor<br />quando <span>resolve algo.</span></h2>
            <p>A NYX Bot é um projeto independente de tecnologia focado em bots para WhatsApp, automação digital e experiências web. A ideia é criar ferramentas, testar soluções e simplificar tarefas do dia a dia — sempre partindo de uma pergunta: como tornar isso mais simples, útil ou interessante?</p>
            <div className="principles">
              <div><span className="principle-icon"><Bot size={16} /></span><span><b>Bots úteis</b><small>Ferramentas que ajudam no dia a dia.</small></span></div>
              <div><span className="principle-icon"><Workflow size={16} /></span><span><b>Automação</b><small>Menos repetição, mais possibilidades.</small></span></div>
              <div><span className="principle-icon"><Code2 size={16} /></span><span><b>Experimentos</b><small>Aprender criando e testar ideias novas.</small></span></div>
            </div>
          </div>
        </div>
      </section>

      <section className="contact-section section-wrap section-block" id="contato">
        <div className="contact-card">
          <div className="contact-glow" />
          <div className="contact-content">
            <div className="eyebrow"><span className="eyebrow-pulse" /> VAMOS CONSTRUIR?</div>
            <h2>O próximo projeto<br /><span>começa com uma ideia.</span></h2>
            <p>Tem uma ideia ou quer conversar sobre um projeto? Me chama no WhatsApp.</p>
            <div className="contact-actions">
              <a
                className="button button-primary contact-button"
                href="https://wa.me/555481296959?text=Ol%C3%A1%21%20Vim%20pelo%20site%20da%20NYX%20Bot."
                target="_blank"
                rel="noopener noreferrer"
              >
                <MessageCircle size={16} /> Falar pelo WhatsApp <ArrowUpRight size={15} />
              </a>
            </div>
          </div>
          <div className="contact-stamp"><MessageCircle size={23} /><span>WHATSAPP</span><small>ONLINE</small></div>
        </div>
      </section>

      <SiteFooter />
    </main>
  );
}
