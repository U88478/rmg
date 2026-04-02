package inno.rmg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import inno.rmg.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Profile user;
    private List<Review> reviews;
    private List<Game> games;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = DataManager.getInstance().getCurrentUser();
        reviews = DataManager.getInstance().getReviewsForUser(user.getUserId());
        games = DataManager.getInstance().getGames();

        // bind user info
        binding.tvUsername.setText(user.getName());
        binding.tvBio.setText(user.getBio());
        binding.tvTwitter.setText("@" + user.getMedia().getOrDefault("twitter", ""));
        binding.tvInstagram.setText("@" + user.getMedia().getOrDefault("instagram", ""));
        binding.tvDiscord.setText("@" + user.getMedia().getOrDefault("discord", ""));

        // stats
        binding.tvStatGamesRated.setText(String.valueOf(user.getGames_rated()));
        binding.tvStatReviews.setText(String.valueOf(reviews.size()));
        binding.tvStatAvg.setText(String.valueOf(user.getRate_avg()));
        binding.tvStatLikes.setText("0");

        // chart
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
        dataSet.setDrawValues(false);
        BarData barData = new BarData(dataSet);
        binding.barChart.setData(barData);
        binding.barChart.getDescription().setEnabled(false);
        binding.barChart.getLegend().setEnabled(false);
        binding.barChart.getAxisRight().setEnabled(false);
        binding.barChart.getAxisLeft().setAxisMinimum(0);
        binding.barChart.getAxisLeft().setAxisMaximum(5);
        binding.barChart.getXAxis().setAxisMinimum(0);
        binding.barChart.getXAxis().setAxisMaximum(100);
        binding.barChart.setTouchEnabled(false);
        binding.barChart.invalidate();

        // tabs
        binding.rvTabContent.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Profil"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Critiques"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Coup de Cœur"));

        showProfilTab();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: showProfilTab(); break;
                    case 1: showCritiquesTab(); break;
                    case 2: showCoupDeCoeurTab(); break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showProfilTab() {
        binding.layoutProfilStats.setVisibility(View.VISIBLE);
        binding.rvTabContent.setVisibility(View.GONE);
    }

    private void showCritiquesTab() {
        binding.layoutProfilStats.setVisibility(View.GONE);
        binding.rvTabContent.setVisibility(View.VISIBLE);
        ReviewAdapter adapter = new ReviewAdapter(requireContext(), reviews);
        binding.rvTabContent.setAdapter(adapter);
    }

    private void showCoupDeCoeurTab() {
        binding.layoutProfilStats.setVisibility(View.GONE);
        binding.rvTabContent.setVisibility(View.VISIBLE);
        List<Game> favGames = new ArrayList<>();
        for (String gameId : user.getFavouriteIds()) {
            Game g = DataManager.getInstance().getGameById(gameId);
            if (g != null) favGames.add(g);
        }
        GameCardAdapter adapter = new GameCardAdapter(requireContext(), favGames);
        binding.rvTabContent.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}