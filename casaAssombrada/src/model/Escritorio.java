/**
 * Classe Escritorio - O escritorio da casa.
 *
 * Um "Escritorio" representa o escritorio da casa. 
 * O escritorio pode ter sangue nos videos e um corpo na mesa.
 * Os atributos sao gerados aleratoriamente para simular um ambiente mal assombrado.
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class Escritorio extends Ambiente {
    private boolean sangueNosLivros;
    private boolean corpoNaMesa;

    /**
     * Construtor do ambiente Escritorio
     * @param descricao 
     */
    public Escritorio(String descricao) {
        super(descricao);
        sangueNosLivros = new Random().nextBoolean();
        corpoNaMesa = new Random().nextBoolean();
    }
    
    /**
     * Metodo para retornar as informacoes do ambiente como uma String
     * @return String informacoes do ambiente
     */
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (sangueNosLivros) {
            retorno += "Sangue nos livros\n";
        } else {
            retorno += "Livros limpos\n";
        }
        if (corpoNaMesa) {
            retorno += "Corpo estirado na mesa";
        } else {
            retorno += "Mesa sem corpo";
        }
        return retorno;
    }
}
