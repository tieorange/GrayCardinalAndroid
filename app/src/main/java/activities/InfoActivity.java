package activities;

import com.flurry.android.FlurryAgent;
import com.tieorange.pember.app.R;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Constants;
import fragments.InfoListFragment;
import models.Contact;
import models.ContactInfo;
import tools.ContactsHelper;


public class InfoActivity extends ActionBarActivity {

    public static final String FLURRY_WATCH_INFO_ACTIVITY = "WatchInfo";
    public static Contact mContact;
    public static List<ContactInfo> mInfoList = new ArrayList<ContactInfo>();
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getExtras(savedInstanceState);
        initViews();
    }

    private void initViews() {
        //Flurry
        Map<String, String> flurryParams = new HashMap<String, String>();

        flurryParams.put("Name", mContact.getName());

        FlurryAgent.logEvent(FLURRY_WATCH_INFO_ACTIVITY, flurryParams);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mContact.getName());

        setContactPhotoToActionBar();

        mInfoList = mContact.infoList();
    }

    private void setContactPhotoToActionBar() {
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
        FlurryAgent.endTimedEvent(FLURRY_WATCH_INFO_ACTIVITY);
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
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SENDTO);
                shareIntent.putExtra(Intent.EXTRA_STREAM, mContact);
                shareIntent.setType(getString(R.string.share_contact_type_intent));

                // Verify the intent will resolve to at least one activity
                if (shareIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(shareIntent,
                            getResources().getText(R.string.send_to)));
                }

                break;
            /*case R.id.action_add_info:
                Intent intent = new Intent(InfoActivity.this, AddInfoActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_INFO);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

}
