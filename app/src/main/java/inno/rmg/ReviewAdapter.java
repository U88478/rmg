package inno.rmg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviews;
    private Context context;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);

        // get the game to show its title and cover
        Game game = DataManager.getInstance().getGameById(review.getGameId());

        if (game != null) {
            holder.tvGameTitle.setText(game.getTitle());
            // cover image
            int resId = context.getResources().getIdentifier(
                    game.getCoverUrl(), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.ivGameCover.setImageResource(resId);
            }
        }

        holder.tvReviewSnippet.setText(review.getText());
        holder.tvScore.setText(String.valueOf(review.getScore()));
        holder.tvDate.setText(String.valueOf(review.getDate()));
    }

    @Override
    public int getItemCount() { return reviews.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGameCover;
        TextView tvGameTitle, tvReviewSnippet, tvScore, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ivGameCover = itemView.findViewById(R.id.ivGameCover);
            tvGameTitle = itemView.findViewById(R.id.tvGameTitle);
            tvReviewSnippet = itemView.findViewById(R.id.tvReviewSnippet);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}