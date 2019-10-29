package model;

import java.util.Random;

public class Porta {
    private String nome;
    private boolean estado;
    private Random gerador;

    public Porta(String nome) {
        this.nome = nome;
        gerador = new Random();
        estado = false;
    }

    public String getNome() {
        return nome;
    }

    public void gerarEstado() {
        estado = gerador.nextBoolean();
    }

    public String getEstado() {
        if (estado) {
            return "funcionando corretamente";
        } else {
            return "emperrada";
        }
    }
}
