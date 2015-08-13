package com.example.asilva.bookbuy;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtCadastrar;
    TextView txtRecuperarSenha;
    Button bttEntrar;

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
