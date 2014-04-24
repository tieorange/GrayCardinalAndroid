package application;

import com.activeandroid.ActiveAndroid;

import android.app.Application;
import android.content.Context;

public class GrayCardinalApplication extends Application {
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
        GrayCardinalApplication.mContext = getApplicationContext();
        ActiveAndroid.initialize(this);
        ActiveAndroid.getDatabase().execSQL("PRAGMA foreign_keys=ON");

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
