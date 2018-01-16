package kasper.android.cross_word_admin.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kasper.android.cross_word_admin.activities.StartTournamentActivity;
import kasper.android.cross_word_admin.callbacks.OnGameGuideReadListener;
import kasper.android.cross_word_admin.callbacks.OnGameLevelModifiesListener;
import kasper.android.cross_word_admin.callbacks.OnGameLevelsReadListener;
import kasper.android.cross_word_admin.callbacks.OnGuideClearedListener;
import kasper.android.cross_word_admin.callbacks.OnGuideUpdatedListener;
import kasper.android.cross_word_admin.callbacks.OnHelpCoinsUpdatedListener;
import kasper.android.cross_word_admin.callbacks.OnMessageModifiedListener;
import kasper.android.cross_word_admin.callbacks.OnMessagesReadListener;
import kasper.android.cross_word_admin.callbacks.OnStoreCoinsUpdatedListener;
import kasper.android.cross_word_admin.callbacks.OnWordModifiedListener;
import kasper.android.cross_word_admin.callbacks.OnWordsReadListener;
import kasper.android.cross_word_admin.core.MyApp;
import kasper.android.cross_word_admin.models.GameLevel;
import kasper.android.cross_word_admin.models.Message;
import kasper.android.cross_word_admin.models.Word;
import kasper.android.cross_word_admin.models.WordInfo;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkHelper {
    
    private final String LOG_TAG = "network helper";

    private final String serverAddress = "http://kaspersoft.ir/";
    private final String adminFirstKey = "s6d5f4g32xc1vbq98er7t6d5g4h321f63b4m4yik65l799i8ketn";
    private final String adminSecondKey = "uo987dg6j51s32fn165qatj465tul7r989ik4w3n152uk465s16a2h";
    private final String gameLevelsControllerName = "GameLevels";
    private final String methodReadGameLevels = "ReadGameLevels";
    private final String methodAddGameLevel = "AddGameLevel";
    private final String methodEditGameLevel = "EditGameLevel";
    private final String methodDeleteGameLevel = "DeleteGameLevel";
    private final String messagesControllerName = "Messages";
    private final String methodReadMessages = "ReadMessages";
    private final String methodReadMessageIds = "ReadMessageIds";
    private final String methodAddMessage = "AddMessage";
    private final String methodDeleteMessage = "DeleteMessage";
    private final String wordsControllerName = "Words";
    private final String methodReadWords = "ReadWords";
    private final String methodAddWord = "AddWord";
    private final String methodDeleteWord = "DeleteWord";
    private final String mainDatasControllerName = "MainDatas";
    private final String methodReadGuide = "ReadGuide";
    private final String methodEditGuide = "EditGuide";
    private final String methodClearGuide = "ClearGuide";
    private final String methodEditStoreCoins = "EditStoreCoinPack";
    private final String methodEditHelpCoins = "EditHelpCoinPack";

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void readGameLevelsFromServer(final OnGameLevelsReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/api/" + gameLevelsControllerName
                            + "/" + methodReadGameLevels + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<GameLevel> gameLevels = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        GameLevel gameLevel = new GameLevel();
                        gameLevel.setId(jsonObject.getInt("id"));
                        gameLevel.setNumber(counter + 1);
                        gameLevel.setPrize(jsonObject.getInt("prize"));

                        String tableData = jsonObject.getString("tableData");

                        gameLevel.setTableData(tableData);

                        gameLevel.setTableSize((int)(Math.sqrt(gameLevel.getTableData().length())));

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

                            String sAnswerData = answerArray[counter1];

                            wordInfo.setAnswerIndex(sAnswerData);

                            String answer = "";

                            String[] sAnswerDataPart = sAnswerData.split("-");

                            for (String aSAnswerDataPart : sAnswerDataPart) {
                                answer += (gameLevel.getTableData().charAt(Integer.parseInt(aSAnswerDataPart)) + "");
                            }

                            wordInfo.setAnswer(answer);

                            wordsList.add(wordInfo);
                        }

                        gameLevel.setWords(wordsList);

                        gameLevels.add(gameLevel);
                    }

                    callback.gameLevelsRead(gameLevels);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void addGameLevelToServer(final GameLevel gameLevel, final OnGameLevelModifiesListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("KasperLogger", "creating new level...");

                try {
                    String number = gameLevel.getNumber() + "";
                    String prize = gameLevel.getPrize() + "";

                    String tableData = gameLevel.getTableData();

                    String questionData = "";
                    String answerData = "";
                    if (gameLevel.getHasQuestion()) {
                        for (WordInfo wordInfo : gameLevel.getWords()) {
                            questionData += wordInfo.getQuestion() + ",";
                            answerData += wordInfo.getAnswerIndex() + ",";
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
                            answerData += wordInfo.getAnswerIndex() + ",";
                        }
                        if (answerData.charAt(answerData.length() - 1) == ',') {
                            answerData = answerData.substring(0, answerData.length() - 1);
                        }
                    }

                    Log.d("KasperLogger", "creating new level...");

                    String urlStr = serverAddress + "/api/" + gameLevelsControllerName
                            + "/" + methodAddGameLevel + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&number=" + number + "&prize=" + prize + "&tableData="
                            + tableData + "&questionData=" + questionData + "&answerData=" + answerData;
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
                        callback.gameLevelModified(gameLevel);
                    }
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void editGameLevelInServer(final GameLevel gameLevel, final OnGameLevelModifiesListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String gameLevelId = gameLevel.getId() + "";
                    String number = gameLevel.getNumber() + "";
                    String prize = gameLevel.getPrize() + "";

                    String tableData = gameLevel.getTableData();

                    String questionData = "";
                    String answerData = "";
                    if (gameLevel.getHasQuestion()) {
                        for (WordInfo wordInfo : gameLevel.getWords()) {
                            questionData += wordInfo.getQuestion() + ",";
                            answerData = wordInfo.getAnswerIndex() + ",";
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
                            answerData = wordInfo.getAnswerIndex() + ",";
                        }
                        if (answerData.charAt(answerData.length() - 1) == ',') {
                            answerData = answerData.substring(0, answerData.length() - 1);
                        }
                    }

                    String urlStr = serverAddress + "/api/" + gameLevelsControllerName
                            + "/" + methodEditGameLevel + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&gameLevelId=" + gameLevelId + "&number=" + number
                            + "&prize=" + prize + "&tableData=" + tableData + "&questionData="
                            + questionData  + "&answerData=" + answerData;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    if (result.equals("\"success\"")) {
                        callback.gameLevelModified(gameLevel);
                    }
                }
                catch (Exception ignored) {

                }
            }
        }).start();
    }

    public void deleteGameLevelFromServer(final GameLevel gameLevel, final OnGameLevelModifiesListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String gameLevelId = gameLevel.getId() + "";

                    String urlStr = serverAddress + "/api/" + gameLevelsControllerName
                            + "/" + methodDeleteGameLevel + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&gameLevelId=" + gameLevelId;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    if (result.equals("\"success\"")) {
                        callback.gameLevelModified(gameLevel);
                    }
                }
                catch (Exception ignored) {

                }
            }
        }).start();
    }

    public void readMessagesFromServer(final OnMessagesReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/api/" + messagesControllerName
                            + "/" + methodReadMessages + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);
                    Log.d(LOG_TAG, System.currentTimeMillis() + "");

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<Message> messages = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        Message message = new Message();
                        message.setId(jsonObject.getInt("id"));
                        message.setContent(jsonObject.getString("content"));
                        message.setTime(jsonObject.getLong("time"));

                        messages.add(message);
                    }

                    callback.messagesRead(messages);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void addMessageToServer(final String content, final OnMessageModifiedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String urlStr = serverAddress + "/api/" + messagesControllerName
                            + "/" + methodAddMessage + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&content=" + content;
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
                        callback.messageModified();
                    }
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void deleteMessageFromServer(final int id, final OnMessageModifiedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String urlStr = serverAddress + "/api/" + messagesControllerName
                            + "/" + methodDeleteMessage + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&messageId=" + id;
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
                        callback.messageModified();
                    }
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void readWordsFromServer(final OnWordsReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/api/" + wordsControllerName
                            + "/" + methodReadWords + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);
                    Log.d(LOG_TAG, System.currentTimeMillis() + "");

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<Word> words = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        Word word = new Word();
                        word.setId(jsonObject.getInt("id"));
                        word.setWord(jsonObject.getString("word"));
                        word.setMeaning(jsonObject.getString("meaning"));

                        words.add(word);
                    }

                    callback.wordsRead(words);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void addWordToServer(final String word, final String meaning, final OnWordModifiedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String urlStr = serverAddress + "/api/" + wordsControllerName
                            + "/" + methodAddWord + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&word=" + word + "&meaning=" + meaning;
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
                        callback.wordModified();
                    }
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void deleteWordFromServer(final int id, final OnWordModifiedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String urlStr = serverAddress + "/api/" + wordsControllerName
                            + "/" + methodDeleteWord + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&wordId=" + id;
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
                        callback.wordModified();
                    }
                }
                catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void readGameGuideFromServer(final OnGameGuideReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/api/" + mainDatasControllerName
                            + "/" + methodReadGuide + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey;

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    result = result.substring(1, result.length() - 1);

                    Log.d(LOG_TAG, result);

                    callback.gameGuideRead(result);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void clearGuideInServer(final OnGuideClearedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "api/" + mainDatasControllerName
                            + "/" + methodClearGuide + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey;

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    result = result.substring(1, result.length() - 1);

                    Log.d(LOG_TAG, result);

                    callback.guideCleared();

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void updateGuideInServer(final String text, final OnGuideUpdatedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "api/" + mainDatasControllerName
                            + "/" + methodEditGuide + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&text=" + text;

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    result = result.substring(1, result.length() - 1);

                    Log.d(LOG_TAG, result);

                    callback.guideUpdated();
                }
                catch (Exception ignored) {

                }
            }
        }).start();
    }

    public void updateStoreCoinsInServer(final int coin, final OnStoreCoinsUpdatedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "api/" + mainDatasControllerName
                            + "/" + methodEditStoreCoins + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&coin=" + coin;

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    callback.storeCoinsUpdated();
                }
                catch (Exception ignored) {

                }
            }
        }).start();
    }

    public void updateHelpCoinsInServer(final int coin, final OnHelpCoinsUpdatedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "api/" + mainDatasControllerName
                            + "/" + methodEditHelpCoins + "?firstKey=" + adminFirstKey + "&secondKey="
                            + adminSecondKey + "&coin=" + coin;

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    callback.helpCoinsUpdated();
                }
                catch (Exception ignored) {

                }
            }
        }).start();
    }
}