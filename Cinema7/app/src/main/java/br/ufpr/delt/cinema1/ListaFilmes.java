package br.ufpr.delt.cinema1;

import android.content.Context;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ListaFilmes {
    private ArrayList<Filme> alFilmes;
    private Context context;
    public static ListaFilmes listaFilmes;
    private static String fileName = "FilmesDB.bin";
    public static final String PREF_LOCAL = "Shopping Jockey Plaza";

    private ListaFilmes(Context context) {
        alFilmes = new ArrayList<Filme>();
        this.context = context;
        // populando a base com alguns exemplos hardcoded
        /*
        alFilmes.add(new Filme("StarWars - O Despertar da Força", 28, 12, 2015, "Imax Palladium",  ""));
        alFilmes.add(new Filme("Os oito condenados", 30, 1, 2016, "Cinemark Muller",  ""));
        alFilmes.add(new Filme("Homem-Formiga", 7, 2, 2016, "Shopping Jd das Américas",  ""));
        alFilmes.add(new Filme("Deadpool", 14, 2, 2016, "Shopping Curitiba", ""));
        alFilmes.add(new Filme("Batman vs Superman", 26, 3, 2016, "Shopping Cristal", ""));
        alFilmes.add(new Filme("Capitão America - Guerra Civil", 15, 5, 2016, "Imax Palladium",  ""));
        alFilmes.add(new Filme("X-Men Apocalipse", 5, 6, 2016, "Shopping Curitiba", ""));
        alFilmes.add(new Filme("Rogue One", 26, 12, 2016, "Shopping Muller", ""));
        alFilmes.add(new Filme("Passageiros", 17, 1, 2017, "Cine Passeio", ""));
        alFilmes.add(new Filme("Estrelas além do Tempo", 2, 2, 2017, "Shopping Muller", ""));
        alFilmes.add(new Filme("Moana", 24, 2, 2017, "Cine Passeio", ""));
        alFilmes.add(new Filme("A Bela e a Fera", 26, 3, 2017, "Shopping Muller", ""));
        alFilmes.add(new Filme("Indiana Jones", 20, 6, 2023, "Shopping Jd das Américas", ""));
        alFilmes.add(new Filme("Freira 2", 5, 9, 2023, "Shopping Muller", ""));
        alFilmes.add(new Filme("Fale Comigo", 5, 9, 2023, "Shopping Patio Batel", ""));
        alFilmes.add(new Filme("Barbie", 20, 9, 2023, "Shopping Jockey", ""));
        alFilmes.add(new Filme("Oppenheimer", 28, 7, 2023, "Shopping Palladium", ""));
        alFilmes.add(new Filme("Besouro Azul", 5, 8, 2023, "Shopping Cristal", ""));
        alFilmes.add(new Filme("Gran Turismo", 18, 9, 2023, "Shopping Curitiba", ""));
        alFilmes.add(new Filme("Noite das Bruxas", 25, 7, 2023, "Shopping Muller", ""));
        alFilmes.add(new Filme("Mercenários 6", 5, 9, 2023, "Cine Passeio", ""));
        */

        //Toast.makeText(this.context.getApplicationContext(), "Chegou Construtor...", Toast.LENGTH_LONG).show();
        //this.saveToFile(); // opção para salvar os filmes acima em uma primeira execução para possuir uma base inicial de filmes

        this.readFromFile(); // opção padrao, vai ler o arquivo persistido, se não houver começa vazio
    }

    public static ListaFilmes getInstance(Context context) {
        if (listaFilmes == null) {
            listaFilmes = new ListaFilmes(context);
        }
        return listaFilmes;
    }

    public int size(){
        return alFilmes.size();
    }

    public void add(Filme filme) {
        alFilmes.add(filme);
    }

    public Filme get(int i) {
        return alFilmes.get(i);
    }

    public void remove(int i){
        alFilmes.remove(i);
    }

    public ArrayList<Filme> getAlFilmes(){
        return alFilmes;
    }

    public Iterator<Filme> iterator() {
        return alFilmes.iterator();
    }

    public void salvar() {
        this.saveToFile();
    }

    // serializa o ArrayList de Filmes gravando no arquivo o conteúdo de alFilmes
    private void saveToFile() {
        try {
            //Toast.makeText(this.context.getApplicationContext(), "Chegou GRAVAR...", Toast.LENGTH_LONG).show();
            FileOutputStream fileOutputStream = this.context.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.alFilmes); // salva o ArrayList inteiro
            objectOutputStream.close();
            fileOutputStream.close();
            //Toast.makeText(this.context.getApplicationContext(), "Salvou arquivo...", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this.context.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    // cria um ArrayList de Filmes lendo do arquivo e atribui a alFilmes
    private void readFromFile() {
        try {
           // Toast.makeText(this.context.getApplicationContext(), "Chegou LER...", Toast.LENGTH_LONG).show();
            FileInputStream fileInputStream = this.context.getApplicationContext().openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.alFilmes = (ArrayList<Filme>) objectInputStream.readObject(); // le o ArrayList inteiro
            objectInputStream.close();
            fileInputStream.close();
           // Toast.makeText(this.context.getApplicationContext(), "Leu aquivo... ", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this.context.getApplicationContext(), "Arquivo ainda não existe...", Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(this.context.getApplicationContext(), "Erro: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        catch (ClassNotFoundException e) {
            Toast.makeText(this.context.getApplicationContext(), "Erro: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

}
