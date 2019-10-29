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
    private HashMap<Porta, Ambiente> saidas;

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * @param descricao A descricao do ambiente.
     */
    public Ambiente(String descricao) {
        saidas = new HashMap<Porta, Ambiente>();
        this.descricao = descricao;
    }

    /**
     * Define as saidas do ambiente. Cada direcao ou leva a um
     * outro ambiente ou eh null (nenhuma saida para la).
     * @param saida saida do ambiente
     * @param ambiente ambiente
     */
    public void ajustarSaidas(Porta saida, Ambiente ambiente) {
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
    public Ambiente getAmbiente(String nome) {
        for (Porta porta : saidas.keySet()) {
            if (porta.getNome().equals(nome)) {
                return saidas.get(porta);
            }
        }
        return null;
    }

    /**
     * @return As saidas do ambiente
     */
    public String getSaidas() {
        String retorno = "";

        for (Porta saida : saidas.keySet()) {
            retorno = retorno + saida.getNome() + " ";
        }

        return retorno;
    }

    /**
     * Gera um numero aleatorio entre min e max
     */
    public void gerarEstados() {
        for (Porta saida : saidas.keySet()) {
            saida.gerarEstado();
        }
    }

    // Metodo de Teste
    public String getEstados() {
        String retorno = " $- ";
        for (Porta saida : saidas.keySet()) {
            retorno = retorno + " " + saida.getEstado();
        }

        return retorno;
    }
}
