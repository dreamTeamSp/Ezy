package com.example.ezymobile.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ezymobile.R;
import com.example.ezymobile.cursos;
import com.example.ezymobile.cursosAdapter;
import com.example.ezymobile.databinding.FragmentDashboardBinding;
import com.example.ezymobile.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    FirebaseFirestore conexaoBD = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inicializa o ViewModel
        // DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Inicializa o View Binding
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Criando uma lista para armazenar objetos do tipo "Produto"
        List<cursos> listaCursos = new ArrayList<>();

        //Recupera o ID do usuário
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        //Conectando na coleção "inscricoes"
        conexaoBD.collection("inscricoes")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("TESTE APP", documentSnapshot.getId() + " => " + documentSnapshot.getData());
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

                                Log.d("TESTE APP", " >>>>>>>>" + titulo);
                                Log.d("TESTE APP", " >>>>>>>>" + descricao);
                                Log.d("TESTE APP", " >>>>>>>>" + codigo);

                                cursos c = new cursos(codigo, titulo, descricao, img);
                                listaCursos.add(c);


                                // Do something else with entry.getKey() and entry.getValue()
                            } else {
                                throw new IllegalStateException("Expecting either String or Class as entry value");
                            }
                        }
                        //RecyclerView....
                        //Após encerrar a repetição e adicionar todos os produtos na lista
                        //fazemos a configuração do RecyclerView e adicionamos a lista para ser exibida nele
                        //Criando um objeto do RecyclerView da tela
                        RecyclerView recyclerTela = root.findViewById(R.id.recyclerView);

                        boolean jaInscrito = true;
                        //Passando a lista para o Adapter personalizado
                        cursosAdapter adapter = new cursosAdapter(listaCursos, getContext(), jaInscrito);

                        //Configuração de um gestor de layout
                        recyclerTela.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        //Passando o adapter para o RecyclerView
                        recyclerTela.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Caso a busca encontre algum erro, esse método é chamado
                        Toast.makeText(root.getContext(), "Erro ao buscar", Toast.LENGTH_SHORT).show();
                    }
                });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Libera o binding para evitar vazamento de memória
        binding = null;
    }
}
