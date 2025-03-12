package com.example.gerenciadordecontatos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditarContatoActivity extends AppCompatActivity {

    private EditText edtNome, edtTelefone, edtEmail;
    private Button btnSalvar, btnCancelar;
    private Contato contato;
    private ContatoDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contato);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtEmail = findViewById(R.id.edtEmail);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);

        dbHelper = new ContatoDatabaseHelper(this);
        contato = (Contato) getIntent().getSerializableExtra("contato");

        if (contato == null) {
            Toast.makeText(this, "Erro ao carregar o contato", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtNome.setText(contato.getNome());
        edtTelefone.setText(contato.getTelefone());
        edtEmail.setText(contato.getEmail());

        btnSalvar.setOnClickListener(v -> {
            if (edtNome.getText().toString().isEmpty() || edtTelefone.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty()) {
                Toast.makeText(EditarContatoActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(EditarContatoActivity.this)
                    .setTitle("Confirmar Alteração")
                    .setMessage("Deseja realmente salvar as alterações desse contato?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        contato.setNome(edtNome.getText().toString());
                        contato.setTelefone(edtTelefone.getText().toString());
                        contato.setEmail(edtEmail.getText().toString());

                        try {
                            dbHelper.atualizarContato(contato);
                            Toast.makeText(EditarContatoActivity.this, "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show();

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("contato", contato);
                            setResult(RESULT_OK, resultIntent);
                        } catch (Exception e) {
                            Toast.makeText(EditarContatoActivity.this, "Erro ao atualizar o contato", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    })
                    .setNegativeButton("Não", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        btnCancelar.setOnClickListener(v -> finish());
    }

    private boolean isFormModified() {
        return !edtNome.getText().toString().equals(contato.getNome()) ||
                !edtTelefone.getText().toString().equals(contato.getTelefone()) ||
                !edtEmail.getText().toString().equals(contato.getEmail());
    }

    @Override
    public void onBackPressed() {
        if (isFormModified()) {
            new AlertDialog.Builder(this)
                    .setMessage("Você tem alterações não salvas. Deseja sair sem salvar?")
                    .setPositiveButton("Sim", (dialog, which) -> super.onBackPressed())
                    .setNegativeButton("Não", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }
}
