package activities;

import com.tieorange.pember.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import application.Constants;
import models.ContactInfo;
import tools.ContactsHelper;

public class EditInfoActivity extends ActionBarActivity {

    private EditText mUiName, mUiValue;
    private Button mUiSaveInfo;
    private ContactInfo mClickedInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        getExtras(savedInstanceState);
        initViews();
    }

    private void initViews() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit info"); //TODO mock;

        mUiName = (EditText) findViewById(R.id.add_info_name);
        mUiValue = (EditText) findViewById(R.id.add_info_value);
        mUiSaveInfo = (Button) findViewById(R.id.add_info_add_button);

        mUiSaveInfo.setText("Save");
        mUiName.setText(mClickedInfo.getName());
        mUiValue.setText(mClickedInfo.getValue());

        mUiSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infoName = mUiName.getText().toString();
                String infoValue = mUiValue.getText().toString();
                if (mClickedInfo.getName().equals(infoName) ||
                        mClickedInfo.getValue().equals(infoValue)) {
                    Intent output = new Intent();
                    output.putExtra(Constants.EXTRAS_INFO_NAME, infoName);
                    output.putExtra(Constants.EXTRAS_INFO_VALUE, infoValue);
                    output.putExtra(Constants.EXTRAS_CLICKED_INFO_ID, mClickedInfo.getId());
                    setResult(RESULT_OK, output);

                    ContactsHelper.hideKeyboard(view.getContext(), mUiValue);
                    finish();
                }
            }
        });
    }

    private void getExtras(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mClickedInfo = null;
            } else {
                Long id = extras.getLong(Constants.EXTRAS_CLICKED_INFO_ID);
                mClickedInfo = ContactInfo.load(ContactInfo.class, id);
            }
        } else {
            Long id = savedInstanceState.getLong(Constants.EXTRAS_CLICKED_INFO_ID);
            mClickedInfo = ContactInfo.load(ContactInfo.class, id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
