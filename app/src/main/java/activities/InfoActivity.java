package activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.tieorange.graycardinal.app.R;

import adapters.InfoAdapter;
import application.Constants;
import models.Contact;


public class InfoActivity extends ActionBarActivity {
    private Contact mContact;

    private ListView mUiInfoListView;
    private InfoAdapter mInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getExtras(savedInstanceState);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TODO mock
        getSupportActionBar().setTitle(mContact.getName());



        mUiInfoListView = (ListView) findViewById(R.id.activity_info_listView);


        mInfoAdapter = new InfoAdapter(this, mContact.infoList());

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mInfoAdapter);
        scaleInAnimationAdapter.setAbsListView(mUiInfoListView);
        mUiInfoListView.setAdapter(scaleInAnimationAdapter);
    }

    private void getExtras(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mContact = null;
            } else {
                Long id = extras.getLong(Constants.EXTRAS_CONTACT);
                mContact = Contact.load(Contact.class, id);
            }
        } else {
            Long id = savedInstanceState.getLong(Constants.EXTRAS_CONTACT);
            mContact = Contact.load(Contact.class, id);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

}
