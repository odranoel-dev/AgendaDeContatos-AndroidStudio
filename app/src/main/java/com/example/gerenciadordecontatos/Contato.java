package com.example.gerenciadordecontatos;

import java.io.Serializable;

public class Contato implements Serializable {
    private static final long serialVersionUID = 1L;  // ID de versão para garantir a compatibilidade

    private String nome;
    private String telefone;
    private String email;
    private int id;  // Novo campo para o ID

    // Construtor com ID (para quando já tiver o ID, como na edição)
    public Contato(int id, String nome, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    // Construtor sem ID (para quando adicionar um novo contato, o ID será atribuído automaticamente pelo banco)
    public Contato(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {  // Método getter para o ID
        return id;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {  // Método setter para o ID
        this.id = id;
    }

    // Método toString para facilitar a visualização do contato
    @Override
    public String toString() {
        return "Contato{id=" + id + ", nome='" + nome + "', telefone='" + telefone + "', email='" + email + "'}";
    }
}
