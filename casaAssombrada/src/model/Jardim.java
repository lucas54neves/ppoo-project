package model;

import java.util.Random;

public class Jardim extends Ambiente {
    private boolean covasAbertas;
    private boolean esqueletosEspalhados;

    public Jardim(String descricao) {
        super(descricao);
        covasAbertas = new Random().nextBoolean();
        esqueletosEspalhados = new Random().nextBoolean();
    }
    
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (covasAbertas) {
            retorno += "Covas abertas\n";
        } else {
            retorno += "Não tem covas abertas\n";
        }
        if (esqueletosEspalhados) {
            retorno += "Esqueletos espalhados";
        } else {
            retorno += "Não tem esqueletos espalhados";
        }
        return retorno;
    }
}
