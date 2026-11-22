# Nyx AI — Android nativo

Aplicativo Android nativo da Nyx, desenvolvido em Java + XML, sem WebView.

## Estrutura

- `AndroidManifest.xml`: manifesto e permissões do aplicativo.
- `src/com/nyxai/android/MainActivity.java`: tela principal, configuração inicial da API Groq, entrada por voz e síntese de fala.
- `res/layout/activity_main.xml`: interface nativa.

## Estado atual

- Interface nativa inicial em tema escuro/roxo.
- Configuração obrigatória da chave Groq na primeira abertura.
- Validação da chave pela API oficial da Groq antes de salvar.
- Chave armazenada localmente com criptografia AES-GCM e chave criptográfica mantida pelo Android Keystore.
- A configuração inicial não reaparece depois de uma chave válida ser salva.
- Campo de conversa e botões de enviar/falar.
- Entrada de voz usando o serviço de reconhecimento disponível no Android.
- Infraestrutura inicial de Text-to-Speech em português do Brasil.
- Nenhuma WebView ou carregamento do site.

## Segurança da chave

A chave é pessoal e fica no dispositivo do usuário, criptografada em armazenamento local. O Android Keystore protege a chave de criptografia. Como qualquer credencial usada por um aplicativo cliente, a chave pode ficar acessível enquanto o app a utiliza em memória; por isso, o usuário deve manter seu aparelho protegido e revogar a chave no painel da Groq se suspeitar de exposição.

A chave ainda não está conectada ao envio de mensagens: a tela inicial e a validação já existem, mas a integração de chat com o modelo Groq é uma próxima etapa.

## Próximas etapas

1. Conectar o chat nativo ao modelo Groq usando a chave configurada.
2. Implementar histórico de conversas e memória personalizada com controles para revisar e apagar dados.
3. Criar o catálogo de ferramentas nativas e o executor de tarefas com permissões explícitas.
4. Implementar controles de privacidade, confirmações para ações sensíveis e registro visível das ações.
5. Testar em diferentes versões e fabricantes Android.

## Compilação

Importe a pasta `android/` no CindraStudio e configure o projeto conforme o formato Android suportado pelo compilador.
