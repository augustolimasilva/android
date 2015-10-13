package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.edmodo.cropper.CropImageView;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.util.BitmapUtility;
import com.example.asilva.bookbuy.util.FileUtility;

/**
 * Created by Airynne on 12/10/2015.
 */
public class ImageActivity extends AppCompatActivity {

    private static final String TAG = ImageActivity.class.getSimpleName();
    public static final String URI = "Uri";

    private static final String IMAGE = "Image";

    private CropImageView cropImageView;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.image);

        cropImageView = (CropImageView) findViewById(R.id.crop_image_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image, menu);

        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus) {

            final Uri uri = getIntent().getParcelableExtra("Uri");

            if (uri == null) {
                throw new IllegalStateException("The uri is null.");
            }

            final int width = cropImageView.getWidth();
            final int height = cropImageView.getHeight();

            cropImageView.setImageBitmap(BitmapUtility.scale(width, height, uri.getPath()));
        }
    }

    public void onSave(final MenuItem menuItem) {

        final String path = FileUtility.save(this,
                IMAGE,
                ".png",
                cropImageView.getCroppedImage());

        final Intent intent = new Intent();

        intent.putExtra("Uri", Uri.parse(path));

        setResult(RESULT_OK, intent);

        finish();
    }
}
