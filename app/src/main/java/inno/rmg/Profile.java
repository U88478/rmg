package inno.rmg;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private String id;
    private String name;
    private String bio;
    private List<String> favouriteIds;

    // empty constructor for Gson
    public Profile() {
        this.favouriteIds = new ArrayList<>();
    }

    public Profile(String name, String bio) {
        this.name = name;
        this.bio = bio;
        this.favouriteIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getFavouriteIds() {
        if (favouriteIds == null) favouriteIds = new ArrayList<>();
        return favouriteIds;
    }

    public void setFavouriteIds(List<String> ids) {
        this.favouriteIds = ids;
    }

    public void addFavourite(String gameId) {
        getFavouriteIds().add(gameId);
    }

    public void toggleCoupDeCoeur(String gameId) {
        if (getFavouriteIds().contains(gameId)) favouriteIds.remove(gameId);
        else favouriteIds.add(gameId);
    }

    public boolean isCoupDeCoeur(String gameId) {
        return getFavouriteIds().contains(gameId);
    }

    public int getGames_rated() {
        return DataManager.getInstance().getReviewsForUser(id).size();
    }

    public float getRate_avg() {
        List<Review> userReviews = DataManager.getInstance().getReviewsForUser(id);
        if (userReviews.isEmpty()) return 0;
        float sum = 0;
        for (Review r : userReviews) sum += r.getScore();
        return sum / userReviews.size();
    }
}