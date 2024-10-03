package com.example.ezymobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.security.Principal;

public class Perfil extends AppCompatActivity {

    private TextView textNomeUsuario, textEmailUsuario, textTelUsuario;
    private Button btDeslogar;

    FirebaseFirestore dbEzy = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        IniciarComponentes();

        btDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Perfil.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = dbEzy.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    textNomeUsuario.setText(documentSnapshot.getString("nome"));
                    textTelUsuario.setText(documentSnapshot.getString("telefone"));
                    textEmailUsuario.setText(email);
                }
            }
        });
    }

    private void IniciarComponentes(){
        textNomeUsuario = findViewById(R.id.textNomeUsuario);
        textEmailUsuario = findViewById(R.id.textEmailUsuario);
        textTelUsuario = findViewById(R.id.textTelUsuario);
        btDeslogar = findViewById(R.id.btDeslogar);
    }
}