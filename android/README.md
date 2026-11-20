# Nyx AI — Android

Primeira versão de teste do aplicativo Android do site principal da Nyx.

## Compatibilidade planejada

- Java 8
- Interface XML tradicional
- Activity Android sem dependências externas
- WebView para exibir o site publicado

## Importar no APK Builder

Use a pasta `android/` como referência do projeto. A estrutura interna segue o formato apresentado pelo APK Builder:

- `AndroidManifest.xml`
- `src/com/nyxai/android/MainActivity.java`
- `res/layout/activity_main.xml`
- `res/values/strings.xml`
- `assets/`, `jniLibs/` e `libs/` (pastas opcionais)

Se o Builder pedir uma pasta de projeto já criada, copie o conteúdo desta pasta para dentro do projeto Java + XML gerado por ele, substituindo os arquivos de demonstração correspondentes.

## Como funciona

O aplicativo abre `https://nyx-bot-creator.vercel.app/` em uma WebView. JavaScript e armazenamento DOM estão habilitados para permitir que a aplicação web funcione. Links externos são encaminhados ao aplicativo apropriado do Android; a navegação no domínio principal permanece dentro do app.

Esta versão precisa ser compilada e testada no APK Builder. O repositório ainda não contém um APK compilado.
