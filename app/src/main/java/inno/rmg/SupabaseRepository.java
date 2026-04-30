package inno.rmg;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupabaseRepository {

    private static SupabaseRepository instance;
    private final SupabaseClient supabase;
    private final Gson gson;
    private final Handler mainThread;

    private SupabaseRepository() {
        supabase = SupabaseClient.getInstance();
        gson = new Gson();
        mainThread = new Handler(Looper.getMainLooper());
    }

    public static SupabaseRepository getInstance() {
        if (instance == null) instance = new SupabaseRepository();
        return instance;
    }

    // ---------------------------------------------------------------- Signs
    public void signUp(String username, String password, DataCallback<String> callback) {
        String email = username + "@rmg.com";
        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        Request request = new Request.Builder()
                .url("https://pedjuinqkirdxfirhawa.supabase.co/auth/v1/signup")
                .addHeader("apikey", supabase.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .build();

        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("RMG", "SignUp response: " + body);
                if (response.isSuccessful()) {
                    // parse user id from response
                    com.google.gson.JsonObject obj = gson.fromJson(body, com.google.gson.JsonObject.class);
                    String userId = obj.getAsJsonObject("user").get("id").getAsString();
                    mainThread.post(() -> callback.onSuccess(userId));
                } else {
                    mainThread.post(() -> callback.onError(body));
                }
            }
        });
    }

    public void signIn(String username, String password, DataCallback<String> callback) {
        String email = username + "@rmg.com";
        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        Request request = new Request.Builder()
                .url("https://pedjuinqkirdxfirhawa.supabase.co/auth/v1/token?grant_type=password")
                .addHeader("apikey", supabase.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .build();

        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("RMG", "SignIn response: " + body);
                if (response.isSuccessful()) {
                    com.google.gson.JsonObject obj = gson.fromJson(body, com.google.gson.JsonObject.class);
                    String userId = obj.getAsJsonObject("user").get("id").getAsString();
                    String accessToken = obj.get("access_token").getAsString();
                    mainThread.post(() -> callback.onSuccess(userId + "|" + accessToken));
                } else {
                    mainThread.post(() -> callback.onError(body));
                }
            }
        });
    }


    // ---------------------------------------------------------------- Games
    public void getGames(DataCallback<List<Game>> callback) {
        Request request = buildGet("games?select=*");
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Type type = new TypeToken<List<Game>>() {
                }.getType();
                List<Game> games = gson.fromJson(body, type);
                mainThread.post(() -> callback.onSuccess(games));
            }
        });
    }

    // ---------------------------------------------------------------- Reviews
    public void getAllReviews(DataCallback<List<Review>> callback) {
        fetchReviews("reviews?select=*", callback);
    }

    public void getReviewsForGame(String gameId, DataCallback<List<Review>> callback) {
        fetchReviews("reviews?select=*&game_id=eq." + gameId, callback);
    }

    public void getReviewsForUser(String userId, DataCallback<List<Review>> callback) {
        fetchReviews("reviews?select=*&user_id=eq." + userId, callback);
    }

    public void addReview(Review review, DataCallback<Void> callback) {
        String json = gson.toJson(review);
        Log.d("RMG", "Sending review JSON: " + json);
        Request request = buildPost("reviews", json);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("RMG", "Supabase response code: " + response.code());
                Log.d("RMG", "Supabase response body: " + body);
                if (response.isSuccessful()) {
                    mainThread.post(() -> callback.onSuccess(null));
                } else {
                    mainThread.post(() -> callback.onError("HTTP " + response.code() + ": " + body));
                }
            }
        });
    }

    public void updateReview(Review review, DataCallback<Void> callback) {
        String json = gson.toJson(review);
        Request request = buildPatch("reviews?game_id=eq." + review.getGameId()
                + "&user_id=eq." + review.getId(), json);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("RMG", "Update response: " + response.code() + " " + body);
                if (response.isSuccessful()) {
                    mainThread.post(() -> callback.onSuccess(null));
                } else {
                    mainThread.post(() -> callback.onError("HTTP " + response.code() + ": " + body));
                }
            }
        });
    }

    public void deleteReview(String gameId, String userId, DataCallback<Void> callback) {
        Request request = buildDelete("reviews?game_id=eq." + gameId + "&user_id=eq." + userId);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mainThread.post(() -> callback.onSuccess(null));
            }
        });
    }

    // ---------------------------------------------------------------- Profile
    public void createProfile(String userId, String username, DataCallback<Void> callback) {
        String json = "{\"id\":\"" + userId + "\",\"name\":\"" + username + "\"}";
        Request request = buildPost("profiles", json);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if (response.isSuccessful()) {
                    mainThread.post(() -> callback.onSuccess(null));
                } else {
                    mainThread.post(() -> callback.onError(body));
                }
            }
        });
    }

    public void getProfile(String userId, DataCallback<Profile> callback) {
        Request request = buildGet("profiles?id=eq." + userId);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Type type = new TypeToken<List<Profile>>() {
                }.getType();
                List<Profile> profiles = gson.fromJson(body, type);
                Profile profile = profiles != null && !profiles.isEmpty() ? profiles.get(0) : null;
                mainThread.post(() -> callback.onSuccess(profile));
            }
        });
    }

    public void getFavourites(String userId, DataCallback<List<String>> callback) {
        Request request = buildGet("favourites?select=game_id&user_id=eq." + userId);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("RMG", "Favourites response: " + body);
                Type type = new TypeToken<List<java.util.Map<String, String>>>() {
                }.getType();
                List<java.util.Map<String, String>> rows = gson.fromJson(body, type);
                List<String> gameIds = new ArrayList<>();
                if (rows != null) {
                    for (java.util.Map<String, String> row : rows) {
                        gameIds.add(row.get("game_id"));
                    }
                }
                mainThread.post(() -> callback.onSuccess(gameIds));
            }
        });
    }

    public void addFavourite(String userId, String gameId, DataCallback<Void> callback) {
        String json = "{\"user_id\":\"" + userId + "\",\"game_id\":\"" + gameId + "\"}";
        Request request = buildPost("favourites", json);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mainThread.post(() -> callback.onSuccess(null));
            }
        });
    }

    public void removeFavourite(String userId, String gameId, DataCallback<Void> callback) {
        Request request = buildDelete("favourites?user_id=eq." + userId + "&game_id=eq." + gameId);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mainThread.post(() -> callback.onSuccess(null));
            }
        });
    }

    // ---------------------------------------------------------------- Helpers
    private Request.Builder baseBuilder(String endpoint) {
        return new Request.Builder()
                .url(supabase.getBaseUrl() + endpoint)
                .addHeader("apikey", supabase.getApiKey())
                .addHeader("Authorization", "Bearer " + supabase.getApiKey())
                .addHeader("Content-Type", "application/json");
    }

    private Request buildGet(String endpoint) {
        return baseBuilder(endpoint).get().build();
    }

    private Request buildPost(String endpoint, String json) {
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        return baseBuilder(endpoint)
                .addHeader("Prefer", "return=minimal")
                .post(body).build();
    }

    private Request buildPatch(String endpoint, String json) {
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        return baseBuilder(endpoint)
                .addHeader("Prefer", "return=minimal")
                .patch(body).build();
    }

    private Request buildDelete(String endpoint) {
        return baseBuilder(endpoint).delete().build();
    }

    private void fetchReviews(String endpoint, DataCallback<List<Review>> callback) {
        Request request = buildGet(endpoint);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Type type = new TypeToken<List<Review>>() {
                }.getType();
                List<Review> reviews = gson.fromJson(body, type);
                mainThread.post(() -> callback.onSuccess(reviews));
            }
        });
    }

    public void updateBio(String userId, String bio, DataCallback<Void> callback) {
        String json = "{\"bio\":\"" + bio + "\"}";
        Request request = buildPatch("profiles?id=eq." + userId, json);
        supabase.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainThread.post(() -> callback.onError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    mainThread.post(() -> callback.onSuccess(null));
                } else {
                    mainThread.post(() -> {
                        try {
                            callback.onError(response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }
}