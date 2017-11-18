package kasper.android.cross_word_admin.callbacks;

import java.util.List;

import kasper.android.cross_word_admin.models.Word;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public interface OnWordsReadListener {
    void wordsRead(List<Word> words);
}
