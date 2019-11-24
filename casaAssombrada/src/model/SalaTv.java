/**
 * Classe SalaTV - A sala de televisao da casa.
 *
 * Uma "SalaTV" representa a sala de televisao da casa. 
 * A sala de televisao tem uma TV que pode estar quebrada ou passando
 * um filme de terror e pode ter sangue pela sala. Os atributos sao gerados
 * aleatoriamente para simular um ambiente mal assombrado.
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class SalaTv extends Ambiente {
    private boolean tvQuebrada;
    private boolean tvPassandoFilmeTerror;
    private boolean sanguePelaSala;

    public SalaTv(String descricao) {
        super(descricao);
        tvQuebrada = new Random().nextBoolean();
        tvPassandoFilmeTerror = new Random().nextBoolean();
        sanguePelaSala = new Random().nextBoolean();
    }

    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (tvQuebrada) {
            retorno += "TV está quebrada\n";
        } else {
            retorno += "TV não está quebrada\n";
        }
        if (tvPassandoFilmeTerror) {
            retorno += "TV está passando um filme de terror\n";
        } else {
            retorno += "TV está desligada\n";
        }
        if (sanguePelaSala) {
            retorno += "Há sangue pela sala";
        } else {
            retorno += "A sala está limpa";
        }
        return retorno;
    }
    
    
}
