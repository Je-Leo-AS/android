package br.ufpr.delt.cinema1;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class FilmeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    ImageView fCartaz;
    TextView fNome;
    TextView fDataLocal;
    FilmeAdapter adapter;
    Context context;

    public FilmeViewHolder(@NonNull View itemView, FilmeAdapter adapter, Context context) {
        super(itemView);
        fCartaz = itemView.findViewById(R.id.ivCartaz);
        fNome = itemView.findViewById(R.id.tvNomeFilme);
        fDataLocal = itemView.findViewById(R.id.tvDataLocal);

        this.adapter = adapter;
        this.context = context;
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int position = getLayoutPosition();
        //Toast.makeText(context, "Clicou " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent (context, VerFilme.class);
        intent.putExtra("filmeId", position);
        startActivity(context, intent, null);
    }
}
