package inno.rmg;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;

    private List<Game> games = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private Profile currentUser;

    // singleton - only one instance exists
    private DataManager() {
        games.add(new Game("Baldur's Gate 3", "PC / PS5", 96, 48000));
        games.add(new Game("Zelda: Tears of the Kingdom", "Nintendo Switch", 94, 32000));
        games.add(new Game("Elden Ring", "PC / Console", 91, 55000));
        games.add(new Game("Final Fantasy XVI", "PS5", 85, 1200));
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public List<Game> getGames() {
        return games;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Profile getCurrentUser() {
        return currentUser;
    }

    public List<Review> getReviewsForGame(String gameId) {
        List<Review> result = new ArrayList<>();
        for (Review r : reviews) {
            if (r.getGameId().equals(gameId)) result.add(r);
        }
        return result;
    }

    public List<Review> getReviewsForUser(String userId) {
        List<Review> result = new ArrayList<>();
        for (Review r : reviews) {
            if (r.getUserId().equals(userId)) result.add(r);
        }
        return result;
    }
}
