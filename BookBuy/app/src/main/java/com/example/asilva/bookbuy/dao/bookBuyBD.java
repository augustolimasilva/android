package com.example.asilva.bookbuy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asilva.bookbuy.basicas.Usuario;

/**
 * Created by Airynne on 08/09/2015.
 */
public class bookBuyBD extends SQLiteOpenHelper {

    public static final String ENDERECO_PROVIDER = "";
    public static final String AUTHORITY = "";
    public static final String NOME_DO_BANCO = "";
    public static final int VERSAO_DO_BANCO = 1;

    public static final String TABELA_CADASTRO_CLIENTE = "clientes";
    public static final String CAMPO_ID = "_id";
    public static final String CAMPO_USUARIO = "usuario";
    public static final String CAMPO_NOME = "nome";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_TELEFONE = "";
    public static final String CAMPO_FOTO_PERFIL = "foto_perfil";

    public bookBuyBD(Context context){
        super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABELA_CADASTRO_CLIENTE + " (" +
                        CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        CAMPO_USUARIO + "TEXT NOT NULL" +
                        CAMPO_NOME + " TEXT NOT NULL UNIQUE," +
                        CAMPO_EMAIL + " TEXT," +
                        CAMPO_TELEFONE + " TEXT," +
                        CAMPO_FOTO_PERFIL + " TEXT,"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
