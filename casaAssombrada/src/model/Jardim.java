/**
 * Classe Jardim - O jardim da casa.
 *
 * Um "Jardim" representa o jardim da casa. 
 * O jardim pode ter covas abertas e esqueletos espalhados.
 * Os atributos sao gerados aleratoriamente para simular um ambiente mal assombrado.
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class Jardim extends Ambiente {
    private boolean covasAbertas;
    private boolean esqueletosEspalhados;

    /**
     * Construtor do ambiente Jardim
     * @param descricao 
     */
    public Jardim(String descricao) {
        super(descricao);
        covasAbertas = new Random().nextBoolean();
        esqueletosEspalhados = new Random().nextBoolean();
    }
    
    /**
     * Metodo para retornar as informacoes do ambiente como uma String
     * @return String informacoes do ambiente
     */
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (covasAbertas) {
            retorno += "Covas abertas\n";
        } else {
            retorno += "Não tem covas abertas\n";
        }
        if (esqueletosEspalhados) {
            retorno += "Esqueletos espalhados";
        } else {
            retorno += "Não tem esqueletos espalhados";
        }
        return retorno;
    }
}
