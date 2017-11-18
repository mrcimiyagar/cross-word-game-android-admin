package kasper.android.cross_word_admin.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.models.Word;

public class WordDetailsActivity extends AppCompatActivity {

    TextView wordTV;
    TextView meaningTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        Word word = (Word) getIntent().getExtras().getSerializable("word");

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;

        getWindow().setAttributes(params);

        TextView titleTV = findViewById(R.id.activity_word_details_title_text_view);
        titleTV.setText("جزئیات کلمه");

        wordTV = findViewById(R.id.activity_word_details_word_text_view);
        meaningTV = findViewById(R.id.activity_words_details_meaning_text_view);

        wordTV.setText(word.getWord());
        meaningTV.setText(word.getMeaning());
    }

    public void onOkBtnClicked(View view) {
        this.finish();
    }
}
