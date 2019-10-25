/**
 * Classe Ambiente - um ambiente em um jogo adventure.
 *
 * Esta classe eh parte da aplicacao "World of Zuul".
 * "World of Zuul" eh um jogo de aventura muito simples, baseado em texto.
 *
 * Um "Ambiente" representa uma localizacao no cenario do jogo. Ele eh
 * conectado aos outros ambientes atraves de saidas. As saidas sao
 * nomeadas como norte, sul, leste e oeste. Para cada direcao, o ambiente
 * guarda uma referencia para o ambiente vizinho, ou null se nao ha
 * saida naquela direcao.
 *
 * @author  Michael Kölling and David J. Barnes (traduzido por Julio Cesar Alves)
 * @version 2011.07.31 (2016.02.01)
 */

import java.util.HashMap;

public class Ambiente {
    private String descricao;
    private HashMap<String, Ambiente> saidas;

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "um jardim aberto".
     * @param descricao A descricao do ambiente.
     */
    public Ambiente(String descricao) {
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
