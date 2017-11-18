package kasper.android.cross_word_admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import kasper.android.cross_word_admin.core.MyApp;
import kasper.android.cross_word_admin.models.TourPlayer;

public class TourPlayerDataAdapter extends TableDataAdapter<TourPlayer> {

    public TourPlayerDataAdapter(Context context, List<TourPlayer> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        TourPlayer player = getRowData(rowIndex);
        TextView fieldView = new TextView(getContext());
        fieldView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT));
        fieldView.setGravity(Gravity.CENTER);
        fieldView.setTextColor(Color.BLACK);
        fieldView.setTextSize(16);
        fieldView.setPadding(0, (int)MyApp.getInstance().getDisplayHelper().dpToPx(16), 0, (int)MyApp
                .getInstance().getDisplayHelper().dpToPx(16));

        switch (columnIndex) {
            case 0:
                fieldView.setText(player.getScore() + "");
                break;
            case 1:
                fieldView.setText(player.getName());
                break;
            case 2:
                fieldView.setText((rowIndex + 1) + "");
                break;
        }

        return fieldView;
    }
}