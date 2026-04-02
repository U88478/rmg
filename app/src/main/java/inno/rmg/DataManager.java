package inno.rmg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static DataManager instance;

    private List<Game> games = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private Profile currentUser;

    // singleton - only one instance exists
    private DataManager() {Map<String, String> media = new HashMap<>();
        media.put("twitter", "GamerUserX_FR");
        media.put("instagram", "GamerUser_FR");
        media.put("discord", "GamerUserDiscord");

        currentUser = new Profile("Gamer_User", media, "La bio détaillée de Gamer_User sur ses préférences de jeu, ses genres favoris, et une description courte et spirituelle de sa philosophie du jeu. Passionné des RPGs et les jeux de simulation.");

        currentUser.addFavourite("g1");
        currentUser.addFavourite("g2");

        games.add(new Game("g1", "Baldur's Gate 3", "PC / PS5", 96, 48000));
        games.add(new Game("g2", "Zelda: Tears of the Kingdom", "Nintendo Switch", 94, 32000));
        games.add(new Game("g3", "Elden Ring", "PC / Console", 91, 55000));
        games.add(new Game("g4", "Final Fantasy XVI", "PS5", 85, 1200));
        games.add(new Game("g5", "Diablo IV", "PC / Console", 75, 3200));
        games.add(new Game("g6", "Starfield", "PC / Xbox", 60, 23400));

        reviews.add(new Review("g1", currentUser.getUserId(), "Chef d'oeuvre absolu, la liberté et la créativité sont inégalées.", 96, "10/06/2026"));
        reviews.add(new Review("g2", currentUser.getUserId(), "Nintendo frappe encore très fort.", 94, "12/06/2026"));
        reviews.add(new Review("g3", currentUser.getUserId(), "Difficile mais tellement gratifiant.", 91, "15/06/2026"));
        reviews.add(new Review("g4", currentUser.getUserId(), "Très bon JRPG, histoire captivante.", 85, "20/06/2026"));
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

    public Game getGameById(String gameId) { return games.get(0);
    }
}
