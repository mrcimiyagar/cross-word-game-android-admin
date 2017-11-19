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

        String content = getIntent().getExtras().getString("present-content");

        TextView contentLayout = (TextView) findViewById(R.id.activity_present_content_layout);

        contentLayout.setText(content);
    }

    public void onOkBtnClicked(View view) {
        this.finish();
    }
}