package com.example.gerenciadordecontatos;

import android.content.Context;
import java.util.List;

public class GerenciadorDeContatos {

    private ContatoDatabaseHelper dbHelper;

    // Construtor da classe, inicializa o banco de dados
    public GerenciadorDeContatos(Context context) {
        dbHelper = new ContatoDatabaseHelper(context);  // Inicializa o helper do banco de dados
    }

    // Método para adicionar um contato
    public boolean adicionarContato(String nome, String telefone, String email) {
        // Verifica se o número já existe no banco de dados
        Contato contatoExistente = dbHelper.obterContatoPorTelefone(telefone);

        if (contatoExistente != null) {
            // Retorna falso se o número já estiver cadastrado
            return false;
        }

        // Se não existe, adiciona o novo contato
        Contato contato = new Contato(nome, telefone, email);
        dbHelper.adicionarContato(contato);
        return true;
    }


    // Método para editar um contato
    public boolean editarContato(int id, String nome, String telefone, String email) {
        Contato contato = new Contato(id, nome, telefone, email); // Passando 4 parâmetros para o construtor
        return dbHelper.atualizarContato(contato);  // Edita no banco de dados
    }

    // Método para remover um contato
    public boolean removerContato(int id) {
        return dbHelper.excluirContato(id);  // Remove do banco de dados
    }

    // Método para listar todos os contatos
    public List<Contato> getContatos() {
        return dbHelper.obterTodosContatos();  // Recupera todos os contatos do banco de dados
    }

    // Método para buscar contatos com base no termo de busca
    public List<Contato> buscarContato(String termoBusca) {
        return dbHelper.buscarContatos(termoBusca);  // Pesquisa no banco de dados
    }

    // Método para obter um contato específico pelo ID
    public Contato getContato(int id) {
        return dbHelper.obterContatoPorId(id);  // Busca no banco de dados pelo ID
    }

    // Método para limpar a lista de contatos (não aplicável no banco de dados, pois a exclusão é individual)
    public void limparContatos() {
        dbHelper.limparContatos();  // Limpa o banco de dados
    }
}
