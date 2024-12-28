package com.example.espol.ed.grupo3.tresenraya.Modelo;

public abstract class Jugador {
    protected String turno;

    public String getTurno(){
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
