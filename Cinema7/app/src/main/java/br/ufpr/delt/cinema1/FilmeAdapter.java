package br.ufpr.delt.cinema1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeViewHolder> {
    Context context;
    ArrayList<Filme> alFilmes;

    public FilmeAdapter(Context context, ArrayList<Filme> alFilmes) {
        this.context = context;
        this.alFilmes = alFilmes;
    }

    @NonNull
    @Override
    public FilmeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmeViewHolder(LayoutInflater.from(context).inflate(R.layout.filme_view, parent, false), this, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmeViewHolder holder, int position) {
        holder.fNome.setText(alFilmes.get(position).getfNome());
        String localData = alFilmes.get(position).getfDataStr() + " - " + alFilmes.get(position).getfLocal();
        holder.fDataLocal.setText(localData);
        //holder.fCartaz.setImageResource(alFilmes.get(position).getCartaz());
    }

    @Override
    public int getItemCount() {
        return alFilmes.size();
    }
}
