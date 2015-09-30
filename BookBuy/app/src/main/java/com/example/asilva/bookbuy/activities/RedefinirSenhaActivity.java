package com.example.asilva.bookbuy.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asilva.bookbuy.GmailSender;
import com.example.asilva.bookbuy.R;

public class RedefinirSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ea9533")));

        final TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
        Button enviarEmail = (Button)findViewById(R.id.bttEnviar);
        enviarEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    GmailSender sender = new GmailSender("bookbuysystem@gmail.com", "a123456*");
                    sender.sendMail("Teste",
                            "This is Body",
                            "augusto_ls17@hotmail.com",
                            "augustolimasilva1993@gmail.com");

                    Log.e("SendMail", "Email Enviado");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }

            }
        });

    }
}
