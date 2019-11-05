package model;

import java.util.Random;

public class SalaEstar extends Ambiente {
    private boolean sofaFurado;
    private boolean fantasma;
    private boolean moveisSeMovendo;

    public SalaEstar(String descricao) {
        super(descricao);
        sofaFurado = new Random().nextBoolean();
        fantasma = new Random().nextBoolean();
        moveisSeMovendo = new Random().nextBoolean();
    }
    
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (sofaFurado) {
            retorno += "Sofá furado\n";
        } else {
            retorno += "Sofá em perfeito estado\n";
        }
        if (fantasma) {
            retorno += "Fantasma na sala de estar\n";
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
