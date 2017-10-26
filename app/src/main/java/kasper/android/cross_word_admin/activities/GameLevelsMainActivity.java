package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kasper.android.cross_word_admin.adapters.GameLevelsAdapter;
import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.core.MyApp;
import kasper.android.cross_word_admin.models.GameLevel;
import kasper.android.cross_word_admin.models.WordInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameLevelsMainActivity extends AppCompatActivity {

    DiscreteScrollView gameLevelsRV;
    FloatingActionButton addBtn;
    RelativeLayout loadingLayout;

    private GameLevel deleteReservedGameLevel;
    public void setDeleteReservedGameLevel(GameLevel gameLevel) {
        this.deleteReservedGameLevel = gameLevel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_levels_main);

        gameLevelsRV = findViewById(R.id.activity_main_game_levels_scroll_view);
        addBtn = findViewById(R.id.activity_main_add_button);
        loadingLayout = findViewById(R.id.activity_game_levels_main_loading_layout);

        gameLevelsRV.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.9f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
        gameLevelsRV.setSlideOnFling(true);
        gameLevelsRV.setAdapter(new GameLevelsAdapter(this, new ArrayList<GameLevel>()));

        loadingLayout.setVisibility(View.VISIBLE);

        this.readGameLevelsFromServer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                GameLevel gameLevel = (GameLevel) data.getExtras().getSerializable("game-level");

                this.addGameLevelToServer(gameLevel);
            }
        }
        else if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                if (data.getExtras().getString("dialog-result").equals("yes")) {

                    deleteGameLevelFromServer(deleteReservedGameLevel);
                }
            }
        }
        else if (requestCode == 3) {

            if (resultCode == RESULT_OK) {

                GameLevel gameLevel = (GameLevel) data.getExtras().getSerializable("game-level");

                loadingLayout.setVisibility(View.VISIBLE);

                this.editGameLevelInServer(gameLevel);
            }
        }
    }

    private void readGameLevelsFromServer() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    final String method = "ReadGameLevels";
                    final String firstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
                    final String secondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";

                    String urlStr = "http://136.243.229.153/CrossWordGame/api/GameLevels/" + method + "?firstKey="
                            + firstKey + "&secondKey=" + secondKey + "&updateVersion=" + System.currentTimeMillis();
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

                    ArrayList<GameLevel> gameLevels = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        GameLevel gameLevel = new GameLevel();
                        gameLevel.setId(jsonObject.getInt("id"));
                        gameLevel.setNumber(counter + 1);
                        gameLevel.setPrize(jsonObject.getInt("prize"));

                        String tableData = jsonObject.getString("tableData");

                        String[] tableDataArr = new String[tableData.length()];

                        for (int counter1 = 0; counter1 < tableData.length(); counter1++) {
                            tableDataArr[counter1] = tableData.charAt(counter1) + "";
                        }

                        gameLevel.setTableData(tableDataArr);

                        gameLevel.setTableSize((int)(Math.sqrt(gameLevel.getTableData().length)));

                        String questionData = jsonObject.getString("questionData");
                        String answerData = jsonObject.getString("answerData");

                        gameLevel.setHasQuestion(!questionData.equals("empty"));

                        String[] answerArray = answerData.split(",");
                        String[] questionArray = null;

                        if (gameLevel.getHasQuestion()) {
                            questionArray = questionData.split(",");
                        }
                        else {
                            questionArray = new String[answerArray.length];
                            for (int counter1 = 0; counter1 < questionArray.length; counter1++) {
                                questionArray[counter1] = "";
                            }
                        }

                        ArrayList<WordInfo> wordsList = new ArrayList<>();

                        for (int counter1 = 0; counter1 < answerArray.length; counter1++) {
                            WordInfo wordInfo = new WordInfo();
                            wordInfo.setQuestion(gameLevel.getHasQuestion() ? questionArray[counter1] : "");

                            int[] sAnswerData = new int[answerArray[counter1].length()];

                            for (int counter2 = 0; counter2 < sAnswerData.length; counter2++) {
                                sAnswerData[counter2] = Integer.parseInt(answerArray[counter1].charAt(counter2) + "");
                            }

                            wordInfo.setAnswerIndex(sAnswerData);

                            String answer = "";

                            for (int counter2 = 0; counter2 < sAnswerData.length; counter2++) {
                                answer += (gameLevel.getTableData()[sAnswerData[counter2]] + "");
                            }

                            wordInfo.setAnswer(answer);

                            wordsList.add(wordInfo);
                        }

                        gameLevel.setWords(wordsList);

                        gameLevels.add(gameLevel);
                    }

                    MyApp.getInstance().setGameLevels(gameLevels);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            gameLevelsRV.setAdapter(new GameLevelsAdapter(GameLevelsMainActivity.this, MyApp.getInstance().getGameLevels()));
                            loadingLayout.setVisibility(View.GONE);
                        }
                    });

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    private void addGameLevelToServer(final GameLevel gameLevel) {

        loadingLayout.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("KasperLogger", "creating new level...");

                try {
                    String method = "AddGameLevel";
                    String firstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
                    String secondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";
                    String number = gameLevel.getNumber() + "";
                    String prize = gameLevel.getPrize() + "";

                    String tableData = "";
                    for (String tablePart : gameLevel.getTableData()) {
                        tableData += tablePart;
                    }

                    String questionData = "";
                    String answerData = "";
                    if (gameLevel.getHasQuestion()) {
                        for (WordInfo wordInfo : gameLevel.getWords()) {
                            questionData += wordInfo.getQuestion() + ",";
                            String answerPart = "";
                            for (int answerIndex : wordInfo.getAnswerIndex()) {
                                answerPart += (answerIndex + "");
                            }
                            answerData += answerPart + ",";
                        }
                        if (questionData.charAt(questionData.length() - 1) == ',') {
                            questionData = questionData.substring(0, questionData.length() - 1);
                        }
                        if (answerData.charAt(answerData.length() - 1) == ',') {
                            answerData = answerData.substring(0, answerData.length() - 1);
                        }
                    }
                    else {
                        questionData = "empty";
                        for (WordInfo wordInfo : gameLevel.getWords()) {
                            String answerPart = "";
                            for (int answerIndex : wordInfo.getAnswerIndex()) {
                                answerPart += (answerIndex + "");
                            }
                            answerData += answerPart + ",";
                        }
                        if (answerData.charAt(answerData.length() - 1) == ',') {
                            answerData = answerData.substring(0, answerData.length() - 1);
                        }
                    }

                    Log.d("KasperLogger", "creating new level...");

                    String urlStr = "http://136.243.229.153/CrossWordGame/api/GameLevels/" + method
                            + "?firstKey=" + firstKey + "&secondKey=" + secondKey + "&number=" + number
                            + "&prize=" + prize + "&tableData=" + tableData + "&questionData=" + questionData
                            + "&answerData=" + answerData;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d("KasperLogger", result);

                    if (result.equals("\"success\"")) {
                        readGameLevelsFromServer();
                    }
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    private void editGameLevelInServer(final GameLevel gameLevel) {

        loadingLayout.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String method = "EditGameLevel";
                    String firstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
                    String secondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";
                    String gameLevelId = gameLevel.getId() + "";
                    String number = gameLevel.getNumber() + "";
                    String prize = gameLevel.getPrize() + "";

                    String tableData = "";
                    for (String tablePart : gameLevel.getTableData()) {
                        tableData += tablePart;
                    }

                    String questionData = "";
                    String answerData = "";
                    if (gameLevel.getHasQuestion()) {
                        for (WordInfo wordInfo : gameLevel.getWords()) {
                            questionData += wordInfo.getQuestion() + ",";
                            String answerPart = "";
                            for (int answerIndex : wordInfo.getAnswerIndex()) {
                                answerPart += (answerIndex + "");
                            }
                            answerData += answerPart + ",";
                        }
                        if (questionData.charAt(questionData.length() - 1) == ',') {
                            questionData = questionData.substring(0, questionData.length() - 1);
                        }
                        if (answerData.charAt(answerData.length() - 1) == ',') {
                            answerData = answerData.substring(0, answerData.length() - 1);
                        }
                    }
                    else {
                        questionData = "empty";
                        for (WordInfo wordInfo : gameLevel.getWords()) {
                            String answerPart = "";
                            for (int answerIndex : wordInfo.getAnswerIndex()) {
                                answerPart += (answerIndex + "");
                            }
                            answerData += answerPart + ",";
                        }
                        if (answerData.charAt(answerData.length() - 1) == ',') {
                            answerData = answerData.substring(0, answerData.length() - 1);
                        }
                    }

                    String urlStr = "http://136.243.229.153/CrossWordGame/api/GameLevels/" + method
                            + "?firstKey=" + firstKey + "&secondKey=" + secondKey + "&gameLevelId="
                            + gameLevelId + "&number=" + number + "&prize=" + prize + "&tableData="
                            + tableData + "&questionData=" + questionData  + "&answerData=" + answerData;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    if (result.equals("\"success\"")) {
                        readGameLevelsFromServer();
                    }
                }
                catch (Exception ignored) {

                }
            }
        }).start();
    }

    private void deleteGameLevelFromServer(final GameLevel gameLevel) {

        loadingLayout.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String method = "DeleteGameLevel";
                    String firstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
                    String secondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";
                    String gameLevelId = gameLevel.getId() + "";

                    String urlStr = "http://136.243.229.153/CrossWordGame/api/GameLevels/" + method
                            + "?firstKey=" + firstKey + "&secondKey=" + secondKey + "&gameLevelId="
                            + gameLevelId;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    if (result.equals("\"success\"")) {
                        readGameLevelsFromServer();
                    }
                }
                catch (Exception ignored) {

                }
            }
        }).start();
    }

    public void onAddBtnClicked(View view) {

        GameLevel gameLevel = new GameLevel();
        gameLevel.setNumber(MyApp.getInstance().getGameLevels().size() + 1);
        gameLevel.setPrize(0);
        gameLevel.setTableSize(1);
        gameLevel.setTableData(new String[] { "" });
        gameLevel.setHasQuestion(false);
        gameLevel.setWords(new ArrayList<WordInfo>());

        startActivityForResult(new Intent(this, GameLevelsAddActivity.class).putExtra("game-level", gameLevel).putExtra("create", true), 1);
    }
}