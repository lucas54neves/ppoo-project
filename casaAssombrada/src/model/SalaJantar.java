/**
 * Classe SalaJantar - A sala de jantar da casa.
 *
 * Uma "SalaJantar" representa a sala de jantar da casa. 
 * A sala de jantar tem uma mesa que pode estar cheia de corpos ou vazia,
 * pode ter um fantasma e moveis parados ou se movendo. Os atributos sao 
 * gerados aleatoriamente para simular um ambiente mal assombrado.
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class SalaJantar extends Ambiente {
    private boolean mesaCheiaCorpos;
    private boolean fantasma;
    private boolean moveisSeMovendo;

    /**
     * Construor do ambiente Sala de Jantar
     * @param descricao 
     */
    public SalaJantar(String descricao) {
        super(descricao);
        mesaCheiaCorpos = new Random().nextBoolean();
        fantasma = new Random().nextBoolean();
        moveisSeMovendo = new Random().nextBoolean();
    }
    
    /**
     * Metodo para retornar as informacoes do ambiente como uma String
     * @return String informacoes do ambiente
     */
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (mesaCheiaCorpos) {
            retorno += "Mesa cheia de corpos\n";
        } else {
            retorno += "Mesa vazia\n";
        }
        if (fantasma) {
            retorno += "Fantasma na sala de jantar\n";
        } else {
            retorno += "Não tem fantasma\n";
        }
        if (moveisSeMovendo) {
            retorno += "Móveis se movendo";
        } else {
            retorno += "Móveis parados";
        }
        return retorno;
    }
}
