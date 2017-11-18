package kasper.android.cross_word_admin.callbacks;

import java.util.List;

import kasper.android.cross_word_admin.models.Message;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public interface OnMessagesReadListener {
    void messagesRead(List<Message> messages);
}
