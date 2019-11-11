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
 *  ambientes, cria o analisador e comeca o jogo. Ela tambem avalia e
 *  executa os comandos que o analisador retorna.
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package view;

import controller.Analisador;
import model.Ambiente;
import controller.Comando;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import model.Banheiro;
import model.Corredor;
import model.Cozinha;
import model.Escritorio;
import model.Jardim;
import model.Quarto;
import model.SalaJantar;
import model.SalaTv;

public class Jogo {
    private Analisador analisador;
    private Ambiente ambienteAtual;
    private Random gerador;
    private int quantidadeTentativas;
    private List<Ambiente> ambientes;
    /**
     * Durabilidade da chave mestra
     */
    private int durabilidade;
    /**
     * Dica de onde nao esta o tesouro
     */
    private int naoEstaTesouro;
    /**
     * Dica de onde esta proximo do tesouro
     */
    private int proximoTesouro;
    /**
     * Localizacao do tesouro
     */
    private int localizacaoTesouro;


    /**
     * Cria o jogo e incializa seu mapa interno.
     */
    public Jogo() {
        ambientes = new ArrayList<Ambiente>();
        gerador = new Random();
        criarAmbientes();
        iniciarAmbientes();
        analisador = new Analisador();
        quantidadeTentativas = gerarAleatorio(20, 50);
        durabilidade = gerarAleatorio(1, 12);
        naoEstaTesouro = gerarAleatorio(0, 11);
        proximoTesouro = gerarAleatorio(0, 11);
        localizacaoTesouro = gerarAleatorio(0, 11);
    }

    /**
     * Cria todos os ambientes e liga as saidas deles
     */
    private void criarAmbientes() {
        // cria os ambientes
        ambientes.add(new Escritorio("Escritorio da casa"));
        ambientes.add(new SalaTv("Sala de tv da casa"));
        ambientes.add(new Jardim("Jardim da casa"));
        ambientes.add(new SalaJantar("Sala de jantar da casa"));
        ambientes.add(new Cozinha("Cozinha da casa"));
        ambientes.add(new Quarto("Primeiro quarto da casa"));
        ambientes.add(new Quarto("Segundo quarto da casa"));
        ambientes.add(new Corredor("Corredor da casa"));
        ambientes.add(new Banheiro("Primeiro banheiro da casa"));
        ambientes.add(new Quarto("Quarto quarto da casa"));
        ambientes.add(new Quarto("Terceiro quarto da casa"));
        ambientes.add(new Banheiro("Segundo banheiro da casa"));
        
    }

    private void iniciarAmbientes () {
               
        // inicializa as saidas dos ambientes
        ambientes.get(0).ajustarSaidas("sul", ambientes.get(1));
        ambientes.get(1).ajustarSaidas("norte", ambientes.get(0));
        ambientes.get(1).ajustarSaidas("oeste", ambientes.get(3));
        ambientes.get(1).ajustarSaidas("sul", ambientes.get(2));
        ambientes.get(2).ajustarSaidas("noroeste", ambientes.get(1));
        ambientes.get(2).ajustarSaidas("nordeste", ambientes.get(4));
        ambientes.get(3).ajustarSaidas("oeste", ambientes.get(1));
        ambientes.get(3).ajustarSaidas("sul", ambientes.get(4));
        ambientes.get(3).ajustarSaidas("leste", ambientes.get(7));
        ambientes.get(4).ajustarSaidas("norte", ambientes.get(3));
        ambientes.get(4).ajustarSaidas("sul", ambientes.get(2));
        ambientes.get(5).ajustarSaidas("sul", ambientes.get(7));
        ambientes.get(6).ajustarSaidas("sul", ambientes.get(7));
        ambientes.get(7).ajustarSaidas("oeste", ambientes.get(3));
        ambientes.get(7).ajustarSaidas("noroeste", ambientes.get(5));
        ambientes.get(7).ajustarSaidas("leste", ambientes.get(10));
        ambientes.get(7).ajustarSaidas("nordeste", ambientes.get(6));
        ambientes.get(7).ajustarSaidas("sudoeste", ambientes.get(8));
        ambientes.get(7).ajustarSaidas("sudeste", ambientes.get(9));
        ambientes.get(9).ajustarSaidas("norte", ambientes.get(7));
        ambientes.get(8).ajustarSaidas("norte", ambientes.get(7));
        ambientes.get(10).ajustarSaidas("oeste", ambientes.get(7));
        ambientes.get(10).ajustarSaidas("sul", ambientes.get(11));
        ambientes.get(11).ajustarSaidas("norte", ambientes.get(10));

        ambienteAtual = ambientes.get(1);  // o jogo comeca na sala de tv
    }

