package com.example.ezymobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastrar extends AppCompatActivity {

    private EditText campoNome, campoTel, campomEmail, campoSenha;
    private Button btCadastrar, btJaTemCad;
    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso!"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        IniciarComponentes();

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = campoNome.getText().toString();
                String telefone = campoTel.getText().toString();
                String email = campomEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    CadastrarUsuario(v);
                }
            }
        });
    }

    private void CadastrarUsuario(View v){
        String email = campomEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    SalvarDadosUsuario();

                    Snackbar snackbar = Snackbar.make(v,mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    String erro;
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha de no mínimo 6 caracteres.";
                    }catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta já foi cadastrada.";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido.";
                    }catch(Exception e){
                        erro = "Erro ao cadastrar usuário!";
                    }

                    Snackbar snackbar = Snackbar.make(v,erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });

        btJaTemCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cadastrar.this, Login.class));
            }
        });
    }

    private void SalvarDadosUsuario(){
        String nome =  campoNome.getText().toString();
        String telefone = campoTel.getText().toString();

        FirebaseFirestore dbEzy = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);
        usuarios.put("telefone", telefone);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = dbEzy.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("dbEzy", "Sucesso ao salvar os dados");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("dbEzy_error", "Erro ao salvar os dados" + e.toString());
                    }
                });
    }

    private void IniciarComponentes(){
        campoNome = findViewById(R.id.campoNome);
        campoTel = findViewById(R.id.campoTel);
        campomEmail = findViewById(R.id.campoEmail);
        campoSenha = findViewById(R.id.campoSenha);
        btCadastrar = findViewById(R.id.btCadastrar);
        btJaTemCad = findViewById(R.id.btJaTemCad);
        btJaTemCad = findViewById(R.id.btJaTemCad);
    }


}