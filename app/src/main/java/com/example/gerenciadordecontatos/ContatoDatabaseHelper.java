package com.example.gerenciadordecontatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;

public class ContatoDatabaseHelper extends SQLiteOpenHelper {

    // Nome e versão do banco de dados
    private static final String DATABASE_NAME = "contatos.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela Contatos
    private static final String TABLE_CONTATOS = "contatos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_TELEFONE = "telefone";
    private static final String COLUMN_EMAIL = "email";

    public ContatoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criar tabela de contatos
        String CREATE_TABLE_CONTATOS = "CREATE TABLE " + TABLE_CONTATOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT, " +
                COLUMN_TELEFONE + " TEXT UNIQUE, " +  // Define o telefone como único
                COLUMN_EMAIL + " TEXT)";
        db.execSQL(CREATE_TABLE_CONTATOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualiza o banco de dados quando a versão mudar
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTATOS);
        onCreate(db);
    }

    // Método para inserir um contato no banco de dados com verificação de duplicação
    public boolean adicionarContato(Contato contato) {
        if (obterContatoPorTelefone(contato.getTelefone()) != null) {
            return false; // Retorna falso se o telefone já estiver cadastrado
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, contato.getNome());
        values.put(COLUMN_TELEFONE, contato.getTelefone());
        values.put(COLUMN_EMAIL, contato.getEmail());

        long resultado = db.insert(TABLE_CONTATOS, null, values);
        db.close();

        return resultado != -1; // Retorna true se a inserção foi bem-sucedida
    }

    // Método para buscar um contato pelo telefone
    public Contato obterContatoPorTelefone(String telefone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTATOS, null, COLUMN_TELEFONE + " = ?", new String[]{telefone}, null, null, null);

        Contato contato = null;
        if (cursor != null && cursor.moveToFirst()) {
            contato = new Contato(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            );
            cursor.close();
        }
        db.close();
        return contato; // Retorna null se não encontrou o contato
    }

    // Método para atualizar um contato
    public boolean atualizarContato(Contato contato) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, contato.getNome());
        values.put(COLUMN_TELEFONE, contato.getTelefone());
        values.put(COLUMN_EMAIL, contato.getEmail());

        int rowsAffected = db.update(TABLE_CONTATOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(contato.getId())});
        db.close();

        return rowsAffected > 0;
    }

    // Método para recuperar todos os contatos
    public ArrayList<Contato> obterTodosContatos() {
        ArrayList<Contato> listaContatos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTATOS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Contato contato = new Contato(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                );
                listaContatos.add(contato);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return listaContatos;
    }

    // Método para excluir um contato
    public boolean excluirContato(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CONTATOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Método para buscar contatos com base em um termo (nome, telefone ou e-mail)
    public ArrayList<Contato> buscarContatos(String termoBusca) {
        ArrayList<Contato> listaContatos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CONTATOS + " WHERE " +
                COLUMN_NOME + " LIKE ? OR " + COLUMN_TELEFONE + " LIKE ? OR " + COLUMN_EMAIL + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + termoBusca + "%", "%" + termoBusca + "%", "%" + termoBusca + "%"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Contato contato = new Contato(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                );
                listaContatos.add(contato);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return listaContatos;
    }

    // Método para obter um contato específico pelo ID
    public Contato obterContatoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTATOS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        Contato contato = null;
        if (cursor != null && cursor.moveToFirst()) {
            contato = new Contato(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            );
            cursor.close();
        }
        db.close();
        return contato;
    }

    // Método para limpar todos os contatos
    public void limparContatos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTATOS, null, null);
        db.close();
    }
}
