package activities;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnFriendsListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.tieorange.pember.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class FacebookActivity extends ActionBarActivity {

    private static final String LOG_TAG = FacebookActivity.class.getSimpleName();
    Button mUiLogin, mUiGetFriends, mUiLogoutButton;
    private SimpleFacebook mSimpleFacebook;
    private OnLoginListener mOnFacebookLoginListener;
    private OnFriendsListener mOnFacebookFriendsListener;
    private OnLogoutListener mOnLogoutListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        initViews();


    }

    private void initViews() {
        //Views
        mUiLogin = (Button) findViewById(R.id.facebook_login_button);
        mUiGetFriends = (Button) findViewById(R.id.facebook_activity_get_friends);
        mUiLogoutButton = (Button) findViewById(R.id.facebook_logout_button);
//views listeners
        mUiLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleFacebook.login(mOnFacebookLoginListener);
            }
        });
        mUiLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimpleFacebook.logout(mOnLogoutListener);
            }
        });


        mUiGetFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* make the API call */
                new Request(
                        mSimpleFacebook.getSession(),
                        "/me/taggable_friends",
                        null,
                        HttpMethod.GET,
                        new Request.Callback() {
                            public void onCompleted(Response response) {
            /* handle the result */
                                final Response response1 = response;
                                final GraphObjectList<GraphObject> graphObjectList = response
                                        .getGraphObjectList();
                                final GraphObject graphObject = response.getGraphObject();
                                final String rawResponse = response.getRawResponse();
                                final Request request = response.getRequest();
                                int a = 0;

                            }
                        }
                ).executeAsync();
            }
        });

        //Facebook
        mSimpleFacebook = SimpleFacebook.getInstance(this);

        mOnLogoutListener = new OnLogoutListener() {
            @Override
            public void onLogout() {
                Log.i(LOG_TAG, "You are logged out");
            }

            @Override
            public void onThinking() {

            }

            @Override
            public void onException(Throwable throwable) {

            }

            @Override
            public void onFail(String s) {

            }

    /*
     * You can override other methods here:
     * onThinking(), onFail(String reason), onException(Throwable throwable)
     */
        };
        mOnFacebookLoginListener = new OnLoginListener() {
            @Override
            public void onLogin() {
                // change the state of the button or do whatever you want
                Log.i(LOG_TAG, "Logged in");
            }

            @Override
            public void onNotAcceptingPermissions(Permission.Type type) {
                // user didn't accept READ or WRITE permission
                Log.w(LOG_TAG, String.format("You didn't accept %s permissions", type.name()));
            }

            @Override
            public void onThinking() {

            }

            @Override
            public void onException(Throwable throwable) {

            }

            @Override
            public void onFail(String s) {

            }
        };


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.facebook, menu);
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
