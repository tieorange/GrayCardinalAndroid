package activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.tieorange.graycardinal.app.R;

import application.Constants;
import models.Contact;

public class AddInfoActivity extends ActionBarActivity {
    private Contact mContact;
    private EditText mUiName, mUiValue;
    private Button mUiAddInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        initViews();
    }

    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Info"); //TODO mock

        mUiName = (EditText) findViewById(R.id.add_info_name);
        mUiValue = (EditText) findViewById(R.id.add_info_value);
        mUiAddInfo = (Button) findViewById(R.id.add_info_add_button);

        mUiAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoName = mUiName.getText().toString();
                String infoValue = mUiValue.getText().toString();
                if (infoName.length() > 0 && infoValue.length() > 0) {
                    Intent output = new Intent();
                    output.putExtra(Constants.EXTRAS_INFO_NAME, infoName);
                    output.putExtra(Constants.EXTRAS_INFO_VALUE, infoValue);
                    setResult(RESULT_OK, output);

                    hideKeyboard();
                    finish();
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUiValue.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_info, menu);
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
