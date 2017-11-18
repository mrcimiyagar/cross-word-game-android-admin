package kasper.android.cross_word_admin.callbacks;

import java.util.ArrayList;

import kasper.android.cross_word_admin.models.GameLevel;

public interface OnGameLevelsReadListener {
    void gameLevelsRead(ArrayList<GameLevel> gameLevels);
}
