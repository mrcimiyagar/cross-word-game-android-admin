package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.core.MyApp;

public class PresentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;

        getWindow().setAttributes(params);

        String title = getIntent().getExtras().getString("present-title");

        TextView titleTV = (TextView) findViewById(R.id.activity_present_title_text_view);
        titleTV.setText(title);

        String[] content = getIntent().getExtras().getStringArray("present-content");

        LinearLayout contentLayout = (LinearLayout) findViewById(R.id.activity_present_content_layout);

        for (int counter = 0; counter < content.length; counter++) {
            TextView lineTV = new TextView(this);
            lineTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , (int)MyApp.getInstance().getDisplayHelper().dpToPx(48)));
            lineTV.setGravity(Gravity.CENTER_VERTICAL);
            lineTV.setTextSize(17.5f);
            lineTV.setTextColor(Color.BLACK);
            lineTV.setText(content[counter]);
            contentLayout.addView(lineTV);

            if (counter < content.length - 1) {
                FrameLayout dividerView = new FrameLayout(this);
                LinearLayout.LayoutParams dividerLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.
                        MATCH_PARENT, (int) Math.max(MyApp.getInstance().getDisplayHelper().dpToPx(0.25f), 1));
                dividerLP.setMargins(0, (int) MyApp.getInstance().getDisplayHelper().dpToPx(8), 0,
                        (int) MyApp.getInstance().getDisplayHelper().dpToPx(8));
                dividerView.setLayoutParams(dividerLP);
                dividerView.setBackgroundColor(Color.parseColor("#ff666666"));
                contentLayout.addView(dividerView);
            }
        }
    }

    public void onOkBtnClicked(View view) {
        this.finish();
    }
}