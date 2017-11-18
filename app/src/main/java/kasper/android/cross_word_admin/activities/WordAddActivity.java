package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kasper.android.cross_word_admin.R;

public class WordAddActivity extends AppCompatActivity {

    EditText wordET;
    EditText meaningET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_add);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;

        getWindow().setAttributes(params);

        TextView titleTV = findViewById(R.id.activity_words_add_title_text_view);
        titleTV.setText("ساخت واژه");

        wordET = findViewById(R.id.activity_words_add_word_edit_text);
        meaningET = findViewById(R.id.activity_words_add_meaning_edit_text);
    }

    public void onCancelBtnClicked(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    public void onOkBtnClicked(View view) {
        if (wordET.getText().toString().length() > 0 && meaningET.getText().toString().length() > 0) {
            setResult(RESULT_OK, new Intent()
                    .putExtra("dialog-first-result", wordET.getText().toString())
                    .putExtra("dialog-second-result", meaningET.getText().toString()));
            this.finish();
        }
        else {
            Toast.makeText(this, "فیلد کلمه یا معنی آن نمی توانند خالی باشند", Toast.LENGTH_SHORT).show();
        }
    }
}
