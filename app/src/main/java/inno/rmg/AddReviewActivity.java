package inno.rmg;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import inno.rmg.databinding.ActivityAddReviewBinding;

public class AddReviewActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_ID = "game_id";
    private ActivityAddReviewBinding binding;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String gameId = getIntent().getStringExtra(EXTRA_GAME_ID);
        game = DataManager.getInstance().getGameById(gameId);

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (game != null) {
            binding.tvGameTitle.setText(game.getTitle());
        }

        binding.sliderScore.setValueFrom(0);
        binding.sliderScore.setValueTo(100);
        binding.sliderScore.setValue(70);
        binding.tvScorePreview.setText("70");

        binding.sliderScore.addOnChangeListener((slider, value, fromUser) ->
                binding.tvScorePreview.setText(String.valueOf((int) value)));

        binding.sliderScore.setStepSize(1);
        binding.sliderScore.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                binding.tvScorePreview.setText(String.valueOf((int) value));
            }
        });

        Review existing = (Review) getIntent().getSerializableExtra("existing_review");
        if (existing != null) {
            binding.sliderScore.setValue(existing.getScore());
            binding.tvScorePreview.setText(String.valueOf(existing.getScore()));
            binding.etComment.setText(existing.getText());
        }

        binding.tvScorePreview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) return;
                try {
                    int val = Integer.parseInt(s.toString());
                    if (val >= 0 && val <= 100) {
                        binding.sliderScore.setValue(val);
                    }
                } catch (NumberFormatException e) {
                    // ignore invalid input
                }
            }
        });
        binding.btnSubmit.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        int score = (int) binding.sliderScore.getValue();
        String text = binding.etComment.getText() != null
                ? binding.etComment.getText().toString().trim() : "";

        if (TextUtils.isEmpty(text)) {
            binding.etComment.setError("Écris quelque chose !");
            return;
        }

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(new Date());
        String userId = DataManager.getInstance().getCurrentUser().getId();
        String username = DataManager.getInstance().getCurrentUser().getName();

        Review existing = (Review) getIntent().getSerializableExtra("existing_review");
        Review review = new Review(game.getId(), userId, username, text, score, date);

        DataManager.getInstance().addReview(review);

        if (existing != null) {
            // editing — use PATCH
            SupabaseRepository.getInstance().updateReview(review, new DataCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Log.d("RMG", "Review updated in Supabase");
                }
                @Override
                public void onError(String error) {
                    Log.e("RMG", "Failed to update review: " + error);
                }
            });
        } else {
            // new review — use POST
            SupabaseRepository.getInstance().addReview(review, new DataCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Log.d("RMG", "Review saved to Supabase");
                }
                @Override
                public void onError(String error) {
                    Log.e("RMG", "Failed to save review: " + error);
                }
            });
        }

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}