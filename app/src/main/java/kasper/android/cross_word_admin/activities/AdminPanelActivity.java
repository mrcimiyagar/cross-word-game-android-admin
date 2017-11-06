package kasper.android.cross_word_admin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kasper.android.cross_word_admin.R;

public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }

    public void onGameLevelsBtnClicked(View view) {
        startActivity(new Intent(this, GameLevelsListActivity.class));
    }

    public void onTournamentBtnClicked(View view) {
        startActivity(new Intent(this, TournamentActivity.class));
    }
}