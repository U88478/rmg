package inno.rmg;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import inno.rmg.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
    List<Review> reviews = DataManager.getInstance()
            .getReviewsForUser(DataManager.getInstance().getCurrentUser().getId());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        Profile user = DataManager.getInstance().getCurrentUser();

        binding.tvUsername.setText(user.getName());
        binding.tvBio.setText(user.getBio());
        binding.tvTwitter.setText(user.getMedia().get("twitter"));
        binding.tvInstagram.setText(user.getMedia().get("instagram"));
        binding.tvDiscord.setText(user.getMedia().get("discord"));
        binding.tvStatGamesRated.setText(String.valueOf(user.getGames_rated()));
        binding.tvStatReviews.setText(String.valueOf(user.getReviews()));
        binding.tvStatAvg.setText(String.valueOf(user.getRate_avg()));
        binding.rvTabContent.setLayoutManager(new LinearLayoutManager(this));


        int[] hist = new int[101];

        for (Review r : reviews) {
            hist[r.getScore()]++;
        }

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            entries.add(new BarEntry(i, hist[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(0xFF7B2FBE);
        dataSet.setDrawValues(false);  // don't show numbers on top of bars

        BarData barData = new BarData(dataSet);

        binding.barChart.setData(barData);
        binding.barChart.getDescription().setEnabled(false); // remove "description" label
        binding.barChart.getLegend().setEnabled(false);       // remove legend
        binding.barChart.getAxisRight().setEnabled(false);    // remove right axis
        binding.barChart.getAxisLeft().setAxisMinimum(0);
        binding.barChart.getAxisLeft().setAxisMaximum(100);
        binding.barChart.setTouchEnabled(false);              // no interaction needed
        binding.barChart.invalidate();                        // triggers redraw

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Profil"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Critiques"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Coup de Cœur"));

        showProfilTab();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        showProfilTab();
                        break;
                    case 1:
                        showCritiquesTab();
                        break;
                    case 2:
                        showCoupDeCoeurTab();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void showProfilTab() {

    }

    private void showCritiquesTab() {
        ReviewAdapter adapter = new ReviewAdapter(this, reviews);
        binding.rvTabContent.setAdapter(adapter);
    }

    private void showCoupDeCoeurTab() {
    }
}