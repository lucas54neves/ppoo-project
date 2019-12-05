package controller;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author  Andrew Takeshi, Davi Horner, Lucas Neves e Ruan Basilio
 * @version 2019.10.25
 */
public class SistemaDeArquivoTxt {
    
    /** Salva todos os dados necessários para vitória no jogo.
     * @param dados - Localização de todas as dicas e onde está o tesouro
     */
    public static void salvar(String dados) throws IOException {
        try (FileWriter arq = new FileWriter("dadosJogo.txt")) {
            arq.write(dados) ;
        } catch (IOException e) {
            throw e;
        }
    }

}
