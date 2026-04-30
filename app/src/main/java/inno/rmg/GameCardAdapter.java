package inno.rmg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.ViewHolder> {

    private List<Game> games;
    private Context context;

    public GameCardAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    // Called when RecyclerView needs a new empty card view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_game_card, parent, false);
        return new ViewHolder(view);
    }

    // Called to fill a card with actual data at a given position
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Game game = games.get(position);
        holder.tvTitle.setText(game.getTitle());
        holder.tvPlatform.setText(game.getPlatform());
        int avg = (int) DataManager.getInstance().getAverageScoreForGame(game.getId());
        holder.tvScore.setText(String.valueOf(avg));
        holder.tvReviewCount.setText(DataManager.getInstance().getReviewsForGame(game.getId()).size() + " critiques");
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GameDetailActivity.class);
            intent.putExtra("game", game);
            context.startActivity(intent);
        });
        Log.d("RMG", "Loading cover: " + game.getCoverUrl());
        Glide.with(context)
                .load(game.getCoverUrl())
                .placeholder(R.color.cardBackground)
                .error(R.color.cardBackground)
                .into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Game> newGames) {
        this.games = newGames;
        notifyDataSetChanged();
    }

    // ViewHolder = holds references to the views inside one card
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover; // add this
        TextView tvTitle, tvPlatform, tvReviewCount, tvScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover); // add this
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPlatform = itemView.findViewById(R.id.tvPlatform);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvReviewCount = itemView.findViewById(R.id.tvReviewCount);
        }
    }
}