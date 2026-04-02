package inno.rmg;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {
    private String id;
    private String name;
    private HashMap<String, String> media;
    private String bio;
    private List<Review> reviews;
    private List<String> favouriteIds;

    public Profile(String name, Map<String, String> media, String bio) {
        this.id = "u1";
        this.name = name;
        this.media = new HashMap<>(media);
        this.bio = bio;
        this.reviews = new ArrayList<>();
        this.favouriteIds = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }

    public HashMap<String, String> getMedia() {
        return media;
    }

    public String getBio() {
        return bio;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public int getGames_rated() {
        return reviews.size();
    }

    public float getRate_avg() {
        if (reviews.isEmpty()) return 0;
        int sum = 0;
        for (Review r : reviews) {
            sum += r.getScore();
        }
        return (float) sum / reviews.size();
    }


    public String getUserId() {return id;}

    public List<String> getFavouriteIds() {
        return favouriteIds;
    }

    public void addFavourite(String gameId) {
        favouriteIds.add(gameId);
    }
}
