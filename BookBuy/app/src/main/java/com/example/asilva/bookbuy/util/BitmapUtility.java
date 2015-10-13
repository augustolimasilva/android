package com.example.asilva.bookbuy.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Airynne on 12/10/2015.
 */
public final class BitmapUtility {

    public static final int QUALITY = 100;

    private BitmapUtility() {

    }

    public static byte[] toByteArray(final Bitmap bitmap) {

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

        bitmap.recycle();

        return output.toByteArray();
    }

    public static Bitmap scale(final int width, final int height, final String path) {

        final BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, option);

        option.inSampleSize = Math.min(option.outWidth / width, option.outHeight / height);

        option.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, option);
    }
}
