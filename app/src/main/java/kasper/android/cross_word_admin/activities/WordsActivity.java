package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.adapters.WordsAdapter;
import kasper.android.cross_word_admin.callbacks.OnWordModifiedListener;
import kasper.android.cross_word_admin.callbacks.OnWordsReadListener;
import kasper.android.cross_word_admin.core.MyApp;
import kasper.android.cross_word_admin.extras.LinearDecoration;
import kasper.android.cross_word_admin.models.Word;

public class WordsActivity extends AppCompatActivity {

    RecyclerView wordsRV;
    RelativeLayout loadingView;

    private Word deleteReservedWord;
    public void setDeleteReservedWord(Word message) {
        this.deleteReservedWord = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        this.initViews();
        this.initDecoration();

        updateWordsList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                if (data.getExtras().getString("dialog-result").equals("yes")) {

                    this.deleteWord(deleteReservedWord);
                }
            }
        }
        else if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                String word = data.getExtras().getString("dialog-first-result");
                String meaning = data.getExtras().getString("dialog-second-result");

                loadingView.setVisibility(View.VISIBLE);

                MyApp.getInstance().getNetworkHelper().addWordToServer(word, meaning, new OnWordModifiedListener() {
                    @Override
                    public void wordModified() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateWordsList();
                            }
                        });
                    }
                });
            }
        }
    }

    private void initViews() {
        this.loadingView = findViewById(R.id.activity_words_loading_layout);
        this.wordsRV = findViewById(R.id.activity_words_recycler_view);
    }

    private void initDecoration() {
        this.wordsRV.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        this.wordsRV.addItemDecoration(new LinearDecoration((int)(MyApp.getInstance()
                .getDisplayHelper().dpToPx(16)), (int)(MyApp.getInstance().getDisplayHelper()
                .dpToPx(16))));
    }

    public void onBackBtnClicked(View view) {
        onBackPressed();
    }

    public void onInfoBtnClicked(View view) {
        startActivity(new Intent(this, PresentActivity.class).putExtra("present-title", "دفترچه ی واژگان")
                .putExtra("present-content", new String[] {
                        wordsRV.getAdapter().getItemCount() + " واژه در دفترچه ذخیره شده است"
                }));
    }

    public void onAddBtnClicked(View view) {
        startActivityForResult(new Intent(this, WordAddActivity.class), 2);
    }

    private void updateWordsList() {
        this.loadingView.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().readWordsFromServer(new OnWordsReadListener() {
            @Override
            public void wordsRead(final List<Word> words) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wordsRV.setAdapter(new WordsAdapter(WordsActivity.this, words));
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void deleteWord(Word word) {
        loadingView.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().deleteWordFromServer(word.getId(), new OnWordModifiedListener() {
            @Override
            public void wordModified() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateWordsList();
                    }
                });
            }
        });
    }
}
