package com.example.espol.ed.grupo3.tresenraya.Modelo;

public class Jugador {
    protected char ficha;
    public Jugador(char ficha){
        this.ficha = ficha;
    }
    public char getFicha(){
        return ficha;
    }
    public void setTurno(char ficha) {
        this.ficha = ficha;
    }
}
