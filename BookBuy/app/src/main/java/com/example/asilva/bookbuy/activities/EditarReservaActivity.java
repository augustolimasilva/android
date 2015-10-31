package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Reserva;
import com.example.asilva.bookbuy.callbacks.EfetuarReservaListener;
import com.example.asilva.bookbuy.dao.DAOReserva;
import com.example.asilva.bookbuy.fragments.MinhasReservasFragment;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class EditarReservaActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTxtNomeRestaurante, editTxtDataHora, editTxtNomeCliente;

    @NotEmpty(message = "É necessário preencher este campo!", trim = true)
    @Length(min = 1, max = 2, trim = true, message = "Valor inválido")
    EditText editTxtQtdPessoas;

    Button bttSalvarAlteracoes;
    SharedPreferences prefs2;
    String nomeCliente, nomeRestaurante, dataHora, status, situacao;
    int idReserva, idCliente, idRestaurante, qtdPessoas;
    Reserva res;
    private Validator validator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_reserva);

        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);

        editTxtNomeRestaurante = (EditText) findViewById(R.id.editTxtNomeRestaurante);
        editTxtDataHora = (EditText) findViewById(R.id.editTxtDataHora);
        editTxtNomeCliente = (EditText) findViewById(R.id.editTxtNomeCliente);
        editTxtQtdPessoas = (EditText) findViewById(R.id.editTxtQtdPessoas);
        bttSalvarAlteracoes = (Button) findViewById(R.id.bttSalvarAlteracoes);
        bttSalvarAlteracoes.setOnClickListener(this);

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        nomeCliente = prefs.getString("nome", "BookBuy");

        prefs2 = getSharedPreferences("dados_reserva", 0);
        dataHora = prefs2.getString("dataHora", "teste");
        status = prefs2.getString("status", "teste");
        situacao = prefs2.getString("situacao", "teste");
        idReserva = prefs2.getInt("idReserva", 1);
        idCliente = prefs2.getInt("idCliente", 1);
        idRestaurante = prefs2.getInt("idRestaurante", 1);
        nomeRestaurante = prefs2.getString("nomeRestaurante", "teste");
        qtdPessoas = prefs2.getInt("qtdPessoas", 1);

        editTxtNomeRestaurante.setText(nomeRestaurante);
        editTxtNomeCliente.setText(nomeCliente);
        editTxtDataHora.setText(dataHora.substring(8, 10) + "-" + dataHora.substring(5, 7) + "-" + dataHora.substring(0, 4) +
                " " + dataHora.substring(11, 13) + ":" + dataHora.substring(14, 16));
        editTxtQtdPessoas.setText(String.valueOf(qtdPessoas));

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHanlder());

    }

    public void onClick(View v) {
        validator.validate();
    }

    private class ValidationHanlder implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {

            if (Integer.parseInt(editTxtQtdPessoas.getText().toString()) == 0 || Integer.parseInt(editTxtQtdPessoas.getText().toString()) < 0 || Integer.parseInt(editTxtQtdPessoas.getText().toString()) > 20) {
                Toast.makeText(EditarReservaActivity.this, "Preencha o campo entre 1 a 20 pessoas.", Toast.LENGTH_SHORT).show();
            } else {
                res = new Reserva();
                res.setIdRestaurante(idRestaurante);
                res.setStatus(status);
                res.setIdCliente(idCliente);
                res.setQtdPessoas(Integer.parseInt(editTxtQtdPessoas.getText().toString()));
                res.setDataHora(dataHora);
                res.setIdReserva(idReserva);
                res.setSituacao(situacao);

                new DAOReserva().atualizarReserva(res, new EfetuarReservaListener() {
                    @Override
                    public void atualizarReserva(boolean retorno) {
                        if (retorno == true) {

                            SharedPreferences.Editor prefes = getSharedPreferences("dados_reserva", 0).edit();
                            prefes.clear();
                            prefes.commit();

                            Toast.makeText(EditarReservaActivity.this, "Reserva Atualizada.", Toast.LENGTH_SHORT).show();

                            Intent it = new Intent(EditarReservaActivity.this, MinhasComprasActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        } else {
                            Toast.makeText(EditarReservaActivity.this, "Não foi possível atualizar a reserva. Tente Novamente!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {

            for (ValidationError error : errors) {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(EditarReservaActivity.this);

                if (view instanceof EditText) {
                    ((EditText) view).setError(message);
                } else {
                    Toast.makeText(EditarReservaActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        SharedPreferences.Editor prefs = getSharedPreferences("dados_reserva", 0).edit();
        prefs.clear();
        prefs.commit();

        Intent it = new Intent(this, MinhasComprasActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }
}

