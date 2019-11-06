package model;

import java.util.Random;

public class SalaJantar extends Ambiente {
    private boolean mesaCheiaCorpos;
    private boolean fantasma;
    private boolean moveisSeMovendo;

    public SalaJantar(String descricao) {
        super(descricao);
        mesaCheiaCorpos = new Random().nextBoolean();
        fantasma = new Random().nextBoolean();
        moveisSeMovendo = new Random().nextBoolean();
    }
    
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
