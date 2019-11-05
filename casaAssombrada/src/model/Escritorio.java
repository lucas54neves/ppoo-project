package model;

import java.util.Random;

public class Escritorio extends Ambiente {
    private boolean sangueNosLivros;
    private boolean corpoNaMesa;

    public Escritorio(String descricao) {
        super(descricao);
        sangueNosLivros = new Random().nextBoolean();
        corpoNaMesa = new Random().nextBoolean();
    }
    
    @Override
    public String toString() {
        String retorno = super.getDescricao() + '\n';
        if (sangueNosLivros) {
            retorno += "Sangue nos livros\n";
        } else {
            retorno += "Livros limpos\n";
        }
        if (corpoNaMesa) {
            retorno += "Corpo estirado na mesa";
        } else {
            retorno += "Mesa sem corpo";
        }
        return retorno;
    }
}
