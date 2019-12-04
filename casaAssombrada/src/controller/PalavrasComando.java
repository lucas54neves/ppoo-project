/**
 * Esta classe eh parte da aplicacao "A Casa Mal Assombrada".
 * "A Casa Mal Assombrada" eh um jogo de aventura muito simples, baseado em texto.
 *
 * Essa classe guarda uma enumeracao de todos os comandos conhecidos do
 * jogo. Ela eh usada no reconhecimento de comandos como eles sao digitados.
 *
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
package controller;

public class PalavrasComando {
    // um vetor constante que guarda todas as palavras de comandos validas
    private static final String[] comandosValidos = {
        "Ir", "Sair", "Ajuda", "Observar", "Explodir"
    };

    /**
     * Construtor - inicializa as palavras de comando.
     */
    public PalavrasComando(){}

    /**
     * Verifica se uma dada String eh uma palavra de comando valida.
     * @param umaString
     * @return true se a string dada eh um comando valido,
     * false se nao eh.
     */
    public static boolean ehComando(String umaString) {
        for (String comandosValido : comandosValidos) {
            if (comandosValido.equals(umaString)) {
                return true;
            }
        }
        // se chegamos aqui, a string nao foi encontrada nos comandos.
        return false;
    }

    /**
     * Printa os comandos existentes.
     * @return 
     */
    public static String getComandos() {
        String retorno = "";

        for (String comando : comandosValidos) {
            retorno = retorno + comando + "  ";
        }

        return retorno;
    }
    
    public static String getComando(int pos) {
        return comandosValidos[pos];
    }
}
