package application;

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
    }
}
