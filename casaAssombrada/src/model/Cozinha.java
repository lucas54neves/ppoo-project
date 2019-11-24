/**
 * Classe Cozinha - A cozinha da casa.
 *
 * Uma "Cozinha" representa a cozinha da casa. 
 * A cozinha pode ter armarios abertos ou fechados, utensilios espalhados ou nos armarios,
 * sangue e corpos espalhados. Os atributos sao gerados aleratoriamente 
 * para simular um ambiente mal assombrado.
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package model;

import java.util.Random;

public class Cozinha extends Ambiente {
    private boolean armariosAbertos;
    private boolean utensiliosEspalhados;
    private boolean sanguePelaCozinha;
    private boolean corposEspalhados;

    /**
     * Construtor do ambiente Cozinha
     * @param descricao 
     */
    public Cozinha(String descricao) {
        super(descricao);
        armariosAbertos = new Random().nextBoolean();
        utensiliosEspalhados = new Random().nextBoolean();
        sanguePelaCozinha = new Random().nextBoolean();
        corposEspalhados = new Random().nextBoolean();
    }
    
    /**
     * Metodo para retornar as informacoes do ambiente como uma String
     * @return String informacoes do ambiente
     */
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (armariosAbertos) {
            retorno += "Armários abertos\n";
        } else {
            retorno += "Armários fechados\n";
        }
        if (utensiliosEspalhados) {
            retorno += "Utensílios espalhados pela cozinha\n";
        } else {
            retorno += "Utensílios nos armários\n";
        }
        if (sanguePelaCozinha) {
            retorno += "Sangue pela cozinha\n";
        } else {
            retorno += "Cozinha limpa\n";
        }
        if (corposEspalhados) {
            retorno += "Corpos espalhados";
        } else {
            retorno += "Sem corpos na cozinha";
        }
        return retorno;
    }
}
