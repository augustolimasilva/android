package com.example.asilva.bookbuy.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.callbacks.EsqueceuSenhaListener;
import com.example.asilva.bookbuy.dao.DAOCliente;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class RedefinirSenhaActivity extends AppCompatActivity implements View.OnClickListener {

    @NotEmpty(message = "É necessário preencher este campo!")
    @Email(message = "Email inválido")
    TextView txtEmail;

    Button bttEnviarEmail;

    private Validator validator;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ea9533")));

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        bttEnviarEmail = (Button) findViewById(R.id.bttEnviar);
        bttEnviarEmail.setOnClickListener(this);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHanlder());
    }

    @Override
    public void onClick(View v) {
        validator.validate();
    }


    private class ValidationHanlder implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {
            email = txtEmail.getText().toString();

            new DAOCliente().esqueceuSenha(txtEmail.getText().toString(), new EsqueceuSenhaListener() {
                @Override
                public void esqueceuSenha(boolean retorno) {
                    if (retorno == true) {
                        Toast.makeText(getApplicationContext(), "Senha enviada para o email informado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Não foi encontrado cadastro para o email informado.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {
            for (ValidationError error : errors) {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(RedefinirSenhaActivity.this);

                if (view instanceof EditText) {
                    ((EditText) view).setError(message);
                } else {
                    Toast.makeText(RedefinirSenhaActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

