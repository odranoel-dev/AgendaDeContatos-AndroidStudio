package com.example.gerenciadordecontatos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private GerenciadorDeContatos gerenciadorDeContatos;
    private EditText etNome, etTelefone, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando componentes de UI
        etNome = findViewById(R.id.etNome);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);

        Button btnAdicionarContato = findViewById(R.id.btnAdicionarContato);
        Button btnListarContatos = findViewById(R.id.btnListarContatos);

        // Inicializa o gerenciador de contatos com o contexto da MainActivity
        gerenciadorDeContatos = new GerenciadorDeContatos(this);

        // Configurando o botão de adicionar contato
        btnAdicionarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os dados inseridos no EditText
                String nome = etNome.getText().toString();
                String telefone = etTelefone.getText().toString();
                String email = etEmail.getText().toString();

                // Verifica se os campos não estão vazios
                if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                } else {
                    // Adiciona o contato
                    gerenciadorDeContatos.adicionarContato(nome, telefone, email);

                    // Exibe uma mensagem informando que o contato foi salvo
                    Toast.makeText(MainActivity.this, "Contato salvo com sucesso!", Toast.LENGTH_SHORT).show();

                    // Limpar os campos após salvar
                    etNome.setText("");
                    etTelefone.setText("");
                    etEmail.setText("");
                }
            }
        });

        // Configurando o botão de listar contatos
        btnListarContatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega para a tela de listar contatos, passando os dados de contatos
                Intent intent = new Intent(MainActivity.this, ListarContatosActivity.class);

                // Passa a lista de contatos, agora com a implementação de Serializable
                intent.putExtra("contatos", (Serializable) gerenciadorDeContatos.getContatos());

                startActivity(intent);
            }
        });
    }
}
