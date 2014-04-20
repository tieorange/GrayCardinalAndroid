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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.tieorange.graycardinal.app.R;

import java.util.ArrayList;

import adapters.ContactsAdapter;
import application.Constants;
import models.Contact;
import models.ContactInfo;
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


     /*   Contact contact = new Contact("Andrii kovalchuk", BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        contact.save();

        ContactInfo contactInfo = new ContactInfo("favourite color", "green");
        contactInfo.contact = contact;
        contactInfo.save();*/

    }

    private void startContactsIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.PICK_CONTACT);
    }

    private void initViews() {
        mUiMyListView = (ListView) findViewById(R.id.main_contacts_list);

        mContactsList = new Select().from(Contact.class).execute();
        mContactsAdapter = new ContactsAdapter(this, mContactsList);

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mContactsAdapter);
        scaleInAnimationAdapter.setAbsListView(mUiMyListView);
        mUiMyListView.setAdapter(scaleInAnimationAdapter);

        mUiMyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Contact selectedItem = (Contact) adapter.getAdapter().getItem(position);

                Intent intent = new Intent(view.getContext(), InfoActivity.class);
                intent.putExtra(Constants.EXTRAS_CONTACT_ID, selectedItem.getId());
                startActivity(intent);

            }
        });
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
        // for (int i = 0; i < 50; i++)
        mContactsList.add(contact);

        for (int i = 0; i < 50; i++) {
            ContactInfo info = new ContactInfo("pin to phone", "7547", contact);
            contact.save();
            info.save();
        }
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
