/**
 * Classe Banheiro - um bnnheiro da casa.
 *
 * Um "Banheiro" representa os banheiros da casa. Os banheiros podem ter
 * vaso quebrado, estar sujos ou ter sangue nos espelhos. Os atributos sao
 * gerados aleratoriamente para simular um ambiente mal assombrado.
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class Banheiro extends Ambiente {
    private boolean vasoQuebrado;
    private boolean banheiroSujo;
    private boolean sangueNosEspelhos;

    /**
     * Construtor do Banheiro
     * @param descricao 
     */
    public Banheiro(String descricao) {
        super(descricao);
        vasoQuebrado = new Random().nextBoolean();
        banheiroSujo = new Random().nextBoolean();
        sangueNosEspelhos = new Random().nextBoolean();
    }
    
    /**
     * Metodo para retornar as informacoes do ambiente como uma String
     * @return String informacoes do ambiente
     */
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (vasoQuebrado) {
            retorno += "Vaso Quebrado\n";
        } else {
            retorno += "Vaso Funcionando\n";
        }
        if (banheiroSujo) {
            retorno += "Banheiro Sujo\n";
        } else {
            retorno += "Banheiro Limpo\n";
        }
        if (sangueNosEspelhos) {
            retorno += "Sangue nos espelhos\n";
        } else {
            retorno += "Os espelhos estão limpos\n";
        }
        return retorno;
    }
}
