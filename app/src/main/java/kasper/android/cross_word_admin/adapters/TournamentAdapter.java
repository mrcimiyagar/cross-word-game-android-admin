package kasper.android.cross_word_admin.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kasper.android.cross_word_admin.R;
import kasper.android.cross_word_admin.models.TourPlayer;

public class TournamentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TourPlayer> tourPlayers;

    public TournamentAdapter(ArrayList<TourPlayer> tourPlayers) {
        this.tourPlayers = tourPlayers;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tournament_header, parent, false));
            }
            case 1: {
                return new TourPlayerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tournament_tourplayer, parent, false));
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {

            case 0: {

                ((HeaderHolder) holder).totalDaysTV.setText("تورنمنت فعال  " + 20 + "  روزه است");
                ((HeaderHolder) holder).daysLeftTV.setText(13 + "  روز تا پایان تورنمنت باقی مانده است");

                break;
            }
            case 1: {

                TourPlayer tourPlayer = tourPlayers.get(position - 1);

                ((TourPlayerHolder) holder).scoreTV.setText(tourPlayer.getLevelsDoneCount() + "");
                ((TourPlayerHolder) holder).nameTV.setText(tourPlayer.getName());
                ((TourPlayerHolder) holder).rankTV.setText(position + "");

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return tourPlayers.size() + 1;
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        TextView totalDaysTV;
        TextView daysLeftTV;

        HeaderHolder(View itemView) {
            super(itemView);
            totalDaysTV = (TextView) itemView.findViewById(R.id.adapter_tournament_header_total_days_text_view);
            daysLeftTV = (TextView) itemView.findViewById(R.id.adapter_tournament_header_days_left_text_view);
        }
    }

    private class TourPlayerHolder extends RecyclerView.ViewHolder {

        TextView scoreTV;
        TextView nameTV;
        TextView rankTV;

        TourPlayerHolder(View itemView) {
            super(itemView);
            scoreTV = (TextView) itemView.findViewById(R.id.adapter_tournament_player_score_text_view);
            nameTV = (TextView) itemView.findViewById(R.id.adapter_tournament_player_name_text_view);
            rankTV = (TextView) itemView.findViewById(R.id.adapter_tournament_player_rank_text_view);
        }
    }
}