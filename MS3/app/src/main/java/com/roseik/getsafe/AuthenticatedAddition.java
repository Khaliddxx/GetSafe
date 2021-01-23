package com.roseik.getsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class AuthenticatedAddition extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";

    static class MyRequest {
        String email;
        String password;
    };
    static class MyResponse {
        Integer result;//???????************************************
    };

    Gson gson;
    RequestQueue queue;
    FirebaseUser user;

    EditText email, password;
    //TextView result;
    Button login, signup;
    boolean validUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = FirebaseAuth.getInstance().getCurrentUser();

        queue = Volley.newRequestQueue(this);
        gson = new Gson();

        EditText email = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);

        //result = (TextView)findViewById(R.id.result_preview);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(v -> {
            // Code here executes on main thread after user presses button
            Log.d("Connection", "Starting...");
            user.getIdToken(true).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Could not get authentication token.");
                    Toast.makeText(AuthenticatedAddition.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Connection", "Got token");
                String idToken = task.getResult().getToken();
                StringRequest example = new StringRequest(Request.Method.POST,
                        "http://10.0.2.2:3000/login",//!!!!!!!!!!!!!!!*************************************
                        response -> {
                            MyResponse r = gson.fromJson(response, MyResponse.class); //!!!!!!!!!***************
                            Log.d(TAG, "Responded with " + response);
                            //result.setText("" + r.result);  **********************
                            validUser = true;
                        }, error -> {
                    Log.e(TAG, "Connection error.");
                    Toast.makeText(AuthenticatedAddition.this, "Connection error. Try again later.", Toast.LENGTH_SHORT).show();
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
                        r.email = email.getText().toString();
                        r.password = password.getText().toString();

                        String json = gson.toJson(r);
                        return json.getBytes();
                    }
                };
                queue.add(example);
            });
        });

        //Button testNotificationButton = (Button) findViewById(R.id.get_test_notification);
//        testNotificationButton.setOnClickListener(v -> {
//            user.getIdToken(true).addOnCompleteListener(task -> {
//                if (!task.isSuccessful()) {
//                    Log.e(TAG, "Could not get authentication token.");
//                    Toast.makeText(AuthenticatedAddition.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Log.d("Connection", "Got token");
//                String idToken = task.getResult().getToken();
//                StringRequest example = new StringRequest(Request.Method.POST,
//                        "http://10.0.2.2:3000/send_test_notification",
//                        response -> {
//                            Log.d(TAG, "Check your notifications.");
//                        }, error -> {
//                    Log.e(TAG, "Connection error.");
//                    Toast.makeText(AuthenticatedAddition.this, "Connection error. Try again later.", Toast.LENGTH_SHORT).show();
//                }
//                ) {
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", "Token " + idToken);
//                        params.put("Content-Type", "application/json");
//                        return params;
//                    }
//
//                    @Override
//                    public byte[] getBody() {
//                        return "{}".getBytes();
//                    }
//                };
//                queue.add(example);
//            });
//        });
    }

}