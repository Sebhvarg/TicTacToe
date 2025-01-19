package com.example.espol.ed.grupo3.tresenraya.Modelo;

public class IA_Experto {
    private Tablero tablero;

    public IA_Experto(Tablero tablero) {
        this.tablero = tablero;
    }

    public int[] calcularMovimiento() {
        int mejorValor = Integer.MIN_VALUE;
        int[] mejorMovimiento = {-1, -1};

        if (tablero.getTablero()[1][1] == '\0') {
            return new int[]{1, 1};
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero.getTablero()[i][j] == '\0') {
                    tablero.colocarFicha(i, j, 'X');
                    int valorMovimiento = minimaxExperto(0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    tablero.vaciarCasilla(i, j);

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

    private int minimaxExperto(int depth, boolean esTurnoHumano, int alpha, int beta) {
        if (tablero.verificarVictoria('X')) return 10 - depth;
        if (tablero.verificarVictoria('O')) return -10 + depth;
        if (tablero.esEmpate()) return 0;

        if (esTurnoHumano) {
            int mejorValor = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero.getTablero()[i][j] == '\0') {
                        tablero.colocarFicha(i, j, 'O');
                        mejorValor = Math.min(mejorValor, minimaxExperto(depth + 1, false, alpha, beta));
                        tablero.vaciarCasilla(i, j);

                        beta = Math.min(beta, mejorValor);
                        if (alpha >= beta) {
                            return mejorValor;
                        }
                    }
                }
            }
            return mejorValor;
        } else {
            int mejorValor = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero.getTablero()[i][j] == '\0') {
                        tablero.colocarFicha(i, j, 'X');
                        mejorValor = Math.max(mejorValor, minimaxExperto(depth + 1, true, alpha, beta));
                        tablero.vaciarCasilla(i, j);

                        alpha = Math.max(alpha, mejorValor);
                        if (alpha >= beta) {
                            return mejorValor;
                        }
                    }
                }
            }
            return mejorValor;
        }
    }
}
