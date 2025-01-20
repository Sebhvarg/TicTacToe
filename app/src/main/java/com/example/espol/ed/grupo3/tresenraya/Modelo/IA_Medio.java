package com.example.espol.ed.grupo3.tresenraya.Modelo;

public class IA_Medio {
    private Tablero tablero;
    public IA_Medio(Tablero tablero) {
        this.tablero = tablero;
    }
    public int[] calcularMovimiento() {
        int mejorValor = Integer.MIN_VALUE;
        int[] mejorMovimiento = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero.getTablero()[i][j] == '\0') {
                    tablero.colocarFicha(i, j, 'X');
                    int valorMovimiento = minimax(false);
                    tablero.vaciarCasilla(i, j);
                    if (valorMovimiento > mejorValor) {
                        mejorValor = valorMovimiento;
                        mejorMovimiento = new int[]{i, j};
                    }
                }
            }
        }
        return mejorMovimiento;
    }

    private int minimax(boolean esTurnoHumano) {
        if (verificarVictoria('X')) return 10;
        if (verificarVictoria('O')) return -10;
        if (tablero.esEmpate()) return 0;

        int mejorValor = esTurnoHumano ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero.getTablero()[i][j] == '\0') {
                    tablero.colocarFicha(i, j, esTurnoHumano ? 'O' : 'X');
                    int valorMovimiento = minimax(!esTurnoHumano);
                    tablero.vaciarCasilla(i, j);
                    if (esTurnoHumano) {
                        mejorValor = Math.min(mejorValor, valorMovimiento);
                    } else {
                        mejorValor = Math.max(mejorValor, valorMovimiento);
                    }
                }
            }
        }
        return mejorValor;
    }

    private boolean verificarVictoria(char jugador) {
        return tablero.verificarVictoria(jugador);
    }
}