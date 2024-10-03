package com.example.ezymobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class cursosAdapter extends RecyclerView.Adapter<cursoViewHolder> {

    List<cursos> cursos;
    //Acessa o firebase Storage para recuperar as imagens
    private StorageReference imgCursos = FirebaseStorage.getInstance().getReference();
    //Acessa o firebase firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Lista com os cursos que foram escolhidos
    List<cursos> listaCursos = new ArrayList<>();
    private Context context;
    //Variável boolean para saber se o curso é um curso já inscrito ou não
    boolean jaInscrito;

    //Construtor
    public cursosAdapter(List<cursos> cursos, Context context, boolean jaInscrito){
        this.cursos = cursos;
        this.context = context;
        this.jaInscrito = jaInscrito;
    }
    @NonNull
    @Override
    public cursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Chamando o layout_item.xml para definir como modelo a ser usado
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.curso, parent, false);
        return new cursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cursoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Convertendo o objeto viewHolder para o nosso ViewHolder
        cursoViewHolder cursoVH  = (cursoViewHolder) holder;

        //Agora podemos acessar os nossos componentes através do objeto "produtoVH"
        //para atribuir os valores de cada campo
        cursoVH.titulo.setText(cursos.get(position).getTitulo());
        cursoVH.desc.setText(cursos.get(position).getDesc());

        final long ONE_MEGABYTE = 1024 * 1024;
        imgCursos.child("cursos/" + cursos.get(position).getImg()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                cursoVH.img.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                exception.printStackTrace();
            }
        });

        //Referência do produto que foi clicado pelo usuário
        cursos produtoClique = cursos.get(position);

        //Preencher a lista com os cursos já inscritos
        List<cursos> cursosInscritos = new ArrayList<>();
        //Recupera o ID do usuário para listar só os cursos inscritos daquele usuário
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        //Conectando na coleção "inscricoes"
        db.collection("inscricoes")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //Recupera o valor gravado no Firestore
                        Map<String, Object> mapa = documentSnapshot.getData();
                        //Atribui que o mapa não estará vazio
                        assert mapa != null;
                        //Para cada item dentro do Map...
                        for (Map.Entry<String, Object> entry : mapa.entrySet()) {
                            if (entry.getValue() != null) {
                                //Recupera cada curso da lista
                                Map<String, Object> cadaCurso = (Map<String, Object>) documentSnapshot.getData().get(entry.getKey());
                                //Recupera informações de cada curso
                                String titulo = (String) cadaCurso.get("titulo");
                                String descricao = (String) cadaCurso.get("desc");
                                String codigo = (String) cadaCurso.get("codigo");
                                String img = (String) cadaCurso.get("img");

                                cursos c = new cursos(codigo, titulo, descricao, img);
                                Log.d("TAGTESTE", ">>>>>>>>>>>>>>>> 5 " + codigo);
                                cursosInscritos.add(c);
                            } else {
                                throw new IllegalStateException("Expecting either String or Class as entry value");
                            }
                        }
                        Log.d("TAGTESTE", ">>>>>>>>>>>>>>>> 6 " + cursosInscritos.size());

                        //Percorre todos os cursos cadastrados
//                        for (int i=0 ; i<cursos.size() ; i++) {
//                            //Percorre a lista dos cursos já inscritos
//                            for (int j=0 ; j<cursosInscritos.size() ; j++) {
                                //Se houver um curso com o mesmo código na lista de cursos inscritos
                                if(cursos.get(position).getCodigo().equals(cursosInscritos.get(0).getCodigo())){
                                    Log.d("TAGTESTE", ">>>>>>>> 1 " + cursos.get(0).getCodigo());
                                    Log.d("TAGTESTE", ">>>>>>>> 2 " + cursosInscritos.get(0).getCodigo());

                                    cursoVH.btInscrever.setEnabled(false);
                                    cursoVH.btInscrever.setText("Já inscrito");

                                    //Cursos que o usuário já se inscreveu
                                    if(jaInscrito == true){
                                        //Só entra nesse IF se for a tela Cursando
                                        cursoVH.btInscrever.setText("Cancelar inscrição");
                                    }

                                }else{

                                    cursoVH.btInscrever.setEnabled(true);
                                    cursoVH.btInscrever.setText("Cadastrar");

                                    //Cursos que o usuário não se inscrevreu
                                    //Evento a ser acionado quando for clicado na imagem da lupa sobre o arquivo
                                    cursoVH.btInscrever.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(cursoVH.btInscrever.getText().equals("Cancelar inscrição")){
                                                //Acessar o firebase e remover o curso da inscrição
                                                //Recupera o ID do usuário
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String uid = user.getUid();
                                                //Buscar a inscrição do usuário
                                                //db.collection("inscricoes").document(uid).u

                                            }else{
                                                //código do curso
                                                String codigo = cursos.get(position).getCodigo();
                                                //Curso escolhido para inscrição
                                                cursos c = cursos.get(position);
                                                //Adicionar o curso na lista
                                                listaCursos.add(c);
                                                //Recupera o ID do usuário
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String uid = user.getUid();
                                                Map<String, cursos> map =
                                                        listaCursos.stream().collect(Collectors.toMap(com.example.ezymobile.cursos::getTitulo, item -> item));
                                                //Cadastrar o usuário e o curso que ele escolheu na coleção "inscricoes"
                                                db.collection("inscricoes").document(uid).set(map)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(context, "Inscrição realizada", Toast.LENGTH_SHORT).show();
                                                                cursoVH.btInscrever.setEnabled(false);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(context, "Erro ao se inscrever", Toast.LENGTH_SHORT).show();
                                                                e.printStackTrace();
                                                            }
                                                        });
                                            }

                                        }
                                    });
                                }
                            //}

                        //}


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }



    @Override
    public int getItemCount() {
        return cursos.size();
    }
}