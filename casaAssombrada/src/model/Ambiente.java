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
package model;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Ambiente {
    private String descricao;
    private HashMap<String, Porta> saidas;

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * @param descricao A descricao do ambiente.
     */
    public Ambiente(String descricao) {
        saidas = new HashMap<String, Porta>();
        this.descricao = descricao;
    }

    /**
     * Define as saidas do ambiente. Cada direcao ou leva a um
     * outro ambiente ou eh null (nenhuma saida para la).
     * @param saida saida do ambiente
     * @param ambiente ambiente
     */
    public void ajustarSaidas(String saida, Ambiente ambiente) {
        Porta porta = new Porta(saida, ambiente);
        porta.gerarAleatorio();
        saidas.put(saida, porta);
    }

    /**
     * @return A descricao do ambiente.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param nome localizacao da porta, ex: sul, leste, oeste, etc..
     * @return destino da porta, ou null em caso de não existir.
     */
    public Ambiente getAmbiente(String nome) {
        return saidas.get(nome).getDestino();
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

    public Porta getSaida(String nome) {
        return saidas.get(nome);
    }

    /**
     * @param nome da porta
     * @return O estado da saida
     */
    public boolean getEstado(String nome) {
        Porta porta = getSaida(nome);
        if (porta != null) {
            return porta.getEstado();
        } else {
            return false;
        }
    }
}
