package kasper.android.cross_word_admin.helpers;

import java.util.List;

import kasper.android.cross_word_admin.models.GameLevel;

public class CacheHelper {
    private List<GameLevel> gameLevels;
    public List<GameLevel> getGameLevels() {
        return this.gameLevels;
    }
    public void setGameLevels(List<GameLevel> gameLevels) {
        this.gameLevels = gameLevels;
    }
}
