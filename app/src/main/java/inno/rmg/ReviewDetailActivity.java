package inno.rmg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import inno.rmg.databinding.ActivityReviewDetailBinding;

public class ReviewDetailActivity extends AppCompatActivity {

    public static final String EXTRA_REVIEW = "review";
    private ActivityReviewDetailBinding binding;
    private Review review; // class level
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        review = (Review) getIntent().getSerializableExtra(EXTRA_REVIEW); // no "Review" in front

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        if (review == null) {
            finish();
            return;
        }

        currentUserId = DataManager.getInstance().getCurrentUser().getId(); // class level too

        Game game = DataManager.getInstance().getGameById(review.getGameId());
        if (game != null) {
            binding.tvGameTitle.setText(game.getTitle());
            binding.tvPlatform.setText(game.getPlatform());
        }

        if (review.getId().equals(currentUserId)) {
            binding.btnEdit.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);
        } else {
            binding.btnEdit.setVisibility(View.GONE);
            binding.btnDelete.setVisibility(View.GONE);
        }

        binding.btnDelete.setOnClickListener(v -> {
            DataManager.getInstance().deleteReview(review.getGameId(), currentUserId);

            SupabaseRepository.getInstance().deleteReview(review.getGameId(), currentUserId, new DataCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Log.d("RMG", "Review deleted from Supabase");
                }
                @Override
                public void onError(String error) {
                    Log.e("RMG", "Failed to delete review: " + error);
                }
            });

            finish();
        });

        binding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddReviewActivity.class);
            intent.putExtra(AddReviewActivity.EXTRA_GAME_ID, review.getGameId());
            intent.putExtra("existing_review", review);
            startActivityForResult(intent, 100);
        });

        bindReview();
    }

    private void bindReview() {
        binding.tvScore.setText(String.valueOf(review.getScore()));
        binding.tvReviewText.setText(review.getText());
        binding.tvDate.setText(review.getDate());
        binding.tvUsername.setText(review.getUsername());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Review updated = DataManager.getInstance()
                    .getReviewByUserAndGame(currentUserId, review.getGameId());
            if (updated != null) {
                review = updated;
                bindReview();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}