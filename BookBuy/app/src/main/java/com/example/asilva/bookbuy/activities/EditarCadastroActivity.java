package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Cliente;
import com.example.asilva.bookbuy.dao.DAOCliente;

public class EditarCadastroActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextNome;
    EditText editTextEmail;
    EditText editTextTelefone;
    Button bttSalvarAlteracoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cadastro);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ea9533")));

        bar.setDisplayHomeAsUpEnabled(false);

        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextEmail.setEnabled(false);
        editTextTelefone = (EditText)findViewById(R.id.editTextTelefone);
        bttSalvarAlteracoes = (Button)findViewById(R.id.bttSalvarAlteracoes);
        bttSalvarAlteracoes.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        String nome = prefs.getString("nome", "BookBuy");
        String email = prefs.getString("email", "bookbuy@email.com");
        String telefone = prefs.getString("telefone", "999999999");

        editTextNome.setText(nome);
        editTextEmail.setText(email);
        editTextTelefone.setText(telefone);

        bttSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
                String usuario = prefs.getString("usuario", "usuario");
                String senha = prefs.getString("senha", "senha");
                int id = prefs.getInt("id", 1);

                DAOCliente clienteDAO = new DAOCliente();
                Cliente c = new Cliente();
                c.setNome(editTextNome.getText().toString());
                c.setEmail(editTextEmail.getText().toString());
                c.setTelefone(editTextTelefone.getText().toString());
                c.setLogin(usuario);
                c.setId(id);
                c.setSenha(senha);
                c.setFotoPerfil(null);

                boolean cliente = clienteDAO.atualizarCliente(c);

                if (cliente){
                    SharedPreferences.Editor prefes = getSharedPreferences("meu_dados", 0).edit();
                    prefes.clear();
                    prefes.commit();

                    SharedPreferences pref = getSharedPreferences("meus_dados", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("usuario", c.getLogin());
                    editor.putString("nome", c.getNome());
                    editor.putString("email", c.getEmail());
                    editor.putString("telefone", c.getTelefone());
                    editor.putInt("id", c.getId());
                    editor.putString("senha", c.getSenha());
                    editor.putBoolean("estalogado", true);

                    editor.commit();

                    Toast.makeText(EditarCadastroActivity.this, "Cadastro Atualizado.", Toast.LENGTH_SHORT).show();

                    Intent it = new Intent(EditarCadastroActivity.this, MinhaContaActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                }

                Log.d("Resultado", cliente + "");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_cadastro, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttSalvarAlteracoes:
                break;
        }
    }
}
