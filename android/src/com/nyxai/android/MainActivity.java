package com.nyxai.android;

import android.app.Activity;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int REQUEST_VOICE = 1001;
    private EditText messageInput;
    private TextView conversation;
    private TextToSpeech speech;

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
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (message.length() == 0) {
            return;
        }

        appendMessage("Você", message);
        messageInput.setText("");
        appendMessage("Nyx", "A interface nativa está pronta. A conexão com a IA será ativada na próxima etapa.");
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
        if (status == TextToSpeech.SUCCESS) {
            speech.setLanguage(new Locale("pt", "BR"));
        }
    }

    @Override
    protected void onDestroy() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
        super.onDestroy();
    }
}
