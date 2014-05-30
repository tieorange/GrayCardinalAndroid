package application;

import com.activeandroid.ActiveAndroid;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import android.app.Application;
import android.content.Context;

public class PemberApplication extends Application {

    private static Context mContext;
    private static MixpanelAPI mMixPanel;


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
