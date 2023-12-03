package br.ufpr.delt.cinema1;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class VerFilme extends AppCompatActivity {
    EditText etNomeFilme;
    TextView tvData;
    EditText etLocal;
    EditText etmlComentarios;
    RatingBar rbAvaliacao;
    ImageView imFavorito;
    LocalDate fData;
    boolean fFavorito;
    float fAval;
    LocalDateTime localDateTime;
    String fDataStr;
    Filme filme;
    ListaFilmes listaFilmes;
    Bundle extras;
    int filmePos;
    TextView tvComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_filme);

        extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        etNomeFilme = findViewById(R.id.etNomeFilme);
        tvData = findViewById(R.id.tvData);
        etLocal = findViewById(R.id.etLocal);
        etmlComentarios = findViewById(R.id.etmlComentarios);
        imFavorito = findViewById(R.id.imFavorito);
        rbAvaliacao = findViewById(R.id. rbAvaliacao);
        tvComentarios = findViewById(R.id.tvComentarios);

        // pega código do filme enviado no lister do onClick do Recycler View
        filmePos = extras.getInt("filmeId");

        listaFilmes = ListaFilmes.getInstance(getApplicationContext());
        this.mostraDadosFilme();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ver_filme, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void mostraDadosFilme() {
        // pega o filme em questão
        filme = listaFilmes.get(filmePos);
        // atualiza nome do filme na action bar
        //ab.setTitle(filme.getfNome());

        // atualiza dados do filme nos diversos atributos usando os getters da classe Filme
        etNomeFilme.setText(filme.getfNome());
        rbAvaliacao.setRating(filme.getfAval());
        tvData.setText(filme.getfDataStr());
        etLocal.setText(filme.getfLocal());

        if (filme.isfFavorito()) {
            imFavorito.setImageResource(R.drawable.ic_favorite_on);
        } // default no layout é ícone normal (vazio = não favorito)
        else {
            // desligando pois pode ter sido alterado no editar filme existente, garantir update visual
            imFavorito.setImageResource(R.drawable.ic_favorite);
        }

        String fComentario = filme.getfComentarios();
        if (fComentario.isEmpty()) {
            // esconde campo comendátios se não houver comentários
            tvComentarios.setVisibility(View.INVISIBLE);
            etmlComentarios.setVisibility(View.INVISIBLE);
            etmlComentarios.setText("");
        } else {
            tvComentarios.setVisibility(View.VISIBLE);
            etmlComentarios.setVisibility(View.VISIBLE);
            etmlComentarios.setText(fComentario);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.miEditar) {
            // chamar a activity para editar os dados deste filme
            Intent intent = new Intent (this, CadastrarFilme.class);
            intent.putExtra("filmeId", filmePos);
            intent.putExtra("from", 2);  // 2 identifica que é edição de filme existente
            startActivity(intent);
            return true;
        }
        if (id == R.id.miShare) {
            // compartilhar os dados desta sessão de filme com alguém
            // montar a string usando a classe StringBuilder
            StringBuilder strShare = new StringBuilder();
            strShare.append("Eu assisti o filme '");
            strShare.append(filme.getfNome());
            strShare.append("' em ");
            strShare.append(filme.getfDataStr());
            strShare.append(" no ");
            strShare.append(filme.getfLocal());
            strShare.append(". Minha avaliação, em uma nota de 0..5, foi de ");
            strShare.append(filme.getfAval());
            strShare.append("!");

            if (filme.isfFavorito()) {
                strShare.append("\nConsidero este um de meus filmes favoritos de todos os tempos!");
            }

            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, strShare.toString());
            startActivity(Intent.createChooser(intent, "Compartilhar filme visto"));

            return true;
        }
        if (id == R.id.miDeletar) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Deletar filme");
            alert.setMessage("Você tem certeza que deseja remover o filme '" + etNomeFilme.getText()+ "' da sua lista?");
            alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continuar apagando o filme da lista
                    listaFilmes.remove(filmePos);
                    listaFilmes.salvar();
                    encerrar(); // termina a activity
                }
            });
            alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        this.mostraDadosFilme(); // atualiza os campos do form
        // pegando os dados do filme, pois este foi editado
    }

    public void encerrar(){
        this.finish(); // termina a activity
    }

}