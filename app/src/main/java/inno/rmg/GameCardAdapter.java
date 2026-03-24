package inno.rmg;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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
        holder.tvScore.setText(String.valueOf(game.getScore()));
        holder.tvReviewCount.setText(game.getReviewCount() + " critiques");
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GameDetailActivity.class);
            intent.putExtra("game", game);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return games.size(); }

    // ViewHolder = holds references to the views inside one card
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPlatform, tvReviewCount;
        TextView tvScore;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPlatform = itemView.findViewById(R.id.tvPlatform);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvReviewCount = itemView.findViewById(R.id.tvReviewCount);
        }
    }
}