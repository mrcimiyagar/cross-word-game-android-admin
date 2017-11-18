package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.adapters.WordsInfoAdapter;
import kasper.android.cross_word_admin.models.GameLevel;
import kasper.android.cross_word_admin.models.WordInfo;

public class GameLevelsWordInfoActivity extends AppCompatActivity {

    GameLevel gameLevel;
    RecyclerView itemsRV;
    FloatingActionButton addBTN;

    private Runnable yesNoActivityReservedCallback;
    public void setYesNoActivityReservedCallback(Runnable callback) {
        this.yesNoActivityReservedCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_levels_word_info);

        gameLevel = (GameLevel) getIntent().getExtras().getSerializable("game-level");

        itemsRV = findViewById(R.id.activity_game_levels_word_info_recycler_view);
        addBTN = findViewById(R.id.activity_game_levels_word_info_add_button);

        itemsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        itemsRV.setAdapter(new WordsInfoAdapter(this, gameLevel.getWords()));

        itemsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    addBTN.hide();
                }
                else {
                    addBTN.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                String question = data.getExtras().getString("question");
                int[] answerIndices = data.getExtras().getIntArray("answer-indices");

                String answer = "";

                for (int answerIndex : answerIndices) {
                    answer += (gameLevel.getTableData().charAt(answerIndex) + "");
                }

                WordInfo wordInfo = new WordInfo();
                wordInfo.setQuestion(question);

                String answerIndexStr = "";
                for (int counter = 0; counter < answerIndices.length; counter++) {
                    answerIndexStr += answerIndices[counter] + "-";
                }
                if (answerIndexStr.endsWith("-")) {
                    answerIndexStr = answerIndexStr.substring(0, answerIndexStr.length() - 1);
                }

                wordInfo.setAnswerIndex(answerIndexStr);
                wordInfo.setAnswer(answer);

                gameLevel.getWords().add(wordInfo);

                itemsRV.setAdapter(new WordsInfoAdapter(this, gameLevel.getWords()));
            }
        }
        else if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                if (data.getExtras().getString("dialog-result").equals("yes")) {

                    this.yesNoActivityReservedCallback.run();
                }
            }
        }
    }

    public void onSaveBtnClicked(View view) {

        if (gameLevel.getWords().size() > 0) {

            setResult(RESULT_OK, new Intent().putExtra("game-level", gameLevel));

            this.finish();
        }
        else {

            Toast.makeText(this, "لطفا کلمه اضافه کنید", Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddBtnClicked(View view) {

        startActivityForResult(new Intent(this, GameLevelsWordsAddActivity.class).putExtra("has-question"
                , gameLevel.getHasQuestion()).putExtra("table-data", gameLevel.getTableData()), 1);
    }

    public void onBackBtnClicked(View view) {

        setResult(RESULT_CANCELED);

        this.finish();
    }
}