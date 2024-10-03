package com.example.ezymobile.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ezymobile.R;
import com.example.ezymobile.cursos;
import com.example.ezymobile.cursosAdapter;
import com.example.ezymobile.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    // Binding para acessar as views do layout
    private FragmentDashboardBinding binding;
    //Iniciando a conexão com o Firebase Firestore
    FirebaseFirestore conexaoBD = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inicializa o ViewModel
       // DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Inicializa o View Binding
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Criando uma lista para armazenar objetos do tipo "Produto"
        List<cursos> listaCursos = new ArrayList<>();

        //Conectando na coleção "produto"
        conexaoBD.collection("cursos")
                .get() //Utilizando o método get() para recuperar todas os documentos da coleção
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Se o comando for executado com sucesso, então
                        if (task.isSuccessful()) {
                            //Repetição para recuperar todos os documentos da coleção
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Convertendo o documento para um objeto do tipo "Produto"
                                cursos p = document.toObject(cursos.class);
                                //Adicionar o código do curso
                                p.setCodigo(document.getId());
                                //Adicionando o produto na lista
                                listaCursos.add(p);
                            }

                            //Após encerrar a repetição e adicionar todos os produtos na lista
                            //fazemos a configuração do RecyclerView e adicionamos a lista para ser exibida nele
                            //Criando um objeto do RecyclerView da tela
                            RecyclerView recyclerTela = root.findViewById(R.id.recyclerView);

                            boolean jaInscrito = false;
                            //Passando a lista para o Adapter personalizado
                            cursosAdapter adapter = new cursosAdapter(listaCursos, getContext(), jaInscrito);

                            //Configuração de um gestor de layout
                            recyclerTela.setLayoutManager(new LinearLayoutManager(root.getContext()));
                            //Passando o adapter para o RecyclerView
                            recyclerTela.setAdapter(adapter);

                        } else {
                            //Exibindo informação caso apresente um erro na conexão
                            Toast.makeText(root.getContext(), "Sem cursos cadastrados", Toast.LENGTH_SHORT).show();
                        }
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
