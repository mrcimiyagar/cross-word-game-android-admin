package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kasper.android.cross_word_admin.adapters.EmptyTableAdapter;
import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.models.GameLevel;
import kasper.android.cross_word_admin.models.WordInfo;

public class GameLevelsAddActivity extends AppCompatActivity {

    TextView levelNumberTV;
    EditText levelPrizeET;
    EditText tableSizeET;
    CheckBox questStatCB;

    CardView tableContainer;
    RecyclerView tableRV;

    GameLevel gameLevel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_levels_add);

        gameLevel = (GameLevel) getIntent().getExtras().getSerializable("game-level");

        this.levelNumberTV = (TextView) findViewById(R.id.activity_game_levels_add_level_number_text_view);
        this.levelPrizeET = (EditText) findViewById(R.id.activity_game_levels_add_level_prize_edit_text);
        this.tableSizeET = (EditText) findViewById(R.id.activity_game_levels_add_table_size_edit_text);
        this.questStatCB = (CheckBox) findViewById(R.id.activity_game_levels_add_quest_stat_check_box);
        this.tableContainer = (CardView) findViewById(R.id.activity_game_levels_add_table_container);
        this.tableRV = (RecyclerView) findViewById(R.id.activity_game_levels_add_table_recycler_view);

        this.levelNumberTV.setText("مرحله ی " + gameLevel.getNumber());
        this.levelPrizeET.setText(gameLevel.getPrize() + "");
        this.tableSizeET.setText(gameLevel.getTableSize() + "");
        this.questStatCB.setChecked(gameLevel.getHasQuestion());

        this.tableRV.post(new Runnable() {
            @Override
            public void run() {
                int width = tableContainer.getMeasuredWidth();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
                int sideMargin = (int)(16 * getResources().getDisplayMetrics().density);
                params.setMargins(sideMargin, 0, sideMargin, sideMargin);
                tableContainer.setLayoutParams(params);
                tableContainer.requestLayout();
                tableContainer.invalidate();

                tableRV.setLayoutManager(new GridLayoutManager(GameLevelsAddActivity.this, gameLevel.getTableSize()
                        , LinearLayoutManager.VERTICAL, false));
                tableRV.setAdapter(new EmptyTableAdapter(gameLevel.getTableData()));
            }
        });

        this.tableSizeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0 && s.toString().charAt(0) != '0' && Integer.parseInt
                        (s.toString()) <= 7) {
                    tableRV.setLayoutManager(new GridLayoutManager(GameLevelsAddActivity.this,
                            Integer.parseInt(s.toString()), LinearLayoutManager.VERTICAL, false));
                    String[] tableDataArr = new String[(int)Math.pow(Integer.parseInt(s.toString()), 2)];
                    for (int counter = 0; counter < tableDataArr.length; counter++) {
                        tableDataArr[counter] = "";
                    }
                    tableRV.setAdapter(new EmptyTableAdapter(tableDataArr));
                }

                if (s.toString().length() > 1 && s.charAt(0) == '0') {
                    tableSizeET.setText(s.subSequence(1, s.length()));
                    tableSizeET.setSelection(1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                gameLevel = (GameLevel) data.getExtras().getSerializable("game-level");

                setResult(RESULT_OK, new Intent().putExtra("game-level", gameLevel));

                this.finish();
            }
        }
    }

    public void onBackBtnClicked(View view) {

        setResult(RESULT_CANCELED);

        this.finish();
    }

    public void onSaveBtnClicked(View view) {

        if (levelPrizeET.getText().toString().length() > 0 && tableSizeET.getText().toString().length() > 0) {

            String[] tableData = ((EmptyTableAdapter) tableRV.getAdapter()).getTableData();

            boolean tableFilled = true;

            for (int counter = 0; counter < tableData.length; counter++) {
                if (tableData[counter].length() == 0) {
                    tableFilled = false;
                    break;
                }
            }

            if (tableFilled) {
                gameLevel.setHasQuestion(questStatCB.isChecked());
                gameLevel.setPrize(Integer.parseInt(levelPrizeET.getText().toString()));
                gameLevel.setTableSize((int) Math.sqrt(tableRV.getAdapter().getItemCount()));

                boolean sameTableData = true;

                if (tableData.length != gameLevel.getTableData().length) {
                    sameTableData = false;
                } else {
                    for (int counter = 0; counter < tableData.length; counter++) {
                        if (!tableData[counter].equals(gameLevel.getTableData()[counter])) {
                            sameTableData = false;
                            break;
                        }
                    }
                }

                gameLevel.setTableData(tableData);

                if (!sameTableData) {
                    gameLevel.setWords(new ArrayList<WordInfo>());
                }

                this.startActivityForResult(new Intent(GameLevelsAddActivity.this, GameLevelsWordInfoActivity.class).putExtra("game-level", gameLevel), 1);
            }
            else {

                Toast.makeText(this, "همه ی خانه های جدول باید پر شوند", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            Toast.makeText(this, "همه ی کادر ها باید پر شوند", Toast.LENGTH_SHORT).show();
        }
    }
}