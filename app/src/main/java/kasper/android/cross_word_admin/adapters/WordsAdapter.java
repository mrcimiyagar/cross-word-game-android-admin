package kasper.android.cross_word_admin.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.activities.MessagesActivity;
import kasper.android.cross_word_admin.activities.WordDetailsActivity;
import kasper.android.cross_word_admin.activities.WordsActivity;
import kasper.android.cross_word_admin.activities.YesNoActivity;
import kasper.android.cross_word_admin.models.Message;
import kasper.android.cross_word_admin.models.Word;
import kasper.android.cross_word_admin.utils.PersianCalendar;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordVH> {

    private WordsActivity activity;
    private List<Word> words;

    public WordsAdapter(WordsActivity activity, List<Word> words) {
        this.activity = activity;
        this.words = words;
        this.notifyDataSetChanged();
    }

    @Override
    public WordVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WordVH(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.adapter_words, parent, false));
    }

    @Override
    public void onBindViewHolder(final WordVH holder, int position) {
        final Word word = this.words.get(position);
        holder.wordTV.setText(word.getWord());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setDeleteReservedWord(word);
                activity.startActivityForResult(new Intent(activity, YesNoActivity.class).putExtra
                        ("dialog-title", "حذف پیام").putExtra("dialog-content",
                        "آیا می خواهید کلمه ی '" + word.getWord() + "' حذف شود ؟"), 1);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, WordDetailsActivity.class)
                        .putExtra("word", word));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.words.size();
    }

    class WordVH extends RecyclerView.ViewHolder {

        TextView wordTV;
        ImageButton deleteBtn;

        WordVH(View itemView) {
            super(itemView);
            this.wordTV = itemView.findViewById(R.id.adapter_words_content_text_view);
            this.deleteBtn = itemView.findViewById(R.id.adapter_words_delete_image_button);
        }
    }
}
