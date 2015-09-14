package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextTelefone = (EditText)findViewById(R.id.editTextTelefone);
        bttSalvarAlteracoes = (Button)findViewById(R.id.bttSalvarAlteracoes);
        bttSalvarAlteracoes.setOnClickListener(this);

        bttSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DAOCliente clienteDAO = new DAOCliente();

                boolean cliente = clienteDAO.atualizarCliente(new Cliente());

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
