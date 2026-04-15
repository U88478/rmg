package inno.rmg;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

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
        String userId = DataManager.getInstance().getCurrentUser().getUserId();

        Review review = new Review(game.getId(), userId, text, score, date);
        DataManager.getInstance().addReview(review);

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}