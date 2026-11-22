package com.nyxai.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private static final int REQUEST_VOICE = 1101;
    private static final String PREFS = "nyx_secure_settings";
    private static final String PREF_KEY = "groq_key";
    private static final String KEY_ALIAS = "nyx_groq_key_v2";
    private static final String MODEL = "qwen/qwen3.8-27b";
    private static final String SYSTEM =
        "Você é Nyx, uma assistente virtual feminina, gentil, inteligente e levemente divertida. " +
        "Fale em português brasileiro, com naturalidade e respostas objetivas. Você é a versão nativa Android. " +
        "Nunca diga que executou uma ação antes de ela acontecer. Não invente informações nem capacidades. " +
        "Quando o usuário pedir uma ação compatível, responda normalmente e acrescente ao final uma linha " +
        "no formato [[ACTION {\\"type\\":\\"...\\",...}]]. Ações: open com url https; search com query; " +
        "maps com query; copy com text; share com text; remember com fact; timer com seconds e label. " +
        "Não gere ações para ligar, enviar mensagens, e-mails, compras ou qualquer ação sensível. " +
        "Para ações que não existem no app, explique a limitação. A chave Groq pertence ao usuário e fica " +
        "armazenada localmente de forma criptografada. Não peça senhas ou dados pessoais.";
    private final ArrayList<JSONObject> history = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private LinearLayout messageList;
    private ScrollView scroll;
    private EditText input;
    private Button sendButton;
    private TextToSpeech tts;
    private boolean voiceEnabled = true;
    private boolean busy = false;
    private String memory = "";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        tts = new TextToSpeech(this, this);
        buildInterface();
        if (readApiKey() == null) handler.postDelayed(this::showApiSetup, 350);
        else addMessage("Nyx", "Oi! Sou a Nyx. Estou pronta para conversar com você. 💜");
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, int size, int color) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextSize(size);
        v.setTextColor(color);
        return v;
    }

    private void buildInterface() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(12), dp(16), dp(12));
        root.setBackgroundColor(Color.rgb(9, 6, 17));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(0, dp(8), 0, dp(14));
        TextView title = text("✦ NYX AI", 25, Color.rgb(205, 169, 255));
        title.setTypeface(null, 1);
        header.addView(title, new LinearLayout.LayoutParams(0, -2, 1));
        Button settings = button("⚙");
        settings.setOnClickListener(v -> showSettings());
        header.addView(settings);
        root.addView(header);

        scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(20, 15, 30));
        messageList = new LinearLayout(this);
        messageList.setOrientation(LinearLayout.VERTICAL);
        messageList.setPadding(dp(12), dp(14), dp(12), dp(14));
        scroll.addView(messageList);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        input = new EditText(this);
        input.setHint("Converse com a Nyx...");
        input.setHintTextColor(Color.rgb(165, 150, 185));
        input.setTextColor(Color.WHITE);
        input.setTextSize(16);
        input.setPadding(dp(14), dp(10), dp(14), dp(10));
        input.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(151, 104, 211)));
        input.setMinLines(1);
        input.setMaxLines(4);
        root.addView(input, new LinearLayout.LayoutParams(-1, -2));

        LinearLayout actions = new LinearLayout(this);
        actions.setGravity(Gravity.CENTER);
        actions.setPadding(0, dp(8), 0, 0);
        Button voice = button("🎙 FALAR");
        voice.setOnClickListener(v -> startVoiceInput());
        actions.addView(voice, new LinearLayout.LayoutParams(0, dp(48), 1));
        sendButton = button("ENVIAR");
        sendButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(119, 72, 174)));
        sendButton.setOnClickListener(v -> sendMessage());
        LinearLayout.LayoutParams sendParams = new LinearLayout.LayoutParams(0, dp(48), 1);
        sendParams.setMargins(dp(8), 0, 0, 0);
        actions.addView(sendButton, sendParams);
        root.addView(actions);
        setContentView(root);
        addMessage("Nyx", "Oi! Sou a Nyx. Como posso ajudar? 💜");
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(77, 48, 112)));
        return b;
    }

    private void addMessage(String who, String value) {
        TextView bubble = text(who + "\n" + value, 15, who.equals("Você") ? Color.WHITE : Color.rgb(239, 229, 250));
        bubble.setPadding(dp(12), dp(10), dp(12), dp(10));
        bubble.setBackgroundColor(who.equals("Você") ? Color.rgb(75, 48, 105) : Color.rgb(37, 28, 51));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(0, 0, 0, dp(10));
        messageList.addView(bubble, params);
        scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
    }

    private void showApiSetup() {
        EditText field = new EditText(this);
        field.setSingleLine(true);
        field.setHint("gsk_...");
        field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(this)
            .setTitle("Configurar a Nyx")
            .setMessage("Para conversar com a IA, informe sua chave pessoal da Groq. Ela será validada e armazenada criptografada neste dispositivo.\n\nCrie uma chave em console.groq.com/keys.")
            .setView(field)
            .setCancelable(false)
            .setPositiveButton("Validar e salvar", (dialog, which) -> {
                String key = field.getText().toString().trim();
                if (key.length() < 20) {
                    Toast.makeText(this, "A chave parece incompleta.", Toast.LENGTH_LONG).show();
                    handler.postDelayed(this::showApiSetup, 500);
                } else validateAndSaveKey(key);
            })
            .setNegativeButton("Depois", (dialog, which) -> addMessage("Nyx", "Tudo bem. Você pode configurar a chave nas opções quando quiser."))
            .show();
    }

    private void showSettings() {
        String[] options = {"Configurar chave Groq", "Ativar/desativar voz", "Limpar conversa", "Sobre a Nyx"};
        new AlertDialog.Builder(this).setTitle("Configurações")
            .setItems(options, (d, which) -> {
                if (which == 0) showApiSetup();
                else if (which == 1) {
                    voiceEnabled = !voiceEnabled;
                    Toast.makeText(this, voiceEnabled ? "Voz ativada" : "Voz desativada", Toast.LENGTH_SHORT).show();
                    if (!voiceEnabled && tts != null) tts.stop();
                } else if (which == 2) {
                    history.clear();
                    messageList.removeAllViews();
                    addMessage("Nyx", "Conversa limpa. Podemos começar de novo. 💜");
                } else new AlertDialog.Builder(this).setTitle("Nyx AI")
                    .setMessage("Assistente nativa Android. Texto, reconhecimento de voz, fala, memória local e ações com confirmação quando necessário.").setPositiveButton("OK", null).show();
            }).show();
    }

    private void validateAndSaveKey(String key) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL("https://api.groq.com/openai/v1/models").openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(12000);
                connection.setReadTimeout(12000);
                connection.setRequestProperty("Authorization", "Bearer " + key);
                int code = connection.getResponseCode();
                if (code == 200) {
                    saveApiKey(key);
                    handler.post(() -> {
                        Toast.makeText(this, "Chave validada e salva com segurança. 💜", Toast.LENGTH_LONG).show();
                        addMessage("Nyx", "Prontinho! Minha conexão com a IA está configurada.");
                    });
                } else handler.post(() -> Toast.makeText(this, code == 401 ? "A Groq não reconheceu essa chave." : "Falha na validação (HTTP " + code + ").", Toast.LENGTH_LONG).show());
            } catch (Exception e) {
                handler.post(() -> Toast.makeText(this, "Não foi possível conectar. Verifique a internet.", Toast.LENGTH_LONG).show());
            } finally { if (connection != null) connection.disconnect(); }
        }).start();
    }

    private SecretKey getSecretKey() throws Exception {
        KeyStore store = KeyStore.getInstance("AndroidKeyStore");
        store.load(null);
        if (store.containsAlias(KEY_ALIAS)) return ((KeyStore.SecretKeyEntry) store.getEntry(KEY_ALIAS, null)).getSecretKey();
        KeyGenerator generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        generator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build());
        return generator.generateKey();
    }

    private void saveApiKey(String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
            byte[] encrypted = cipher.doFinal(key.getBytes(StandardCharsets.UTF_8));
            String packed = android.util.Base64.encodeToString(cipher.getIV(), android.util.Base64.NO_WRAP) + ":" +
                android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP);
            getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(PREF_KEY, packed).apply();
        } catch (Exception e) { handler.post(() -> Toast.makeText(this, "Não consegui proteger a chave neste aparelho.", Toast.LENGTH_LONG).show()); }
    }

    private String readApiKey() {
        try {
            String packed = getSharedPreferences(PREFS, MODE_PRIVATE).getString(PREF_KEY, null);
            if (packed == null) return null;
            String[] parts = packed.split(":", 2);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new GCMParameterSpec(128, android.util.Base64.decode(parts[0], android.util.Base64.NO_WRAP)));
            return new String(cipher.doFinal(android.util.Base64.decode(parts[1], android.util.Base64.NO_WRAP)), StandardCharsets.UTF_8);
        } catch (Exception e) { return null; }
    }

    private void sendMessage() {
        if (busy) return;
        String message = input.getText().toString().trim();
        if (message.isEmpty()) return;
        String key = readApiKey();
        if (key == null) { showApiSetup(); return; }
        input.setText("");
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(input.getWindowToken(), 0);
        addMessage("Você", message);
        try { history.add(new JSONObject().put("role", "user").put("content", message)); }
        catch (Exception ignored) {}
        busy = true;
        sendButton.setEnabled(false);
        addMessage("Nyx", "Pensando...");
        new Thread(() -> requestChat(key)).start();
    }

    private void requestChat(String key) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://api.groq.com/openai/v1/chat/completions").openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Authorization", "Bearer " + key);
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", SYSTEM + (memory.isEmpty() ? "" : "\nMemória local autorizada pelo usuário:\n" + memory)));
            int start = Math.max(0, history.size() - 20);
            for (int i = start; i < history.size(); i++) messages.put(history.get(i));
            JSONObject body = new JSONObject().put("model", MODEL).put("messages", messages).put("temperature", 0.7);
            try (OutputStream out = connection.getOutputStream()) { out.write(body.toString().getBytes(StandardCharsets.UTF_8)); }
            int code = connection.getResponseCode();
            InputStream stream = code >= 200 && code < 300 ? connection.getInputStream() : connection.getErrorStream();
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line; while ((line = reader.readLine()) != null) response.append(line);
            }
            if (code < 200 || code >= 300) {
                JSONObject error = new JSONObject(response.toString());
                String detail = error.optJSONObject("error") == null ? "" : error.optJSONObject("error").optString("message", "");
                throw new Exception(code == 429 ? "Limite de pedidos atingido. Tente novamente mais tarde." : "A Groq retornou HTTP " + code + (detail.isEmpty() ? "" : ": " + detail));
            }
            String answer = new JSONObject(response.toString()).getJSONArray("choices").getJSONObject(0).getJSONObject("message").optString("content", "");
            String visible = answer;
            String action = null;
            int marker = answer.lastIndexOf("[[ACTION ");
            if (marker >= 0 && answer.endsWith("]]")) {
                action = answer.substring(marker + 9, answer.length() - 2).trim();
                visible = answer.substring(0, marker).trim();
            }
            final String finalVisible = visible.isEmpty() ? "…" : visible;
            final String finalAction = action;
            handler.post(() -> {
                removeThinkingBubble();
                addMessage("Nyx", finalVisible);
                try { history.add(new JSONObject().put("role", "assistant").put("content", answer)); } catch (Exception ignored) {}
                if (voiceEnabled && tts != null) tts.speak(finalVisible, TextToSpeech.QUEUE_FLUSH, null, "nyx_reply");
                if (finalAction != null) handleAction(finalAction);
                finishRequest();
            });
        } catch (Exception e) {
            String message = e.getMessage() == null ? "Não consegui falar com a IA agora." : e.getMessage();
            handler.post(() -> { removeThinkingBubble(); addMessage("Nyx", "Não consegui responder: " + message); finishRequest(); });
        } finally { if (connection != null) connection.disconnect(); }
    }

    private void removeThinkingBubble() {
        int count = messageList.getChildCount();
        if (count > 0) {
            View last = messageList.getChildAt(count - 1);
            if (last instanceof TextView && ((TextView) last).getText().toString().startsWith("Nyx\nPensando")) messageList.removeView(last);
        }
    }

    private void finishRequest() { busy = false; sendButton.setEnabled(true); }

    private void handleAction(String raw) {
        try {
            JSONObject action = new JSONObject(raw);
            String type = action.optString("type");
            if (type.equals("remember")) {
                String fact = action.optString("fact").trim();
                if (!fact.isEmpty()) {
                    memory += (memory.isEmpty() ? "" : "\n") + "- " + fact;
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString("nyx_memory", memory).apply();
                    Toast.makeText(this, "Memória salva neste dispositivo.", Toast.LENGTH_SHORT).show();
                }
            } else if (type.equals("copy")) {
                String value = action.optString("text");
                ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Nyx", value));
                Toast.makeText(this, "Texto copiado.", Toast.LENGTH_SHORT).show();
            } else if (type.equals("share")) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, action.optString("text"));
                startActivity(Intent.createChooser(intent, "Compartilhar"));
            } else if (type.equals("timer")) {
                int seconds = Math.max(1, Math.min(86400, action.optInt("seconds", 60)));
                String label = action.optString("label", "Seu timer terminou.");
                Toast.makeText(this, "Timer iniciado: " + seconds + " segundos.", Toast.LENGTH_SHORT).show();
                handler.postDelayed(() -> {
                    new AlertDialog.Builder(this).setTitle("Nyx • Timer").setMessage(label).setPositiveButton("OK", null).show();
                    if (voiceEnabled && tts != null) tts.speak(label, TextToSpeech.QUEUE_FLUSH, null, "nyx_timer");
                }, seconds * 1000L);
            } else if (type.equals("open") || type.equals("search") || type.equals("maps")) {
                String url;
                if (type.equals("open")) {
                    url = action.optString("url");
                    if (!url.startsWith("https://")) throw new Exception("Só posso abrir links HTTPS.");
                } else if (type.equals("maps")) {
                    url = "https://www.google.com/maps/search/?api=1&query=" + android.net.Uri.encode(action.optString("query"));
                } else {
                    url = "https://www.google.com/search?q=" + android.net.Uri.encode(action.optString("query"));
                }
                new AlertDialog.Builder(this).setTitle("Confirmar ação")
                    .setMessage("A Nyx pediu para abrir este endereço:\n" + url)
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Abrir", (d, w) -> startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))))
                    .show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Não consegui executar essa ação.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale com a Nyx");
        try { startActivityForResult(intent, REQUEST_VOICE); }
        catch (Exception e) { Toast.makeText(this, "Reconhecimento de voz indisponível neste aparelho.", Toast.LENGTH_LONG).show(); }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VOICE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) { input.setText(results.get(0)); sendMessage(); }
        }
    }

    @Override public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS && tts != null) {
            tts.setLanguage(new Locale("pt", "BR"));
            tts.setPitch(1.08f);
            tts.setSpeechRate(0.98f);
        }
    }

    @Override protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }
}