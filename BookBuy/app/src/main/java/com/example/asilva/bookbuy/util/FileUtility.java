package com.example.asilva.bookbuy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Airynne on 12/10/2015.
 */
public class FileUtility {

    private static final String TAG = FileUtility.class.getSimpleName();

    private FileUtility() {

    }

    public static String save(final Context context, final String name, final String extension, final Bitmap bitmap) {

        final File file = new File(context.getFilesDir(), name + extension);

        try {

            final FileOutputStream output = new FileOutputStream(file);

            output.write(BitmapUtility.toByteArray(bitmap));

            output.close();

        } catch (final IOException e) {
            Log.d(TAG, e.getMessage());

            return null;
        }

        return file.getAbsolutePath();
    }
}
