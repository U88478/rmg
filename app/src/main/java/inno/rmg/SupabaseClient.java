package inno.rmg;

import okhttp3.OkHttpClient;

public class SupabaseClient {
    private static final String BASE_URL = "https://pedjuinqkirdxfirhawa.supabase.co/rest/v1/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBlZGp1aW5xa2lyZHhmaXJoYXdhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzc0OTU2MjcsImV4cCI6MjA5MzA3MTYyN30.qGTG-uOk0T7DGHP25P78uu2iX332UUu6XUS3yqJ2wO8";

    private static SupabaseClient instance;
    private final OkHttpClient client;

    private SupabaseClient() {
        client = new OkHttpClient();
    }

    public static SupabaseClient getInstance() {
        if (instance == null) instance = new SupabaseClient();
        return instance;
    }

    public OkHttpClient getClient() { return client; }
    public String getBaseUrl() { return BASE_URL; }
    public String getApiKey() { return API_KEY; }
}