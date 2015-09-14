package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Cliente;
import com.example.asilva.bookbuy.dao.DAOCliente;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

public class CadastrarActivity extends AppCompatActivity implements View.OnClickListener {

    Button bttCadastrar;

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
    @Password(min = 6, scheme = Password.Scheme.NUMERIC, message = "Senha Inválida")
    EditText txtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtNome = (EditText)findViewById(R.id.txtNome);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtTelefone = (EditText)findViewById(R.id.txtTelefone);
        txtSenha = (EditText)findViewById(R.id.txtSenha);
        bttCadastrar = (Button)findViewById(R.id.bttCadastrar);
        bttCadastrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bttCadastrar:

                Cliente c = new Cliente();

                c.setUsuario(txtUsuario.getText().toString());
                c.setNome(txtNome.getText().toString());
                c.setEmail(txtEmail.getText().toString());
                c.setTelefone(txtTelefone.getText().toString());
                c.setSenha(txtSenha.getText().toString());

                DAOCliente clienteDAO = new DAOCliente();
                boolean resultado = clienteDAO.inserirCliente(c);

                if(resultado == true){
                    SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("usuario", c.getUsuario());
                    editor.putString("nome", c.getNome());
                    editor.putString("email", c.getEmail());
                    editor.putString("telefone", c.getTelefone());
                    editor.putBoolean("estalogado", true);

                    editor.commit();

                    Intent it = new Intent(this, MapaActivity.class);
                    startActivity(it);
                }

                Log.d("Resultado", resultado + "" + c.getEmail());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastrar, menu);
        return true;
    }
}
