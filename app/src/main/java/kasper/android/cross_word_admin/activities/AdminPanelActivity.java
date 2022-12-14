package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kasper.android.cross_word_admin.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminPanelActivity extends AppCompatActivity {

    private RelativeLayout loadingLayout;

    private TextView gameLevelsCountTV;
    private TextView tourTotalDaysTV;
    private TextView tourLeftDaysTV;
    private TextView tourPlayersTV;
    private Button tourControlBtn;
    private Button tourPlayersBtn;
    private TextView msgsCountTV;
    private TextView wordsCountTV;
    private TextView storeCoinsTV;
    private TextView helpCoinsTV;

    private boolean tourActive;
    private int tourTotalDays, tourLeftDays, tourPlayersCount;
    private int storeCoinsCount;
    private int helpCoinsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        this.loadingLayout = findViewById(R.id.activity_admin_panel_loading_layout);
        this.gameLevelsCountTV = findViewById(R.id.activity_admin_panel_game_levels_count_text_view);
        this.tourTotalDaysTV = findViewById(R.id.activity_admin_panel_tour_total_days_count_text_view);
        this.tourLeftDaysTV = findViewById(R.id.activity_admin_panel_tour_left_days_count_text_view);
        this.tourPlayersTV = findViewById(R.id.activity_admin_panel_tour_players_count_text_view);
        this.tourPlayersBtn = findViewById(R.id.activity_admin_panel_tour_players_button);
        this.tourControlBtn = findViewById(R.id.activity_admin_panel_tour_control_button);
        this.msgsCountTV = findViewById(R.id.activity_admin_panel_messages_count_text_view);
        this.wordsCountTV = findViewById(R.id.activity_admin_panel_words_count_text_view);
        this.storeCoinsTV = findViewById(R.id.activity_admin_panel_store_coin_pack_text_view);
        this.helpCoinsTV = findViewById(R.id.activity_admin_panel_help_coin_pack_text_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.readMainDataFromServer();
    }

    public void onGameLevelsBtnClicked(View view) {
        startActivity(new Intent(this, GameLevelsListActivity.class));
    }

    public void onTournamentBtnClicked(View view) {
        startActivity(new Intent(this, StartTournamentActivity.class));
    }

    public void onTourPlayersBtnClicked(View view) {
        startActivity(new Intent(this, TournamentActivity.class)
                .putExtra("tour-total-days", tourTotalDays)
                .putExtra("tour-left-days", tourLeftDays));
    }

    public void onMessagesBtnClicked(View view) {
        startActivity(new Intent(this, MessagesActivity.class));
    }

    public void onDictionaryBtnClicked(View view) {
        startActivity(new Intent(this, WordsActivity.class));
    }

    public void onGuideBtnClicked(View view) {
        startActivity(new Intent(this, GuideEditActivity.class));
    }

    public void onCoinsBtnClicked(View view) {
        startActivity(new Intent(this, CoinsActivity.class)
                .putExtra("store-coins", storeCoinsCount)
                .putExtra("help-coins", helpCoinsCount));
    }

    private void readMainDataFromServer() {

        loadingLayout.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    final String method = "ReadMainData";
                    final String firstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
                    final String secondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";

                    String urlStr = "http://www.kaspersoft.ir/api/MainDatas/" + method + "?firstKey="
                            + firstKey + "&secondKey=" + secondKey + "&updateVersion=" + System.currentTimeMillis();

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

                    if (result.length() > 0) {

                        result = result.substring(1, result.length() - 1);

                        String[] resultParts = result.split(",");

                        final int gameLevelsCount = Integer.parseInt(resultParts[0]);
                        tourActive = Boolean.parseBoolean(resultParts[1]);
                        tourTotalDays = Integer.parseInt(resultParts[2]);
                        tourLeftDays = Integer.parseInt(resultParts[3]);
                        tourPlayersCount = Integer.parseInt(resultParts[4]);
                        final int msgsCount = Integer.parseInt(resultParts[5]);
                        final int wordsCount = Integer.parseInt(resultParts[6]);
                        storeCoinsCount = Integer.parseInt(resultParts[7]);
                        helpCoinsCount = Integer.parseInt(resultParts[8]);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                gameLevelsCountTV.setText(gameLevelsCount + " ?????????? ???? ???????? ?????????? ??????");

                                if (tourActive) {
                                    tourTotalDaysTV.setText("?????????????? " + tourTotalDays + " ???????? ???????? ??????");
                                    tourLeftDaysTV.setText(tourLeftDays + " ?????? ???? ?????????? ?????????????? ???????? ?????????? ??????");
                                    tourPlayersTV.setText(tourPlayersCount + " ?????????? ???? ?????????????? ??????????");
                                    tourControlBtn.setVisibility(View.GONE);
                                }
                                else {
                                    tourTotalDaysTV.setText("?????????????? ?????? ???????? ??????");
                                    tourLeftDaysTV.setText("-");
                                    tourPlayersTV.setText("-");
                                    tourControlBtn.setVisibility(View.VISIBLE);
                                    tourControlBtn.setText("?????????? ??????????????");
                                }

                                msgsCountTV.setText(msgsCount + " ???????? ?????????? ?????????? ?????? ??????");
                                wordsCountTV.setText(wordsCount + " ???????? ???? ???????????? ?????????? ??????");
                                storeCoinsTV.setText("?????????? ?????? ?? ?????????????? : " + storeCoinsCount);
                                helpCoinsTV.setText("?????????? ?????? ?? ?????? : " + helpCoinsCount);

                                loadingLayout.setVisibility(View.GONE);
                            }
                        });
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }
}