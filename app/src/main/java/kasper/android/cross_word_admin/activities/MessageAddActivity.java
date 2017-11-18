package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kasper.android.cross_word_admin.R;

public class MessageAddActivity extends AppCompatActivity {

    EditText messageET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_add);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;

        getWindow().setAttributes(params);

        TextView titleTV = findViewById(R.id.activity_messages_add_title_text_view);
        titleTV.setText("ارسال پیام");

        messageET = findViewById(R.id.activity_messages_add_content_edit_text);
    }

    public void onCancelBtnClicked(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    public void onOkBtnClicked(View view) {
        if (messageET.getText().toString().length() > 0) {
            setResult(RESULT_OK, new Intent().putExtra("dialog-result", messageET.getText().toString()));
            this.finish();
        }
        else {
            Toast.makeText(this, "متن پیام نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
        }
    }
}
