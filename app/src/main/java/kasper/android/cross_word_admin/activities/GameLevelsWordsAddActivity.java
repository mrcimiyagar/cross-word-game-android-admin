package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.adapters.FilledTableAdapter;

public class GameLevelsWordsAddActivity extends AppCompatActivity {

    boolean hasQuestion;
    String[] tableData;
    int tableSize;

    CardView questionContainer;
    EditText questionET;
    CardView wordContainer;
    RecyclerView wordRV;

    List<Integer> answerPosesX;
    List<Integer> answerPosesY;
    List<Integer> answerIndices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_levels_words_add);

        questionContainer = findViewById(R.id.activity_game_levels_words_add_question_container);
        questionET = findViewById(R.id.activity_game_levels_words_add_question_edit_text);
        wordContainer = findViewById(R.id.activity_game_levels_words_add_word_container);
        wordRV = findViewById(R.id.activity_game_levels_words_add_recycler_view);

        this.answerPosesX = new ArrayList<>();
        this.answerPosesY = new ArrayList<>();
        this.answerIndices = new ArrayList<>();
        this.hasQuestion = getIntent().getExtras().getBoolean("has-question");
        this.tableData = getIntent().getExtras().getStringArray("table-data");
        this.tableSize = (int)(Math.sqrt(tableData.length));

        questionContainer.setVisibility(hasQuestion ? View.VISIBLE : View.GONE);

        wordContainer.post(new Runnable() {
            @Override
            public void run() {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wordContainer.getMeasuredWidth()
                        , wordContainer.getMeasuredWidth());
                int margin = (int)(16 * getResources().getDisplayMetrics().density);
                params.setMargins(margin, margin, margin, margin);
                wordContainer.setLayoutParams(params);

                wordRV.setLayoutManager(new GridLayoutManager(GameLevelsWordsAddActivity.this, (int)Math.sqrt
                        (tableData.length), LinearLayoutManager.VERTICAL, false));

                wordRV.setAdapter(new FilledTableAdapter(tableData));

                wordRV.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

                            final int blockSize = (wordRV.getMeasuredWidth() / tableSize);

                            int blockX = (int)(event.getX() / blockSize);
                            int blockY = (int)(event.getY() / blockSize);

                            if (Math.abs(event.getX() - (blockX + 0.5f) * blockSize) < blockSize * 0.3f
                                    && Math.abs(event.getY() - (blockY + 0.5f) * blockSize) < blockSize * 0.3f) {

                                int answerIndex = blockY * tableSize + blockX;

                                if (answerIndices.contains(answerIndex)) {
                                    return true;
                                }

                                if (answerIndices.size() == 0) {

                                    answerPosesX.add(blockX);
                                    answerPosesY.add(blockY);
                                    answerIndices.add(answerIndex);

                                    ((FilledTableAdapter)wordRV.getAdapter()).notifyItemSelected(answerIndex);
                                }
                                else {

                                    int lastBX = answerPosesX.get(answerPosesX.size() - 1);
                                    int lastBY = answerPosesY.get(answerPosesY.size() - 1);

                                    if (Math.abs(lastBX - blockX) <= 1 && Math.abs(lastBY - blockY) <= 1) {

                                        answerPosesX.add(blockX);
                                        answerPosesY.add(blockY);
                                        answerIndices.add(answerIndex);

                                        ((FilledTableAdapter)wordRV.getAdapter()).notifyItemSelected(answerIndex);
                                    }
                                }
                            }

                            return true;
                        }

                        return false;
                    }
                });
            }
        });
    }

    public void onSaveBtnClicked(View view) {

        if (this.hasQuestion && questionET.getText().toString().length() == 0) {
            Toast.makeText(this, "لطفا سوال را وارد کنید", Toast.LENGTH_SHORT).show();
            return;
        }

        if (this.answerIndices.size() == 0) {
            Toast.makeText(this, "لطفا کلمه را بسازید", Toast.LENGTH_SHORT).show();
            return;
        }

        int[] answerIndicesArr = new int[answerIndices.size()];

        int counter = 0;

        for (Integer answerIndex : answerIndices) {
            answerIndicesArr[counter] = answerIndex;
            counter++;
        }

        setResult(RESULT_OK, new Intent().putExtra("question", questionET.getText().toString()).putExtra("answer-indices", answerIndicesArr));

        this.finish();
    }

    public void onBackBtnClicked(View view) {

        this.finish();
    }
}