package br.ufpr.delt.cinema1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FilmeAdapter filmeAdapter;
    ArrayList<Filme> alFilmes;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListaFilmes listaFilmes = ListaFilmes.getInstance(this);//getApplicationContext()
        alFilmes = listaFilmes.getAlFilmes();

        recyclerView = findViewById(R.id.rvFilmes);
        filmeAdapter = new FilmeAdapter(this, alFilmes);
        recyclerView.setAdapter(filmeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adiciona o Floating Action Button para adicionar filmes
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabNovoFilme);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (getApplicationContext(), CadastrarFilme.class);
                intent.putExtra("filmeId", 0);
                intent.putExtra("from", 1); // 1 identifica que é inclusão de NOVO filme
                startActivity(intent);

                /*
                //filmeAdapter.notifyDataSetChanged(); // alteração geral na lista de filmes
                alFilmes.add(new Filme("TESTE", 18, 9, 2023, "Teste Shopping", ""));
                filmeAdapter.notifyItemRangeInserted(alFilmes.size()-1, 1);  // alterando apenas o último
                */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu; // ao inflar o layout do menu, guardar a referência
        // para uso posterior de on/off dos icones de Alpha e Sort
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.miAlpha) {
            Collections.sort(alFilmes); // ordenar filmes em ordem alfabética crescente
            // usando ordenação NATURAL da classe Filme, que usa o compareTo por nome do Filme

            // atualizar desenho dos icones
            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_sort_alphabetical_on));
            menu.getItem(1).setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_sort_time));

            // notificar o recyclerview que os dados mudaram para ele fazer refresh
            filmeAdapter.notifyDataSetChanged();

            // sobe para o topo do recycler view
            recyclerView.scrollToPosition(0);

            return true;
        }
        if (id == R.id.miCrono) {
            // ordenar colocando os filmes mais recentes em data primeiro usando a interface Comparator
            // que é uma ordem não natural, usando para isso classe anômica
            Collections.sort(alFilmes, new Comparator<Filme>() {
                @Override
                public int compare(Filme filme1, Filme filme2) {
                    return filme2.getfData().compareTo(filme1.getfData());
                }
            });

            // atualizar desenho dos icones
            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_sort_alphabetical));
            menu.getItem(1).setIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.ic_sort_time_on));

            // notificar o recyclerview que os dados mudaram para ele fazer refresh
            filmeAdapter.notifyDataSetChanged();

            // sobe para o topo do recycler view
            recyclerView.scrollToPosition(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        // se houver alguma ordenação específica, ordenar e focar no novo
        filmeAdapter.notifyDataSetChanged();
        // este é pesado, pois atualiza a totalidade do widget

        // para implementar um scroll se necessário
        // recyclerView.scrollToPosition(0);
    }
}