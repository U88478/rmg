package inno.rmg;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {
    private String id;
    private String name;
    private HashMap<String, String> media;
    private String bio;
    private List<Review> reviews;
    private int games_rated;
    private float rate_avg;

    public Profile(String name, Map<String, String> media, String bio) {
        this.name = name;
        this.media = new HashMap<>(media);
        this.bio = bio;
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

    public void getUserId() {

    }

    public String getId() {return id;}
}
