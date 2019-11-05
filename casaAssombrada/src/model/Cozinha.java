package model;

import java.util.Random;

public class Cozinha extends Ambiente {
    private boolean armariosAbertos;
    private boolean utensiliosEspalhados;
    private boolean sanguePelaCozinha;
    private boolean corposEspalhados;

    public Cozinha(String descricao) {
        super(descricao);
        armariosAbertos = new Random().nextBoolean();
        utensiliosEspalhados = new Random().nextBoolean();
        sanguePelaCozinha = new Random().nextBoolean();
        corposEspalhados = new Random().nextBoolean();
    }
    
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
