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
import kasper.android.cross_word_admin.activities.YesNoActivity;
import kasper.android.cross_word_admin.models.Message;
import kasper.android.cross_word_admin.utils.PersianCalendar;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageVH> {

    private MessagesActivity activity;
    private List<Message> messages;

    public MessagesAdapter(MessagesActivity activity, List<Message> messages) {
        this.activity = activity;
        this.messages = messages;
        this.notifyDataSetChanged();
    }

    @Override
    public MessageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageVH(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.adapter_messages, parent, false));
    }

    @Override
    public void onBindViewHolder(final MessageVH holder, int position) {
        final Message msg = this.messages.get(position);
        holder.contentTV.setText(msg.getContent());
        PersianCalendar pc = new PersianCalendar(msg.getTime());
        holder.timeTV.setText(pc.getPersianShortDateTime());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setDeleteReservedMessage(msg);
                activity.startActivityForResult(new Intent(activity, YesNoActivity.class).putExtra
                        ("dialog-title", "حذف پیام").putExtra("dialog-content",
                        "آیا می خواهید این پیام حذف شود ؟"), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    class MessageVH extends RecyclerView.ViewHolder {

        TextView contentTV;
        TextView timeTV;
        ImageButton deleteBtn;

        MessageVH(View itemView) {
            super(itemView);
            this.contentTV = itemView.findViewById(R.id.adapter_messages_content_text_view);
            this.timeTV = itemView.findViewById(R.id.adapter_messages_time_text_view);
            this.deleteBtn = itemView.findViewById(R.id.adapter_messages_delete_image_button);
        }
    }
}
