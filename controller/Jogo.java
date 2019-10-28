package controller;

import model.Ambiente;
import model.Comando;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import model.Porta;

/**
 *  Esta classe eh parte da aplicacao "A Casa Mal Assombrada".
 * "A Casa Mal Assombrada" eh um jogo de aventura muito simples, baseado em texto.
 *
 * Essa aplicacao eh o projeto final da disciplina de Praticas de Programacao
 * Orientada a Objetos do curso de Ciencia da Computacao da Univeridade Federal
 * de Lavras
 *
 *  Para jogar esse jogo, crie uma instancia dessa classe e chame o metodo
 *  "jogar".
 *
 *  Essa classe principal cria e inicializa todas as outras: ela cria os
 *  ambientes, cria o analisador e comeca o jogo. Ela tambeme avalia e
 *  executa os comandos que o analisador retorna.
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */

public class Jogo {
    private Analisador analisador;
    private Ambiente ambienteAtual;
    private Random gerador;
    private int quantidadeTentivas;
    private List<Ambiente> ambientes;

    /**
     * Cria o jogo e incializa seu mapa interno.
     */
    public Jogo() {
        ambientes = new ArrayList<Ambiente>();
        gerador = new Random();
        criarAmbientes();
        analisador = new Analisador();
        quantidadeTentivas = gerarAleatorio(20, 50);
    }

    /**
     * Cria todos os ambientes e liga as saidas deles
     */
    private void criarAmbientes() {
        // cria os ambientes
        ambientes.add(new Ambiente("no escritorio da casa"));
        ambientes.add(new Ambiente("na sala de tv da casa"));
        ambientes.add(new Ambiente("no jardim da casa"));
        ambientes.add(new Ambiente("na sala de jantar da casa"));
        ambientes.add(new Ambiente("na cozinha da casa"));
        ambientes.add(new Ambiente("no primeiro quarto da casa"));
        ambientes.add(new Ambiente("no segundo quarto da casa"));
        ambientes.add(new Ambiente("no corredor da casa"));
        ambientes.add(new Ambiente("no primeiro banheiro da casa"));
        ambientes.add(new Ambiente("no quarto quarto da casa"));
        ambientes.add(new Ambiente("no terceiro quarto da casa"));
        ambientes.add(new Ambiente("no segundo banheiro da casa"));

        // inicializa as saidas dos ambientes
        ambientes.get(0).ajustarSaidas(new Porta("sul"), ambientes.get(1));
        ambientes.get(1).ajustarSaidas(new Porta("norte"), ambientes.get(0));
        ambientes.get(1).ajustarSaidas(new Porta("oeste"), ambientes.get(3));
        ambientes.get(1).ajustarSaidas(new Porta("sul"), ambientes.get(2));
        ambientes.get(2).ajustarSaidas(new Porta("noroeste"), ambientes.get(1));
        ambientes.get(2).ajustarSaidas(new Porta("nordeste"), ambientes.get(4));
        ambientes.get(3).ajustarSaidas(new Porta("oeste"), ambientes.get(1));
        ambientes.get(3).ajustarSaidas(new Porta("sul"), ambientes.get(4));
        ambientes.get(3).ajustarSaidas(new Porta("leste"), ambientes.get(7));
        ambientes.get(4).ajustarSaidas(new Porta("norte"), ambientes.get(3));
        ambientes.get(4).ajustarSaidas(new Porta("sul"), ambientes.get(2));
        ambientes.get(5).ajustarSaidas(new Porta("sul"), ambientes.get(7));
        ambientes.get(6).ajustarSaidas(new Porta("sul"), ambientes.get(7));
        ambientes.get(7).ajustarSaidas(new Porta("oeste"), ambientes.get(3));
        ambientes.get(7).ajustarSaidas(new Porta("noroeste"), ambientes.get(5));
        ambientes.get(7).ajustarSaidas(new Porta("leste"), ambientes.get(10));
        ambientes.get(7).ajustarSaidas(new Porta("nordeste"), ambientes.get(6));
        ambientes.get(7).ajustarSaidas(new Porta("sudoeste"), ambientes.get(8));
        ambientes.get(7).ajustarSaidas(new Porta("sudeste"), ambientes.get(9));
        ambientes.get(9).ajustarSaidas(new Porta("norte"), ambientes.get(7));
        ambientes.get(8).ajustarSaidas(new Porta("norte"), ambientes.get(7));
        ambientes.get(10).ajustarSaidas(new Porta("oeste"), ambientes.get(7));
        ambientes.get(10).ajustarSaidas(new Porta("sul"), ambientes.get(11));
        ambientes.get(11).ajustarSaidas(new Porta("norte"), ambientes.get(10));

        ambienteAtual = ambientes.get(1);  // o jogo comeca na sala de tv
    }

