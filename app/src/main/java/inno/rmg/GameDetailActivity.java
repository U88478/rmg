package inno.rmg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

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

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        updateCoupDeCoeurButton();

        binding.btnCoupDeCoeur.setOnClickListener(v -> {
            String userId = DataManager.getInstance().getCurrentUser().getId();
            boolean isFav = DataManager.getInstance().getCurrentUser().isCoupDeCoeur(game.getId());

            DataManager.getInstance().getCurrentUser().toggleCoupDeCoeur(game.getId());
            updateCoupDeCoeurButton();

            if (!isFav) {
                SupabaseRepository.getInstance().addFavourite(userId, game.getId(), new DataCallback<Void>() {
                    @Override public void onSuccess(Void data) { Log.d("RMG", "Favourite added"); }
                    @Override public void onError(String error) { Log.e("RMG", "Failed to add favourite: " + error); }
                });
            } else {
                SupabaseRepository.getInstance().removeFavourite(userId, game.getId(), new DataCallback<Void>() {
                    @Override public void onSuccess(Void data) { Log.d("RMG", "Favourite removed"); }
                    @Override public void onError(String error) { Log.e("RMG", "Failed to remove favourite: " + error); }
                });
            }
        });

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

        Glide.with(this)
                .load(game.getBannerUrl())
                .placeholder(R.color.cardBackground)
                .error(R.color.cardBackground)
                .into(binding.ivCover);

        int avg = (int) DataManager.getInstance().getAverageScoreForGame(game.getId());
        binding.tvGlobalScore.setText(String.valueOf(avg));
        binding.tvReviewCount.setText(DataManager.getInstance()
                .getReviewsForGame(game.getId()).size() + " critiques");

        binding.tvReviewCount.setText(DataManager.getInstance().getReviewsForGame(game.getId()).size() + " critiques");

        String userId = DataManager.getInstance().getCurrentUser().getId();
        Review myReview = DataManager.getInstance().getReviewByUserAndGame(userId, game.getId());
        if (myReview != null) {
            binding.tvMyScore.setText(String.valueOf(myReview.getScore()));
        } else {
            binding.tvMyScore.setText("—");
        }
    }

    private void updateCoupDeCoeurButton() {
        boolean isFav = DataManager.getInstance().getCurrentUser().isCoupDeCoeur(game.getId());
        binding.btnCoupDeCoeur.setText(isFav ? "♥ Coup de Cœur" : "♡ Coup de Cœur");
        binding.btnCoupDeCoeur.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        isFav ? 0xFF7B2FBE : 0xFF16213e
                )
        );
        binding.btnCoupDeCoeur.setTextColor(isFav ? 0xFFFFFFFF : 0xFF7B2FBE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindGame();
        if (reviewAdapter != null) {
            reviewAdapter.updateData(DataManager.getInstance().getReviewsForGame(game.getId()));
        }
    }
}