import type { Metadata } from "next";
import "./globals.css";

const siteUrl = "https://nyx-bot-creator.vercel.app";

export const metadata: Metadata = {
  metadataBase: new URL(siteUrl),
  title: {
    default: "NYX Bot | Bots para WhatsApp, automação e projetos digitais",
    template: "%s | NYX Bot",
  },
  description:
    "Conheça a NYX Bot: projeto independente de bots para WhatsApp, automação de tarefas e experiências web. Tecnologia prática para transformar ideias em soluções digitais.",
  applicationName: "NYX Bot",
  alternates: {
    canonical: "/",
  },
  icons: {
    icon: "/favicon.png?v=2",
    shortcut: "/favicon.png?v=2",
    apple: "/favicon.png?v=2",
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      "max-image-preview": "large",
      "max-snippet": -1,
      "max-video-preview": -1,
    },
  },
  openGraph: {
    type: "website",
    url: siteUrl,
    siteName: "NYX Bot",
    locale: "pt_BR",
    title: "NYX Bot | Bots para WhatsApp, automação e projetos digitais",
    description:
      "Bots para WhatsApp, automação de tarefas e experiências web desenvolvidos com foco em ideias úteis e soluções digitais.",
    images: [
      {
        url: "/nyx-background.jpg",
        alt: "NYX Bot — bots, automação e tecnologia em tons violetas",
      },
    ],
  },
  twitter: {
    card: "summary_large_image",
    title: "NYX Bot | Bots para WhatsApp, automação e projetos digitais",
    description:
      "Bots para WhatsApp, automação de tarefas e experiências web desenvolvidos com foco em ideias úteis e soluções digitais.",
    images: ["/nyx-background.jpg"],
  },
};

const structuredData = {
  "@context": "https://schema.org",
  "@graph": [
    {
      "@type": "Organization",
      "@id": `${siteUrl}/#organization`,
      name: "NYX Bot",
      url: siteUrl,
      logo: `${siteUrl}/favicon.png`,
      description:
        "Projeto independente dedicado a bots para WhatsApp, automação de tarefas e experiências digitais.",
    },
    {
      "@type": "WebSite",
      "@id": `${siteUrl}/#website`,
      url: siteUrl,
      name: "NYX Bot",
      inLanguage: "pt-BR",
      publisher: {
        "@id": `${siteUrl}/#organization`,
      },
    },
  ],
};

export default function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="pt-BR">
      <body>
        {children}
        <script
          type="application/ld+json"
          dangerouslySetInnerHTML={{
            __html: JSON.stringify(structuredData).replace(/</g, "\\u003c"),
          }}
        />
      </body>
    </html>
  );
}
