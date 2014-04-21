package application;

import android.content.Context;
import android.content.ContextWrapper;

import com.activeandroid.ActiveAndroid;

import java.io.File;

public class GrayCardinalApplication extends com.activeandroid.app.Application {
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
