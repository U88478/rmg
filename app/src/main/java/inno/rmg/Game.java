package inno.rmg;

import java.io.Serializable;

public class Game implements Serializable {
    private String id;
    private String title;
    private String platform;
    private String developer;
    private int score;
    private int reviewCount;

    public Game(String id, String title, String platform, String developer, int score, int reviewCount) {
        this.id = id;
        this.title = title;
        this.platform = platform;
        this.developer = developer;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    // getters
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public int getScore() { return score; }
    public int getReviewCount() { return reviewCount; }

    public String getCoverUrl() { return "http://";
    }

    public String getId() {
        return id;
    }

    public String getDeveloper() {return developer;}
}
