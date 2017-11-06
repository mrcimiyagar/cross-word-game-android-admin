package kasper.android.cross_word_admin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.adapters.TournamentAdapter;
import kasper.android.cross_word_admin.models.TourPlayer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TournamentActivity extends AppCompatActivity {

    private RecyclerView itemsRV;
    private RelativeLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);

        itemsRV = (RecyclerView) findViewById(R.id.activity_tournament_recycler_view);
        loadingLayout = (RelativeLayout) findViewById(R.id.activity_tournament_main_loading_layout);

        itemsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        readTourPlayersFromServer();
    }

    public void onBackBtnClicked(View view) {
        this.finish();
    }

    private void readTourPlayersFromServer() {

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
                        tourPlayer.setLevelsDoneCount(jsonObject.getInt("levelsDoneCount"));

                        tourPlayers.add(tourPlayer);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            itemsRV.setAdapter(new TournamentAdapter(tourPlayers));
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
