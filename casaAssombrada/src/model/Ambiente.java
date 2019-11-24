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

import java.util.HashMap;

public abstract class Ambiente {
    private String descricao;
    private String dica;
    private boolean dicaAcessada;
    private boolean chaveMestra;
    private boolean tesouro;
    private HashMap<String, Porta> saidas;

    /**
     * Cria um ambiente com a "descricao" passada. Inicialmente, ele
     * nao tem saidas. "descricao" eh algo como "uma cozinha" ou
     * "um jardim aberto".
     * @param descricao A descricao do ambiente.
     */
    public Ambiente(String descricao) {
        dica = "";
        dicaAcessada = false;
        chaveMestra = false;
        tesouro = false;
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
     * @param saida localizacao da porta, ex: sul, leste, oeste, etc..
     * @return destino da porta, ou null em caso de não existir.
     */
    public Ambiente getAmbiente(String saida) {
        try {
            return saidas.get(saida).getDestino();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return As saidas do ambiente
     */
    public String getSaidas() {
        String retorno = "";
        
        for (String saida : saidas.keySet()) {
            retorno += saida + "\t";
        }

        return retorno;
    }

    /**
     * Retorna a saida do ambiente
     * @param nome
     * @return Porta
     */
    public Porta getSaida(String nome) {
        try {
            System.out.println(saidas);
            return saidas.get(nome);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param nome da porta
     * @return O estado da saida
     */
    public boolean getEstado(String nome) {
        Porta porta = getSaida(nome);
        if (porta != null) {
            System.out.println("oi");
            porta.setEstado();
            return porta.getEstado();
        } else {
            return false;
        }
    }

    /**
     * Seta se tem dica no ambiente
     * @param dica 
     */
    public void setDica(String dica) {
        this.dica = dica;
    }

    /**
     * Seta se existe chave mestra no ambiente
     */
    public void setChaveMestra() {
        this.chaveMestra = true;
    }

    /**
     * Seta se existe o tesouro no ambiente
     */
    public void setTesouro() {
        this.tesouro = true;
    }

    /**
     * Retorna dica
     * @return String dica
     */
    public String getDica() {
        return dica;
    }

    /**
     * Retorna se tem chave mestra no ambiente
     * @return boolean
     */
    public boolean isChaveMestra() {
        return chaveMestra;
    }
    
    /**
     * Retorna se tem tesouro no ambiente
     * @return boolean tesouro no ambiente
     */
    public boolean isTesouro() {
        return tesouro;
    }

    /**
     * Seta se a dica foi acessada
     * @param dicaAcessada 
     */
    public void setDicaAcessada(boolean dicaAcessada) {
        this.dicaAcessada = dicaAcessada;
    }

    /**
     * Retorna se a dica foi acessada
     * @return boolean dica acessada
     */
    public boolean isDicaAcessada() {
        return dicaAcessada;
    }
 
}
