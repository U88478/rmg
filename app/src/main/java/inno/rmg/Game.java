package inno.rmg;

import java.io.Serializable;

public class Game implements Serializable {
    private String title;
    private String platform;
    private int score;
    private int reviewCount;

    public Game(String title, String platform, int score, int reviewCount) {
        this.title = title;
        this.platform = platform;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    // getters
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public int getScore() { return score; }
    public int getReviewCount() { return reviewCount; }
}
