package com.example.gerenciadordecontatos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListarContatosActivity extends AppCompatActivity {

    private ListView listViewContatos;
    private EditText edtBuscar;
    private ContatosAdapter adapter;
    private List<Contato> contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contatos);

        listViewContatos = findViewById(R.id.lvContatos);
        edtBuscar = findViewById(R.id.edtBuscar);
        Button btnBuscar = findViewById(R.id.btnBuscar);

        contatos = (List<Contato>) getIntent().getSerializableExtra("contatos");

        if (contatos != null && !contatos.isEmpty()) {
            // Ordenando a lista de contatos por nome antes de setar no adapter
            ordenarContatosPorNome(contatos);

            adapter = new ContatosAdapter(this, contatos);
            listViewContatos.setAdapter(adapter);

            // Define o comportamento ao clicar em um contato da lista para editar
            listViewContatos.setOnItemClickListener((parent, view, position, id) -> {
                Contato contatoSelecionado = contatos.get(position);
                Intent intent = new Intent(ListarContatosActivity.this, EditarContatoActivity.class);
                intent.putExtra("contato", contatoSelecionado);
                startActivityForResult(intent, 1); // Inicia a atividade de edição com um código de requisição
            });
        }

        btnBuscar.setOnClickListener(v -> realizarBusca());

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

    private void realizarBusca() {
        String termoBusca = edtBuscar.getText().toString().trim();
        adapter.filtrarContatos(termoBusca);
    }

    // Método para ordenar a lista de contatos pelo nome
    private void ordenarContatosPorNome(List<Contato> contatos) {
        Collections.sort(contatos, (c1, c2) -> c1.getNome().compareTo(c2.getNome()));
    }

    // Método que lida com o resultado da edição de um contato
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Obtém o contato atualizado
            Contato contatoAtualizado = (Contato) data.getSerializableExtra("contato");

            // Atualiza o contato na lista
            for (int i = 0; i < contatos.size(); i++) {
                if (contatos.get(i).getTelefone().equals(contatoAtualizado.getTelefone())) {
                    contatos.set(i, contatoAtualizado);  // Substitui o contato antigo pelo atualizado
                    break;
                }
            }

            // Notifica o adaptador sobre a atualização da lista
            adapter.notifyDataSetChanged();

            // Atualiza a lista de contatos visualmente
            listViewContatos.invalidateViews();

            // Exibe a mensagem de sucesso
            Toast.makeText(ListarContatosActivity.this, "Contato atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

}