    /**
     *  Rotina principal do jogo. Fica em loop ate terminar o jogo.
     */
    public void jogar() {
        imprimirBoasVindas();

        // Entra no loop de comando principal. Aqui nos repetidamente lemos
        // comandos e os executamos ate o jogo terminar.

        boolean terminado = false;
        while (! terminado) {
            Comando comando = analisador.pegarComando();
            terminado = processarComando(comando);
        }
        System.out.println("Obrigado por jogar. Ate mais!");
    }

    /**
     * Imprime a mensagem de abertura para o jogador.
     */
    private void imprimirBoasVindas() {
        System.out.println();
        System.out.println("Bem-vindo ao A Casa Mal Assombrada!");
        System.out.println("A Casa Mal Assombrada eh um novo jogo suspense.");
        System.out.println("Digite 'ajuda' se voce precisar de ajuda.");
        System.out.println();
        System.out.println("Quantidade de Tentativas: " + quantidadeTentivas);
        System.out.println();

        gerarEstados();
        estados();

        imprimir_localizacao_atual();
    }

    /**
     * Dado um comando, processa-o (ou seja, executa-o)
     * @param comando O Comando a ser processado.
     * @return true se o comando finaliza o jogo.
     */
    private boolean processarComando(Comando comando) {
        boolean querSair = false;

        if(comando.ehDesconhecido()) {
            System.out.println("Eu nao entendi o que voce disse...");
            return false;
        }

        String palavraDeComando = comando.getPalavraDeComando();
        if (palavraDeComando.equals("ajuda")) {
            imprimirAjuda();
        } else if (palavraDeComando.equals("ir")) {
            irParaAmbiente(comando);
        } else if (palavraDeComando.equals("sair")) {
            querSair = sair(comando);
        } else if (palavraDeComando.equals("observar")) {
            observar();
        }

        return querSair;
    }

    // Implementacoes dos comandos do usuario

    /**
     * Printe informacoes de ajuda.
     * Aqui nos imprimimos algo bobo e enigmatico e a lista de
     * palavras de comando
     */
    private void imprimirAjuda() {
        System.out.println("Voce esta perdido. Voce esta sozinho. Voce caminha");
        System.out.println("pela universidade.");
        System.out.println();
        System.out.println("Suas palavras de comando sao:");
        System.out.println(analisador.getComandos());
    }

    /**
     * Tenta ir em uma direcao. Se existe uma saida entra no
     * novo ambiente, caso contrario imprime mensagem de erro.
     */
    private void irParaAmbiente(Comando comando) {
        if(!comando.temSegundaPalavra()) {
            // se nao ha segunda palavra, nao sabemos pra onde ir...
            System.out.println("Ir pra onde?");
            return;
        }

        String direcao = comando.getSegundaPalavra();

        // Tenta sair do ambiente atual
        Ambiente proximoAmbiente = ambienteAtual.getAmbiente(direcao);

        if (proximoAmbiente == null) {
            System.out.println("Nao ha passagem!");
        }
        else {
            ambienteAtual = proximoAmbiente;

            imprimir_localizacao_atual();
        }
    }

    /**
     * "Sair" foi digitado. Verifica o resto do comando pra ver
     * se nos queremos realmente sair do jogo.
     * @return true, se este comando sai do jogo, false, caso contrario
     */
    private boolean sair(Comando comando) {
        if(comando.temSegundaPalavra()) {
            System.out.println("Sair o que?");
            return false;
        }
        else {
            return true;  // sinaliza que nos queremos sair
        }
    }

    /**
     * Printa a localizacao atual
     */
    public void imprimir_localizacao_atual() {
        System.out.println("Voce esta " + ambienteAtual.getDescricao());
        System.out.print("Saidas: " + ambienteAtual.getSaidas());
        System.out.println();
    }

    /**
     * "observar" foi digitado. Printa a localizacao atual
     */
    private void observar() {
        imprimir_localizacao_atual();
    }

    /**
     * Gera um numero aleatorio entre min e max
     */
    private int gerarAleatorio(int min, int max) {
        return (gerador.nextInt(max - min) + 1) + min;
    }

    /**
     * Gera aleatoriamente os estados de todas as saidas de todos ambientes
     */
    public void gerarEstados() {
        for (Ambiente ambiente : ambientes) {
            ambiente.gerarEstados();
        }
    }

    // Metodo de Teste
    public void estados() {
        for (Ambiente ambiente : ambientes) {
            System.out.println(ambiente.getDescricao() + " " + ambiente.getEstados());
        }
    }
}
