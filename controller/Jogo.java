package controller;

import model.Ambiente;
import model.Comando;
import java.util.Random;

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
    /**
     * Cria o jogo e incializa seu mapa interno.
     */
    public Jogo() {
        gerador = new Random();
        criarAmbientes();
        analisador = new Analisador();
    }

    /**
     * Cria todos os ambientes e liga as saidas deles
     */
    private void criarAmbientes() {
        Ambiente escritorio, sala_tv, jardim, sala_jantar, cozinha;
        Ambiente quarto1, quarto2, corredor, banheiro1, quarto4;
        Ambiente quarto3, banheiro2;

        // cria os ambientes
        escritorio = new Ambiente("no escritorio da casa");
        sala_tv = new Ambiente("na sala de tv da casa");
        jardim = new Ambiente("no jardim da casa");
        sala_jantar = new Ambiente("na sala de jantar da casa");
        cozinha = new Ambiente("na cozinha da casa");
        quarto1 = new Ambiente("no primeiro quarto da casa");
        quarto2 = new Ambiente("no segundo quarto da casa");
        corredor = new Ambiente("no corredor da casa");
        banheiro1 = new Ambiente("no primeiro banheiro da casa");
        quarto4 = new Ambiente("no quarto quarto da casa");
        quarto3 = new Ambiente("no terceiro quarto da casa");
        banheiro2 = new Ambiente("no segundo banheiro da casa");

        // inicializa as saidas dos ambientes
        escritorio.ajustarSaidas("sul", sala_tv);
        sala_tv.ajustarSaidas("norte", escritorio);
        sala_tv.ajustarSaidas("oeste", sala_jantar);
        sala_tv.ajustarSaidas("sul", jardim);
        jardim.ajustarSaidas("noroeste", sala_tv);
        jardim.ajustarSaidas("nordeste", cozinha);
        sala_jantar.ajustarSaidas("oeste", sala_tv);
        sala_jantar.ajustarSaidas("sul", cozinha);
        sala_jantar.ajustarSaidas("leste", corredor);
        cozinha.ajustarSaidas("norte", sala_jantar);
        cozinha.ajustarSaidas("sul", jardim);
        quarto1.ajustarSaidas("sul", corredor);
        quarto2.ajustarSaidas("sul", corredor);
        corredor.ajustarSaidas("oeste", sala_jantar);
        corredor.ajustarSaidas("noroeste", quarto1);
        corredor.ajustarSaidas("nordeste", quarto2);
        corredor.ajustarSaidas("leste", quarto3);
        corredor.ajustarSaidas("sudeste", quarto4);
        corredor.ajustarSaidas("sudoeste", banheiro1);
        banheiro1.ajustarSaidas("norte", corredor);
        quarto4.ajustarSaidas("norte", corredor);
        quarto3.ajustarSaidas("oeste", corredor);
        quarto3.ajustarSaidas("sul", banheiro2);
        banheiro2.ajustarSaidas("norte", quarto3);

        ambienteAtual = sala_tv;  // o jogo comeca na sala de tv
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
        return gerador.nextInt(max - min) + 1) + min;
    }
}
