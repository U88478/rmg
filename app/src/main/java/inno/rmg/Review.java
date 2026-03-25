package inno.rmg;

import java.sql.Time;
import java.sql.Timestamp;

public class Review {
    private final String gameId;
    private final String userId;
    private String text;
    private int score;
    private long date;

    public Review(String gameId, String userId, String text, int score) {
        this.gameId = gameId;
        this.userId = userId;
        this.text = text;
        this.score = score;
        this.date = System.currentTimeMillis();
    }

    public void setText(String text) {this.text = text;}

    public void setScore(int score) {this.score = score;}

    public String getGameId() {return gameId;}

    public String getUserId() {return userId;}

    public String getText() {return text;}

    public int getScore() {return score;}

    public long getDate() {return date;}
}