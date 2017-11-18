package kasper.android.cross_word_admin.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.activities.GameLevelsWordInfoActivity;
import kasper.android.cross_word_admin.activities.YesNoActivity;
import kasper.android.cross_word_admin.models.WordInfo;

public class WordsInfoAdapter extends RecyclerView.Adapter<WordsInfoAdapter.WordInfoHolder> {

    private GameLevelsWordInfoActivity activity;
    private ArrayList<WordInfo> wordInfos;

    public WordsInfoAdapter(GameLevelsWordInfoActivity activity, ArrayList<WordInfo> wordInfos) {
        this.activity = activity;
        this.wordInfos = wordInfos;
    }

    @Override
    public WordInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WordInfoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_words_infos, parent, false));
    }

    @Override
    public void onBindViewHolder(final WordInfoHolder holder, int position) {
        holder.titleTV.setText("سوال " + (position + 1));
        holder.questionTV.setText(wordInfos.get(position).getQuestion().length() > 0 ? "سوال : "
                + wordInfos.get(position).getQuestion() : "سوال : " + "-");
        holder.wordTV.setText("کلمه : " + wordInfos.get(position).getAnswer());

        holder.upBtn.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        holder.upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordInfo temp = wordInfos.get(holder.getAdapterPosition() - 1);
                wordInfos.set(holder.getAdapterPosition() - 1, wordInfos
                        .get(holder.getAdapterPosition()));
                wordInfos.set(holder.getAdapterPosition(), temp);

                notifyItemChanged(holder.getAdapterPosition());
                notifyItemChanged(holder.getAdapterPosition() - 1);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.setYesNoActivityReservedCallback(new Runnable() {
                    @Override
                    public void run() {

                        wordInfos.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });

                activity.startActivityForResult(new Intent(activity, YesNoActivity.class)
                        .putExtra("dialog-title", "حذف کلمه").putExtra("dialog-content"
                                , "آیا می خواهید کلمه حذف شود ؟"), 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.wordInfos.size();
    }

    class WordInfoHolder extends RecyclerView.ViewHolder {

        TextView titleTV;
        ImageButton upBtn;
        ImageButton deleteBtn;
        TextView questionTV;
        TextView wordTV;

        WordInfoHolder(View itemView) {
            super(itemView);
            this.titleTV = itemView.findViewById(R.id.adapter_words_info_title_text_view);
            this.upBtn = itemView.findViewById(R.id.adapter_words_info_up_button);
            this.deleteBtn = itemView.findViewById(R.id.adapter_words_info_delete_button);
            this.questionTV = itemView.findViewById(R.id.adapter_words_info_question_text_view);
            this.wordTV = itemView.findViewById(R.id.adapter_words_info_word_text_view);
        }
    }
}