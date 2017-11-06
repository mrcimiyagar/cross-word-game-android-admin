package kasper.android.cross_word_admin.adapters;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.activities.GameLevelsAddActivity;
import kasper.android.cross_word_admin.activities.GameLevelsListActivity;
import kasper.android.cross_word_admin.activities.YesNoActivity;
import kasper.android.cross_word_admin.models.GameLevel;

public class GameLevelsAdapter extends RecyclerView.Adapter<GameLevelsAdapter.GameLevelHolder> {

    private GameLevelsListActivity activity;
    private ArrayList<GameLevel> gameLevels;

    public GameLevelsAdapter(GameLevelsListActivity activity, ArrayList<GameLevel> gameLevels) {
        this.activity = activity;
        this.gameLevels = gameLevels;
        this.notifyDataSetChanged();
    }

    @Override
    public GameLevelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameLevelHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adatper_game_levels, parent, false));
    }

    @Override
    public void onBindViewHolder(final GameLevelHolder holder, int position) {

        final GameLevel gameLevel = gameLevels.get(position);

        holder.levelNumberTV.setText("مرحله ی " + gameLevel.getNumber());
        holder.levelPrizeTV.setText(gameLevel.getPrize() + " سکه");
        holder.tableSizeTV.setText(gameLevel.getTableSize() + " * " + gameLevel.getTableSize());
        holder.tableWordsCountTV.setText(gameLevel.getWords().size() + " کلمه");
        holder.questionMarkIV.setVisibility(gameLevel.getHasQuestion() ? View.VISIBLE : View.GONE);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.setDeleteReservedGameLevel(gameLevel);
                activity.startActivityForResult(new Intent(activity, YesNoActivity.class).putExtra
                        ("dialog-title", "حذف مرحله").putExtra("dialog-content", "آیا می خواهید مرحله ی "
                        + (holder.getAdapterPosition() + 1) + " حذف شود ؟"), 2);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.startActivityForResult(new Intent(activity, GameLevelsAddActivity.class)
                        .putExtra("game-level", gameLevel).putExtra("create", false), 3);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameLevels.size();
    }

    class GameLevelHolder extends RecyclerView.ViewHolder {

        AppCompatTextView levelNumberTV;
        AppCompatTextView tableSizeTV;
        AppCompatTextView levelPrizeTV;
        AppCompatTextView tableWordsCountTV;
        AppCompatImageView questionMarkIV;
        AppCompatImageButton deleteBtn;

        GameLevelHolder(View itemView) {
            super(itemView);
            this.levelNumberTV = itemView.findViewById(R.id.adapter_game_levels_level_number_text_view);
            this.tableSizeTV = itemView.findViewById(R.id.adapter_game_levels_table_size_text_view);
            this.levelPrizeTV = itemView.findViewById(R.id.adapter_game_levels_level_prize_text_view);
            this.tableWordsCountTV = itemView.findViewById(R.id.adapter_game_levels_words_count_text_view);
            this.questionMarkIV = itemView.findViewById(R.id.adapter_game_levels_question_mark_image_view);
            this.deleteBtn = itemView.findViewById(R.id.adapter_game_levels_delete_image_button);
        }
    }
}