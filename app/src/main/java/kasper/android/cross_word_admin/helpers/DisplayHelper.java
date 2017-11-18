package kasper.android.cross_word_admin.helpers;

import kasper.android.cross_word_admin.core.MyApp;

public class DisplayHelper {
    public float dpToPx(float dp) {
        return MyApp.getInstance().getResources().getDisplayMetrics().density * dp;
    }
}
