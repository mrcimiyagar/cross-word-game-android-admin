package kasper.android.cross_word_admin.core;

import android.app.Application;

import java.util.ArrayList;

import kasper.android.cross_word_admin.models.GameLevel;

public class MyApp extends Application {

    private static MyApp instance;
    public static MyApp getInstance() {
        return instance;
    }

    private ArrayList<GameLevel> gameLevels;

    public ArrayList<GameLevel> getGameLevels() {
        return gameLevels;
    }

    public void setGameLevels(ArrayList<GameLevel> gameLevels) {
        this.gameLevels = gameLevels;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}