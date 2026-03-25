package inno.rmg;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import inno.rmg.databinding.ActivityGameDetailBinding;

public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityGameDetailBinding binding = ActivityGameDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Game game = (Game) getIntent().getSerializableExtra("game");

        assert game != null;
        binding.tvGameTitle.setText(game.getTitle());
        binding.tvGlobalScore.setText(String.valueOf(game.getScore()));
        binding.tvReviewCount.setText(String.valueOf(game.getReviewCount()));


    }
}