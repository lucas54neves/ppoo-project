package model;

/**
 * Classe Ambiente - um ambiente do jogo.
 *
 * Um "Ambiente" representa uma localizacao no cenario do jogo. Ele eh
 * conectado aos outros ambientes atraves de saidas. As saidas sao
 * nomeadas como norte, sul, leste e oeste. No ambiente Jardim, as
 * saidas sao a saida noroeste e a saida nordeste. Para cada direcao,
 * o ambiente guarda uma referencia para o ambiente vizinho, ou null
 * se nao ha saida naquela direcao.
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */

import java.util.HashMap;

public class Ambiente {
    private String descricao;
    private HashMap<String, Ambiente> saidas;

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * @param descricao A descricao do ambiente.
     */
    public Ambiente(String descricao) {
        saidas = new HashMap<String, Ambiente>();
        this.descricao = descricao;
    }

    /**
     * Define as saidas do ambiente. Cada direcao ou leva a um
     * outro ambiente ou eh null (nenhuma saida para la).
     * @param saida saida do ambiente
     * @param ambiente ambiente
     */
    public void ajustarSaidas(String saida, Ambiente ambiente) {
        saidas.put(saida, ambiente);
    }

    /**
     * @return A descricao do ambiente.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o ambiente de determinada saida
     * @param saida saida do ambiente
     */
    public Ambiente getAmbiente(String saida) {
        return saidas.get(saida);
    }

    /**
     * @return As saidas do ambiente
     */
    public String getSaidas() {
        String retorno = "";

        for (String saida : saidas.keySet()) {
            retorno = retorno + saida + " ";
        }

        return retorno;
    }
}
