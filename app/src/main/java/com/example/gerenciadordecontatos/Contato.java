package com.example.gerenciadordecontatos;

import android.os.Parcel;
import android.os.Parcelable;

public class Contato implements Parcelable {
    private String nome;
    private String telefone;
    private String email;

    // Construtor
    public Contato(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Métodos para implementar Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(telefone);
        dest.writeString(email);
    }

    // Construtor que cria o objeto a partir do Parcel
    protected Contato(Parcel in) {
        nome = in.readString();
        telefone = in.readString();
        email = in.readString();
    }

    // Criador necessário para a classe Parcelable
    public static final Creator<Contato> CREATOR = new Creator<Contato>() {
        @Override
        public Contato createFromParcel(Parcel in) {
            return new Contato(in);
        }

        @Override
        public Contato[] newArray(int size) {
            return new Contato[size];
        }
    };
}
