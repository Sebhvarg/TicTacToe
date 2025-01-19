package com.example.espol.ed.grupo3.tresenraya.Modelo;

public class Tablero {
    private char[][] tablero;
    public Tablero() {
        tablero = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = '\0'; // Vacío
            }
        }
    }

    public char[][] getTablero() {
        return tablero;
    }

    public void colocarFicha(int fila, int columna, char jugador) {
        tablero[fila][columna] = jugador;
    }

    public void vaciarCasilla(int fila, int columna) {
        tablero[fila][columna] = '\0';
    }

    public boolean verificarVictoria(char jugador) {
        for (int i = 0; i < 3; i++) {
            if ((tablero[i][0] == jugador && tablero[i][1] == jugador && tablero[i][2] == jugador) ||
                    (tablero[0][i] == jugador && tablero[1][i] == jugador && tablero[2][i] == jugador)) {
                return true;
            }
        }
        if ((tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) ||
                (tablero[0][2] == jugador && tablero[1][1] == jugador && tablero[2][0] == jugador)) {
            return true;
        }
        return false;
    }

    public boolean esEmpate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

}
