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
        currentUser = new Profile(); // empty but not null
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Profile getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Profile user) {
        this.currentUser = user;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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
            if (r.getId().equals(userId)) result.add(r);
        }
        return result;
    }

    public Game getGameById(String gameId) {
        for (Game g : games) {
            if (g.getId().equals(gameId)) {
                return g;
            }
        }
        return null;
    }

    public List<Game> searchGames(String query) {
        List<Game> result = new ArrayList<>();
        for (Game g : games) {
            if (g.getTitle().toLowerCase().contains(query.toLowerCase())) {
                result.add(g);
            }
        }
        return result;
    }

    public void addReview(Review review) {
        // remove existing review from same user for same game first
        reviews.removeIf(r -> r.getGameId().equals(review.getGameId())
                && r.getId().equals(review.getId()));
        reviews.add(review);
    }

    public void deleteReview(String gameId, String userId) {
        reviews.removeIf(r -> r.getGameId().equals(gameId)
                && r.getId().equals(userId));
    }

    public Review getReviewByUserAndGame(String userId, String gameId) {
        for (Review r : reviews) {
            if (r.getId().equals(userId) && r.getGameId().equals(gameId)) {
                return r;
            }
        }
        return null;
    }

    public double getAverageScoreForGame(String gameId) {
        List<Review> gameReviews = getReviewsForGame(gameId);
        if (gameReviews.isEmpty()) return 0;
        int sum = 0;
        for (Review r : gameReviews) sum += r.getScore();
        return (double) sum / gameReviews.size();
    }
}
