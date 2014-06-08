package activities;

import com.tieorange.pember.app.R;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import application.Constants;
import models.Contact;
import tools.ContactsHelper;

public class EditContactActivity extends ActionBarActivity {

    private EditText mUiName, mUiValue;
    private Button mUiSaveInfo;
    private Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        getExtras(savedInstanceState);
        initViews();
    }

    private void initViews() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit contact"); //TODO mock;
        setContactPhotoToActionBar();

        mUiName = (EditText) findViewById(R.id.add_info_name);
        mUiValue = (EditText) findViewById(R.id.add_info_value);
        mUiSaveInfo = (Button) findViewById(R.id.add_info_add_button);

        mUiSaveInfo.setText("Save");
        mUiName.setText(mContact.getName());
        mUiValue.setVisibility(View.GONE); //set Value invisible

        mUiSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infoName = mUiName.getText().toString();
                if (!mContact.getName().equals(infoName)) {//if smth changed
                    Intent output = new Intent();
                    output.putExtra(Constants.EXTRAS_CONTACT_NAME, infoName);
                    output.putExtra(Constants.EXTRAS_EDITED_CONTACT_ID, mContact.getId());
                    setResult(RESULT_OK, output);

                    ContactsHelper.hideKeyboard(view.getContext(), mUiValue);
                    finish();
                } else {
                    finish();
                }
            }
        });
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

    private void getExtras(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mContact = null;
            } else {
                Long id = extras.getLong(Constants.EXTRAS_EDITED_CONTACT_ID);
                mContact = Contact.load(Contact.class, id);
            }
        } else {
            Long id = savedInstanceState.getLong(Constants.EXTRAS_EDITED_CONTACT_ID);
            mContact = Contact.load(Contact.class, id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_contact, menu);
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
