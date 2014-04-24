package tools;


import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import application.Constants;
import models.Contact;

public class ContactsHelper {

    public static Bitmap getContactPhoto(Activity activity, long contactId) {
        Uri contactUri = ContentUris
                .withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri
                .withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = activity.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public static Contact getContact(Activity activity, Uri contactData) {
        Cursor c = activity.getContentResolver().query(contactData, null, null, null, null);

        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            long id = Long.parseLong(c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)));
            String photoName = String.valueOf(id); //unique id of photo get from contact_id
            Bitmap contactPhotoBitmap = getContactPhoto(activity, id);
            saveBitmapToInternalStorage(contactPhotoBitmap, photoName, activity);

            return new Contact(name, photoName);
        } else {
            return null;
        }
    }

    public static String saveBitmapToInternalStorage(Bitmap bitmapImage, String fileName,
            Context activity) {

        /*ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir*/
        File DIRECTORY_IMAGES = new ContextWrapper(activity.getApplicationContext())
                .getDir(Constants.PHOTOS_DIR_NAME, Context.MODE_PRIVATE);

        File myPath = new File(DIRECTORY_IMAGES, fileName);

        FileOutputStream fos;
        try {

            fos = new FileOutputStream(myPath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DIRECTORY_IMAGES.getAbsolutePath();
    }

    public static Bitmap loadBitmapFromStorage(String fileName, Context activity) {
        try {
            File DIRECTORY_IMAGES = new ContextWrapper(activity.getApplicationContext())
                    .getDir(Constants.PHOTOS_DIR_NAME, Context.MODE_PRIVATE);
            File f = new File(DIRECTORY_IMAGES.getAbsolutePath(), fileName);
            return BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }


}
