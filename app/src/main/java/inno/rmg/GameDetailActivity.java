package inno.rmg;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import inno.rmg.databinding.ActivityGameDetailBinding;

public class GameDetailActivity extends AppCompatActivity {
    private ActivityGameDetailBinding binding;
    private Game game;
    private ReviewAdapter reviewAdapter; // class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        game = (Game) getIntent().getSerializableExtra("game");

        bindGame();

        List<Review> reviews = DataManager.getInstance().getReviewsForGame(game.getId());
        reviewAdapter = new ReviewAdapter(this, reviews); // no "ReviewAdapter" in front
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);

        binding.btnRate.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddReviewActivity.class);
            intent.putExtra(AddReviewActivity.EXTRA_GAME_ID, game.getId());
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            bindGame();
            reviewAdapter.updateData(DataManager.getInstance().getReviewsForGame(game.getId()));
        }
    }

    private void bindGame() {
        if (game == null) return;
        binding.tvGameTitle.setText(game.getTitle());
        binding.tvDeveloper.setText(game.getDeveloper());
        binding.tvGlobalScore.setText(String.valueOf(game.getScore()));
        binding.tvReviewCount.setText(game.getReviewCount() + " critiques");

        String userId = DataManager.getInstance().getCurrentUser().getUserId();
        Review myReview = DataManager.getInstance().getReviewByUserAndGame(userId, game.getId());
        if (myReview != null) {
            binding.tvMyScore.setText(String.valueOf(myReview.getScore()));
        } else {
            binding.tvMyScore.setText("—");
        }
    }
}