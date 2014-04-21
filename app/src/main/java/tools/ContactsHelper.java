package tools;


import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.ByteArrayInputStream;

import activities.MainActivity;
import models.Contact;

public class ContactsHelper {

    public static Bitmap getContactPhoto(MainActivity activity, long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
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

    public static Contact getContact(MainActivity mainActivity, Uri contactData) {
        Cursor c = mainActivity.getContentResolver().query(contactData, null, null, null, null);

        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            long id = Long.parseLong(c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)));
            Bitmap contactPhoto = getContactPhoto(mainActivity, id);

            //TODO mock
            return new Contact(name, "photoPath/");
        } else
            return null;
    }
}
