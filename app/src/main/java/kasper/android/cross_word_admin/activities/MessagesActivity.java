package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.adapters.MessagesAdapter;
import kasper.android.cross_word_admin.callbacks.OnMessageModifiedListener;
import kasper.android.cross_word_admin.callbacks.OnMessagesReadListener;
import kasper.android.cross_word_admin.core.MyApp;
import kasper.android.cross_word_admin.extras.LinearDecoration;
import kasper.android.cross_word_admin.models.Message;

public class MessagesActivity extends AppCompatActivity {

    RecyclerView messagesRV;
    RelativeLayout loadingView;

    private Message deleteReservedMessage;
    public void setDeleteReservedMessage(Message message) {
        this.deleteReservedMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        this.initViews();
        this.initDecoration();

        updateMessagesList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                if (data.getExtras().getString("dialog-result").equals("yes")) {

                    this.deleteMessage(deleteReservedMessage);
                }
            }
        }
        else if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                String msgText = data.getExtras().getString("dialog-result");

                loadingView.setVisibility(View.VISIBLE);

                MyApp.getInstance().getNetworkHelper().addMessageToServer(msgText, new OnMessageModifiedListener() {
                    @Override
                    public void messageModified() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateMessagesList();
                            }
                        });
                    }
                });
            }
        }
    }

    private void initViews() {
        this.loadingView = findViewById(R.id.activity_messages_loading_layout);
        this.messagesRV = findViewById(R.id.activity_messages_recycler_view);
    }

    private void initDecoration() {
        this.messagesRV.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        this.messagesRV.addItemDecoration(new LinearDecoration((int)(MyApp.getInstance()
                .getDisplayHelper().dpToPx(16)), (int)(MyApp.getInstance().getDisplayHelper()
                .dpToPx(16))));
    }

    public void onBackBtnClicked(View view) {
        onBackPressed();
    }

    public void onInfoBtnClicked(View view) {
        startActivity(new Intent(this, PresentActivity.class).putExtra("present-title", "پیام های عمومی")
                .putExtra("present-content", new String[] {
                        messagesRV.getAdapter().getItemCount() + " پیام عمومی ارسال شده است"
                }));
    }

    public void onAddBtnClicked(View view) {
        startActivityForResult(new Intent(this, MessageAddActivity.class), 2);
    }

    private void updateMessagesList() {
        this.loadingView.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().readMessagesFromServer(new OnMessagesReadListener() {
            @Override
            public void messagesRead(final List<Message> messages) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messagesRV.setAdapter(new MessagesAdapter(MessagesActivity.this, messages));
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void deleteMessage(Message message) {
        loadingView.setVisibility(View.VISIBLE);
        MyApp.getInstance().getNetworkHelper().deleteMessageFromServer(message.getId(), new OnMessageModifiedListener() {
            @Override
            public void messageModified() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateMessagesList();
                    }
                });
            }
        });
    }
}
