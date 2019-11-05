package model;

import java.util.Random;

public class Corredor extends Ambiente {
    private boolean luzesPiscando;
    private boolean fantasma;

    public Corredor(String descricao) {
        super(descricao);
        luzesPiscando = new Random().nextBoolean();
        fantasma = new Random().nextBoolean();
    }

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
