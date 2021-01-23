package  com.roseik.getsafe.ui.login;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.roseik.getsafe.AuthenticatedAddition;
import com.roseik.getsafe.MainActivity;
import com.roseik.getsafe.MyFirebaseMessagingService;
import com.roseik.getsafe.R;
import com.roseik.getsafe.Settings;
import com.roseik.getsafe.UserProfile;
import com.roseik.getsafe.ui.login.LoginViewModel;
import com.roseik.getsafe.ui.login.LoginViewModelFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {



    static class MyRequest {
        String fcmToken;
    }

    // Tag for Log.d/Log.e.
    private static final String TAG = "MAIN";

    // This is a random code used for onActivityResult to know which activity it was. It can be anything.
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the activity result is indeed from authentication
        if (requestCode != RC_SIGN_IN) {
            return;
        }

        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Check if authentication succeeded at all
        if (resultCode != RESULT_OK) {
            if (response == null) {
                Log.d(TAG, "User pressed the back button!");
                Log.d(TAG, "You might want to close the app or something.");
            } else {
                Log.e(TAG, response.getError().getMessage());
            }
        }

        // Get user data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "User authenticated with " + user.getEmail());

        // Set up messaging token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
                task -> {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Failed to obtain FCM token.");
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, "FCM token obtained: " + token);

                    sendRegistrationToServer(token);
                }
        );

        Intent intent = new Intent(this, AuthenticatedAddition.class);
        startActivity(intent);
    }

    // You need to send the token to the server after you get it.
    // This is so your server can associate a device
    // with a specific account.
    private void sendRegistrationToServer(String token) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        RequestQueue queue = Volley.newRequestQueue(this);
        Gson gson = new Gson();

        if (user == null) {
            Log.d(TAG, "No user logged in.");
            return;
        }

        // Get the *account authentication* token: The API is going to require authentication.
        // Without authentication, how will we know which user is matched to this device?
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
                return;
            }
            Log.d(TAG, "Got authentication token.");

            String idToken = task.getResult().getToken();

            StringRequest example = new StringRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:3000/register_notification_token",
                    response -> {
                        // Status code 2XX: Seems good!
                        Log.d(TAG, "Server responded okay.");
                    }, error -> {
                // Failed to connect or status code 4XX/5XX: No good!
                Log.e(TAG, "Server responded with communication error.");
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token " + idToken);
                    params.put("Content-Type", "application/json");
                    return params;
                }

                @Override
                public byte[] getBody() {
                    MyRequest r = new MyRequest();
                    r.fcmToken = token;

                    String json = gson.toJson(r);
                    return json.getBytes();
                }
            };
            queue.add(example);
        });
    }








    private LoginViewModel loginViewModel;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);




        // Here, you can list authorized authentication requirements. I only support email in this demo.
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        // This starts the FCM service, enabling you to receive notifications.
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent);

        // Start Authentication UI
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false) // Without Smart Lock, you will have to log in each time. You can enable it by uploading your debug SHA to Firebase.
                        .build(),
                RC_SIGN_IN);





        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);




        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (!isFirstRun) {

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());


            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

//            if(!isFirstRun){
//                Intent i = new Intent(getApplicationContext(), UserProfile.class);
//                startActivity(i);
//            }else {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
//            }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}