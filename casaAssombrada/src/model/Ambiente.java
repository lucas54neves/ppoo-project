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

public class Ambiente {
    private String descricao;
    private List<Porta> saidas;

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * @param descricao A descricao do ambiente.
     */
    public Ambiente(String descricao) {
        saidas = new ArrayList<Porta>();
        this.descricao = descricao;
    }

    /**
     * Define as saidas do ambiente. Cada direcao ou leva a um
     * outro ambiente ou eh null (nenhuma saida para la).
     * @param saida saida do ambiente
     * @param ambiente ambiente
     */
    public void ajustarSaidas(String saida, Ambiente ambiente) {
        saidas.add(new Porta(saida, ambiente));
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
        for (Porta porta : saidas) {
            if (porta.getNome().equals(nome)) {
                return porta.getDestino();
            }
        }
        return null;
    }

    /**
     * @return As saidas do ambiente
     */
    public String getSaidas() {
        String retorno = "";

        for (Porta saida : saidas) {
            retorno = retorno + saida.getNome() + " ";
        }

        return retorno;
    }

    public Porta getSaida(String nome) {
        for (Porta porta : saidas) {
            if (porta.getNome().equals(nome)) {
                return porta;
            }
        }
        return null;
    }

    /**
     * @return O estado da saida
     */
    public boolean getEstado(String nome) {
        Porta porta = getSaida(nome);
        if (porta != null)
            return porta.getEstado();
        return false;
    }

    /**
     * Gera aleatoriamente um novo estado para cada porta do ambiente
     */
    public void gerarAleatorioPortas() {
        for (Porta porta : saidas) {
            porta.gerarAleatorio();
        }
    }
}
