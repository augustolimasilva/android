package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;

public class MinhaContaActivity extends ActionBarActivity implements View.OnClickListener{


    TextView txtNome;
    TextView txtEmail;
    TextView txtTelefone;
    Button bttEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);


        txtNome = (TextView)findViewById(R.id.txtNome);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtTelefone = (TextView)findViewById(R.id.txtTelefone);
        bttEditar = (Button)findViewById(R.id.bttEditar);
        bttEditar.setOnClickListener(this);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ea9533")));

        bar.setDisplayHomeAsUpEnabled(false);

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        String nome = prefs.getString("nome", "BookBuy");
        String email = prefs.getString("email", "bookbuy@email.com");
        String telefone = prefs.getString("telefone", "999999999");

        txtNome.setText(nome);
        txtEmail.setText(email);
        txtTelefone.setText(telefone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttEditar:
                Intent it = new Intent(this, EditarCadastroActivity.class);
                startActivity(it);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_minha_conta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
