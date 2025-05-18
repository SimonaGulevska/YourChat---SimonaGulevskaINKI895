package com.example.yourchat;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.Executors;

public class LoginPhoneNumberActivity extends AppCompatActivity {
    public static final String TAG = "LoginPhoneNumberActivity";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CredentialManager credentialManager;
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    EditText usernameInput;
    EditText passwordInput;
    Button sendOtpBtn;
    Button loginAnonymous;
    Button loginGmail;
    Button loginFb;
    Button loginUserPass;
    Button createUser;
    ProgressBar progressBar;

    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);
        credentialManager = CredentialManager.create(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "login");
        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                .logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        countryCodePicker = findViewById(R.id.login_countrycode);
        phoneInput = findViewById(R.id.login_mobile_number);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        loginAnonymous = findViewById(R.id.login_anonymous);
        loginGmail = findViewById(R.id.login_gmail);
        loginFb = findViewById(R.id.login_fb);
        loginUserPass = findViewById(R.id.login);
        createUser = findViewById(R.id.create);
        usernameInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        sendOtpBtn.setOnClickListener((v) -> {
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Invalid Phone number");
                return;
            }
            Intent intent = new Intent(LoginPhoneNumberActivity.this, LoginOtpActivity.class);
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });

        createUser.setOnClickListener((v) -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                signUpUser(username, password);
            }else {
                Toast.makeText(LoginPhoneNumberActivity.this, R.string.please_populate_credentials, Toast.LENGTH_LONG).show();
            }
        });

        loginUserPass.setOnClickListener((view -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                signInWithUsernamePassword(username, password);
            } else {
                Toast.makeText(LoginPhoneNumberActivity.this, R.string.please_populate_credentials, Toast.LENGTH_LONG).show();
            }
        }));

        loginAnonymous.setOnClickListener((v) -> signInAnonymously());

        loginGmail.setOnClickListener((v) -> signInWithGoogle());
        initializeFbBtn();
    }

    private void initializeFbBtn() {
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_fb);
        loginButton.setBackgroundColor(getResources().getColor(R.color.my_primary));
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            openUsernameActivity(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginPhoneNumberActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUpUser(String email, String password) {
        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                .logEvent("CreateUserClicked", new Bundle());
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                                .logEvent("CreateUserSuccess", new Bundle());

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        openUsernameActivity(user);
                    } else {
                        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                                .logEvent("CreateUserFail", new Bundle());

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginPhoneNumberActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void signInWithUsernamePassword(String username, String password) {
        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                .logEvent(FirebaseAnalytics.Event.LOGIN, new Bundle());

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                                .logEvent("SignInWithUsernamePasswordSuccess", new Bundle());

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        openUsernameActivity(user);
                    } else {
                        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                                .logEvent("SignInWithUsernamePasswordFail", new Bundle());

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginPhoneNumberActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void signInAnonymously() {
        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                .logEvent("SignInAnonymouslyClicked", new Bundle());

        mAuth.signInAnonymously().addOnCompleteListener(LoginPhoneNumberActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                            .logEvent("SignInAnonymouslySuccess", new Bundle());

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    openUsernameActivity(user);

                } else {
                    ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                            .logEvent("SignInAnonymouslyFail", new Bundle());
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(LoginPhoneNumberActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void signInWithGoogle() {
        ((YourChatApplication) getApplication()).getFirebaseAnalytics()
                .logEvent("SignInWithGoogleClicked", new Bundle());

        // [START create_credential_manager_request]
        // Instantiate a Google sign-in request
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        // Create the Credential Manager request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();
        // [END create_credential_manager_request]

        // Launch Credential Manager UI
        credentialManager.getCredentialAsync(
                LoginPhoneNumberActivity.this,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onError(@NonNull androidx.credentials.exceptions.GetCredentialException e) {
                        Log.e(TAG, "Couldn't retrieve user's credentials: " + e.getLocalizedMessage());

                    }

                    @Override
                    public void onResult(GetCredentialResponse result) {
                        // Extract credential from the result returned by Credential Manager
                        handleSignIn(result.getCredential());
                    }
                }
        );
    }

    private void handleSignIn(Credential credential) {
        // Check if credential is of type Google ID
        if (credential instanceof CustomCredential
                && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = credential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.w(TAG, "Credential is not of type Google ID!");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        openUsernameActivity(user);
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    private void openUsernameActivity(FirebaseUser user) {
        Intent intent = new Intent(LoginPhoneNumberActivity.this, LoginUsernameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}