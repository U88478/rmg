package inno.rmg;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Review implements Serializable {
    @SerializedName("game_id")
    private final String gameId;
    @SerializedName("user_id")
    private final String userId;
    private String username;
    private String text;
    private int score;
    private String date;

    public Review(String gameId, String userId, String username, String text, int score, String date) {
        this.gameId = gameId;
        this.userId = userId;
        this.username = username;
        this.text = text;
        this.score = score;
        this.date = date;
    }

    public String getGameId() {
        return gameId;
    }

    public String getId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }
}