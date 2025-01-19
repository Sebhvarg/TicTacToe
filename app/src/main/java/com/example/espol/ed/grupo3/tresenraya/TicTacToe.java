package com.example.espol.ed.grupo3.tresenraya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Computadora;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Humano;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Jugador;

import java.util.Objects;


public class TicTacToe extends AppCompatActivity {
    private Jugador jugadorActual;
    private Button[][] botones;
    private char[][] tablero = new char[3][3];

    private TextView cputext;

    private TextView playertext;

    private ImageView cpuimg;

    private ImageView playerimg;

    private ImageButton btnexit;
    private boolean esNivelExperto = false;


    private void iniciarJuego(GridLayout gridLayout) {
        // Reiniciar el tablero y establecer el primer turno al humano
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = i * 3 + j;
                Button boton = (Button) gridLayout.getChildAt(index);
                botones[i][j] = boton;
                configurarBotones(boton, i, j);
            }
        }

        // El humano siempre tiene el primer turno
        jugadorActual = new Humano(); // Asegúrate de que el primer turno es del humano

        // Si el turno es de la computadora y se ha seleccionado "Experto" o "Medio", la computadora juega automáticamente
        if (jugadorActual.getTurno().equals("X")) { // Si la computadora es el siguiente turno
            turnoComputadora(); // La computadora hace su primer movimiento si es su turno
        } else {
            habilitarBotones(); // Habilitar los botones para que el humano pueda jugar
        }
    }

    private void configurarBotones(Button boton, int fila, int columna) {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boton.getText().toString().isEmpty() && jugadorActual instanceof Humano) { // Asegurarse de que sea el turno del humano
                    // Realizar el movimiento
                    boton.setTextColor(Color.parseColor("#418FBF"));
                    boton.setText(jugadorActual.getTurno());

                    // Actualizar el tablero
                    tablero[fila][columna] = jugadorActual.getTurno().charAt(0);

                    // Deshabilitar botones inmediatamente después de realizar un movimiento
                    deshabilitarBotones();

                    // Verificar si hay un ganador
                    if (verificarVictoria('O')) {  // Si el jugador humano gana
                        mostrarGanador("Humano");
                        deshabilitarBotones();
                    } else if (verificarVictoria('X')) {  // Si la computadora gana
                        mostrarGanador("Computadora");
                        deshabilitarBotones();
                    } else if (esEmpate()) {  // Si es empate
                        mostrarGanador("Empate");
                        deshabilitarBotones();
                    } else {
                        // Cambiar el turno y llamar a la computadora si es necesario
                        cambiarTurno();
                        if (jugadorActual.getTurno().equals("X")) {  // Si es el turno de la computadora
                            turnoComputadora(); // La computadora realiza su movimiento
                        } else {
                            habilitarBotones();  // Habilitar botones después del turno del humano
                        }
                    }
                }
            }
        });
    }

    private void turnoComputadora() {
        if (verificarVictoria('O') || verificarVictoria('X') || esEmpate()) {
            return;  // No hacer nada si ya hay un ganador o empate
        }

        // Deshabilitar botones mientras la computadora hace su movimiento
        deshabilitarBotones();

        int[] mejorMovimiento = {-1, -1}; // Inicializamos el movimiento

        // Calcular el mejor movimiento de acuerdo con el nivel seleccionado
        if (esNivelExperto) {
            mejorMovimiento = calcularMovimientoExperto();
        } else {
            mejorMovimiento = calcularMovimientoMedio();
        }

        if (mejorMovimiento[0] != -1 && mejorMovimiento[1] != -1) {
            playertext.animate().alpha(0.2f).setDuration(500).start();
            playerimg.animate().alpha(0.2f).setDuration(500).start();
            cputext.animate().alpha(1f).setDuration(200).start();
            cpuimg.animate().alpha(1f).setDuration(200).start();

            // Hacer el movimiento de la computadora
            int[] finalMejorMovimiento = mejorMovimiento;
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                botones[finalMejorMovimiento[0]][finalMejorMovimiento[1]].setText("X");
                tablero[finalMejorMovimiento[0]][finalMejorMovimiento[1]] = 'X';

                // Cambiar la visibilidad de los elementos después de un pequeño retraso
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    cputext.animate().alpha(0.3f).setDuration(500).start();
                    cpuimg.animate().alpha(0.3f).setDuration(500).start();
                    playertext.animate().alpha(1f).setDuration(200).start();
                    playerimg.animate().alpha(1f).setDuration(200).start();
                }, 800);

                // Verificar si hay un ganador o empate después del movimiento de la computadora
                if (verificarVictoria('X')) {
                    mostrarGanador("Computadora");
                    deshabilitarBotones();
                } else if (esEmpate()) {
                    mostrarGanador("Empate");
                    deshabilitarBotones();
                } else {
                    cambiarTurno();
                    habilitarBotonesConRetraso();  // Habilitar botones después del turno de la computadora
                }
            }, 1000);
        }
    }



    private void deshabilitarBotones() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setEnabled(false);
            }
        }
    }

    // Habilitar después de 1 segundo
    private void habilitarBotonesConRetraso() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                habilitarBotones();  // Llamar a habilitar botones después del retraso
            }
        }, 1000);  // 1000 ms = 1 segundo
    }


    private void habilitarBotones() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (botones[i][j].getText().toString().isEmpty()) {
                    botones[i][j].setEnabled(true);  // Habilitar solo los botones vacíos
                }
            }
        }
    }


    private void cambiarTurno(){
        if (jugadorActual instanceof Humano){
            jugadorActual = new Computadora();
            deshabilitarBotones();  // Deshabilitar botones cuando sea el turno de la computadora
        } else{
            jugadorActual = new Humano();
            habilitarBotones();  // Habilitar botones cuando sea el turno del humano
        }

        if(Objects.equals(jugadorActual.getTurno(), "X")){
            cputext.animate().alpha(0.2f).setDuration(500).start();
            cpuimg.animate().alpha(0.2f).setDuration(500).start();
            playertext.animate().alpha(1f).setDuration(200).start();
            playerimg.animate().alpha(1f).setDuration(200).start();
        }
    }


    private boolean verificarVictoria(char jugador) {
        // Comprobar filas
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == jugador && tablero[i][1] == jugador && tablero[i][2] == jugador) {
                return true;
            }
        }

        // Comprobar columnas
        for (int i = 0; i < 3; i++) {
            if (tablero[0][i] == jugador && tablero[1][i] == jugador && tablero[2][i] == jugador) {
                Log.d("VerificarVictoria", "Victoria en columna " + i);
                return true;
            }
        }

        // Comprobar diagonales
        if (tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) {
            return true;
        }
        if (tablero[0][2] == jugador && tablero[1][1] == jugador && tablero[2][0] == jugador) {
            return true;
        }

        return false;
    }


    public int[] calcularMovimientoMedio() {

        int[] mejorMovimiento = {-1, -1};


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    tablero[i][j] = 'X';
                    if (verificarVictoria('X')) {
                        tablero[i][j] = '\0';
                        return new int[]{i, j};
                    }
                    tablero[i][j] = '\0';
                }
            }
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    tablero[i][j] = 'O';
                    if (verificarVictoria('O')) {
                        tablero[i][j] = '\0';
                        return new int[]{i, j};
                    }
                    tablero[i][j] = '\0';
                }
            }
        }

        int mejorValor = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    tablero[i][j] = 'X';
                    int valorMovimiento = minimaxMedio(false);
                    tablero[i][j] = '\0';

                    if (valorMovimiento > mejorValor) {
                        mejorValor = valorMovimiento;
                        mejorMovimiento[0] = i;
                        mejorMovimiento[1] = j;
                    }
                }
            }
        }
        return mejorMovimiento;
    }

    public int[] calcularMovimientoExperto() {
        int[] mejorMovimiento = {-1, -1};
        int mejorValor = Integer.MIN_VALUE; // Inicia con el peor valor posible para maximizar la puntuación

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') { // Solo considerar las casillas vacías
                    tablero[i][j] = 'X'; // Probar la jugada para la computadora
                    int valorMovimiento = minimaxExperto(false); // Evaluar el movimiento
                    tablero[i][j] = '\0'; // Revertir la jugada

                    if (valorMovimiento > mejorValor) {
                        mejorValor = valorMovimiento; // Actualizar el mejor valor
                        mejorMovimiento[0] = i; // Guardar la fila del mejor movimiento
                        mejorMovimiento[1] = j; // Guardar la columna del mejor movimiento
                    }
                }
            }
        }
        return mejorMovimiento;
    }

    private int minimaxExperto(boolean esTurnoHumano) {
        // Comprobar si hay un ganador o empate
        if (verificarVictoria('X')) return 10; // Computadora gana
        if (verificarVictoria('O')) return -10; // Humano gana
        if (esEmpate()) return 0; // Empate

        if (esTurnoHumano) {
            // Es el turno del humano, tratamos de minimizar la puntuación
            int mejorValor = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero[i][j] == '\0') { // Solo considerar las casillas vacías
                        tablero[i][j] = 'O'; // Probar la jugada para el humano
                        mejorValor = Math.min(mejorValor, minimaxExperto(false)); // Evaluar el movimiento
                        tablero[i][j] = '\0'; // Revertir la jugada
                    }
                }
            }
            return mejorValor;
        } else {
            // Es el turno de la computadora, tratamos de maximizar la puntuación
            int mejorValor = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero[i][j] == '\0') { // Solo considerar las casillas vacías
                        tablero[i][j] = 'X'; // Probar la jugada para la computadora
                        mejorValor = Math.max(mejorValor, minimaxExperto(true)); // Evaluar el movimiento
                        tablero[i][j] = '\0'; // Revertir la jugada
                    }
                }
            }
            return mejorValor;
        }
    }

    private int minimaxMedio(boolean esTurnoHumano) {
        if (verificarVictoria('X')) return 10;
        if (verificarVictoria('O')) return -10;
        if (esEmpate()) return 0;

        if (esTurnoHumano) {
            int mejorValor = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero[i][j] == '\0') {
                        tablero[i][j] = 'O';
                        mejorValor = Math.min(mejorValor, minimaxMedio(false));
                        tablero[i][j] = '\0';
                    }
                }
            }
            return mejorValor;
        } else {
            int mejorValor = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero[i][j] == '\0') {
                        tablero[i][j] = 'X';
                        mejorValor = Math.max(mejorValor, minimaxMedio(true));
                        tablero[i][j] = '\0';
                    }
                }
            }
            return mejorValor;
        }
    }


    private boolean esEmpate() {
        // Verificamos si hay alguna casilla vacía
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    return false;  // Si hay alguna casilla vacía, no es empate
                }
            }
        }
        return true;  // Si no hay casillas vacías, es empate
    }


    private void mostrarGanador(String ganador) {
        // Aplicar la animación de transición
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        // Usar Handler para retrasar la llamada a startActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Animar al ganador o los elementos de la interfaz
            if (ganador.equals("Humano")) {
                playertext.animate().alpha(1f).setDuration(500).start();
                playerimg.animate().alpha(1f).setDuration(500).start();
            } else if (ganador.equals("Computadora")) {
                cputext.animate().alpha(1f).setDuration(500).start();
                cpuimg.animate().alpha(1f).setDuration(500).start();
            }

            // Llamar a la siguiente actividad y pasar el ganador
            Intent intent = new Intent(this, GanadorActivity.class);
            intent.putExtra("GANADOR", ganador);  // "Humano", "Computadora" o "Empate"
            startActivity(intent);
            finish();  // Para evitar que el usuario regrese a la actividad anterior
        }, 300); // Retraso de 300ms (ajustar si es necesario)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear un launcher para la actividad de selección de dificultad
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Se obtiene el nivel de dificultad del Intent devuelto
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String nivel = data.getStringExtra("nivel");

                            // Configurar el nivel de dificultad
                            if (nivel != null) {
                                esNivelExperto = nivel.equals("Experto");
                            }

                            // Configurar la actividad de TicTacToe después de recibir el nivel
                            setContentView(R.layout.activity_tic_tac_toe);

                            cputext = findViewById(R.id.cputext);
                            cpuimg = findViewById(R.id.cpuimg);
                            playertext = findViewById(R.id.playertext);
                            playerimg = findViewById(R.id.playerimg);
                            btnexit = findViewById(R.id.btnexit);
                            GridLayout gridLayout = findViewById(R.id.gridLayout);
                            botones = new Button[3][3];

                            btnexit.setOnClickListener(v -> {
                                deshabilitarBotones();
                                Intent intent = new Intent(TicTacToe.this, MainActivity.class);
                                startActivity(intent);
                            });

                            iniciarJuego(gridLayout); // Iniciar el juego con el nivel seleccionado
                        }
                    }
                }
        );

        // Redirigir a la actividad de selección de dificultad
        Intent intent = new Intent(TicTacToe.this, SeleccionDificultadActivity.class);
        activityResultLauncher.launch(intent); // Lanzar la actividad para seleccionar el nivel de dificultad
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Obtener el nivel de dificultad desde el Intent
            String nivel = data.getStringExtra("nivel");

            // Configurar el nivel de dificultad
            if (nivel != null) {
                if (nivel.equals("Experto")) {
                    esNivelExperto = true;
                } else {
                    esNivelExperto = false;
                }
            }

            // Configurar la actividad de TicTacToe después de recibir el nivel
            setContentView(R.layout.activity_tic_tac_toe);

            cputext = findViewById(R.id.cputext);
            cpuimg = findViewById(R.id.cpuimg);
            playertext = findViewById(R.id.playertext);
            playerimg = findViewById(R.id.playerimg);
            btnexit = findViewById(R.id.btnexit);
            GridLayout gridLayout = findViewById(R.id.gridLayout);
            botones = new Button[3][3];

            btnexit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deshabilitarBotones();
                    Intent intent = new Intent(TicTacToe.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            iniciarJuego(gridLayout); // Iniciar el juego con el nivel seleccionado
        }
    }


}


