package inno.rmg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import inno.rmg.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignIn.setOnClickListener(v -> handleSignIn());
        binding.btnSignUp.setOnClickListener(v -> handleSignUp());
    }

    private String getUsername() {
        return binding.etUsername.getText().toString().trim();
    }

    private String getPassword() {
        return binding.etPassword.getText().toString().trim();
    }

    private boolean validate() {
        if (TextUtils.isEmpty(getUsername())) {
            binding.tvError.setText("Entre un nom d'utilisateur");
            return false;
        }
        if (getPassword().length() < 6) {
            binding.tvError.setText("Mot de passe trop court (6 caractères min)");
            return false;
        }
        return true;
    }

    private void handleSignIn() {
        Log.d("RMG", "signing in with: " + getUsername() + " / " + getPassword().length() + " chars");
        if (!validate()) return;
        setLoading(true);

        SupabaseRepository.getInstance().signIn(getUsername(), getPassword(),
                new DataCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        String[] parts = result.split("\\|");
                        String userId = parts[0];
                        String token = parts[1];
                        SessionManager.getInstance(AuthActivity.this)
                                .saveSession(userId, getUsername(), token);
                        // create/update profile in DataManager
                        DataManager.getInstance().getCurrentUser().setId(userId);
                        goToMain();
                    }

                    @Override
                    public void onError(String error) {
                        setLoading(false);
                        Log.e("RMG", "SignIn error: " + error);
                        binding.tvError.setText("Identifiants incorrects");
                    }
                });
    }

    private void handleSignUp() {
        Log.d("RMG", "handleSignUp called");
        if (!validate()) return;
        setLoading(true);

        SupabaseRepository.getInstance().signUp(getUsername(), getPassword(),
                new DataCallback<String>() {
                    @Override
                    public void onSuccess(String userId) {
                        // save session
                        SessionManager.getInstance(AuthActivity.this)
                                .saveSession(userId, getUsername(), "");
                        // create profile in Supabase
                        SupabaseRepository.getInstance().createProfile(userId, getUsername(),
                                new DataCallback<Void>() {
                                    @Override
                                    public void onSuccess(Void data) {
                                        DataManager.getInstance().getCurrentUser().setId(userId);
                                        goToMain();
                                    }

                                    @Override
                                    public void onError(String error) {
                                        setLoading(false);
                                        binding.tvError.setText("Erreur lors de la création du profil");
                                    }
                                });
                    }

                    @Override
                    public void onError(String error) {
                        setLoading(false);
                        binding.tvError.setText("Nom d'utilisateur déjà pris");
                    }
                });
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setLoading(boolean loading) {
        binding.btnSignIn.setEnabled(!loading);
        binding.btnSignUp.setEnabled(!loading);
        binding.tvError.setText("");
    }
}