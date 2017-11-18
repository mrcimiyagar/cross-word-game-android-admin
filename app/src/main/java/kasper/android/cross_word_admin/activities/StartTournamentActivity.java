package kasper.android.cross_word_admin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import kasper.android.cross_word_admin.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartTournamentActivity extends AppCompatActivity {

    private EditText totalDaysET;
    private RelativeLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tournament);

        this.totalDaysET = findViewById(R.id.activity_start_tournament_total_days_edit_text);
        this.loadingLayout = findViewById(R.id.activity_start_tournament_loading_layout);
    }

    public void onBackBtnClicked(View view) {
        this.finish();
    }

    public void onDoneBtnClicked(View view) {
        startTournament(Integer.parseInt(this.totalDaysET.getText().toString()));
    }

    private void startTournament(final int totalDays) {

        this.loadingLayout.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    final String method = "EnableTournament";
                    final String firstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
                    final String secondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";

                    String urlStr = "http://136.243.229.153/CrossWordGame/api/MainDatas/" + method + "?firstKey="
                            + firstKey + "&secondKey=" + secondKey + "&totalDays=" + totalDays;

                    Log.d("KasperLogger", urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d("KasperLogger", result);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StartTournamentActivity.this.finish();
                        }
                    });

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }
}