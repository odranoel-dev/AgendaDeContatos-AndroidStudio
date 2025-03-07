package com.example.gerenciadordecontatos;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeContatos {

    private List<Contato> contatos;

    // Construtor da classe, inicializa a lista de contatos
    public GerenciadorDeContatos(Context context) {
        contatos = new ArrayList<>();
        // Aqui você pode inicializar a lista de contatos com dados preexistentes se necessário
    }

    // Método para adicionar um contato
    public void adicionarContato(String nome, String telefone, String email) {
        Contato contato = new Contato(nome, telefone, email);
        contatos.add(contato);
    }

    // Método para editar um contato
    public boolean editarContato(int position, String nome, String telefone, String email) {
        if (position >= 0 && position < contatos.size()) {
            Contato contato = contatos.get(position);
            contato.setNome(nome);
            contato.setTelefone(telefone);
            contato.setEmail(email);
            return true;
        }
        return false;
    }

    // Método para remover um contato
    public boolean removerContato(int position) {
        if (position >= 0 && position < contatos.size()) {
            contatos.remove(position);
            return true;
        }
        return false;
    }

    // Método para listar todos os contatos
    public List<Contato> getContatos() {
        return contatos;
    }


// Método para buscar contatos com base no termo de busca
    public List<Contato> buscarContato(String termoBusca) {
        List<Contato> resultado = new ArrayList<>();

        // Se o termo de busca não for nulo ou vazio, faz a busca
        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            String termoBuscaLower = termoBusca.toLowerCase(); // Transformar o termo de busca para minúsculas
            for (Contato contato : contatos) {
                // Verifica se o nome ou telefone contém o termo de busca, ignorando maiúsculas/minúsculas
                if (contato.getNome().toLowerCase().contains(termoBuscaLower) ||
                        contato.getTelefone().toLowerCase().contains(termoBuscaLower)) {
                    resultado.add(contato);
                }
            }
        }

        return resultado;
    }


    // Método para obter um contato específico pelo índice
    public Contato getContato(int position) {
        if (position >= 0 && position < contatos.size()) {
            return contatos.get(position);
        }
        return null;
    }

    // Método para limpar a lista de contatos
    public void limparContatos() {
        contatos.clear();
    }


}
