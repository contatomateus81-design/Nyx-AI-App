package com.nyxai.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

// Projeto novo, em branco. Edite este arquivo pra criar seu app:
// a tela é montada aqui mesmo em código (sem XML e sem HTML).
// A marca d'água "Cindra Studio" NÃO fica aqui — ela é aplicada
// automaticamente em todo app gerado (veja CindraWatermarkApp.java).
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView texto = new TextView(this);
        texto.setText("Seu projeto está pronto.\nEdite o MainActivity.java pra começar.");
        texto.setTextColor(Color.WHITE);
        texto.setTextSize(18);
        texto.setGravity(Gravity.CENTER);
        texto.setBackgroundColor(Color.parseColor("#03050a"));

        setContentView(texto);
    }
}
