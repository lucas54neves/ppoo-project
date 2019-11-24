/**
 * Classe Quarto - Um quarto da casa.
 *
 * Um "Quarto" representa os quartos da casa. 
 * Os quartos possuem uma cama que pode estar com corpos ou nao, e pode ter sangue pelo quarto.
 * Os atributos sao gerados aleratoriamente para simular um ambiente mal assombrado.
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class Quarto extends Ambiente {
    private boolean corposNaCama;
    private boolean sanguePeloQuarto;

    public Quarto(String descricao) {
        super(descricao);
        corposNaCama = new Random().nextBoolean();
        sanguePeloQuarto = new Random().nextBoolean();
    }
    
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (corposNaCama) {
            retorno += "Corpos na cama\n";
        } else {
            retorno += "Não tem corpos na cama\n";
        }
        if (sanguePeloQuarto) {
            retorno += "Sangue pelo quarto";
        } else {
            retorno += "Quarto limpo";
        }
        return retorno;
    }
}
