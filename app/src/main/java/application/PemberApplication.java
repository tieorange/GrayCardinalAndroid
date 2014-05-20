package application;

import com.activeandroid.ActiveAndroid;
import com.flurry.android.FlurryAgent;

import android.app.Application;
import android.content.Context;

public class PemberApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PemberApplication.mContext = getApplicationContext();

        FlurryAgent.onStartSession(this, Constants.FLURRY_API_KEY);

        ActiveAndroid.initialize(this);
        ActiveAndroid.getDatabase().execSQL("PRAGMA foreign_keys=ON");

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
        FlurryAgent.onEndSession(this);
    }
}
