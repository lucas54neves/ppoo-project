package controller;

/**
 * Esta classe eh parte da aplicacao "A Casa Mal Assombrada".
 * "A Casa Mal Assombrada" eh um jogo de aventura muito simples, baseado em texto.
 *
 * Essa aplicacao eh o projeto final da disciplina de Praticas de Programacao
 * Orientada a Objetos do curso de Ciencia da Computacao da Univeridade Federal
 * de Lavras
 *
 * Essa classe guarda informacoes sobre um comando que foi digitado pelo
 * usuario. Um comando atualmente consiste em duas strings: uma palavra
 * de comando e uma segunda palavra (por exemplo, se o campo for "pegar
 * mapa", entao as duas strings obviamente serao "pegar" e "mapa").
 *
 * Isso eh usado assim: comandos ja estao validados como comandos validos
 * Se o usuario entrou um comando invalido (uma palavra que nao eh
 * conhecida) entao o a palavra de comando eh <null>.
 *
 * Se o comando tem so uma palavra, a segunda palavra eh <null>
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */

public class Comando {
    private String palavraDeComando;
    private String segundaPalavra;

    /**
     * Cria um objeto comando. Primeira e segunda palavra devem ser
     * fornecidas, mas qualquer uma (ou ambas) podem ser null.
     * @param primeiraPalavra A primeira palavra do comando. Null se
     * 						  o comando nao foi reconhecido
     * @param segundaPalavra A segunda palavra do comando.
     */
    public Comando(String primeiraPalavra, String segundaPalavra) {
        palavraDeComando = primeiraPalavra;
        this.segundaPalavra = segundaPalavra;
    }

    /**
     * Retorna a palavra de comando (a primeira palavra) deste comando.
     * Se o comando nao foi entendido, o resultado eh null.
     * @return A palavra de comando.
     */
    public String getPalavraDeComando() {
        return palavraDeComando;
    }

    /**
     * @return A segunda palavra deste comando. Retorna null se
     * nao existe segunda palavra.
     */
    public String getSegundaPalavra() {
        return segundaPalavra;
    }

    /**
     * @return true se o comando nao foi entendido.
     */
    public boolean ehDesconhecido() {
        return (palavraDeComando == null);
    }

    /**
     * @return true se o comando tem uma segunda palavra.
     */
    public boolean temSegundaPalavra() {
        return (segundaPalavra != null);
    }
}