    /**
     *  Rotina principal do jogo. Fica em loop ate terminar o jogo.
     */
    public void jogar() {
        imprimirBoasVindas();
        // Entra no loop de comando principal. Aqui nos repetidamente lemos
        // comandos e os executamos ate o jogo terminar.
        int terminado = 0;
        while (terminado < 1 && quantidadeTentativas > 0) {
            Comando comando = analisador.pegarComando();
            terminado = processarComando(comando);
        }
        if(terminado == 1)
            System.out.println("Obrigado por jogar. Ate mais!");
        else if(quantidadeTentativas == 0)
            System.out.println("Game Over! Suas tentativas acabaram!");
        else if(terminado == 2)
            System.out.println("CONGRATULATIONS!!! Voce venceu!!! Voce encontrou o tesouro escondido!");
        else if(terminado == 3)
            System.out.println("GAME OVER! Voce gastou sua carga explosiva e nao encontrou o tesouro.");
    }

    /**
     * Imprime a mensagem de abertura para o jogador.
     */
    private void imprimirBoasVindas() {
        System.out.println();
        System.out.println("Bem-vindo ao A Casa Mal Assombrada!");
        System.out.println("A Casa Mal Assombrada eh um novo jogo de suspense.");
        System.out.println("Digite 'ajuda' se voce precisar de ajuda.");
        System.out.println();
        System.out.print("Onde o tesouro nao esta ");
        System.out.println(ambientes.get(naoEstaTesouro).getDescricao());
        System.out.print("Onde o tesouro esta proximo ");
        System.out.println(ambientes.get(proximoTesouro).getDescricao());

        imprimirLocalizacaoAtual();
    }

    /**
     * Dado um comando, processa-o (ou seja, executa-o)
     * @param comando O Comando a ser processado.
     * @return int que corresponde aos comandos do jogo.
     */
    private int processarComando(Comando comando) {

        if(comando.ehDesconhecido()) {
            System.out.println("Eu nao entendi o que voce disse...");
            return 0;
        }

        String palavraDeComando = comando.getPalavraDeComando();
        if (palavraDeComando.equals("ajuda")) {
            imprimirAjuda();
        } else if (palavraDeComando.equals("ir")) {
            irParaAmbiente(comando);
        } else if (palavraDeComando.equals("sair")) {
            return sair(comando);
        } else if (palavraDeComando.equals("observar")) {
            observar();
        }
        else if(palavraDeComando.equals("explodir")){
            if(ambienteAtual.getDescricao().equals(ambientes.get(localizacaoTesouro).getDescricao()))
                return 2;
            else
                return 3;
        }

        return 0;
    }

    // Implementacoes dos comandos do usuario

    /**
     * Printe informacoes de ajuda.
     * Aqui nos imprimimos algo bobo e enigmatico e a lista de
     * palavras de comando
     */
    private void imprimirAjuda() {
        System.out.println("Voce esta perdido. Voce esta sozinho. Voce caminha pela casa");
        System.out.println();
        System.out.println("Suas palavras de comando sao:");
        System.out.println(analisador.getComandos());
    }

    /**
     * Tenta ir em uma direcao. Se existe uma saida entra no
     * novo ambiente, caso contrario imprime mensagem de erro.
     */
    private void irParaAmbiente(Comando comando) {
        // gerarEstados();

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
        } else if (proximoAmbiente.getEstado(direcao)) {
            // Entra nesse if se a porta nao estiver emperrada
            ambienteAtual = proximoAmbiente;

            imprimirLocalizacaoAtual();
        } else if (!proximoAmbiente.getEstado(direcao)) {
            // Entra nesse if se a porta estiver emperrada
            System.out.println("Passagem emperrada!");
            imprimirLocalizacaoAtual();
        }

        quantidadeTentativas--;
    }

    /**
     * "Sair" foi digitado. Verifica o resto do comando pra ver
     * se nos queremos realmente sair do jogo.
     * @return true, se este comando sai do jogo, false, caso contrario
     */
    private int sair(Comando comando) {
        if(comando.temSegundaPalavra()) {
            System.out.println("Sair o que?");
            return 0;
        }
        else {
            return 1;  // sinaliza que nos queremos sair
        }
    }

    /**
     * Printa a localizacao atual
     */
    public void imprimirLocalizacaoAtual() {
        System.out.println("Quantidade de Tentativas: " + quantidadeTentativas);
        System.out.println();
        System.out.print("Voce esta ");
        System.out.println(ambienteAtual);
        System.out.print("Saidas: " + ambienteAtual.getSaidas());
        System.out.println();
    }

    /**
     * "observar" foi digitado. Printa a localizacao atual
     */
    private void observar() {
        imprimirLocalizacaoAtual();
    }

    /**
     * Gera um numero aleatorio entre min e max
     */
    private int gerarAleatorio(int min, int max) {
        return (gerador.nextInt(max - min) + 1) + min;
    }
}
