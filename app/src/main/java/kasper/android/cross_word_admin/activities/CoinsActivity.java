package kasper.android.cross_word_admin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.callbacks.OnHelpCoinsUpdatedListener;
import kasper.android.cross_word_admin.callbacks.OnStoreCoinsUpdatedListener;
import kasper.android.cross_word_admin.core.MyApp;

public class CoinsActivity extends AppCompatActivity {

    private TextView storeCoinsET;
    private TextView helpCoinsET;
    private RelativeLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);

        storeCoinsET = findViewById(R.id.activity_coins_store_edit_text);
        helpCoinsET = findViewById(R.id.activity_coins_help_edit_text);
        loadingView = findViewById(R.id.activity_coins_loading_view);

        final int storeCoinsCount = getIntent().getExtras().getInt("store-coins");
        final int helpCoinsCount = getIntent().getExtras().getInt("help-coins");

        storeCoinsET.setText(storeCoinsCount + "");
        helpCoinsET.setText(helpCoinsCount + "");
    }

    public void onBackBtnClicked(View view) {
        onBackPressed();
    }

    public void onDoneBtnClicked(View view) {

        final int storeCoinsCount;
        final int helpCoinsCount;

        String storeCoins = storeCoinsET.getText().toString();
        String helpCoins = helpCoinsET.getText().toString();

        try {
            storeCoinsCount = Integer.parseInt(storeCoins);
            helpCoinsCount = Integer.parseInt(helpCoins);
        }
        catch (Exception ignored) {
            Toast.makeText(this, "لطفا مقادیر درست و عددی وارد کنید", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingView.setVisibility(View.VISIBLE);

        MyApp.getInstance().getNetworkHelper().updateStoreCoinsInServer(storeCoinsCount, new OnStoreCoinsUpdatedListener() {
            @Override
            public void storeCoinsUpdated() {
                MyApp.getInstance().getNetworkHelper().updateHelpCoinsInServer(helpCoinsCount, new OnHelpCoinsUpdatedListener() {
                    @Override
                    public void helpCoinsUpdated() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingView.setVisibility(View.GONE);
                                CoinsActivity.this.finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
