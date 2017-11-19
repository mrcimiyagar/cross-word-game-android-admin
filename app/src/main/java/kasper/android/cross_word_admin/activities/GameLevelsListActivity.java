package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;

import kasper.android.cross_word_admin.adapters.GameLevelsAdapter;
import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.callbacks.OnGameLevelModifiesListener;
import kasper.android.cross_word_admin.callbacks.OnGameLevelsReadListener;
import kasper.android.cross_word_admin.core.MyApp;
import kasper.android.cross_word_admin.models.GameLevel;
import kasper.android.cross_word_admin.models.WordInfo;

public class GameLevelsListActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_game_levels_list);

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

        updateGameLevelsList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                GameLevel gameLevel = (GameLevel) data.getExtras().getSerializable("game-level");

                this.addGameLevel(gameLevel);
            }
        }
        else if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                if (data.getExtras().getString("dialog-result").equals("yes")) {

                    this.deleteGameLevel(deleteReservedGameLevel);
                }
            }
        }
        else if (requestCode == 3) {

            if (resultCode == RESULT_OK) {

                GameLevel gameLevel = (GameLevel) data.getExtras().getSerializable("game-level");

                loadingLayout.setVisibility(View.VISIBLE);

                this.editGameLevel(gameLevel);
            }
        }
    }

    private void updateGameLevelsList() {
        loadingLayout.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().readGameLevelsFromServer(new OnGameLevelsReadListener() {
            @Override
            public void gameLevelsRead(final ArrayList<GameLevel> gameLevels) {
                MyApp.getInstance().getCacheHelper().setGameLevels(gameLevels);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameLevelsRV.setAdapter(new GameLevelsAdapter(GameLevelsListActivity.this, gameLevels));
                        loadingLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void addGameLevel(GameLevel gameLevel) {
        loadingLayout.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().addGameLevelToServer(gameLevel, new OnGameLevelModifiesListener() {
            @Override
            public void gameLevelModified(GameLevel gameLevel) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateGameLevelsList();
                    }
                });
            }
        });
    }

    private void editGameLevel(GameLevel gameLevel) {
        loadingLayout.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().editGameLevelInServer(gameLevel, new OnGameLevelModifiesListener() {
            @Override
            public void gameLevelModified(GameLevel gameLevel) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateGameLevelsList();
                    }
                });
            }
        });
    }

    private void deleteGameLevel(GameLevel gameLevel) {
        loadingLayout.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().deleteGameLevelFromServer(gameLevel, new OnGameLevelModifiesListener() {
            @Override
            public void gameLevelModified(GameLevel gameLevel) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateGameLevelsList();
                    }
                });
            }
        });
    }

    public void onAddBtnClicked(View view) {

        GameLevel gameLevel = new GameLevel();
        gameLevel.setNumber(MyApp.getInstance().getCacheHelper().getGameLevels().size() + 1);
        gameLevel.setPrize(0);
        gameLevel.setTableSize(1);
        gameLevel.setTableData("");
        gameLevel.setHasQuestion(false);
        gameLevel.setWords(new ArrayList<WordInfo>());

        startActivityForResult(new Intent(this, GameLevelsAddActivity.class).putExtra("game-level"
                , gameLevel).putExtra("create", true), 1);
    }

    public void onBackBtnClicked(View view) {
        this.finish();
    }

    public void onInfoBtnClicked(View view) {
        startActivity(new Intent(this, PresentActivity.class).putExtra("present-title", "مراحل بازی")
                .putExtra("present-content", gameLevelsRV.getAdapter().getItemCount() + " مرحله در بازی موجود است"));
    }
}