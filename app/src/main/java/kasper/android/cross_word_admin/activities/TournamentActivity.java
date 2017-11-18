package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.adapters.TourPlayerDataAdapter;
import kasper.android.cross_word_admin.core.MyApp;
import kasper.android.cross_word_admin.models.TourPlayer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TournamentActivity extends AppCompatActivity {

    private TableView<TourPlayer> tableView;
    private RelativeLayout loadingLayout;

    private int totalDays, leftDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        tableView = findViewById(R.id.activity_tournament_table_view);
        loadingLayout = findViewById(R.id.activity_tournament_main_loading_layout);

        int colorEvenRows = Color.parseColor("#ffffffff");
        int colorOddRows = Color.parseColor("#ffeeeeee");
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(3);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 5);
        columnModel.setColumnWeight(2, 2);
        tableView.setColumnModel(columnModel);

        tableView.setSwipeToRefreshEnabled(true);
        tableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(RefreshIndicator refreshIndicator) {

                readTourPlayersFromServer(refreshIndicator);
            }
        });

        tableView.setHeaderAdapter(new TableHeaderAdapter(this, 3) {
            @Override
            public View getHeaderView(int columnIndex, ViewGroup parentView) {

                TextView headerView = new TextView(getContext());
                headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT));
                headerView.setGravity(Gravity.CENTER);
                headerView.setTextColor(Color.WHITE);
                headerView.setTextSize(16);
                headerView.setPadding(0, (int) MyApp.getInstance().getDisplayHelper().dpToPx(16), 0,
                        (int) MyApp.getInstance().getDisplayHelper().dpToPx(16));

                switch (columnIndex) {
                    case 0:
                        headerView.setText("امتیاز");
                        break;
                    case 1:
                        headerView.setText("نام بازیکن");
                        break;
                    case 2:
                        headerView.setText("رتبه");
                        break;
                }

                return headerView;
            }
        });

        this.totalDays = getIntent().getExtras().getInt("tour-total-days");
        this.leftDays = getIntent().getExtras().getInt("tour-left-days");

        readTourPlayersFromServer(null);
    }

    public void onBackBtnClicked(View view) {
        this.finish();
    }

    public void onInfoBtnClicked(View view) {
        startActivity(new Intent(this, PresentActivity.class).putExtra("present-title", "تورنمنت")
                .putExtra("present-content", new String[] {
                        "تورنمنت " + totalDays + " روزه فعال است",
                        leftDays + " روز تا پایان تورنمنت باقی مانده است"
                }));
    }

    private void readTourPlayersFromServer(final SwipeToRefreshListener.RefreshIndicator refreshIndicator) {

        loadingLayout.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    final String method = "ReadTopTourPlayers";
                    final String firstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
                    final String secondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";

                    String urlStr = "http://136.243.229.153/CrossWordGame/api/TourPlayers/" + method + "?firstKey="
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

                    JSONArray jsonArray = new JSONArray(result);

                    final ArrayList<TourPlayer> tourPlayers = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        TourPlayer tourPlayer = new TourPlayer();
                        tourPlayer.setId(jsonObject.getInt("id"));
                        tourPlayer.setName(jsonObject.getString("name"));
                        tourPlayer.setScore(jsonObject.getInt("score"));

                        tourPlayers.add(tourPlayer);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (refreshIndicator != null) {
                                refreshIndicator.hide();
                            }

                            tableView.setDataAdapter(new TourPlayerDataAdapter(TournamentActivity.this, tourPlayers));
                            loadingLayout.setVisibility(View.GONE);
                        }
                    });

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }
}