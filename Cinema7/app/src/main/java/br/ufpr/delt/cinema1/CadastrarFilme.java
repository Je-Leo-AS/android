package br.ufpr.delt.cinema1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

public class CadastrarFilme extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
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
    int iActId;
    int filmePos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_filme);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // no objeto Bundle recebemos o from e o filmeId
        //  from = 1 identifica que é inclusão de novo filme
        //           ou seja, quem chamou foi a MainActivity
        //  from = 2 identifica que é edição de filme existente
        //           ou seja, quem chamou foi a DadosFilme
        //  filmeId = posição na ListView, igual a posição no ArrayList alFilmes
        //  pega então o código do filme e o codigo da activity que chamou
        filmePos = extras.getInt("filmeId");
        iActId = extras.getInt("from");

        etNomeFilme = findViewById(R.id.etNomeFilme);
        tvData = findViewById(R.id.tvData);
        etLocal = findViewById(R.id.etLocal);
        etmlComentarios = findViewById(R.id.etmlComentarios);
        imFavorito = findViewById(R.id.imFavorito);
        rbAvaliacao = findViewById(R.id. rbAvaliacao);

        listaFilmes = ListaFilmes.getInstance(getApplicationContext());

        if (iActId == 2) {
            // altera o texto do botão para Atualizar filme
            Button bCadastrar = findViewById(R.id.bCadastrar);
            bCadastrar.setText("Atualizar filme");
            // pega o filme em questão que será editado
            filme = listaFilmes.get(filmePos);
            // atualiza todos os dados do filme nos respectivos atributos usando os GETTERS da classe Filme
            etNomeFilme.setText(filme.getfNome());
            rbAvaliacao.setRating(filme.getfAval());
            tvData.setText(filme.getfDataStr());
            etLocal.setText(filme.getfLocal());
            etmlComentarios.setText(filme.getfComentarios());
            if (filme.isfFavorito()) {
                imFavorito.setImageResource(R.drawable.ic_favorite_on);
            }
        } else { // veio da Main, iActId == 1, então é filme NOVO,
            filme = new Filme();

        }
        // atualiza atributos para funcionamento da activity
        fData = filme.getfData();
        fFavorito = filme.isfFavorito();
        fDataStr = filme.getfDataStr();
        tvData.setText(fDataStr);
    }
    public void showDatePickerDialog (View view)
    {
        // monta a data como argumento para o Picker
        Bundle bfDate = new Bundle();
        // em milisegundos, um long
        Locale.setDefault(new Locale("pt", "BR"));
        localDateTime = filme.getfData().atTime(12, 0);
        bfDate.putLong("data", localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
        DialogFragment dfDatePicker = new DatePickerFragment();
        dfDatePicker.setArguments(bfDate);
        dfDatePicker.show(getSupportFragmentManager(), "Data da sessão");
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        filme.setfData(LocalDate.of(year, month+1, day));
        fDataStr = filme.getfDataStr();
        tvData.setText(fDataStr);
    }

    public void onClickFavoritarFilme (View view){
        if (!fFavorito) {
            fFavorito = true;
            imFavorito.setImageResource(R.drawable.ic_favorite_on);
            //Toast.makeText(getApplicationContext(), "Like...", Toast.LENGTH_LONG).show();
        }else {
            fFavorito = false;
            imFavorito.setImageResource(R.drawable.ic_favorite);
            //Toast.makeText(getApplicationContext(), "Dislike...", Toast.LENGTH_LONG).show();
        }
    }
    public void onClickCadastrarFilme(View view) {
        // pegar os campos digitados do novo filme ou editados de filme existente
        String fNome = etNomeFilme.getText().toString();
        String fLocal = etLocal.getText().toString();
        String fComentarios = etmlComentarios.getText().toString();

        // nome ou local do filme está vazio?
        // data não fica vazia
        if (fNome.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha o nome do filme", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fLocal.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Preencha o local do filme", Toast.LENGTH_SHORT).show();
            return;
        }

        // passou verificação obrigatória, salvar os dados ALTERADOS ou NOVOS deste filme
        filme.setfNome(fNome);
        filme.setfLocal(fLocal);
        filme.setfComentarios(fComentarios);
        filme.setfFavorito(fFavorito);
        filme.setfAval(rbAvaliacao.getRating());
        // data já foi atualizada no onDateSet

        //Toast.makeText(getApplicationContext(), "Filme: " + fDataStr + " Fav? " + filme.isfFavorito() + " Aval: " + filme.getfAval(), Toast.LENGTH_LONG).show();

        if (iActId == 1) { // é filme novo?
            listaFilmes.add(filme); // ok, filme tem nome e local, pode adicionar ao ArrayList
        }
        listaFilmes.salvar(); // chama o metodo static para salvar a Lista de Filmes em disco
        // pois houve alteração de algum EXISTENTE ou INCLUSAO de um novo

        this.finish(); // termina a activity
    }

}