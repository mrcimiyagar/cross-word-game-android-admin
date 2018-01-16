package kasper.android.cross_word_admin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.callbacks.OnGameGuideReadListener;
import kasper.android.cross_word_admin.callbacks.OnGuideClearedListener;
import kasper.android.cross_word_admin.callbacks.OnGuideUpdatedListener;
import kasper.android.cross_word_admin.core.MyApp;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GuideEditActivity extends AppCompatActivity {

    private RelativeLayout loadingView;
    private EditText contentET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_edit);

        loadingView = findViewById(R.id.activity_guide_loading_view);
        contentET = findViewById(R.id.activity_guide_edit_edit_text);

        loadingView.setVisibility(View.VISIBLE);



        MyApp.getInstance().getNetworkHelper().readGameGuideFromServer(new OnGameGuideReadListener() {
            @Override
            public void gameGuideRead(final String guide) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        contentET.setText(guide);

                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    public void onBackBtnClicked(View view) {
        onBackPressed();
    }

    public void onDoneBtnClicked(View view) {

        final String text = contentET.getText().toString();

        if (text.length() == 0) {
            Toast.makeText(this, "راهنما نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingView.setVisibility(View.VISIBLE);

        MyApp.getInstance().getNetworkHelper().clearGuideInServer(new OnGuideClearedListener() {
            @Override
            public void guideCleared() {
                sendGuidePart(0, text);
            }
        });
    }

    private void sendGuidePart(final int partStartIndex, final String text) {

        if (partStartIndex + 63 < text.length()) {
            MyApp.getInstance().getNetworkHelper().updateGuideInServer(text.substring(partStartIndex, partStartIndex + 63), new OnGuideUpdatedListener() {
                @Override
                public void guideUpdated() {

                    sendGuidePart(partStartIndex + 64, text);
                }
            });
        }
        else {
            MyApp.getInstance().getNetworkHelper().updateGuideInServer(text.substring(partStartIndex), new OnGuideUpdatedListener() {
                @Override
                public void guideUpdated() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.setVisibility(View.GONE);
                            finish();
                        }
                    });
                }
            });
        }
    }
}
