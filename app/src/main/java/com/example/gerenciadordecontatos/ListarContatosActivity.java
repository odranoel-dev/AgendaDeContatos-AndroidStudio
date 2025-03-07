package com.example.gerenciadordecontatos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListarContatosActivity extends AppCompatActivity {

    private ListView listViewContatos;
    private EditText edtBuscar;
    private ContatosAdapter adapter;
    private List<Contato> contatos; // Lista de contatos que será exibida
    private List<Contato> contatosFiltrados; // Lista de contatos filtrados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contatos);

        // Configura os componentes da interface
        listViewContatos = findViewById(R.id.lvContatos);
        edtBuscar = findViewById(R.id.edtBuscar);
        Button btnBuscar = findViewById(R.id.btnBuscar);

        // Obtém a lista de contatos passada pela MainActivity
        contatos = (List<Contato>) getIntent().getSerializableExtra("contatos");
        contatosFiltrados = new ArrayList<>(contatos); // Inicializa a lista filtrada com todos os contatos

        // Verifica se há contatos para exibir
        if (contatos != null && !contatos.isEmpty()) {
            // Configura o adaptador para exibir os contatos
            adapter = new ContatosAdapter(this, contatos, contatosFiltrados);
            listViewContatos.setAdapter(adapter);
        }

        // Implementação do botão de buscar
        btnBuscar.setOnClickListener(v -> realizarBusca());

        // Adicionando um TextWatcher para buscar enquanto digita
        edtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                realizarBusca();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Método para buscar contatos com base no termo de busca
    private void realizarBusca() {
        String termoBusca = edtBuscar.getText().toString().trim();
        List<Contato> contatosFiltradosTemp = buscarContato(termoBusca);

        // Atualiza a lista filtrada
        contatosFiltrados.clear();
        contatosFiltrados.addAll(contatosFiltradosTemp);

        // Notifica o adaptador para que ele saiba que a lista foi alterada
        adapter.notifyDataSetChanged();
    }

    // Método para buscar contatos com base no termo de busca
    private List<Contato> buscarContato(String termoBusca) {
        List<Contato> contatosFiltradosTemp = new ArrayList<>();
        for (Contato contato : contatos) {
            if (contato.getNome().contains(termoBusca) || contato.getTelefone().contains(termoBusca)) {
                contatosFiltradosTemp.add(contato);
            }
        }
        return contatosFiltradosTemp;
    }
}
