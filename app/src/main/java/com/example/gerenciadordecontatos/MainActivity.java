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

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etNome, etTelefone, etEmail;
    private Button btnAdicionarContato, btnListarContatos;
    private ContatoDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNome = findViewById(R.id.etNome);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);
        btnAdicionarContato = findViewById(R.id.btnAdicionarContato);
        btnListarContatos = findViewById(R.id.btnListarContatos);

        dbHelper = new ContatoDatabaseHelper(this);

        // Adiciona o TextWatcher para formatar o telefone
        etTelefone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            private final String mask = "(##) #####-####";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                String str = s.toString().replaceAll("[^\\d]", "");
                String formatted = formatarTelefone(str);
                isUpdating = true;
                etTelefone.setText(formatted);
                etTelefone.setSelection(formatted.length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnAdicionarContato.setOnClickListener(v -> {
            String nome = etNome.getText().toString().trim();
            String telefone = etTelefone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else if (telefone.length() < 14) {
                Toast.makeText(MainActivity.this, "Número de telefone inválido!", Toast.LENGTH_SHORT).show();
            } else {
                // Verifica se o telefone já está cadastrado
                Contato contatoExistente = dbHelper.obterContatoPorTelefone(telefone);
                if (contatoExistente != null) {
                    Toast.makeText(MainActivity.this, "Número já cadastrado!", Toast.LENGTH_SHORT).show();
                } else {
                    Contato novoContato = new Contato(nome, telefone, email);
                    dbHelper.adicionarContato(novoContato);
                    Toast.makeText(MainActivity.this, "Contato salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                }
            }
        });

        btnListarContatos.setOnClickListener(v -> {
            List<Contato> contatos = dbHelper.obterTodosContatos();
            Intent intent = new Intent(MainActivity.this, ListarContatosActivity.class);
            intent.putExtra("contatos", (Serializable) contatos);
            startActivityForResult(intent, 1);  // Usando startActivityForResult para receber o resultado
        });
    }

    private void limparCampos() {
        etNome.setText("");
        etTelefone.setText("");
        etEmail.setText("");
    }

    private String formatarTelefone(String telefone) {
        String str = telefone.replaceAll("[^\\d]", "");
        StringBuilder formatted = new StringBuilder();
        String mask = "(##) #####-####";

        int i = 0;
        for (char m : mask.toCharArray()) {
            if (m != '#' && i < str.length()) {
                formatted.append(m);
            } else {
                try {
                    formatted.append(str.charAt(i));
                } catch (Exception e) {
                    break;
                }
                i++;
            }
        }
        return formatted.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Contato contatoAtualizado = (Contato) data.getSerializableExtra("contato");

            // Atualiza o contato no banco de dados
            dbHelper.atualizarContato(contatoAtualizado);

            // Atualiza a lista de contatos na interface
            List<Contato> contatosAtualizados = dbHelper.obterTodosContatos();
            ListView listView = findViewById(R.id.lvContatos); // Supondo que tenha esse ListView na MainActivity
            ContatosAdapter adapter = new ContatosAdapter(this, contatosAtualizados); // Atualiza o adaptador
            listView.setAdapter(adapter);

            Toast.makeText(this, "Contato atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }
}
