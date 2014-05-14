package tools;


import com.activeandroid.query.Delete;

import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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

import adapters.ContactsAdapter;
import application.Constants;
import models.Contact;
import models.ContactInfo;

public class ContactsHelper {

    public static Bitmap getContactPhoto(Context context, long contactId) {
        Uri contactUri = ContentUris
                .withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri
                .withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
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

    public static Contact getContact(Context context, Uri contactData) {
        Cursor c = context.getContentResolver().query(contactData, null, null, null, null);

        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            long id = Long.parseLong(c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)));
            String photoName = String.valueOf(id); //unique id of photo get from contact_id
            Bitmap contactPhotoBitmap = getContactPhoto(context, id);
            saveBitmapToInternalStorage(contactPhotoBitmap, photoName, context);

            return new Contact(name, photoName);
        } else {
            return null;
        }
    }

    public static String saveBitmapToInternalStorage(Bitmap bitmapImage, String fileName,
            Context context) {

        /*ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir*/
        File DIRECTORY_IMAGES = new ContextWrapper(context.getApplicationContext())
                .getDir(Constants.PHOTOS_DIR_NAME, Context.MODE_PRIVATE);

        File myPath = new File(DIRECTORY_IMAGES, fileName);

        FileOutputStream fileOutputStream;
        try {

            fileOutputStream = new FileOutputStream(myPath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DIRECTORY_IMAGES.getAbsolutePath();
    }

    public static Bitmap loadBitmapFromStorage(String fileName, Context context) {
        try {
            File DIRECTORY_IMAGES = new ContextWrapper(context.getApplicationContext())
                    .getDir(Constants.PHOTOS_DIR_NAME, Context.MODE_PRIVATE);
            File fileContactPhoto = new File(DIRECTORY_IMAGES.getAbsolutePath(), fileName);
            return BitmapFactory.decodeStream(new FileInputStream(fileContactPhoto));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void removeContact(Contact longClickedItem, ContactsAdapter contactsAdapter) {
        longClickedItem.delete();
        deleteBitmapFromStorage(longClickedItem.getPhotoName(), contactsAdapter.getContext());
        //cascade delete
        new Delete().from(ContactInfo.class).where("Contact = ?", longClickedItem.getId())
                .execute();
        contactsAdapter.getList().remove(longClickedItem);
        contactsAdapter.notifyDataSetChanged();
    }

    public static boolean addContact(Contact contact, ContactsAdapter contactsAdapter) {

       /* if (contactsAdapter.getList().contains(contact)) {
            return false;
        }*/
        String contactName = contact.getName();
        for (int i = 0; i < 50; i++) {
            contact.setName(i
                    + " " + contactName);
            contactsAdapter.getList().add(contact);

            ContactInfo info = new ContactInfo("pin to phone", Integer.toString(i), contact);
            contact.save();
            info.save();
        }
        contactsAdapter.notifyDataSetChanged();
        return true;
    }

    public static void deleteBitmapFromStorage(String fileName, Context context) {
        File DIRECTORY_IMAGES = new ContextWrapper(context.getApplicationContext())
                .getDir(Constants.PHOTOS_DIR_NAME, Context.MODE_PRIVATE);
        File filePhoto = new File(DIRECTORY_IMAGES.getAbsolutePath(), fileName);
        filePhoto.delete();
    }

    public static int convertToPixels(int dps, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public static void shareContact(Contact contact, Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contact);
        sendIntent.setType("file/GrayCardinalContact");
        context.startActivity(sendIntent);
    }
}
