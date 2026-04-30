package inno.rmg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = DataManager.getInstance().getCurrentUser();
        reviews = DataManager.getInstance().getReviewsForUser(user.getId());
        games = DataManager.getInstance().getGames();

        // bind user info
        binding.tvUsername.setText(user.getName());
        binding.tvBio.setText(user.getBio());

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
        binding.barChart.setBackgroundColor(0xFF1a1a2e);
        dataSet.setColor(0xFF7B2FBE);
        binding.barChart.getAxisLeft().setTextColor(0xFFFFFFFF);
        binding.barChart.getXAxis().setTextColor(0xFFFFFFFF);
        binding.barChart.getAxisLeft().setGridColor(0xFF444444);
        binding.barChart.getXAxis().setGridColor(0xFF444444);
        binding.barChart.setTouchEnabled(false);
        binding.barChart.invalidate();

        // tabs
        binding.rvTabContent.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Profil"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Critiques"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Coup de Cœur"));

        binding.btnLogout.setOnClickListener(v -> {
            SessionManager.getInstance(requireContext()).clearSession();
            DataManager.getInstance().setGames(new ArrayList<>());
            DataManager.getInstance().setReviews(new ArrayList<>());
            DataManager.getInstance().setCurrentUser(new Profile());
            Intent intent = new Intent(requireContext(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        binding.btnEditBio.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
            builder.setTitle("Modifier la bio");

            EditText input = new EditText(requireContext());
            input.setText(user.getBio());
            input.setTextColor(0xFF000000);
            input.setHintTextColor(0xFF888888);
            input.setPadding(48, 24, 48, 24);
            builder.setView(input);

            builder.setPositiveButton("Sauvegarder", (dialog, which) -> {
                String newBio = input.getText().toString().trim();
                // update locally
                user.setBio(newBio);
                binding.tvBio.setText(newBio);
                // update in Supabase
                SupabaseRepository.getInstance().updateBio(user.getId(), newBio, new DataCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.d("RMG", "Bio updated");
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("RMG", "Bio update failed: " + error);
                    }
                });
            });

            builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
            builder.show();
        });

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
        binding.layoutProfilStats.setVisibility(View.VISIBLE);
        binding.rvTabContent.setVisibility(View.GONE);
    }

    private void showCritiquesTab() {
        binding.layoutProfilStats.setVisibility(View.GONE);
        binding.rvTabContent.setVisibility(View.VISIBLE);
        String userId = DataManager.getInstance().getCurrentUser().getId();
        List<Review> userReviews = DataManager.getInstance().getReviewsForUser(userId);
        ReviewAdapter adapter = new ReviewAdapter(requireContext(), userReviews);
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
    public void onResume() {
        super.onResume();
        reviews = DataManager.getInstance().getReviewsForUser(user.getId());
        // refresh whichever tab is currently selected
        int selected = binding.tabLayout.getSelectedTabPosition();
        switch (selected) {
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}