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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


import controller.Analisador;
import model.Ambiente;
import controller.Comando;
import controller.PalavrasComando;
import controller.SistemaDeArquivoTxt;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
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
    
    private final String[] direcoesSaidas = {"Escritorio", "Sala de Tv", "Jardim", "Sala de Jantar",
        "Cozinha", "Quarto 1", "Quarto 2", "Corredor", "Banheiro 1", "Quarto 4", "Quarto 3", "Banheiro 2" };
    /**
     * Durabilidade da chave mestra
     */
    private int durabilidade;
    
    private int localizacaoChaveMestra;
    
    private boolean temChaveMestra;
    /**
     * Dica de onde nao esta o tesouro
     */
    private int[] naoEstaTesouro;
    /**
     * Dica de onde esta proximo do tesouro
     */
    private int proximoTesouro;
    /**
     * Localizacao do tesouro
     */
    private int localizacaoTesouro;
    
    //Atributos de interface
    private JFrame fjanela;
    private JLabel lrotuloMap;
    
    private JPanel ppainelInferior;
    private JPanel pcampoDeDigitacao;
    private JTextField tCampoDigitacao;

    private JPanel ppainelDir;
    private JLabel lquantidadeTentativas;
    private JLabel ldurabilidade;

    private JPanel ppainelEsq;
    private JPanel pstatusNormais;
    
    private static Jogo instance;

    public synchronized static Jogo getInstance() { //Forma para instanciar a classe sem alocar objetos
        if (instance == null)
            instance = new Jogo();
        return instance;
    }
    
    /**
     * Cria o jogo e incializa seu mapa interno.
     */
    private Jogo() {
        ambientes = new ArrayList<Ambiente>();
        gerador = new Random();
        criarAmbientes();
        iniciarAmbientes();
        analisador = new Analisador();
        temChaveMestra = false;
        quantidadeTentativas = gerarAleatorio(20, 50, null, null, null, null);
        durabilidade = gerarAleatorio(1, 12, null, null, null, null);
        localizacaoTesouro = gerarAleatorio(0, 11, null, null, null, null);
        //localizacaoChaveMestra = gerarAleatorio(0, 11, null, null, null, null);
        localizacaoChaveMestra = 1; //Teste
        proximoTesouro = gerarOndeTesouroEstaProx(localizacaoTesouro);
        geradorNaoEstaTesouro(localizacaoTesouro, proximoTesouro);  
        configurarAmbientes();
        salvarDados();
        
        //Intanciamento de atributos de interface
        fjanela = new JFrame("Casa Assombrada 2"); //Texto que fica na Header
        fjanela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lrotuloMap = new JLabel(new ImageIcon("src/view/static_map.png"), SwingConstants.CENTER);
        tCampoDigitacao = new JTextField();
        tCampoDigitacao.addKeyListener(new OnEnter());
        
        ppainelInferior = new JPanel();
        pstatusNormais = new JPanel();
        
        montarJanelaBorderLayout();
    }

    /**
     * Cria todos os ambientes e liga as saidas deles
     */
    private void criarAmbientes() {
        // cria os ambientes
        /* 0 - Escritorio
           1 - Sala TV
           2 - Sala Jantar
           3 - Cozinha
           4 - Quarto 1
           6 - Quarto 2
           7 - Corredor
           8 - Banheiro 1
           9 - Quarto 4
           10 - Quarto 5
           11 - Banheiro 2
        */
        
        ambientes.add(new Escritorio("Escritorio da casa"));
        ambientes.add(new SalaTv("Sala de tv da casa"));
        ambientes.add(new Jardim("Jardim da casa"));
        ambientes.add(new SalaJantar("Sala de jantar da casa"));
        ambientes.add(new Cozinha("Cozinha da casa"));
        ambientes.add(new Quarto("Quarto 1 da casa"));
        ambientes.add(new Quarto("Quarto 2 da casa"));
        ambientes.add(new Corredor("Corredor da casa"));
        ambientes.add(new Banheiro("Primeiro banheiro da casa"));
        ambientes.add(new Quarto("Quarto 4 da casa"));
        ambientes.add(new Quarto("Quarto 3 da casa"));
        ambientes.add(new Banheiro("Segundo banheiro da casa"));
        
    }

    private void iniciarAmbientes () {
        // inicializa as saidas dos ambientes
        ajustarAmbientesDoJogo();
        ambienteAtual = ambientes.get(1);  // o jogo comeca na sala de tv
    }

    /**
     *  Rotina principal do jogo. Fica em loop ate terminar o jogo.
     */
    public void jogar() {
        exibir();
        
    }

    /**
     * Imprime a mensagem de abertura para o jogador.
     */
