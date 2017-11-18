package kasper.android.cross_word_admin.core;

import android.app.Application;
import android.support.constraint.solver.Cache;

import java.util.ArrayList;

import kasper.android.cross_word_admin.helpers.CacheHelper;
import kasper.android.cross_word_admin.helpers.DisplayHelper;
import kasper.android.cross_word_admin.helpers.NetworkHelper;
import kasper.android.cross_word_admin.models.GameLevel;

public class MyApp extends Application {

    private static MyApp instance;
    public static MyApp getInstance() {
        return instance;
    }

    private NetworkHelper networkHelper;
    public NetworkHelper getNetworkHelper() {
        return this.networkHelper;
    }

    private CacheHelper cacheHelper;
    public CacheHelper getCacheHelper() {
        return this.cacheHelper;
    }

    private DisplayHelper displayHelper;
    public DisplayHelper getDisplayHelper() {
        return this.displayHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.networkHelper = new NetworkHelper();
        this.cacheHelper = new CacheHelper();
        this.displayHelper = new DisplayHelper();
    }
}