# Nyx AI — Android nativo

Projeto Android nativo (Java), sem WebView.

## Abrir e compilar

Abra a pasta `Nyx_Ai` como projeto Android no Android Studio ou no ambiente Android compatível. O projeto usa Android Gradle Plugin 8.7.3, Gradle 8.9 ou superior e JDK 17. Compile o módulo `:app`.

- SDK de compilação: Android 35
- SDK mínimo: Android 6.0 (API 23)
- Pacote: `com.nyxai.app`

## Configuração inicial

1. Instale e abra o app.
2. Em Configurar a Nyx, informe uma chave Groq criada pelo próprio usuário em https://console.groq.com/keys.
3. A chave é validada pela API e armazenada localmente usando Android Keystore + AES-GCM.
4. A conexão exige internet.

## Recursos implementados nesta base

- Interface Android nativa em tema escuro roxo.
- Chat com Groq e modelo configurado como `qwen/qwen3.8-27b`.
- Entrada de voz em português brasileiro pelo reconhecedor instalado no aparelho.
- Respostas faladas pelo Android TextToSpeech.
- Histórico recente da conversa salvo localmente (até 60 mensagens).
- Memória local persistente, enviada junto ao contexto da IA.
- Ações locais: abrir links HTTPS com confirmação, pesquisa web, Google Maps, copiar, compartilhar e timer.
- Permissão de internet e microfone declaradas no Manifest.

## Ainda não portado nesta etapa

O compartilhamento contínuo da tela com MediaProjection, a análise de imagens, o histórico de múltiplas conversas, a localização em tempo real, o modo live de escuta contínua, as preferências visuais e a voz neural do serviço web ainda precisam de implementação e testes próprios. A versão atual não deve ser considerada uma paridade completa com o site.

## Segurança e privacidade

A chave Groq é guardada no dispositivo de forma criptografada. O conteúdo das conversas e a memória persistida ficam no armazenamento privado do app. As mensagens enviadas à Groq são processadas pelo serviço da Groq para gerar respostas. Não coloque senhas, códigos de autenticação ou informações financeiras na conversa.
