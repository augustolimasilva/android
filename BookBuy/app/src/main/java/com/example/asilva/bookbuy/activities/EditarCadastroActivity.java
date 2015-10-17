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

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Cliente;
import com.example.asilva.bookbuy.callbacks.CadastroClienteListener;
import com.example.asilva.bookbuy.dao.DAOCliente;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class EditarCadastroActivity extends AppCompatActivity implements View.OnClickListener{

    @NotEmpty(message = "É necessário preencher este campo!")
    @Length(trim = true)
    EditText editTextNome;

    EditText editTextEmail;

    @NotEmpty(message = "É necessário preencher este campo!")
    @Length(min = 10, max = 11, trim = true, message = "Tamanho inválido")
    EditText editTextTelefone;

    Button bttSalvarAlteracoes;

    private Validator validator;

    Cliente c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cadastro);

        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextEmail.setEnabled(false);
        editTextTelefone = (EditText)findViewById(R.id.editTextTelefone);
        bttSalvarAlteracoes = (Button)findViewById(R.id.bttSalvarAlteracoes);
        bttSalvarAlteracoes.setOnClickListener(this);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHanlder());

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        String nome = prefs.getString("nome", "BookBuy");
        String email = prefs.getString("email", "bookbuy@email.com");
        String telefone = prefs.getString("telefone", "999999999");

        editTextNome.setText(nome);
        editTextEmail.setText(email);
        editTextTelefone.setText(telefone);

        bttSalvarAlteracoes.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editar_cadastro, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        validator.validate();
    }

    private class ValidationHanlder implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {
            SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
            String usuario = prefs.getString("usuario", "usuario");
            String senha = prefs.getString("senha", "senha");
            int id = prefs.getInt("id", 1);

            c = new Cliente();
            c.setNome(editTextNome.getText().toString());
            c.setEmail(editTextEmail.getText().toString());
            c.setTelefone(editTextTelefone.getText().toString());
            c.setLogin(usuario);
            c.setId(id);
            c.setSenha(senha);
            c.setFotoPerfil(null);

            new DAOCliente().atualizarCliente(c, new CadastroClienteListener() {
                @Override
                public void onCliente(boolean retorno) {
                    if (retorno == true) {
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
                    } else {
                        Toast.makeText(EditarCadastroActivity.this, "Não foi possível atualizar seu cadastro. Tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {

            for (ValidationError error : errors) {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(EditarCadastroActivity.this);

                if (view instanceof EditText) {
                    ((EditText) view).setError(message);
                } else {
                    Toast.makeText(EditarCadastroActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
