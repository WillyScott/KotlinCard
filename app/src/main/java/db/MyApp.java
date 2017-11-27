package db;

import android.app.Application;
import android.util.Log;

/**
 * Created by Dad on 11/15/2017.
 */

public class MyApp extends Application {
    private static final String TAG = "MyApp";
    public static AppDataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        dataBase = AppDataBase.getINSTANCE(this);
        Log.d(TAG,"onCreate()");
    }
}
