package com.example.recetadecomida;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout layoutLogin, layoutRegister;
    private EditText edTxtEmailLog, edTxtPassLog, edTxtEmailReg, edTxtPassReg;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        layoutLogin = findViewById(R.id.LayoutLogin);
        ImageButton btnLogin = findViewById(R.id.BtnLogin);
        Button btnSignin = findViewById(R.id.BtnSignin);
        edTxtEmailLog = findViewById(R.id.EdTxtEmailLog);
        edTxtPassLog = findViewById(R.id.EdTxtPassLog);

        layoutRegister = findViewById(R.id.LayoutRegister);
        ImageButton btnRegister = findViewById(R.id.BtnRegister);
        Button btnCreate = findViewById(R.id.BtnCreate);
        edTxtEmailReg = findViewById(R.id.EdTxtEmailReg);
        edTxtPassReg = findViewById(R.id.EdTxtPassReg);

        ImageButton btnGoogle = findViewById(R.id.BtnGoogle);
        layoutRegister.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegister.setVisibility(View.GONE);
                layoutLogin.setVisibility(View.VISIBLE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLogin.setVisibility(View.GONE);
                layoutRegister.setVisibility(View.VISIBLE);
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edTxtPassLog.getText().toString().trim();
                String email = edTxtEmailLog.getText().toString().trim();
                String emailPattern = "[a-z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                } else if (email.matches(emailPattern)) {
                    loginUser(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid e-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edTxtEmailReg.getText().toString().trim();
                String password = edTxtPassReg.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                } else if (email.matches("[a-z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    registerUser(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAndSignIn();
            }
        });
    }

    public void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", user.getEmail());
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                            startActivity(intent);
                            finish();
                            edTxtEmailLog.setText("");
                            edTxtPassLog.setText("");
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Log in error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser(String email, String password) {
        if (password.length() < 8) {
            Toast.makeText(LoginActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(LoginActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                    layoutRegister.setVisibility(View.GONE);
                    layoutLogin.setVisibility(View.VISIBLE);
                    edTxtEmailReg.setText("");
                    edTxtPassReg.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
                });
    }

    private void signOutAndSignIn() {
        googleSignInClient.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    signIn();
                }
            });
    }
    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e) {
                Log.w("LoginActivity", "Google sign in failed", e);
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", user.getEmail());
                                editor.apply();

                                Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Log in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}