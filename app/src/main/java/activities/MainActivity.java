package activities;

import com.google.gson.Gson;

import com.activeandroid.query.Select;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.tieorange.pember.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapters.ContactsAdapter;
import application.Constants;
import models.Contact;
import models.SerializableContact;
import tools.ContactsHelper;
import tools.popupmenu.PopupMenu;


public class MainActivity extends ActionBarActivity implements PopupMenu.OnItemSelectedListener {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private List<Contact> mContactsList = new ArrayList<Contact>();
    private ListView mUiContactsListView;
    private ContactsAdapter mContactsAdapter;
    private Contact mLongClickedItem;


    @Override
    protected void onStart() {
        super.onStart();
        final android.content.Intent intent = getIntent();

        if (intent != null) {

            final android.net.Uri data = intent.getData();

            if (data != null) {

                Gson gsonContact = new Gson();
                final String filePath = data.getEncodedPath();
                File contactFile = new File(filePath);
                String contactJson = ContactsHelper.readPemberContactFile(contactFile);
                SerializableContact serializableContact = gsonContact
                        .fromJson(contactJson, SerializableContact.class);

                Log.d(LOG_TAG, serializableContact.getContactName());
            }
        }

        return;
    }

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

        mUiContactsListView = (ListView) findViewById(R.id.main_contacts_list);

        mContactsList = new Select().from(Contact.class).execute();
        mContactsAdapter = new ContactsAdapter(this, mContactsList);

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(
                mContactsAdapter);
        scaleInAnimationAdapter.setAbsListView(mUiContactsListView);
        mUiContactsListView.setAdapter(scaleInAnimationAdapter);

        mUiContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Contact selectedItem = (Contact) adapter.getAdapter().getItem(position);

                Intent intent = new Intent(view.getContext(), InfoActivity.class);
                intent.putExtra(Constants.EXTRAS_CONTACT_ID, selectedItem.getId());
                startActivity(intent);

            }
        });
        mUiContactsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                    long id) {
                mLongClickedItem = (Contact) mUiContactsListView.getItemAtPosition(position);
                // Create Instance
                PopupMenu menu = new PopupMenu(MainActivity.this);
                menu.setHeaderTitle(mLongClickedItem.getName());
                // Set Listener
                menu.setOnItemSelectedListener(MainActivity.this);
                // Add Menu (Android menu like style)
                menu.add(Constants.EDIT_IN_POPUP, R.string.edit_info).setIcon(
                        getResources().getDrawable(R.drawable.ic_edit_info));
                menu.add(Constants.REMOVE_IN_POPUP, R.string.remove_info).setIcon(
                        getResources().getDrawable(R.drawable.ic_remove_info));
                menu.show(view);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (Constants.PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    //get contact from address book
                    Uri contactData = data.getData();

                    Contact contact = ContactsHelper.getContact(this, contactData);

                    if (!ContactsHelper.addContact(contact, mContactsAdapter)) {
                        //avoid contact duplication
                        Toast.makeText(this, R.string.contact_already_exist, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
        }
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
        if (id == R.id.main_menu_action_add_contact) {
            startContactsIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.main_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mContactsAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }


    @Override
    public void onItemSelected(tools.popupmenu.MenuItem item) {
        switch (item.getItemId()) {
            case Constants.EDIT_IN_POPUP:
                break;

            case Constants.REMOVE_IN_POPUP:
                ContactsHelper.removeContact(mLongClickedItem, mContactsAdapter);
                break;
        }
    }


}
