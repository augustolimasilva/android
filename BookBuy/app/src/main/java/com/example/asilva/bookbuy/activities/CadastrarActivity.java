package com.example.asilva.bookbuy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Cliente;
import com.example.asilva.bookbuy.dao.DAOCliente;

public class CadastrarActivity extends AppCompatActivity {

    Button bttCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        bttCadastrar = (Button)findViewById(R.id.bttCadastrar);

        bttCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente c = new Cliente();

                c.setId(2);
                c.setUsuario("teste");
                c.setNome("teste");
                c.setEmail("teste");
                c.setTelefone("teste");
                c.setSenha("teste");

                DAOCliente clienteDAO = new DAOCliente();
                boolean resultado = clienteDAO.inserirCliente(c);

                Log.d("Resultado", resultado + "" + c.getEmail());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastrar, menu);
        return true;
    }
}
