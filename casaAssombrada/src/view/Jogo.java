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
import controller.SistemaDeArquivoTxt;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.JOptionPane;

import javax.swing.UIManager;
import model.Banheiro;
import model.Corredor;
import model.Cozinha;
import model.Escritorio;
import model.Jardim;
import model.Quarto;
import model.SalaJantar;
import model.SalaTv;

public class Jogo {
   
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
    private int dificuldade;
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
    private JPanel pstatusChaveMestra; // Status Chave Mestra do ppainelEsq
    
    private Clip clip;
    
    private static Jogo instance;

    /**
     * Padrão de projeto Sigleton: instanciar Jogo apenas uma vez 
     * que é necessário
     */
    public synchronized static Jogo getInstance() { //Forma para instanciar a classe sem alocar objetos
        if (instance == null)
            instance = new Jogo();
        return instance;
    }
    
    /**
     * Instância todos os atributos do Jogo 
     * incializa seu mapa interno .
     */
    private Jogo() {
        ambientes = new ArrayList<>();
        gerador = new Random();
        criarAmbientes();
        
        iniciarAmbientes(dificuldade);
        temChaveMestra = false;
        quantidadeTentativas = gerarAleatorio(20, 50, null, null, null, null);
        durabilidade = gerarAleatorio(1, 12, null, null, null, null);
        localizacaoTesouro = gerarAleatorio(0, 11, null, null, null, null);
        localizacaoChaveMestra = gerarAleatorio(0, 11, null, null, null, null);
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
        
        ppainelEsq = new JPanel();
        ppainelDir = new JPanel();
        ppainelInferior = new JPanel();
        pstatusNormais = new JPanel();
        pstatusChaveMestra = new JPanel();
        
        setAudio("normal.wav");
        
        montarJanelaBorderLayout();
        clip.start();
        gerarDificuldade();
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

    /**
     * @param dificuldade 
     */
    private void iniciarAmbientes(int dificuldade) {
        // inicializa as saidas dos ambientes
        ajustarAmbientesDoJogo(dificuldade);
        ambienteAtual = ambientes.get(1);  // o jogo comeca na sala de tv
    }
    
    /** Ajusta os ambientes do jogo
     *  @param dificuldade 
     */
    private void ajustarAmbientesDoJogo (int dificuldade) {
        setSaidaDoAmbiente(0,1,1,dificuldade); // Escritorio <- 1 e salaTv
        
        setSaidaDoAmbiente(1,0,0,dificuldade); //Sala de TV <- 0 e Escritorio
        setSaidaDoAmbiente(1,3,3,dificuldade); //Sala de TV <- 3 e Sala de Jantar
        setSaidaDoAmbiente(1,2,2,dificuldade); //Sala de TV <- 2 e Jardim
        
        setSaidaDoAmbiente(2,1,1,dificuldade); //Jardim <- 1 e salaTV
        setSaidaDoAmbiente(2,4,4,dificuldade); //Jardim <- 4 cozinha
        
        setSaidaDoAmbiente(3,1,1,dificuldade); //Sala de Jantar <- 1 e salaTv
        setSaidaDoAmbiente(3,4,4,dificuldade); //Sala de Jantar <- 4 e cozinha
        setSaidaDoAmbiente(3,7,7,dificuldade); //Sala de Jantar <- 7 e corredor
        
        setSaidaDoAmbiente(4,3,3,dificuldade); //Cozinha <- 3 e sala de Jantar
        setSaidaDoAmbiente(4,2,2,dificuldade); //Cozinha <- 2 e jardim
        
        setSaidaDoAmbiente(5,7,7,dificuldade); //Quarto 1 <- 7 e Corredor
        
        setSaidaDoAmbiente(6,7,7,dificuldade); //Quarto 2 <- 7 e Corredor
        
        setSaidaDoAmbiente(7,3,3,dificuldade); //Corredor <- 3 e sala de jantar
        setSaidaDoAmbiente(7,5,5,dificuldade); //Corredor <- 1 e Quarto 1 
        setSaidaDoAmbiente(7,6,6,dificuldade); //Corredor <- 6 e Quarto 2
        setSaidaDoAmbiente(7,10,10,dificuldade); //Corredor <- 10 e Quarto 3
        setSaidaDoAmbiente(7,9,9,dificuldade); //Corredor <- 9 e Quarto 4
        setSaidaDoAmbiente(7,8,8,dificuldade); //Corredor <- 8 e Banheiro 1
        
        setSaidaDoAmbiente(8,7,7,dificuldade); //Banheiro 1 <- 7 e Corredor
        
        setSaidaDoAmbiente(9,7,7,dificuldade); //Quarto 4 <- 7 e Corredor
        
        setSaidaDoAmbiente(10,7,7,dificuldade); //Quarto 3 <- 7 e Corredor 
        setSaidaDoAmbiente(10,11,11,dificuldade); //Quarto 3 <- 11 e Banheiro 2
        
        setSaidaDoAmbiente(11,10,10,dificuldade); //Banheiro 2 <- 10 e Quarto 3
    }
    
    
        /**
     * Gera onde nao esta o tesouro
     * @param locTesouro
     * @param locDicaProx 
     */
    private void geradorNaoEstaTesouro(int locTesouro, int locDicaProx) {
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
     * Configura os ambientes
     */
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
     * Chama o metodo que salva os dados em um arquivo texto
     */
    private void salvarDados() {
        try {
            SistemaDeArquivoTxt.salvar(prepararDados());
        } catch (IOException e) {
            System.out.println("Falha ao salvar dados");
        }
    }
   
    /** Põem audio no objeto de que inicia música do jogo.
     * @param arquivo - Nome da música e a extensão (.wav)
     */
    private void setAudio(String arquivo) {
        try {
            File file = new File("src/view/" + arquivo);         
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (IOException e) {
            System.out.println("Erro na abertura do arquivo para música");
        } catch (LineUnavailableException | UnsupportedAudioFileException ex) {
            Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Troca a música do jogo caso ganhe ou perca.
     * @param file - Nome do audio e sua extensão.
     */
    private void trocarAudio(String file) {
        if (clip.isRunning()) {
            clip.stop();
            clip.close();
        }

        setAudio(file);
        clip.setFramePosition(0);
        clip.start();
    }
    
    
    /**
     * Quando o ocorrer o evento de apertar o botão da bomba ou
     * de digitar o comando é setado o efeito de bomba .
     */
    private void colocaBombEffect() {
        trocarAudio("bomb_effect.wav");
        if (clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    
    /**Mostra um painel para ser selecionado a dificuldade do
     * jogo.
     */
    private void gerarDificuldade() {
        UIManager.put("OptionPane.cancelButtonText", "Difícil");
        UIManager.put("OptionPane.noButtonText", "Moderado");
        UIManager.put("OptionPane.yesButtonText", "Fácil");
        dificuldade = JOptionPane.showConfirmDialog(null, "Qual dificuldade desejada?");
    }

    /**
     *  Rotina principal do jogo. Fica em loop ate terminar o jogo.
     */
    public void jogar() {
        exibir();
    }
    

    /**
     * Métoodo adaptado para interface gráfica
     * @return  a mensagem de abertura para o jogador.
     */
    private String  imprimirBoasVindas() {
        return 
            "Bem-vindo ao A Casa Mal Assombrada! \n" +
            "A Casa Mal Assombrada eh um novo jogo de suspense. \n" +
            "Digite 'Ajuda' ou clique em Ajuda -> Exibir se voce precisar de ajuda. \n" +
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
        
        String palavraDeComando = comando.getPalavraDeComando();
        if (palavraDeComando.equals( Analisador.getComandoValido(2))) {
            JOptionPane.showMessageDialog(fjanela, imprimirAjuda());
        } else if (palavraDeComando.equals(Analisador.getComandoValido(0)))
            irParaAmbiente(comando);
        else if (palavraDeComando.equals(Analisador.getComandoValido(1)))
            return sair(comando);
        else if (palavraDeComando.equals(Analisador.getComandoValido(3))) {
            observar();
        }
        else if(palavraDeComando.equals(Analisador.getComandoValido(4))){
            if(ambienteAtual.getDescricao().equals(ambientes.get(localizacaoTesouro).getDescricao()))
                return 2;
            else
                return 3;
        }

        return 0;
    }

    // Implementacoes dos comandos do usuario

    /**
     * @return String informacoes de ajuda
     */
    private String imprimirAjuda() {

        return "Voce esta perdido, sozinho e pela casa \n \n" +
        "Suas palavras de comando sao: " + Analisador.getComandos();
    }


    /**
     * Tenta ir em uma direcao. Se existe uma saida entra no
     * novo ambiente, caso contrario imprime mensagem de erro.
     */
    private void irParaAmbiente(Comando comando) {
        if(!comando.temSegundaPalavra()) {
            // se nao ha segunda palavra, nao sabemos pra onde ir...
            JOptionPane.showMessageDialog(fjanela, "Ir pra onde?");
            return;
        }

        String direcao = comando.getSegundaPalavra();

        // Tenta sair do ambiente atual
        Ambiente proximoAmbiente = ambienteAtual.getAmbiente(direcao);
               
        if (temChaveMestra) {
            String escolha = JOptionPane.showInputDialog(fjanela, "Deseja usar a chave mestra ? [sim / nao]");
            if (escolha != null && escolha.equals("sim")) {
                
                if (proximoAmbiente == null) {
                    JOptionPane.showMessageDialog(fjanela, "Nao ha passagem! \n ");
                    locAtualDefault();
                } else {
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
    
    private void andarComtentativasNormais(Ambiente proximoAmbiente, String direcao) {
            
            if (proximoAmbiente == null) {
                JOptionPane.showMessageDialog(fjanela, "Nao ha passagem! \n");
                imprimirLocalizacaoAtual();
            } else if (ambienteAtual.getEstado(direcao)) {
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
    
    
    private void setVerificaNovasDicas() {
         if ((!"".equals(ambienteAtual.getDica())) && (ambienteAtual.isDicaAcessada() == false)) {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma dica");
            ambienteAtual.setDicaAcessada(true);
            
            setNovaDicaPpainelDir();
         }
        if (ambienteAtual.isChaveMestra() && temChaveMestra == false 
                && durabilidade > 0) {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma chave mestra");
            temChaveMestra = true;
            
            setPainelEsqComChaveMestra();
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
    
    private void setPainelEsqComChaveMestra () {
        
            String[] infoEsq = chaveInfo().split("\n");
            JLabel configTemp;
            
            configTemp =  new JLabel(infoEsq[0]);
            configTemp.setFont(new Font("Arial", Font.BOLD, 20));
            pstatusChaveMestra.add(configTemp); //Durabilidade da chave   

            configTemp =  new JLabel(infoEsq[1]);
            configTemp.setFont(new Font("Arial", Font.BOLD, 20));
            pstatusChaveMestra.add(configTemp); // mestra
            
            ldurabilidade = new JLabel(infoEsq[2]);
            ldurabilidade.setFont(new Font("Arial", Font.ROMAN_BASELINE, 20));
            pstatusChaveMestra.add(ldurabilidade);//display durabilidade
            
            pstatusChaveMestra.setBackground(Color.WHITE);
            pstatusChaveMestra.setLayout(new BoxLayout(pstatusChaveMestra, BoxLayout.Y_AXIS) );
          
            ppainelEsq.repaint();
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
     * Método adaptado para interface gráfica;
     * @return String localizacao atual
     */
    private String imprimirLocalizacaoAtual() {
        String locAtual = "Voce esta: " + ambienteAtual.getDescricao() + "\n";
        String[] tempSplit = ambienteAtual.getSaidas().split("\t");
        String temp = "";
        for (String saida : tempSplit) {
            temp += saida + "    ";
        }
        locAtual += "Saidas: " + temp + "\n";
        
        return locAtual;          
    }

    /**
     * @return String mensagem da area da esquerda da janela
     */
    private String infoEsquerdaBar () {
        String mensagemEsq = "Número de \n" +
                "Tentativas restantes: \n" +
                quantidadeTentativas + "\n";
        
        return mensagemEsq;
    }
    
    private String chaveInfo () {
        String mensagemEsq = "";
        if (temChaveMestra) {
            mensagemEsq += "Durabilidade da chave \n" 
                    + "mestra: \n"
                    + durabilidade;
        }
        return mensagemEsq;
    }
    
    
    /**
     * @return String mensagem da area da direita da janela
     */
    private String infoDireitaBar () {
        String mensageDir = "Dicas encontradas: \n";
        if (!ambienteAtual.getDica().equals("") 
                && !(ambienteAtual.isDicaAcessada())) {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma dica");
            ambienteAtual.setDicaAcessada(true);
            mensageDir += ambienteAtual.getDica() + "\n";
        }
        if (ambienteAtual.isChaveMestra() && temChaveMestra == false 
                && durabilidade > 0) {
            JOptionPane.showMessageDialog(fjanela, "Você conseguiu uma chave mestra");
            temChaveMestra = true;
            
            setPainelEsqComChaveMestra();
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
     * @return int numero aleatorio
     */
    private int gerarAleatorio(int min, int max, Integer excessaoTesouro,
            Integer excessaoProx, Integer excessaNProx, Integer exNProx2) {
        int numAle = (gerador.nextInt(max - min) + 1) + min;
        if (excessaoTesouro == null)
            return numAle;
        else {
            if (excessaoProx == null) {
                while (excessaoTesouro == numAle) {
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                }
            } else if (excessaNProx == null) {
                while ((excessaoTesouro == numAle) ||
                        (excessaoProx == numAle)) {
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                }
            } else if (exNProx2 == null){
                while ((excessaNProx == numAle) || (excessaoTesouro == numAle) 
                        || (excessaoProx == numAle)) {
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                }
            }  else {
                while ((excessaNProx == numAle) || (excessaoTesouro == numAle) 
                        || (excessaoProx == numAle) || (exNProx2 == numAle)) {
                    numAle = (gerador.nextInt(max - min) + 1) + min;
                }
            }
        }
        return numAle;
    }
    
    /**
     * Gera 
     * @param saidas
     * @return int numero 
     */
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
    
    
    
    /**
     * Seta a saida do ambiente
     * @param posAmb
     * @param nomeAmb
     * @param posAmbSaida 
     */
    private void setSaidaDoAmbiente (int posAmb, int nomeAmb, int posAmbSaida, int dificuldade) {
        ambientes.get(posAmb).ajustarSaidas(direcoesSaidas[nomeAmb], ambientes.get(posAmbSaida), dificuldade);
    }
    

    
    /**
     * Método para gerar número de uma 
     * das saidas do local que o tesouro está próximo.
     * @param locTesouro
     * @return numero do ambinte que o tesouro está próximo
     */
    private int gerarOndeTesouroEstaProx (int locTesouro) {
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
    

    
    /**
     * Prepara os dados para serem salvos no arquivo texto
     * @return String dados preparados
     */
    private String prepararDados() {
        return "Localização do tesouro: " + ambientes.get(localizacaoTesouro).getDescricao() + "\n" +
                "Dica1 -> Tesouro não está: " + ambientes.get(naoEstaTesouro[0]).getDescricao() + "\n" +
                "Dica2 -> Tesouro não está: " + ambientes.get(naoEstaTesouro[1]).getDescricao() + "\n" +
                "Dica3 -> Tesouro não está: " + ambientes.get(naoEstaTesouro[2]).getDescricao() + "\n" +
                "Dica Alternativa -> Tesouro está próximo: " + ambientes.get(proximoTesouro).getDescricao() + "\n" +
                "Localização da chave Mestra: " +  ambientes.get(localizacaoChaveMestra).getDescricao() + "\n";
    }

    
    /**
     * Método para embalharar valores de array
     * @param array - vetor de int[]
     */
    private void shuffle(int array[]) {
        int index;
 
        for (int i= array.length; i>1; i--){
            index = (int) Math.abs( 50 % i );
            //swap
            int tmp = array[i-1];
            array[i-1] = array[index];
            array[index] = tmp;
        }

    }
    
    
    // #### Metodos de Interface ####
    
    /**
     * Metodo para exibir a janela
     */
    private void exibir () {
        fjanela.setVisible(true);
    }
    
    /**
     * Monta a janela da interface e inicializa os valores da interface
     */
    private void montarJanelaBorderLayout() {
        fjanela.setSize(800, 400);
        fjanela.setLayout(new BorderLayout());
   
        montagemPainelEsquerdo();
 
        montagemPainelDireito();
        
        montagemPainelInferior();
        
        montarPainelCentro();
        
        montarMenuBr();
        
        fjanela.pack();
    }
    
    
    /**Seta todos os componentes necessários para visilização 
    * e interação do painel esquerdo (Onde os status das tentativas
    * e da chave mestra caso encontrada).
    */
    private void montagemPainelEsquerdo() {
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
        
        pstatusChaveMestra.setBackground(Color.WHITE);
        ppainelEsq.add(pstatusChaveMestra);
        
        ppainelEsq.setBackground(Color.WHITE);
        ppainelEsq.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));//Adiciona Borda Preta
        ppainelEsq.setSize(new Dimension(200, 50));
        ppainelEsq.setLayout(new BoxLayout(ppainelEsq, BoxLayout.Y_AXIS));
        fjanela.add(ppainelEsq, BorderLayout.WEST);
    }
    
    /**Seta todos os componentes necessários para visilização 
    * e interação do painel Direito (Onde fica as dicas).
    */
    private void montagemPainelDireito () {
         //////Painel Direito        
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
    }
    
    
    /**Seta todos os componentes necessários para visilização 
    * e interação do painel Inferior (Onde fica a indicação da localização
    * e do campo de texto).
    */
    private void montagemPainelInferior() {

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
    }
    
    /**Seta todos os componentes necessários para visilização 
    * e interação do painel Centro (Onde fica o mapa da casa e o
    * botão de usar o detonador).
    */
    private void montarPainelCentro() {
        //////Painel Centro
        JPanel painelCentro = new JPanel();
        painelCentro.setBackground(Color.WHITE);
        painelCentro.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));//Adiciona Borda Preta
        painelCentro.setSize(100, 50);
        painelCentro.setLayout(new BorderLayout());
        painelCentro.add(lrotuloMap, BorderLayout.CENTER);
        
        JPanel miniPanel = new JPanel();
        JButton botaoBomb = new JButton(new ImageIcon("src/view/bomb.gif"));
        botaoBomb.setPreferredSize(new Dimension(60, 60));
        botaoBomb.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((e.getModifiers() == KeyEvent.MOUSE_EVENT_MASK)){
                    colocaBombEffect();
                    
                    if(ambienteAtual.getDescricao().equals(ambientes.get(localizacaoTesouro).getDescricao())) {
                        saudacaoWinner();
                    } else {
                        saudacaoLoser();
                    }
                }
            }
        }
        );
        
        miniPanel.add(botaoBomb);
        miniPanel.setLayout(new FlowLayout(10));
        miniPanel.setBackground(Color.WHITE);
        miniPanel.add(botaoBomb);
        
        painelCentro.add(miniPanel, BorderLayout.SOUTH);
        
        
        fjanela.add(painelCentro,( BorderLayout.CENTER));
    }
    
    /**
     * Instancia todos os componentes necessários para a barra de menu.
     * Em que fica o botão de ajuda.
     */
    private void montarMenuBr() {
        //MenuBar
        JMenuBar barraMenu = new JMenuBar();
        
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem botaoAjuda = new JMenuItem("Exibir");
        botaoAjuda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(fjanela, imprimirAjuda());
            }
        });
        
        JMenuItem botaoAutoComplete = new JMenuItem("Auto Completa");
        botaoAutoComplete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(fjanela, "A funcionalidade de substituir "
                        + "a primeira letra da primeira palavra de minúsculo para maiúscula "
                        + "é automático. \n"
                        + "Para completar o comando basta digitar as palavras "
                        + "similares e pressionar a tecla 'ctrl', isso funciona \n"
                        + "também com nomes dos cenários");
            }
        });
                
        menuAjuda.add(botaoAjuda);
        menuAjuda.add(botaoAutoComplete);
        barraMenu.add(menuAjuda);
        fjanela.setJMenuBar(barraMenu);
    }
    
    
    /**
     * Muda o campo Inferior para mostrar a localização atual
     * e as direções possíveis.
     */
    private void locAtualDefault () {
        String[] locAtual = imprimirLocalizacaoAtual().split("\n");
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
    
    /**
     * Mostra a janela de despedida, ao usar a opção Sair.
     */
    private void saudacaoSair() {
        JOptionPane.showMessageDialog(fjanela,"Obrigado por jogar. Ate mais!");
        fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Mostra a janela de quando as tentativas acabam
     * e inicia o audio de derrota.
     */
    private void saudacaoTerminadoTentivas() {
        trocarAudio("gameOver.wav");
        JOptionPane.showMessageDialog(fjanela,"Game Over! Suas tentativas acabaram!");
        fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Mostra a janela de quando explodiu no local em que está o 
     * tesouro e inicia o audio de vitória.
     */
    private void saudacaoWinner() {
        trocarAudio("winner.wav");
        JOptionPane.showMessageDialog(fjanela,"CONGRATULATIONS!!! Voce venceu!!! Voce encontrou o tesouro escondido!");
        fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
    }

    
    /**
     *Mostra a janela de quando explode no local onde não está o tesouro
     * e inicia o audio de derrota.
     */
    private void saudacaoLoser() {
        trocarAudio("gameOver.wav");
        JOptionPane.showMessageDialog(fjanela,"GAME OVER! Voce gastou sua carga explosiva e nao encontrou o tesouro.");
        fjanela.dispatchEvent(new WindowEvent(fjanela, WindowEvent.WINDOW_CLOSING));
    }
    
    /**
     * Classe interna, instância no addListerner do atributo
     * tCampoDigitacao
     */
    private class OnEnter implements KeyListener {
        
        /**Método que troca a primeira letra da frase por maiúscula, por princípio
         * de evitar erros.
         * @param e
         */
        @Override
        public void keyTyped(KeyEvent e) {
            String test = tCampoDigitacao.getText();
            
            if ( ( (!(test.equals(""))) || (!(test.equals(" "))) ) && 
                    (tCampoDigitacao.getText().length() == 1) ) {
                    modaLetraInicialParaMaiuscula();
              
            } else if ( (test.length() > 1) 
                    && (String.valueOf(test.charAt(0)).equals(" ")) ) {
                tCampoDigitacao.setText(test.trim());
                modaLetraInicialParaMaiuscula();
            }
        }

        /**
         * Realiza subimissão da frase ao pressionar ENTER
         * @param e 
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               
                String fraseDigitada = tCampoDigitacao.getText();
  
                int terminado = 0;
                Comando comando;
 
                comando = Analisador.pegarComando(fraseDigitada);
                
                //Limpando o StringBuilder
                tCampoDigitacao.setText("");
                
                terminado = processarComando(comando);
                if(terminado == 1) {
                    saudacaoSair();
                    
                }else if ((quantidadeTentativas == 0) && (!temChaveMestra)) {      
                    saudacaoTerminadoTentivas();
                }else if(terminado == 2) {
                    colocaBombEffect();
                    
                    saudacaoWinner();
                }else if(terminado == 3) {
                    colocaBombEffect();
                    saudacaoLoser();
                }
            }
            
        }

        /** Método que auto completa a primeira e a segunda palavra apertando ctrl
         * @param e
         *
         */
        @Override
        public void keyReleased(KeyEvent e) { 

            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                String test = tCampoDigitacao.getText();
                String[] fraseSeparada = tCampoDigitacao.getText().split(" ");
                
                if (fraseSeparada.length > 0) {
                    
                    if (fraseSeparada.length == 1) {
                        String[] cmds = Analisador.getComandos().split(" ");
                        String palavraSimiString = "";
                        for (String cmd : cmds) {
                            if (cmd.trim().contains(fraseSeparada[0]))
                                palavraSimiString = cmd.trim();
                        }
                        
                        if (!palavraSimiString.equals("")) {
                            tCampoDigitacao.setText(palavraSimiString + " ");
                            tCampoDigitacao.repaint();
                        }
                        
                        
                    } else if (fraseSeparada[0].equals(Analisador.getComandoValido(0))) {
                        
                        String palavraSimiString = "";
                        String[] ambSaidas = ambienteAtual.getSaidas().split("\t");
                        for (String cmd : ambSaidas) {
                            String palavraTratada = fraseSeparada[1].substring(0,1).toUpperCase();
                            if (cmd.trim().contains(palavraTratada))
                                palavraSimiString = cmd.trim();
                        }
                        
                        if (!palavraSimiString.equals("")) {
                            tCampoDigitacao.setText(fraseSeparada[0] +
                                   " " + palavraSimiString);
                            tCampoDigitacao.repaint();
                        }
                    }
                }
            }
            
        }
        
        /**
         * Método que muda a letra inicail para mauúscula 
         * e corrige espaços no início do texto
         */
        private void modaLetraInicialParaMaiuscula() {
            String[] fraseSeparada;

            fraseSeparada = tCampoDigitacao.getText().split(" ");

            if ((fraseSeparada.length > 0) && (!fraseSeparada[0].toUpperCase().equals(fraseSeparada[0])) ) {
                fraseSeparada[0] = fraseSeparada[0].substring(0,1).toUpperCase() + fraseSeparada[0].substring(1);
                if (fraseSeparada.length > 1) {
                    fraseSeparada[1] = fraseSeparada[1].substring(0,1).toUpperCase() + fraseSeparada[1].substring(1);
                }

                String frase = "";
                for (String palavra : fraseSeparada) {
                    frase += palavra;
                }

                tCampoDigitacao.setText(frase);
                tCampoDigitacao.repaint();
            }
        }
       
    }
    
    
}
