# NYX Bot

Site pessoal de apresentação da marca **NYX Bot**.

> Bots, automação e tecnologia.

## Desenvolvimento local

Requisitos: Node.js 20 ou superior.

```bash
npm install
npm run dev
```

Abra http://localhost:3000.

## Background

Quando a arte de fundo estiver pronta, coloque o arquivo em:

`public/background.png`

O site já está preparado para usar essa imagem com uma camada escura por cima, para manter o texto legível. Se o arquivo ainda não existir, o fundo em degradê continua funcionando.

## Publicação na Vercel

1. Importe este repositório privado na sua conta Vercel.
2. Mantenha o framework como Next.js e a raiz como `./`.
3. Use o comando de build padrão `npm run build`.
4. Faça o deploy. Os próximos pushes para a branch principal podem disparar novas publicações.

O repositório pode continuar privado; o site publicado será público para quem tiver o endereço.

## Próximas etapas

- Adicionar a arte final do fundo e definir o favicon.
- Incluir os canais de contato quando estiverem definidos.
- Preparar a imagem Open Graph definitiva para as prévias em apps de mensagens.
- Integrar a mensagem de apresentação e a imagem ao comando de dono do bot, mantendo o vCard enviado em seguida.