//    private void imprimirBoasVindas() {
//        System.out.println();
//        System.out.println("Bem-vindo ao A Casa Mal Assombrada!");
//        System.out.println("A Casa Mal Assombrada eh um novo jogo de suspense.");
//        System.out.println("Digite 'ajuda' se voce precisar de ajuda.");
//        System.out.println();
//
//        imprimirLocalizacaoAtual();
//    }
 
    /**
     * Métoodo adaptado para interface gráfica
     * @return  a mensagem de abertura para o jogador.
     */
    private String  imprimirBoasVindas() {
        return 
            "Bem-vindo ao A Casa Mal Assombrada! \n" +
            "A Casa Mal Assombrada eh um novo jogo de suspense. \n" +
            "Digite 'Ajuda' se voce precisar de ajuda. \n" +
            "Não se esqueça que os comandos começam com letra maiúscula. \n";
    }

    /**
     * Dado um comando, processa-o (ou seja, executa-o)
     * @param comando O Comando a ser processado.
     * @return int que corresponde aos comandos do jogo.
     */
    private int processarComando(Comando comando) {

        if(comando.ehDesconhecido()) {
            JOptionPane.showMessageDialog(fjanela,"Eu nao entendi o que voce disse...");
            return 0;
        }

        System.out.println("comando" + comando.getPalavraDeComando());
        
        String palavraDeComando = comando.getPalavraDeComando();
        if (palavraDeComando.equals(PalavrasComando.getComando(2))) {
            JOptionPane.showMessageDialog(fjanela, imprimirAjuda());
        } else if (palavraDeComando.equals(PalavrasComando.getComando(0)))
            irParaAmbiente(comando);
        else if (palavraDeComando.equals(PalavrasComando.getComando(1)))
            return sair(comando);
        else if (palavraDeComando.equals(PalavrasComando.getComando(3))) {
            observar();
        }
        else if(palavraDeComando.equals(PalavrasComando.getComando(4))){
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
    // private void imprimirAjuda() {
    //     System.out.println("Voce esta perdido. Voce esta sozinho. Voce caminha pela casa");
    //     System.out.println();
    //     System.out.println("Suas palavras de comando sao:");
    //     System.out.println(analisador.getComandos());
    // }
    private String imprimirAjuda() {

        return "Voce esta perdido, sozinho e pela casa \n \n" +
        "Suas palavras de comando sao: " + analisador.getComandos();
    }



    /**
     * Tenta ir em uma direcao. Se existe uma saida entra no
     * novo ambiente, caso contrario imprime mensagem de erro.
     */
    private void irParaAmbiente(Comando comando) {
        // gerarEstados();

        if(!comando.temSegundaPalavra()) {
            // se nao ha segunda palavra, nao sabemos pra onde ir...
            JOptionPane.showMessageDialog(fjanela, "Ir pra onde?");
            return;
        }

        String direcao = comando.getSegundaPalavra();

        // Tenta sair do ambiente atual
        Ambiente proximoAmbiente = ambienteAtual.getAmbiente(direcao);
        System.out.println("direcao: " + direcao);
        System.out.println("direcao: " + proximoAmbiente);
        
        
        if (temChaveMestra) {
            String escolha = JOptionPane.showInputDialog(fjanela, "Deseja usar a chave mestra ? [sim / nao]");
            if (escolha != null && escolha.equals("sim")) {
                
                if (proximoAmbiente == null) {
                    JOptionPane.showMessageDialog(fjanela, "Nao ha passagem! \n ");
                    locAtualDefault();
                } else {
                    System.out.println("AQUI !!!");
                    ambienteAtual = proximoAmbiente;
                    setVerificaNovasDicas();
                    locAtualDefault();
                }    
                --durabilidade;
                ldurabilidade.setText(Integer.toString(durabilidade));
                if (durabilidade == 0) {
                    JOptionPane.showMessageDialog(fjanela, "Acabou a duração da chave mestra");
                    temChaveMestra = false;
                    ppainelEsq.removeAll();
                    ppainelEsq.add(pstatusNormais);
                    ppainelEsq.repaint();
                }
            } else andarComtentativasNormais(proximoAmbiente, direcao);
        } else {
            andarComtentativasNormais(proximoAmbiente, direcao);
        }
    }
    
    public void andarComtentativasNormais(Ambiente proximoAmbiente, String direcao) {
            if (proximoAmbiente == null) {
                JOptionPane.showMessageDialog(fjanela, "Nao ha passagem! \n");
                imprimirLocalizacaoAtual();
            } else if (proximoAmbiente.getEstado(direcao)) {
                // Entra nesse if se a porta nao estiver emperrada
                ambienteAtual = proximoAmbiente;
                setVerificaNovasDicas();
                locAtualDefault();
            } else {
                // Entra nesse if se a porta estiver emperrada
                JOptionPane.showMessageDialog(fjanela, "Passagem emperrada!");
                locAtualDefault();
            }

            --quantidadeTentativas;
            lquantidadeTentativas.setText(Integer.toString(quantidadeTentativas));
    }
    
    
    public void setVerificaNovasDicas() {
         if ((ambienteAtual.getDica() != "") && (ambienteAtual.isDicaAcessada() == false)) {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma dica");
            ambienteAtual.setDicaAcessada(true);
            
            setNovaDicaPpainelDir();
         }
        if (ambienteAtual.isChaveMestra() && temChaveMestra == false 
                && durabilidade > 0) {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma chave mestra");
            temChaveMestra = true;
        }
    }
    
    private void setNovaDicaPpainelDir () {
        String[] infoDirNova = ambienteAtual.getDica().split("\n");
        JLabel tempNova = new JLabel(infoDirNova[0]);
        tempNova.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));

        ppainelDir.add( tempNova );

        JLabel tempNova2 = new JLabel(infoDirNova[1]);
        tempNova2.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));
        ppainelDir.add(tempNova2);
        
        ppainelDir.add(new JLabel("\n"));
        ppainelDir.add(new JLabel("\n"));
        ppainelDir.repaint(); //Método importante para refresh no painel direito.
    }

    /**
     * "Sair" foi digitado. Verifica o resto do comando pra ver
     * se nos queremos realmente sair do jogo.
     * @return true, se este comando sai do jogo, false, caso contrario
     */
    private int sair(Comando comando) {
        if(comando.temSegundaPalavra()) {
            JOptionPane.showMessageDialog(fjanela, "Sair o que?");
            return 0;
        }
        else {
            return 1;  // sinaliza que nos queremos sair
        }
    }

    /**
     * Printa a localizacao atual
     */
