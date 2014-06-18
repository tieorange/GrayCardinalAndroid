package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tieorange.pember.app.R;

import application.Constants;
import models.Contact;
import tools.ContactsHelper;

public class AddNewContactActivity extends ActionBarActivity {

    private EditText mUiName;
    private Button mUiSaveInfo;
    private Contact mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        initViews();
    }

    private void initViews() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.add_new_contact));

        mUiName = (EditText) findViewById(R.id.add_contact_name);
        mUiSaveInfo = (Button) findViewById(R.id.add_contact_add_button);

        mUiSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infoName = mUiName.getText().toString();
                if (mUiName.getText() != null || !mUiName.getText()
                        .equals("")) {//if name isn't empty

                    mContact = new Contact(infoName);

                    Intent output = new Intent();
                    output.putExtra(Constants.EXTRAS_NEW_CONTACT_OBJECT, mContact);
                    setResult(RESULT_OK, output);

                    ContactsHelper.hideKeyboard(view.getContext(), mUiName);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_contact, menu);
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
