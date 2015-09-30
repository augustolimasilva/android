package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.Util;
import com.example.asilva.bookbuy.basicas.Cliente;
import com.example.asilva.bookbuy.dao.DAOCliente;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    @NotEmpty(message = "É necessário preencher este campo!")
    EditText txtUsuario;

    @NotEmpty(message = "É necessário preencher este campo!")
    @Password(min = 6, scheme = Password.Scheme.NUMERIC, message = "Senha Inválida")
    EditText txtSenha;

    TextView txtCadastrar, txtRecuperarSenha;
    Button bttEntrar;
    private static final String FACEBOOK_PERMISSION_PUBLIC_PROFILE = "public_profile";
    private final CallbackManager callbackManager = CallbackManager.Factory.create();
    Cliente cliente = new Cliente();

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHanlder());

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtCadastrar = (TextView) findViewById(R.id.txtCadastrar);
        txtCadastrar.setOnClickListener(this);
        txtRecuperarSenha = (TextView) findViewById(R.id.txtRecuperarSenha);
        txtRecuperarSenha.setOnClickListener(this);
        bttEntrar = (Button) findViewById(R.id.bttEntrar);
        bttEntrar.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        boolean jaLogou = prefs.getBoolean("estalogado", false);

        if (jaLogou) {
            Intent it = new Intent(this, MapaActivity.class);
            startActivity(it);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txtCadastrar:
                Intent it = new Intent(this, CadastrarActivity.class);
                startActivity(it);
                break;

            case R.id.txtRecuperarSenha:
                Intent intent = new Intent(this, RedefinirSenhaActivity.class);
                startActivity(intent);
                break;

            case R.id.bttEntrar:

                if (Util.isNetworkConnected(this)) {
                    new DAOCliente().pesquisarClientePorLogin(txtUsuario.getText().toString(), new ClienteListener() {

                        @Override
                        public void onLogin(final Cliente cliente) {

                            if (cliente == null) {
                                //Toast.makeText(getApplicationContext(), "Usuário não cadastrado.", Toast.LENGTH_SHORT).show();

                                new MaterialDialog.Builder(LoginActivity.this)
                                        .title("Usuário inválido!")
                                        .content("O usuário informado não existe.")
                                        .positiveText("Ok").callback(new MaterialDialog.ButtonCallback() {

                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        dialog.dismiss();
                                    }

                                }).build().show();

                            } else {

                                if (!cliente.getSenha().toString().equals(txtSenha.getText().toString())) {

                                    new MaterialDialog.Builder(LoginActivity.this)
                                            .title("Senha inválida!")
                                            .content("A senha digitada é inválida. Tente novamente!")
                                            .positiveText("Ok").callback(new MaterialDialog.ButtonCallback() {

                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            dialog.dismiss();
                                        }

                                    }).build().show();

                                } else {
                                    SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt("id", cliente.getId());
                                    editor.putString("usuario", cliente.getLogin());
                                    editor.putString("nome", cliente.getNome());
                                    editor.putString("email", cliente.getEmail());
                                    editor.putString("telefone", cliente.getTelefone());
                                    editor.putString("senha", cliente.getSenha());
                                    editor.putBoolean("estalogado", true);

                                    editor.commit();

                                    final Intent itMapa = new Intent(LoginActivity.this, MapaActivity.class);
                                    startActivity(itMapa);
                                }
                            }
                        }
                    });
                } else {

                    new MaterialDialog.Builder(LoginActivity.this)
                            .title("Alerta")
                            .content("Ative sua Internet")
                            .positiveText("Ok").callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                    }).build().show();
                }
        }
    }

    public void onLoginFacebook(View view) {
        FacebookSdk.sdkInitialize(this);

        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(this, Arrays.asList(FACEBOOK_PERMISSION_PUBLIC_PROFILE));
        loginManager.registerCallback(callbackManager, new FacebookHandler());
    }

    public class FacebookHandler implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ValidationHanlder implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {
            Toast.makeText(LoginActivity.this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {
            for (ValidationError error : errors) {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(LoginActivity.this);

                // Display error messages ;)
                if (view instanceof EditText) {
                    ((EditText) view).setError(message);
                } else {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
