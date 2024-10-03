package com.example.ezymobile;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cursoViewHolder extends RecyclerView.ViewHolder {
    TextView titulo, desc;
    ImageView img;
    Button btInscrever;
    public cursoViewHolder(@NonNull View itemView) {
        super(itemView);
        //Vincular o ID para cada componente
        titulo = itemView.findViewById(R.id.titulo);
        desc = itemView.findViewById(R.id.desc);
        img = itemView.findViewById(R.id.img);
        btInscrever = itemView.findViewById(R.id.btInscrever);
    }
}
