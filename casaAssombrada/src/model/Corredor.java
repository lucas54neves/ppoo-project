/**
 * Classe Corredor - um corredor da casa.
 *
 * Um "Corredor" representa o corredor da casa que liga os quartos e o banheiro. 
 * O corredor pode ter luzes piscando e fantasmas. Os atributos sao
 * gerados aleratoriamente para simular um ambiente mal assombrado.
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class Corredor extends Ambiente {
    private boolean luzesPiscando;
    private boolean fantasma;

    /**
     * Construtor do ambiente Corredor
     * @param descricao 
     */
    public Corredor(String descricao) {
        super(descricao);
        luzesPiscando = new Random().nextBoolean();
        fantasma = new Random().nextBoolean();
    }

    /**
     * Metodo para retornar as informacoes do ambiente como uma String
     * @return String informacoes do ambiente
     */
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (luzesPiscando) {
            retorno += "Luzes piscando\n";
        } else {
            retorno += "Luzes apagadas\n";
        }
        if (fantasma) {
            retorno += "Fantasma no corredor";
        } else {
            retorno += "Não tem fantasma";
        }
        return retorno;
    }
}
