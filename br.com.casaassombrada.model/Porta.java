/**
 * Esta classe eh parte da aplicacao "A Casa Mal Assombrada".
 * "A Casa Mal Assombrada" eh um jogo de aventura muito simples, baseado em texto.
 *
 * Essa aplicacao eh o projeto final da disciplina de Praticas de Programacao
 * Orientada a Objetos do curso de Ciencia da Computacao da Univeridade Federal
 * de Lavras
 *
 * A Porta representa as saidas de cada ambiente. A porta possui um nome ("sul",
 * ou "leste", por exemplo), um destino (o ambiente para onde ela leva) e o estado
 * dela (true se ela estiver “funcionando corretamente” ou false para “emperrada”)
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.30
 */

package br.com.casaassombrada.model;

import java.util.Random;

public class Porta {
    private String nome;
    private Ambiente destino;
    private Random gerador;
    private boolean estado;

    public Porta(String nome, Ambiente destino) {
        this.nome = nome;
        this.destino = destino;
        gerador = new Random();
        estado = false;
    }

    public String getNome() {
        return nome;
    }

    public boolean getEstado() {
        return estado;
    }

    public Ambiente getDestino() {
        return destino;
    }

    public void gerarAleatorio() {
        estado = gerador.nextBoolean();
    }
}
