package activities;

import com.google.gson.Gson;

import com.tieorange.pember.app.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import application.Constants;
import fragments.InfoListFragment;
import models.Contact;
import models.ContactInfo;
import models.SerializableContact;
import models.SerializableContactInfo;
import tools.ContactsHelper;


public class InfoActivity extends ActionBarActivity {

    public static final String LOG_TAG = InfoActivity.class.getSimpleName();
    public static Contact mContact;
    public static List<ContactInfo> mInfoList = new ArrayList<ContactInfo>();
    private ShareActionProvider mShareActionProvider;

    public static byte[] getSerializedObject(Serializable s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        String loggerTag = "Info";
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
        } catch (IOException e) {
            Log.e(loggerTag, e.getMessage(), e);
            return null;
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
        byte[] result = baos.toByteArray();
        Log.d(loggerTag,
                "Object " + s.getClass().getSimpleName() + " written to byte[]: "
                        + result.length
        );
        return result;
    }

    public static Object readSerializedObject(byte[] in) {
        Object result = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(in);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            result = ois.readObject();
        } catch (Exception e) {
            result = null;
        } finally {
            try {
                ois.close();
            } catch (Throwable e) {
            }
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getExtras(savedInstanceState);
        initViews();
    }

    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mContact.getName());

        setContactPhotoToActionBar();

        mInfoList = mContact.infoList();
    }

    private void setContactPhotoToActionBar() {
        if (mContact.getPhotoName() == null) {
            return;
        }

        BitmapDrawable contactPhotoDrawable = new BitmapDrawable(getResources(),
                ContactsHelper.loadBitmapFromStorage(mContact.getPhotoName(), this));
        if (contactPhotoDrawable.getBitmap() != null) {
            getSupportActionBar().setIcon(contactPhotoDrawable);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // In a function that captures when a user navigates away from article

    }

    private void getExtras(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mContact = null;
            } else {
                Long id = extras.getLong(Constants.EXTRAS_CONTACT_ID);
                mContact = Contact.load(Contact.class, id);
            }
        } else {
            Long id = savedInstanceState.getLong(Constants.EXTRAS_CONTACT_ID);
            mContact = Contact.load(Contact.class, id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);

        android.view.MenuItem searchMenuItem = menu.findItem(R.id.info_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                InfoListFragment.mAdapter.getFilter().filter(s);
                return false;
            }
        });

        //share
        /*MenuItem item = menu.findItem(R.id.info_menu_search);// Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                this.finish();
                break;
            case R.id.inf_menu_item_share:
                Gson gsonContact = new Gson();
                String jsonContact = gsonContact.toJson(createSerialContact(mContact));
                Log.d(LOG_TAG, jsonContact);

                File file = null;
                try {
                    file = getCreatedSharedContactFile(jsonContact, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType(getString(R.string.share_contact_type_intent));

                // Verify the intent will resolve to at least one activity
                /*if (shareIntent.resolveActivity(getPackageManager()) != null) {*/
                startActivity(Intent.createChooser(shareIntent,
                        getResources().getText(R.string.send_to)));
                //}

                break;
            /*case R.id.action_add_info:
                Intent intent = new Intent(InfoActivity.this, AddInfoActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_INFO);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public File getCreatedSharedContactFile(String text, Context context) throws IOException {
        final String fileExtension = getString(R.string.extension_contact_file);
        final String fileName = getString(R.string.app_name) + "SharedFile"
                + fileExtension; //PemberSharedFile.pember
        File file = new
                File(ContactsHelper.getExternalCacheDir(context)
                + File.separator + fileName);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(text);
        bufferedWriter.close();
        return file;
    }


    public SerializableContact createSerialContact(Contact contact) {
        SerializableContact serialContact;
        List<SerializableContactInfo> serialContacts = new ArrayList<SerializableContactInfo>();

        SerializableContactInfo tmpInfo;
        for (ContactInfo info : contact.infoList()) {
            tmpInfo = new SerializableContactInfo(
                    info.getName(), info.getValue());
            serialContacts.add(tmpInfo);
        }

        String contactName = contact.getName();
        Bitmap contactBitmap = ContactsHelper.loadBitmapFromStorage(contact.getPhotoName(), this);
        serialContact = new SerializableContact(contactName, contactBitmap, serialContacts);

        return serialContact;
    }

    public File createFile(String text) {
        String filename = "mysecondfile.txt";
        File myDir = getFilesDir();

        File secondFile = null;
        try {
            secondFile = new File(myDir + "/text/", filename);
            if (secondFile.getParentFile().mkdirs()) {
                secondFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(secondFile);

                fos.write(text.getBytes());
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secondFile;
    }

}
