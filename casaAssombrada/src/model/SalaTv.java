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
