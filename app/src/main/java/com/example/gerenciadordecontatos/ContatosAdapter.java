package com.example.gerenciadordecontatos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

public class ContatosAdapter extends BaseAdapter {

    private Context context;
    private List<Contato> contatos;
    private List<Contato> contatosFiltrados;

    public ContatosAdapter(Context context, List<Contato> contatos) {
        this.context = context;
        this.contatos = contatos;
        this.contatosFiltrados = new ArrayList<>(contatos);
    }

    @Override
    public int getCount() {
        return contatosFiltrados.size();
    }

    @Override
    public Object getItem(int position) {
        return contatosFiltrados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_contato, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contato contato = contatosFiltrados.get(position);
        holder.bind(contato);

        holder.btnExcluir.setOnClickListener(v -> mostrarConfirmacaoDeExclusao(contato));
        holder.btnEditar.setOnClickListener(v -> editarContato(contato));

        return convertView;
    }

    private void mostrarConfirmacaoDeExclusao(Contato contato) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmar Exclusão")
                .setMessage("Deseja realmente excluir o contato?")
                .setPositiveButton("Sim", (dialog, which) -> excluirContato(contato))
                .setNegativeButton("Não", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void excluirContato(Contato contato) {
        ContatoDatabaseHelper dbHelper = new ContatoDatabaseHelper(context);
        boolean excluido;

        try {
            excluido = dbHelper.excluirContato(contato.getId());
        } finally {
            dbHelper.close();
        }

        if (excluido) {
            contatos.remove(contato);
            contatosFiltrados.remove(contato);
            notifyDataSetChanged();
            Toast.makeText(context, "Contato excluído", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Erro ao excluir contato", Toast.LENGTH_SHORT).show();
        }
    }

    private void editarContato(Contato contato) {
        Intent intent = new Intent(context, EditarContatoActivity.class);
        intent.putExtra("contato", contato);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    // Método para atualizar a lista de contatos após a edição
    public void atualizarContatoNaLista(Contato contatoAtualizado) {
        for (int i = 0; i < contatos.size(); i++) {
            if (contatos.get(i).getId() == contatoAtualizado.getId()) {
                contatos.set(i, contatoAtualizado);  // Substitui o contato na lista principal
                break;
            }
        }

        // Atualiza a lista filtrada também
        for (int i = 0; i < contatosFiltrados.size(); i++) {
            if (contatosFiltrados.get(i).getId() == contatoAtualizado.getId()) {
                contatosFiltrados.set(i, contatoAtualizado);  // Substitui o contato na lista filtrada
                break;
            }
        }

        notifyDataSetChanged();  // Notifica o adaptador de que os dados foram alterados
    }

    public void filtrarContatos(String termoBusca) {
        if (termoBusca == null || termoBusca.isEmpty()) {
            contatosFiltrados.clear();
            contatosFiltrados.addAll(contatos);
        } else {
            List<Contato> tempList = new ArrayList<>();
            for (Contato contato : contatos) {
                if (contato.getNome().toLowerCase().contains(termoBusca.toLowerCase()) ||
                        contato.getTelefone().contains(termoBusca) ||
                        contato.getEmail().toLowerCase().contains(termoBusca.toLowerCase())) {
                    tempList.add(contato);
                }
            }
            contatosFiltrados.clear();
            contatosFiltrados.addAll(tempList);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvNome, tvTelefone, tvEmail;
        ImageButton btnExcluir, btnEditar;

        ViewHolder(View view) {
            tvNome = view.findViewById(R.id.tvNome);
            tvTelefone = view.findViewById(R.id.tvTelefone);
            tvEmail = view.findViewById(R.id.tvEmail);
            btnExcluir = view.findViewById(R.id.btnExcluir);
            btnEditar = view.findViewById(R.id.btnEditar);
        }

        void bind(Contato contato) {
            tvNome.setText(contato.getNome());
            tvTelefone.setText(contato.getTelefone());
            tvEmail.setText(contato.getEmail());
        }
    }
}
