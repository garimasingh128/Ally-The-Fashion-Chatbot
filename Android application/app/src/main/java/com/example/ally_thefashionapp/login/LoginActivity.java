package in.codepredators.vedanta.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

import in.codepredators.vedanta.Constants;
import in.codepredators.vedanta.R;
import in.codepredators.vedanta.Vedanta;
import in.codepredators.vedanta.home.HomeActivity;
import in.codepredators.vedanta.home.fragments.Home;
import in.codepredators.vedanta.voice.VoiceActivity;
import in.codepredators.vedanta.volley.DiseaseRecommender;

public class LoginActivity extends AppCompatActivity implements Constants {

    ImageView googleSignInButton;
    int RC_SIGN_IN = 400;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignVariables();
        setup();
    }

    private void assignVariables() {
        googleSignInButton = findViewById(R.id.login_btn);
        googleSignInButton.setAlpha(0f);
        googleSignInButton.setTranslationY(50f);
    }

    private void setup() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            googleSignInButton.animate().translationY(0).alpha(1f).setDuration(400);
        else
            checkAccountDetails();

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });


    }

    void checkAccountDetails() {
        try {
            if (!Vedanta.getDatabase(this).VedantaDAO().getDoctors().get(0).getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                moveToEnterDetails();
            } else {
                moveToHome();
            }
        } catch (Exception e) {
            moveToEnterDetails();
        }
    }

    public void startSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        Intent signInIntent = GoogleSignIn.getClient(this, gso).getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException ignored) {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkAccountDetails();
                        } else {
                            Toast.makeText(LoginActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void moveToEnterDetails() {
        startActivity(new Intent(this, EnterDetails.class));
        finish();
    }

    private void moveToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
