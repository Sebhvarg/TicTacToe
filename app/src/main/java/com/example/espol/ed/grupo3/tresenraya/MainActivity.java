package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlayer = findViewById(R.id.btnplayer);
        Button btnMultiplayer = findViewById(R.id.btnmultiplayer);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (ReproductorControlador.getInstance().isPlaying()) {
            ReproductorControlador.getInstance().stop();
        }

        // Iniciar juego de un jugador
        btnPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReproductorControlador.getInstance().init(MainActivity.this, R.raw.music);
                Intent intent = new Intent(MainActivity.this, TicTacToeActivity.class);
                startActivity(intent);
            }
        });

        // Iniciar juego multijugador
        btnMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MultiplayerTicTacToe.class);
                startActivity(intent);
            }
        });
    }
}



