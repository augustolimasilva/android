package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    TextView txtCadastrar;
    TextView txtRecuperarSenha;
    Button bttEntrar;
    private static final String FACEBOOK_PERMISSION_PUBLIC_PROFILE = "public_profile";
    private final CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCadastrar = (TextView) findViewById(R.id.txtCadastrar);
        txtCadastrar.setOnClickListener(this);
        txtRecuperarSenha = (TextView)findViewById(R.id.txtRecuperarSenha);
        txtRecuperarSenha.setOnClickListener(this);
        bttEntrar = (Button)findViewById(R.id.bttEntrar);
        bttEntrar.setOnClickListener(this);

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
                    Intent itMapa = new Intent(this, MapaActivity.class);
                    startActivity(itMapa);
                    break;

            }
    }

    public void onLoginFacebook(View view){
        FacebookSdk.sdkInitialize(this);

        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(this, Arrays.asList(FACEBOOK_PERMISSION_PUBLIC_PROFILE));
        loginManager.registerCallback(callbackManager, new FacebookHandler());
    }

    public class FacebookHandler implements FacebookCallback<LoginResult>{
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
}