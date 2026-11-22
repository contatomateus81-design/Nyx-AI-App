# Nyx AI — Android nativo

Aplicativo Android nativo da Nyx, desenvolvido em Java 8 + XML, sem WebView.

## Estrutura

- `AndroidManifest.xml`: manifesto e permissões do aplicativo.
- `src/com/nyxai/android/MainActivity.java`: tela principal, entrada por voz via reconhecedor do Android e síntese de fala (TTS).
- `res/layout/activity_main.xml`: interface nativa.

## Estado atual

- Interface nativa inicial em tema escuro/roxo.
- Campo de conversa e botões de enviar/falar.
- Entrada de voz usando o serviço de reconhecimento disponível no Android.
- Infraestrutura inicial de Text-to-Speech em português do Brasil.
- Nenhuma WebView ou carregamento do site.

## Próximas etapas

1. Integrar a API da Groq por uma camada segura, sem colocar chaves secretas no APK.
2. Criar o catálogo de ferramentas nativas e o executor de tarefas com permissões explícitas.
3. Implementar controles de privacidade, confirmações para ações sensíveis e registro visível das ações.
4. Testar em diferentes versões e fabricantes Android.

## Compilação

Importe a pasta `android/` no CindraStudio e configure o projeto conforme o formato Android suportado pelo compilador.
