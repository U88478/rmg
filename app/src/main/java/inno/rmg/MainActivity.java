package inno.rmg;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import inno.rmg.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the RecyclerView in the layout
        RecyclerView rvGames = findViewById(R.id.rvGames);

        // Create some fake data for now
        List<Game> games = new ArrayList<>();
        games.add(new Game("Baldur's Gate 3", "PC / PS5", 96, 48000));
        games.add(new Game("Zelda: Tears of the Kingdom", "Nintendo Switch", 94, 32000));
        games.add(new Game("Elden Ring", "PC / Console", 91, 55000));
        games.add(new Game("Final Fantasy XVI", "PS5", 85, 1200));

        // Tell the RecyclerView to stack items vertically
        rvGames.setLayoutManager(new LinearLayoutManager(this));

        // Give it the adapter with the data
        rvGames.setAdapter(new GameCardAdapter(this, games));
    }
}