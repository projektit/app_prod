package com.grupp3.projekt_it;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Marcus on 2015-04-29.
 * When storing pictures in SQLite database
 * One might need to use the BLOB type, binary large object
 * that stores binary data.
 * getBytes convert pic from URL to a byte array to store pic in database
 * getImage converts the byte array back to an image
 */
public class ImageUtilities {

    //convert from bitmap to byte array method
    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