//    public void imprimirLocalizacaoAtual() {
//        
//        System.out.println("Quantidade de Tentativas: " + quantidadeTentativas);
//        System.out.println();
//    
//        if (temChaveMestra) {
//            System.out.println("Duração da chave mestra: " + durabilidade);
//            System.out.println();
//        }
//        System.out.print("Voce esta ");
//        System.out.println(ambienteAtual);
//        System.out.print("Saidas: " + ambienteAtual.getSaidas());
//        System.out.println();
//        
//        
//        if (ambienteAtual.getDica() != "")
//            System.out.println(ambienteAtual.getDica());
//        if (ambienteAtual.isChaveMestra() && temChaveMestra == false 
//                && durabilidade > 0) {
//            System.out.println("Você conseguiu uma chave mestra");
//            temChaveMestra = true;
//        }
//        if (ambienteAtual.getDicaAlternativa() != "")
//            System.out.println(ambienteAtual.getDicaAlternativa());
//    }
    
    /**
    *Método adaptado para interface gráfica;
    *@return       
    */
    public String imprimirLocalizacaoAtual() {
        System.out.println("Onde você está : "  + ambienteAtual.getDescricao());
        String locAtual = "Voce esta: " + ambienteAtual.getDescricao() + "\n";
        String[] tempSplit = ambienteAtual.getSaidas().split("\t");
        String temp = "";
        for (String saida : tempSplit) {
            temp += saida + "    ";
        }
        locAtual += "Saidas: " + temp + "\n";
        
        return locAtual;
                
    }

    
    public String infoEsquerdaBar () {
        String mensagemEsq = "Número de \n" +
                "Tentativas restantes: \n" +
                quantidadeTentativas + "\n";
    
        if (temChaveMestra) {
            mensagemEsq += "Durabilidade da chave \n" 
                    + "mestra: \n"
                    + durabilidade;
        }
        
        return mensagemEsq;
    }
    
    public String infoDireitaBar () {
        String mensageDir = "Dicas encontradas: \n";
        if (ambienteAtual.getDica() != "") {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma dica");
            mensageDir += ambienteAtual.getDica() + "\n";
        }
        if (ambienteAtual.isChaveMestra() && temChaveMestra == false 
                && durabilidade > 0) {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma chave mestra");
            temChaveMestra = true;
        }

        return mensageDir;
    }
    
    /**
     * "observar" foi digitado. Printa a localizacao atual
     */
    private void observar() {
        JOptionPane.showMessageDialog(fjanela, ambienteAtual.toString());
    }

    /**
     * Gera um numero aleatorio entre min e max
     */
    private int gerarAleatorio(int min, int max, Integer excessaoTesouro,
            Integer excessaoProx, Integer excessaNProx, Integer exNProx2) {
//        System.out.println("Tesouro : " + excessaoTesouro + "\t" +
//                "Prox: " + excessaoProx + "\t" + 
//                        "NProx: " + excessaNProx + "\n");
        int numAle = (gerador.nextInt(max - min) + 1) + min;
        //System.out.println("numAle : " + numAle + "\t");
        if (excessaoTesouro == null)
            return numAle;
        else {
            if (excessaoProx == null) {
                //System.out.println("excessaoProx != null");
                while (excessaoTesouro == numAle) {
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                    //System.out.print("numAleTes : " + numAle + "\t");
                }
            } else if (excessaNProx == null) {
                //System.out.println("excessaoNProx != null");
                while ((excessaoTesouro == numAle) ||
                        (excessaoProx == numAle)) {
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                    //System.out.print("numAleProx : " + numAle + "\t");
                }
            } else if (exNProx2 == null){
                //System.out.println("excessaoProx != null && excessaoProx != null");
                while ((excessaNProx == numAle) || (excessaoTesouro == numAle) 
                        || (excessaoProx == numAle)) {
                    //System.out.print("numAleNProx : " + numAle + "\t");
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                }
            }  else {
//                System.out.println("excessaoProx != null && excessaoProx != null"
//                        + "&& exNProx2 != null");
                while ((excessaNProx == numAle) || (excessaoTesouro == numAle) 
                        || (excessaoProx == numAle) || (exNProx2 == numAle)) {
                    //System.out.print("numAleNProx : " + numAle + "\t");
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                }
            }
        }
//        System.out.println();
//        System.out.println("Dicas: " + ambientes.get(numAle).getDescricao());
        return numAle;
    }
    
    private int gerarAleatorioOndeEstaProx(int[] saidas) {
        boolean acertou = false;
        int numAle = (gerador.nextInt(11) + 1);
       do {
            for (int num : saidas) {
                if (numAle == num && !acertou) { 
                    acertou = true;
                }
            }
            if (!acertou)
                numAle = (gerador.nextInt(11 - 0) + 1) + 0;
        } while (!acertou); // Enquanto não sair um número entre as saídas .
        return numAle;
    }
    
    public void ajustarAmbientesDoJogo () {
        //Ambiente <- DirecaoAmb(Key) & Amb(valor)
        
        setSaidaDoAmbiente(0,1,1); // Escritorio <- 1 e salaTv
        
        setSaidaDoAmbiente(1,0,0); //Sala de TV <- 0 e Escritorio
        setSaidaDoAmbiente(1,3,3); //Sala de TV <- 3 e Sala de Jantar
        setSaidaDoAmbiente(1,2,2); //Sala de TV <- 2 e Jardim
        
        setSaidaDoAmbiente(2,1,1); //Jardim <- 1 e salaTV
        setSaidaDoAmbiente(2,4,4); //Jardim <- 4 cozinha
        
        setSaidaDoAmbiente(3,1,1); //Sala de Jantar <- 1 e salaTv
        setSaidaDoAmbiente(3,4,4); //Sala de Jantar <- 4 e cozinha
        setSaidaDoAmbiente(3,7,7); //Sala de Jantar <- 7 e corredor
        
        setSaidaDoAmbiente(4,3,3); //Cozinha <- 3 e sala de Jantar
        setSaidaDoAmbiente(4,2,2); //Cozinha <- 2 e jardim
        
        setSaidaDoAmbiente(5,7,7); //Quarto 1 <- 7 e Corredor
        
        setSaidaDoAmbiente(6,7,7); //Quarto 2 <- 7 e Corredor
        
        setSaidaDoAmbiente(7,3,3); //Corredor <- 3 e sala de jantar
        setSaidaDoAmbiente(7,5,5); //Corredor <- 1 e Quarto 1 
        setSaidaDoAmbiente(7,6,6); //Corredor <- 6 e Quarto 2
        setSaidaDoAmbiente(7,10,10); //Corredor <- 10 e Quarto 3
        setSaidaDoAmbiente(7,9,9); //Corredor <- 9 e Quarto 4
        setSaidaDoAmbiente(7,8,8); //Corredor <- 8 e Banheiro 1
        
        setSaidaDoAmbiente(8,7,7); //Banheiro 1 <- 7 e Corredor
        
        setSaidaDoAmbiente(9,7,7); //Quarto 4 <- 7 e Corredor
        
        setSaidaDoAmbiente(10,7,7); //Quarto 3 <- 7 e Corredor 
        setSaidaDoAmbiente(10,11,11); //Quarto 3 <- 11 e Banheiro 2
        
        setSaidaDoAmbiente(11, 10, 10); //Banheiro 2 <- 10 e Quarto 3
        
    }
    
    public void setSaidaDoAmbiente (int posAmb, int nomeAmb, int posAmbSaida) {
        ambientes.get(posAmb).ajustarSaidas(direcoesSaidas[nomeAmb], ambientes.get(posAmbSaida));
    }
    
    public void geradorNaoEstaTesouro(int locTesouro, int locDicaProx) {
        naoEstaTesouro = new int[3];
        int ca;
        Integer nProx = null;
        Integer nProx2 = null;
        for (int i = 0; i < 3; ++i) {
            ca = gerarAleatorio(0, 11, (Integer)locTesouro, 
                    (Integer)locDicaProx, nProx, nProx2);
            nProx2 = nProx;
            nProx = ca;
            naoEstaTesouro[i] = ca;
        }
    }
    
    /**
     * Método para gerar número de uma 
     * das saidas do local que o tesouro está próximo.
     * @param locTesouro
     * @return numero do ambinte que o tesouro está próximo
     */
    public int gerarOndeTesouroEstaProx (int locTesouro) {
        String[] keySaidas = ambientes.get(locTesouro).getSaidas().split("\t");
        List<Ambiente> listAmb = new ArrayList<>();
        for (String key : keySaidas) {
            listAmb.add(ambientes.get(locTesouro).getSaida(key).getDestino());
        }
        int[] saidasNum = new int[listAmb.size()];
        int j = 0;
        for (Ambiente amb : listAmb) {
            int k = ambientes.indexOf(amb);
            saidasNum[j] = ambientes.indexOf(amb);
            ++j;
            
        }
        shuffle(saidasNum);
        int ganhador = gerarAleatorioOndeEstaProx(saidasNum);
        return ganhador;
    }
    
    public void salvarDados() {
        try {
            SistemaDeArquivoTxt.salvar(prepararDados());
        } catch (IOException e) {
            System.out.println("Falha ao salvar dados");
        }
    }
    
    public String prepararDados() {
        return "Localização do tesouro: " + ambientes.get(localizacaoTesouro).getDescricao() + "\n" +
                "Dica1 -> Tesouro não está: " + ambientes.get(naoEstaTesouro[0]).getDescricao() + "\n" +
                "Dica2 -> Tesouro não está: " + ambientes.get(naoEstaTesouro[1]).getDescricao() + "\n" +
                "Dica3 -> Tesouro não está: " + ambientes.get(naoEstaTesouro[2]).getDescricao() + "\n" +
                "Dica Alternativa -> Tesouro está próximo: " + ambientes.get(proximoTesouro).getDescricao() + "\n" +
                "Localização da chave Mestra: " +  ambientes.get(localizacaoChaveMestra).getDescricao() + "\n";
    }

    private void configurarAmbientes() {
        ambientes.get(localizacaoTesouro).setTesouro();
        String nomeAmb;
        for (int pos : naoEstaTesouro) {
            nomeAmb = ambientes.get(pos).getDescricao();
           ambientes.get(pos).setDica("O tesouro não está no(a) \n" + nomeAmb);
        }
        nomeAmb = ambientes.get(proximoTesouro).getDescricao();
        ambientes.get(proximoTesouro).setDica("O tesouro está próximo ao(à)\n" + nomeAmb);
        ambientes.get(localizacaoChaveMestra).setChaveMestra();
    }
    
    /**
     * Método para embalharar valores de array
     * @param array - vetor de int[]
     */
    static void shuffle(int array[]) {
        int index;
 
        for (int i= array.length; i>1; i--){
            index = (int) Math.abs( 50 % i );
            //swap
            int tmp = array[i-1];
            array[i-1] = array[index];
            array[index] = tmp;
        }

    }
    
    
    //////////Métodos de Interface
    
    //Montagem da interface e inicando valores na interface
    private void montarJanelaBorderLayout() {
        fjanela.setSize(800, 400);
        fjanela.setLayout(new BorderLayout());
        
        
        //////Painel Direito
        ppainelDir = new JPanel();
        ppainelDir.setBackground(Color.WHITE);
        
        String[] infoDir = infoDireitaBar().split("\n");
        JLabel frase1 = new JLabel(infoDir[0]);
        ppainelDir.add(frase1);
        frase1.setFont(new Font("Arial", Font.BOLD, 20));
        ppainelDir.add( new JLabel("\n"));
        ppainelDir.add( new JLabel("\n"));
        for (int i = 1; i < infoDir.length; ++i) {
            frase1 = new JLabel(infoDir[i]);
            frase1.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));
            ppainelDir.add(frase1);
            if ( (i%2) == 0 )
                ppainelDir.add( new JLabel("\n"));
        }
       
        ppainelDir.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));//Adiciona Borda Preta
        ppainelDir.setSize(100, 50);
        ppainelDir.setLayout(new BoxLayout(ppainelDir, BoxLayout.Y_AXIS));
        
        JScrollPane scrollbar = new JScrollPane(ppainelDir);
        scrollbar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        fjanela.add(scrollbar, BorderLayout.EAST);
        
        //////Painel Esquerdo
        ppainelEsq = new JPanel();
        
        String[] infoEsq = infoEsquerdaBar().split("\n");
        JLabel configTemp = new JLabel(infoEsq[0]);
        configTemp.setFont(new Font("Arial", Font.BOLD, 20));
        pstatusNormais.add(configTemp); // "Número de"
        
        configTemp = new JLabel(infoEsq[1]);
        configTemp.setFont(new Font("Arial", Font.BOLD, 20));
        pstatusNormais.add(configTemp); // "Tentativas Restantes"
        pstatusNormais.add( new JLabel("\n"));
        
        lquantidadeTentativas = new JLabel(infoEsq[2]); //Definido as tentativas
        lquantidadeTentativas.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));
        pstatusNormais.add(lquantidadeTentativas);
        pstatusNormais.add( new JLabel("\n"));
        pstatusNormais.add( new JLabel("\n"));
        
        pstatusNormais.setBackground(Color.WHITE);
        pstatusNormais.setLayout(new BoxLayout(pstatusNormais, BoxLayout.Y_AXIS) );
        
        ppainelEsq.add(pstatusNormais);
        
        if (infoEsq.length > 3) {
            configTemp =  new JLabel(infoEsq[3]);
            configTemp.setFont(new Font("Arial", Font.BOLD, 20));
            ppainelEsq.add(configTemp); //Durabilidade da chave
        
            configTemp =  new JLabel(infoEsq[4]);
            configTemp.setFont(new Font("Arial", Font.BOLD, 20));
            ppainelEsq.add(configTemp); // mestra
            
            ldurabilidade = new JLabel(infoEsq[5]);
            ldurabilidade.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));
            ppainelEsq.add(ldurabilidade);//display durabilidade

        }
        
        ppainelEsq.setBackground(Color.WHITE);
        ppainelEsq.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));//Adiciona Borda Preta
        ppainelEsq.setSize(new Dimension(200, 50));
        ppainelEsq.setLayout(new BoxLayout(ppainelEsq, BoxLayout.Y_AXIS));
        fjanela.add(ppainelEsq, BorderLayout.WEST);
        
        
        ////////Painel Inferior
        ppainelInferior.setBackground(Color.WHITE);
        
        //Separa em mensagens separa para serem usadas como Labels separadas
        String[] boasVindas = imprimirBoasVindas().split("\n");
        
        ppainelInferior.add( new JLabel("\n"));
        for (String fraseBoasVindas : boasVindas) {
            ppainelInferior.add( new JLabel(fraseBoasVindas));
        }
        ppainelInferior.add( new JLabel("\n"));
        
        String[] locAtual = imprimirLocalizacaoAtual().split("\n");
        for (String fraselocAtual : locAtual) {
            ppainelInferior.add( new JLabel(fraselocAtual));
        }
        ppainelInferior.add( new JLabel("\n"));
        
        //Construindo campo de digitação
        pcampoDeDigitacao = new JPanel();
        pcampoDeDigitacao.add(new JLabel(">"));
        pcampoDeDigitacao.add(tCampoDigitacao);
        pcampoDeDigitacao.setLayout(new BoxLayout(pcampoDeDigitacao, BoxLayout.X_AXIS));
        
        ppainelInferior.add(pcampoDeDigitacao);
        ppainelInferior.setSize(200, 100);
        ppainelInferior.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));//Adiciona Borda Preta
        ppainelInferior.setLayout(new BoxLayout(ppainelInferior, BoxLayout.Y_AXIS));
        fjanela.add(ppainelInferior, BorderLayout.SOUTH);
        
        //////Painel Centro
        JPanel painelCentro = new JPanel();
        painelCentro.setBackground(Color.WHITE);
        painelCentro.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));//Adiciona Borda Preta
        painelCentro.setSize(100, 50);
        painelCentro.setLayout(new GridLayout());
        painelCentro.add(lrotuloMap);
        fjanela.add(painelCentro,( BorderLayout.CENTER));
        
        fjanela.pack();
        
    }
    
    public void locAtualDefault () {
        String[] locAtual = imprimirLocalizacaoAtual().split("\n");
//        JPanel campo = (JPanel) ppainelInferior.getComponent(8);
        ppainelInferior.removeAll();
        ppainelInferior.add(new Label("\n"));
        for (String fraselocAtual : locAtual) {
            ppainelInferior.add(new JLabel(fraselocAtual));
        }
        ppainelInferior.add( new JLabel("\n"));
        ppainelInferior.add(pcampoDeDigitacao);
        ppainelInferior.add( new JLabel("\n"));
        ppainelInferior.repaint();
    }
    
    public void exibir () {
        fjanela.setVisible(true);
    }
    
    private class OnEnter implements KeyListener {
        
        public OnEnter() {}
        
        @Override
        public void keyTyped(KeyEvent e) {
             /* Implemente aqui o analise se a primeira palavra é
                minúscula substituir por maiúscula para evitar erros.
                */
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                
                String fraseDigitada = tCampoDigitacao.getText();
  
                int terminado = 0;
                Comando comando;
                
                System.out.println(fraseDigitada);
                //while (terminado < 1 && quantidadeTentativas > 0) {
                comando = analisador.pegarComando(fraseDigitada);
                
                //Limpando o StringBuilder
                tCampoDigitacao.setText("");
                
                terminado = processarComando(comando);
                //}
                if(terminado == 1) {
                    JOptionPane.showMessageDialog(fjanela,"Obrigado por jogar. Ate mais!");
                    fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
                    
                }else if(quantidadeTentativas == 0) {
                    JOptionPane.showMessageDialog(fjanela,"Game Over! Suas tentativas acabaram!");
                    fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
                }else if(terminado == 2) {
                    JOptionPane.showMessageDialog(fjanela,"CONGRATULATIONS!!! Voce venceu!!! Voce encontrou o tesouro escondido!");
                    fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
                }else if(terminado == 3) {
                    JOptionPane.showMessageDialog(fjanela,"GAME OVER! Voce gastou sua carga explosiva e nao encontrou o tesouro.");
                    fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
                }
            
                
            }
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_TAB) {
                System.out.println("AQUI" + tCampoDigitacao.getText());
                
                /* Implemente aqui o autocomplete de palavra a partir quando 
                    apertar TAB.
                */
            }
        }
       
    }
}
