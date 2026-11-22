package com.nyxai.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int REQUEST_VOICE = 1001;
    private static final String PREFS = "nyx_secure_settings";
    private static final String PREF_API_KEY = "groq_api_key";
    private static final String KEY_ALIAS = "nyx_groq_key_v1";
    private static final String GROQ_MODELS_URL = "https://api.groq.com/openai/v1/models";

    private EditText messageInput;
    private TextView conversation;
    private TextToSpeech speech;
    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        messageInput = (EditText) findViewById(R.id.message_input);
        conversation = (TextView) findViewById(R.id.conversation);
        Button sendButton = (Button) findViewById(R.id.send_button);
        Button voiceButton = (Button) findViewById(R.id.voice_button);

        speech = new TextToSpeech(this, this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVoiceInput();
            }
        });

        if (loadApiKey() == null) {
            messageInput.post(new Runnable() {
                @Override
                public void run() {
                    showApiSetupDialog();
                }
            });
        }
    }

    private void showApiSetupDialog() {
        final LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(24), dp(8), dp(24), dp(8));

        ScrollView scroll = new ScrollView(this);
        TextView instructions = new TextView(this);
        instructions.setTextColor(Color.rgb(48, 39, 60));
        instructions.setTextSize(15);
        instructions.setLineSpacing(dp(3), 1.0f);
        instructions.setText(
                "Olá, meu bem!\n" +
                "Primeira vez que você me vê?\n" +
                "Bom, antes de continuar, você precisa configurar uma coisinha. Uma API, tá? 😸\n\n" +
                "Mas não é difícil não. Só segue esses passos.\n\n" +
                "1. Acesse https://console.groq.com/keys\n" +
                "2. Faça login se precisar.\n" +
                "3. Crie uma nova API com o nome que você quiser.\n" +
                "4. Copie ela e cole aqui no campo abaixo.\n\n" +
                "Essa API vai ser o que me mantém ativa com você, tá? Só isso. Agora pode clicar no botão de prosseguir ☺️"
        );
        SpannableString linkedText = new SpannableString(instructions.getText());
        final String link = "https://console.groq.com/keys";
        int linkStart = instructions.getText().toString().indexOf(link);
        if (linkStart >= 0) {
            linkedText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    } catch (ActivityNotFoundException ignored) {
                        Toast.makeText(MainActivity.this, "Não foi possível abrir o navegador.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, linkStart, linkStart + link.length(), 0);
        }
        instructions.setText(linkedText);
        instructions.setMovementMethod(LinkMovementMethod.getInstance());
        scroll.addView(instructions);
        content.addView(scroll, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        TextView fieldLabel = new TextView(this);
        fieldLabel.setText("Sua chave da API Groq");
        fieldLabel.setTextColor(Color.rgb(80, 48, 120));
        fieldLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        fieldLabel.setPadding(0, dp(16), 0, dp(6));
        content.addView(fieldLabel);

        final EditText apiInput = new EditText(this);
        apiInput.setSingleLine(true);
        apiInput.setHint("gsk_...");
        apiInput.setInputType(EditorInfo.TYPE_CLASS_TEXT |
                EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        apiInput.setTextColor(Color.BLACK);
        apiInput.setHintTextColor(Color.GRAY);
        apiInput.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                Color.rgb(117, 70, 168)));
        apiInput.setPadding(dp(12), dp(10), dp(12), dp(10));
        content.addView(apiInput);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Configuração inicial")
                .setView(content)
                .setCancelable(false)
                .setPositiveButton("Prosseguir", null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
            @Override
            public void onShow(android.content.DialogInterface ignored) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String apiKey = apiInput.getText().toString().trim();
                        if (apiKey.length() < 20) {
                            apiInput.setError("Cole uma chave Groq válida.");
                            return;
                        }
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Verificando...");
                        validateGroqKey(apiKey, dialog);
                    }
                });
            }
        });
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Prosseguir");
    }

    private void validateGroqKey(final String apiKey, final AlertDialog dialog) {
        networkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                int responseCode = -1;
                try {
                    URL url = new URL(GROQ_MODELS_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(12000);
                    connection.setReadTimeout(12000);
                    connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                    responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        saveApiKey(apiKey);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this,
                                        "Chave configurada! Agora podemos conversar. 💜",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        final int code = responseCode;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Prosseguir");
                                String message = code == 401
                                        ? "A Groq não reconheceu essa chave. Confira e tente novamente."
                                        : "Não foi possível validar a chave (HTTP " + code + "). Tente novamente.";
                                apiInputError(dialog, message);
                            }
                        });
                    }
                } catch (final Exception exception) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Prosseguir");
                            Toast.makeText(MainActivity.this,
                                    "Falha de conexão. Confira sua internet e tente novamente.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    if (connection != null) connection.disconnect();
                }
            }
        });
    }

    private void apiInputError(AlertDialog dialog, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void saveApiKey(String apiKey) throws Exception {
        byte[] iv;
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey());
        iv = cipher.getIV();
        byte[] encrypted = cipher.doFinal(apiKey.getBytes(StandardCharsets.UTF_8));
        String stored = Base64.encodeToString(iv, Base64.NO_WRAP) + ":" +
                Base64.encodeToString(encrypted, Base64.NO_WRAP);
        getSharedPreferences(PREFS, MODE_PRIVATE).edit().putString(PREF_API_KEY, stored).apply();
    }

    private String loadApiKey() {
        try {
            String stored = getSharedPreferences(PREFS, MODE_PRIVATE).getString(PREF_API_KEY, null);
            if (stored == null) return null;
            String[] parts = stored.split(":", 2);
            if (parts.length != 2) return null;
            byte[] iv = Base64.decode(parts[0], Base64.NO_WRAP);
            byte[] encrypted = Base64.decode(parts[1], Base64.NO_WRAP);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), new GCMParameterSpec(128, iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            getSharedPreferences(PREFS, MODE_PRIVATE).edit().remove(PREF_API_KEY).apply();
            return null;
        }
    }

    private SecretKey getOrCreateSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        java.security.Key existing = keyStore.getKey(KEY_ALIAS, null);
        if (existing instanceof SecretKey) return (SecretKey) existing;

        KeyGenerator generator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        generator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build());
        return generator.generateKey();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (message.length() == 0) return;
        if (loadApiKey() == null) {
            showApiSetupDialog();
            return;
        }
        appendMessage("Você", message);
        messageInput.setText("");
        appendMessage("Nyx", "A configuração da API está pronta. A integração de conversa será ativada na próxima etapa.");
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale com a Nyx");
        try {
            startActivityForResult(intent, REQUEST_VOICE);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this, "Reconhecimento de voz indisponível neste dispositivo.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VOICE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                messageInput.setText(results.get(0));
                messageInput.setSelection(messageInput.length());
            }
        }
    }

    private void appendMessage(String author, String message) {
        String current = conversation.getText().toString();
        conversation.setText(current + (current.length() == 0 ? "" : "\n\n")
                + author + ": " + message);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) speech.setLanguage(new Locale("pt", "BR"));
    }

    @Override
    protected void onDestroy() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
        networkExecutor.shutdownNow();
        super.onDestroy();
    }
}
