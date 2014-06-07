package activities;

import com.tieorange.pember.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import application.Constants;
import application.PemberApplication;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tools.ContactsHelper;


public class AddInfoActivity extends ActionBarActivity {

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
        getSupportActionBar().setTitle("New Info"); //TODO mock;

        mUiName = (EditText) findViewById(R.id.add_info_name);
        mUiValue = (EditText) findViewById(R.id.add_info_value);
        mUiAddInfo = (Button) findViewById(R.id.add_info_add_button);

        mUiAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infoName = mUiName.getText().toString();
                String infoValue = mUiValue.getText().toString();
                if (infoName.length() > 0 && infoValue.length() > 0) {
                    Intent output = new Intent();
                    output.putExtra(Constants.EXTRAS_INFO_NAME, infoName);
                    output.putExtra(Constants.EXTRAS_INFO_VALUE, infoValue);
                    setResult(RESULT_OK, output);

                    ContactsHelper.hideKeyboard(view.getContext(), mUiValue);

                    mixPanelSendInfo(infoName, infoValue);

                    finish();
                } else {
                    //show toast to fill fields
                    Crouton.cancelAllCroutons();
                    Crouton.showText(AddInfoActivity.this, "Fill all the fields please",
                            Style.ALERT);

                }
            }
        });
    }

    private void mixPanelSendInfo(String infoName, String infoValue) {
        JSONObject props = new JSONObject();
        try {
            props.put("Name", infoName);
            props.put("Value", infoValue);
            PemberApplication.getMixPanel()
                    .track(getResources().getString(R.string.add_new_info), props);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
