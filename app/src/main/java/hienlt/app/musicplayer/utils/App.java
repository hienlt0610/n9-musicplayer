package hienlt.app.musicplayer.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by hienl_000 on 4/17/2016.
 */
public class App extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }
}
