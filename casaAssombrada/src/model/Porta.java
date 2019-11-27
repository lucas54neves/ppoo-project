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

package model;

import java.util.Random;

public class Porta {
    private final String nome;
    private final Ambiente destino;
    private boolean estado;
    private int dificuldade;

    /**
     * Construtor da Porta
     * @param nome
     * @param destino 
     */
    public Porta(String nome, Ambiente destino, int dificuldade) {
        this.nome = nome;
        this.destino = destino;
        estado = false;
        this.dificuldade = dificuldade;
    }

    /**
     * Retorna o nome da porta
     * @return String nome da porta
     */
    public String getNome() {
        return nome;
    }

    /**
     * Atribui um estado para a porta de acordo com a dificuldade
     * Se a dificuldade for 'difícil', a probalibidade de a porta nao estar
     * emperrada e de 50%. Se a dificuldade for 'moderada', a probabilidade
     * e de 75%. Se for dificuldade 'fácil', a probabilidade e de 90%.
     */
    public void setEstado() {
        double estatistica = Math.random();
        if (dificuldade == 0) {
            if (estatistica <= 0.5) {
                    estado = true;
                } else {
                    estado = false;
                }
        } else if (dificuldade == 1) {
             if (estatistica <= 0.75) {
                    estado = true;
                } else {
                    estado = false;
                }
        } else if (dificuldade == 2) {
            if (estatistica <= 0.9) {
                    estado = true;
                } else {
                    estado = false;
                }
        }
    }
    
    /**
     * Retorna o estado da porta
     * @return boolean estado da porta
     */
    public boolean getEstado() {
        return estado;
    }

    /**
     * Retorna o ambiente de destino da prota
     * @return Ambiente destino
     */
    public Ambiente getDestino() {
        return destino;
    }

    /**
     * Gera um estado aletatorio para a porta
     * true nao emperrada
     */
    public void gerarAleatorio() {
        setEstado();
    }
}
