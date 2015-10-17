package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.util.BitmapUtility;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MinhaContaActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtNome;
    TextView txtEmail;
    TextView txtTelefone;
    Button bttEditar;
    ImageView circle_image_view_photo;
    private static final String PHOTO = "Photo";
    private static final int REQUEST_CODE_PHOTO = 7000;
    private static final int REQUEST_CODE_CROP_PHOTO = 9000;
    private File tempFile;


    private File getTempFile() {

        if (tempFile == null) {

            try {

                tempFile = File.createTempFile(PHOTO,
                        ".png",
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

            } catch (final IOException e) {
                return null;
            }
        }

        return tempFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        txtNome = (TextView) findViewById(R.id.txtNome);

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        circle_image_view_photo = (CircleImageView) findViewById(R.id.circle_image_view_photo);
        txtTelefone = (TextView) findViewById(R.id.txtTelefone);
        bttEditar = (Button) findViewById(R.id.bttEditar);

        bttEditar.setOnClickListener(this);
        circle_image_view_photo.setOnClickListener(this);

        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        String nome = prefs.getString("nome", "BookBuy");
        String email = prefs.getString("email", "bookbuy@email.com");
        String telefone = prefs.getString("telefone", "999999999");

        txtNome.setText("Nome: " + nome);
        txtEmail.setText("Email: " + email);
        txtTelefone.setText("Telefone: " + telefone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttEditar:

                final Intent it = new Intent(this, EditarCadastroActivity.class);
                startActivity(it);
                break;

            case R.id.circle_image_view_photo:

                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));

                startActivityForResult(intent, REQUEST_CODE_PHOTO);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQUEST_CODE_PHOTO) {

            if (resultCode == RESULT_OK) {

                final Intent photoIntent = new Intent(this, ImageActivity.class);

                photoIntent.putExtra("Uri", Uri.fromFile(getTempFile()));

                startActivityForResult(photoIntent, REQUEST_CODE_CROP_PHOTO);
            }

        } else if (requestCode == REQUEST_CODE_CROP_PHOTO) {

            if (resultCode == RESULT_OK) {

                final Uri uri = intent.getParcelableExtra("Uri");

                final int width = circle_image_view_photo.getWidth();
                final int height = circle_image_view_photo.getHeight();

                circle_image_view_photo.setImageBitmap(BitmapUtility.scale(width, height, uri.getPath()));
            }
        }
    }
}
