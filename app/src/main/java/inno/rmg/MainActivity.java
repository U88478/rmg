package inno.rmg;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import inno.rmg.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String userId = SessionManager.getInstance(this).getUserId();
        String username = SessionManager.getInstance(this).getUsername();

        // set basic user info from session while we load the rest
        DataManager.getInstance().getCurrentUser().setId(userId);

        loadAllData(userId);

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_search) {
                loadFragment(new SearchFragment());
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void loadAllData(String userId) {
        SupabaseRepository.getInstance().getGames(new DataCallback<List<Game>>() {
            @Override
            public void onSuccess(List<Game> games) {
                DataManager.getInstance().setGames(games);
                Log.d("RMG", "Loaded " + games.size() + " games");

                SupabaseRepository.getInstance().getAllReviews( new DataCallback<List<Review>>() {
                    @Override
                    public void onSuccess(List<Review> reviews) {
                        DataManager.getInstance().setReviews(reviews);

                        SupabaseRepository.getInstance().getFavourites(userId, new DataCallback<List<String>>() {
                            @Override
                            public void onSuccess(List<String> gameIds) {
                                DataManager.getInstance().getCurrentUser().setFavouriteIds(gameIds);

                                SupabaseRepository.getInstance().getProfile(userId, new DataCallback<Profile>() {
                                    @Override
                                    public void onSuccess(Profile profile) {
                                        if (profile != null) {
                                            profile.setFavouriteIds(DataManager.getInstance().getCurrentUser().getFavouriteIds());
                                            DataManager.getInstance().setCurrentUser(profile);
                                        }
                                        loadFragment(new HomeFragment());
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Log.e("RMG", "Failed to load profile: " + error);
                                        loadFragment(new HomeFragment());
                                    }
                                });
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("RMG", "Failed to load favourites: " + error);
                                loadFragment(new HomeFragment());
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("RMG", "Failed to load reviews: " + error);
                        loadFragment(new HomeFragment());
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.e("RMG", "Failed to load games: " + error);
                loadFragment(new HomeFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.commit();
    }
}