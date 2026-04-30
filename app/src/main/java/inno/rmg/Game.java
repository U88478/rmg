package inno.rmg;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Game implements Serializable {
    private String id;
    private String title;
    private String platform;
    private String developer;
    private int score;
    @SerializedName("review_count")
    private int reviewCount;
    @SerializedName("cover_url")
    private String coverUrl;

    @SerializedName("banner_url")
    private String bannerUrl;

    @SerializedName("steam_app_id")
    private String steamAppId;

    public Game(String id, String title, String platform, String developer, int score, int reviewCount) {
        this.id = id;
        this.title = title;
        this.platform = platform;
        this.developer = developer;
        this.score = score;
        this.reviewCount = reviewCount;
    }

    // getters
    public String getTitle() {
        return title;
    }

    public String getPlatform() {
        return platform;
    }

    public String getSteamAppId() {
        return steamAppId;
    }

    public String getCoverUrl() {
        return "https://cdn.akamai.steamstatic.com/steam/apps/" + steamAppId + "/library_600x900.jpg";
    }

    public String getBannerUrl() {
        return "https://cdn.akamai.steamstatic.com/steam/apps/" + steamAppId + "/header.jpg";
    }

    public String getId() {
        return id;
    }

    public String getDeveloper() {
        return developer;
    }
}
