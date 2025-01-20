package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
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

    // Declaración del launcher para recibir el resultado de la actividad de selección de dificultad
    private final ActivityResultLauncher<Intent> seleccionarDificultadLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // Obtener el nivel de dificultad seleccionado
                        String nivel = data.getStringExtra("nivel");

                        // Inicializar el juego con el nivel seleccionado
                        boolean esExperto = "Experto".equals(nivel);
                        juego = new Juego(this, esExperto, playertext, cputext, playerimg, cpuimg);
                        juego.configurarBotones(botones);

                        // Habilitar los botones para que el jugador pueda hacer su movimiento
                        juego.habilitarBotones();

                        // Agregar un listener para manejar el turno del jugador en los botones
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                Button boton = botones[i][j];
                                int fila = i;
                                int columna = j;

                                // Configurar el evento de clic en cada botón
                                boton.setOnClickListener(v -> {
                                    if (boton.getText().toString().isEmpty()) {
                                        // Marcar la ficha del jugador
                                        boton.setText("O"); // El jugador coloca su ficha como 'O'
                                        boton.setTextColor(Color.parseColor("#418FBF"));
                                        // Actualizar el tablero con la ficha del jugador
                                        juego.getTablero().getTablero()[fila][columna] = 'O';

                                        // Verificar si ha ganado el jugador
                                        if (juego.verificarVictoria()) {
                                            juego.mostrarGanador("Jugador");
                                        } else {
                                            // Cambiar al turno de la computadora
                                            juego.cambiarTurno();
                                            juego.turnoComputadora();  // Cambio aquí: ahora usa turnoComputadora()
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

        // Inicialización de las vistas
        botones = new Button[3][3];
        playertext = findViewById(R.id.playertext);
        cputext = findViewById(R.id.cputext);
        playerimg = findViewById(R.id.playerimg);
        cpuimg = findViewById(R.id.cpuimg);

        // Asignar los botones del tablero
        botones[0][0] = findViewById(R.id.button_00);
        botones[0][1] = findViewById(R.id.button_01);
        botones[0][2] = findViewById(R.id.button_02);
        botones[1][0] = findViewById(R.id.button_10);
        botones[1][1] = findViewById(R.id.button_11);
        botones[1][2] = findViewById(R.id.button_12);
        botones[2][0] = findViewById(R.id.button_20);
        botones[2][1] = findViewById(R.id.button_21);
        botones[2][2] = findViewById(R.id.button_22);

        // Iniciar la actividad de selección de dificultad
        Intent intent = new Intent(TicTacToeActivity.this, SeleccionDificultadActivity.class);
        seleccionarDificultadLauncher.launch(intent);  // Usar el launcher para obtener el resultado
    }
}

