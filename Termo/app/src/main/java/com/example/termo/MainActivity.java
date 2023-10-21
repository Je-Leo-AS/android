package com.example.termo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Random random;
    Banco banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        // OPCAO 2: criando classe Banco e classe TERMO
        // com isso tem TODAS as palvras em memória com
        // apenas uma leitura do csv
        banco = Banco.getInstance();

        String segredo = banco.getTermo(0).getPalavra();
        Toast.makeText(getApplicationContext(), "Termo: " + segredo, Toast.LENGTH_LONG).show();

        //readCSV();
    }

    // OPCAO 1: sem usar classes extras, cada novo jogo
    // le novamente o arquivo csv de forma sequencial até chegar
    // na N-éssima palavra que foi sorteada
    private void readCSV() {
        try {
            String palavra = null;
            int pos = 1 + random.nextInt(86);
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
        }
    }
}