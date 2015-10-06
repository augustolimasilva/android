package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Cliente;
import com.example.asilva.bookbuy.dao.DAOCliente;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class CadastrarActivity extends AppCompatActivity implements View.OnClickListener {

    Button bttCadastrar;
    Cliente c;
    DAOCliente clienteDAO;

    @NotEmpty(message = "É necessário preencher este campo!")
    EditText txtUsuario;

    @NotEmpty(message = "É necessário preencher este campo!")
    EditText txtNome;

    @NotEmpty(message = "É necessário preencher este campo!")
    @Email(message = "Email inválido")
    EditText txtEmail;

    @NotEmpty(message = "É necessário preencher este campo!")
    EditText txtTelefone;

    @NotEmpty(message = "É necessário preencher este campo!")
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC, message = "Senha Inválida")
    EditText txtSenha;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        bttCadastrar = (Button) findViewById(R.id.bttCadastrar);
        bttCadastrar.setOnClickListener(this);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHanlder());
    }

    @Override
    public void onClick(View v) {
        validator.validate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastrar, menu);
        return true;
    }

    private class ValidationHanlder implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {
            c = new Cliente();

            c.setSituacao("ATIVO");
            c.setLogin(txtUsuario.getText().toString());
            c.setNome(txtNome.getText().toString());
            c.setEmail(txtEmail.getText().toString());
            c.setTelefone(txtTelefone.getText().toString());
            c.setSenha(txtSenha.getText().toString());

            clienteDAO = new DAOCliente();

            clienteDAO.pesquisarClientePorLogin(txtUsuario.getText().toString(), new ClienteListener() {
                @Override
                public void onLogin(Cliente cliente) {
                    if(cliente != null){
                        new MaterialDialog.Builder(CadastrarActivity.this)
                                .title("Cadastro")
                                .content("O usuário informado já está cadastrado.")
                                .positiveText("Ok").callback(new MaterialDialog.ButtonCallback() {

                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                dialog.dismiss();
                            }

                        }).build().show();
                    }
                }
            });

            clienteDAO.pesquisarClientePorEmail(txtEmail.getText().toString(), new ClienteListener() {
                @Override
                public void onLogin(Cliente cliente) {
                    if (cliente != null) {
                        new MaterialDialog.Builder(CadastrarActivity.this)
                                .title("Cadastro")
                                .content("O email informado já está cadastrado.")
                                .positiveText("Ok").callback(new MaterialDialog.ButtonCallback() {

                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                dialog.dismiss();
                            }

                        }).build().show();
                    } else{
                        boolean resultado = clienteDAO.inserirCliente(c);

                        if (resultado) {
                            Intent it = new Intent(CadastrarActivity.this, LoginActivity.class);
                            startActivity(it);
                        }
                    }
                }
            });
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {

            for (ValidationError error : errors) {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(CadastrarActivity.this);

                if (view instanceof EditText) {
                    ((EditText) view).setError(message);
                } else {
                    Toast.makeText(CadastrarActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
