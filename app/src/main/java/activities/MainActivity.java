package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.tieorange.graycardinal.app.R;

import java.util.ArrayList;

import adapters.ContactsAdapter;
import application.Constants;
import models.Contact;
import tools.ContactsHelper;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Contact> mContactsList = new ArrayList<Contact>();
    private ListView mUiMyListView;
    private ContactsAdapter mContactsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    private void startContactsIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.PICK_CONTACT);
    }

    private void initViews() {
        mUiMyListView = (ListView) findViewById(R.id.main_contacts_list);

        mContactsAdapter = new ContactsAdapter(this, mContactsList);

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mContactsAdapter);
        scaleInAnimationAdapter.setAbsListView(mUiMyListView);
        mUiMyListView.setAdapter(scaleInAnimationAdapter);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (Constants.PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();

                    Contact contact = ContactsHelper.getContact(this, contactData);
                    addContactToList(contact);
                }
                break;
        }
    }

    private void addContactToList(Contact contact) {
        for (int i =0 ; i < 200; i++)
        mContactsList.add(contact);

        mContactsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.action_add_contact) {
            startContactsIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
