package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.espol.ed.grupo3.tresenraya.Modelo.Juego;

public class TicTacToeActivity extends AppCompatActivity {
    private Button[][] botones;
    private TextView playertext, cputext;
    private ImageView playerimg, cpuimg;
    private Juego juego;
    private HistorialManager historialManager;

    private final ActivityResultLauncher<Intent> seleccionarDificultadLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String nivel = data.getStringExtra("nivel");
                        boolean esExperto = "Experto".equals(nivel);
                        juego = new Juego(this, esExperto, playertext, cputext, playerimg, cpuimg, historialManager);
                        juego.configurarBotones(botones);
                        juego.habilitarBotones();
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                Button boton = botones[i][j];
                                int fila = i;
                                int columna = j;
                                boton.setOnClickListener(v -> {
                                    if (boton.getText().toString().isEmpty()) {
                                        boton.setText("O");
                                        boton.setTextColor(Color.parseColor("#418FBF"));
                                        juego.getTablero().getTablero()[fila][columna] = 'O';
                                        historialManager.registrarJugada("Humano", fila, columna); // Usar las variables correctas
                                        if (juego.verificarVictoria()) {
                                            juego.mostrarGanador("Humano");
                                        } else {
                                            juego.cambiarTurno();
                                            juego.turnoComputadora();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        //AQUI ES PARA EL HISTORIAL
        historialManager = HistorialManager.getInstance(this);

        ReproductorControlador.getInstance().init(this, R.raw.music);
        ReproductorControlador.getInstance().play();
        botones = new Button[3][3];
        playertext = findViewById(R.id.playertext);
        cputext = findViewById(R.id.cputext);
        playerimg = findViewById(R.id.playerimg);
        cpuimg = findViewById(R.id.cpuimg);
        botones[0][0] = findViewById(R.id.button_00);
        botones[0][1] = findViewById(R.id.button_01);
        botones[0][2] = findViewById(R.id.button_02);
        botones[1][0] = findViewById(R.id.button_10);
        botones[1][1] = findViewById(R.id.button_11);
        botones[1][2] = findViewById(R.id.button_12);
        botones[2][0] = findViewById(R.id.button_20);
        botones[2][1] = findViewById(R.id.button_21);
        botones[2][2] = findViewById(R.id.button_22);
        Intent intent = new Intent(TicTacToeActivity.this, SeleccionDificultadActivity.class);
        seleccionarDificultadLauncher.launch(intent);
        ImageButton btnExit = findViewById(R.id.btnexit);
        btnExit.setOnClickListener(v -> {
            finish();
        });
    }
}

