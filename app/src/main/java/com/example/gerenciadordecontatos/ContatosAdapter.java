package com.example.gerenciadordecontatos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContatosAdapter extends BaseAdapter {

    private Context context;
    private List<Contato> contatos;
    private List<Contato> contatosFiltrados;

    public ContatosAdapter(Context context, List<Contato> contatos, List<Contato> contatosFiltrados) {
        this.context = context;
        this.contatos = contatos;
        this.contatosFiltrados = contatosFiltrados;
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_contato, parent, false);

            holder = new ViewHolder();
            holder.tvNome = convertView.findViewById(R.id.tvNome);
            holder.tvTelefone = convertView.findViewById(R.id.tvTelefone);
            holder.btnExcluir = convertView.findViewById(R.id.btnExcluir);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Obtém o contato filtrado atual
        Contato contato = contatosFiltrados.get(position);

        // Exibe os dados
        holder.tvNome.setText(contato.getNome());
        holder.tvTelefone.setText(contato.getTelefone());

        // Configura o botão de exclusão
        holder.btnExcluir.setOnClickListener(v -> {
            contatos.remove(contato); // Remove da lista original de contatos
            contatosFiltrados.remove(contato); // Remove da lista filtrada
            notifyDataSetChanged(); // Atualiza o adaptador
            Toast.makeText(context, "Contato excluído", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvNome;
        TextView tvTelefone;
        ImageButton btnExcluir;
    }
}
