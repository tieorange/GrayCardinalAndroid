package application;

import com.activeandroid.ActiveAndroid;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import android.app.Application;
import android.content.Context;

public class PemberApplication extends Application {

    private static Context mContext;
    private static MixpanelAPI mMixPanel;
    private static Permission[] mFacebookPermissions = new Permission[]{
            Permission.EMAIL,
            Permission.USER_FRIENDS,
            Permission.PUBLIC_PROFILE,
            Permission.READ_FRIENDLISTS
    };
    SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
            .setAppId("748339995189458")
            .setNamespace("app")
            .setPermissions(mFacebookPermissions)
            .build();

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PemberApplication.mContext = getApplicationContext();
        setMixPanel(MixpanelAPI.getInstance(mContext, Constants.MIXPANEL_TOKEN));

        ActiveAndroid.initialize(this);
        ActiveAndroid.getDatabase().execSQL("PRAGMA foreign_keys=ON");

        SimpleFacebook.setConfiguration(configuration);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    public static Context getContext() {
        return mContext;
    }

    public static MixpanelAPI getMixPanel() {
        return mMixPanel;
    }

    public static void setMixPanel(MixpanelAPI mixPanel) {
        mMixPanel = mixPanel;
    }

}
