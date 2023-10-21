package com.example.termo;

import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Banco {

    private ArrayList<Termo> termos;
    private static Banco banco;


    // construtor privado (escondido), criando um SINGLETON
    private Banco() {
        termos = new ArrayList<Termo>();  // lista vazia

        // mostanto teste de termos alocados em memória
        termos.add(new Termo("teste", "TESTE"));
        termos.add(new Termo("poeta", "POETA"));
        termos.add(new Termo("caros", "CAROS"));

        // trocar por chamar o readCSV para popular a lista
        //readCSV();
    }

    public static Banco getInstance() {
        if (banco == null) {// não existe? então é primeira chamada
            // logo, criar o objeto, cria a lista e popula
            banco = new Banco();
        }
        return banco;
    }

    public int size(){
        return termos.size();
    }

    public Termo getTermo(int i){
        return termos.get(i);
    }

    private static void readCSV() {
        // precisa terminar o método atualizando o getInstance para receber o Context
        // como atributo da classe, assim o trecho abaixo irá funcionar...
        /*try {
            String palavra = null;
            InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.palavras_termo);
            Scanner scanner = new Scanner(inputStream);
            for (int i = 1; i <= pos; i++) {
                palavra = scanner.nextLine();
            }
            String[] termo = palavra.split("\\s*;\\s*");
            Toast.makeText(getApplicationContext(), "Sorteado: " + termo[0] + " - Termo: " + termo[1], Toast.LENGTH_LONG).show();
            inputStream.close();
            scanner.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Algo deu errado: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }

}
