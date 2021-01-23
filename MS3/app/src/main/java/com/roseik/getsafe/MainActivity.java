package com.roseik.getsafe;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.roseik.getsafe.SimpleGestureFilter.SimpleGestureListener;
import com.roseik.getsafe.ui.login.LoginActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.emergency.EmergencyNumber;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SimpleGestureListener {


    int sDirection = 0;
    FloatingActionButton nav_btn, onpress;
    Button profile_nav, settings_nav, emergency_nav, login_nav, harassmap_nav, stopBtn;
    ImageButton EBtn, dismiss;
    boolean emergBool = false;
    boolean cancelAction = false;

    private SimpleGestureFilter detector;

    private FusedLocationProviderClient mFusedLocationClient;
    private int locationRequestCode = 1000;
    private double Latitude = 0.0, Longitude = 0.0;

    SharedPreferences shared_pref = null;
    SharedPreferences contact = null;
    // Map<String, CheckBox> checkboxMap;

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1000: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                                locationRequestCode);
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
//                        if (location != null) {
//                            Latitude = location.getLatitude();
//                            Longitude = location.getLongitude();
//                        }
//                    });
//                } else {
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//        }
//    }

//    public void loadInitialValues(Map<String, CheckBox> checkboxMap) {
//        for (Map.Entry<String, CheckBox> checkboxEntry : checkboxMap.entrySet()) {
//            Boolean checked = shared_pref.getBoolean(checkboxEntry.getKey(), false);
//            checkboxEntry.getValue().setChecked(checked);
//        }
//    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        detector = new SimpleGestureFilter(this, this);
        nav_btn = (FloatingActionButton) findViewById(R.id.navigation);
        onpress = (FloatingActionButton) findViewById(R.id.onpress);
        dismiss = (ImageButton) findViewById(R.id.dismissBtn);
        profile_nav = (Button) findViewById(R.id.profile_nav);
        settings_nav = (Button) findViewById(R.id.settings_nav);
        emergency_nav = (Button) findViewById(R.id.emergency_nav);
        login_nav = (Button) findViewById(R.id.logout_nav);
        harassmap_nav = (Button) findViewById(R.id.harassmap_nav);

        stopBtn = (Button) findViewById(R.id.stopBtn);

        EBtn = (ImageButton) findViewById(R.id.imageButton);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Rect dismissRect = new Rect(dismiss.getLeft(), dismiss.getTop(), dismiss.getRight(), dismiss.getBottom());

        shared_pref = getSharedPreferences("emergency procedures", MODE_PRIVATE);
//        checkboxMap = new HashMap();
//
//        loadInitialValues(checkboxMap);

//        SharedPreferences alarm_preferences = getSharedPreferences("emergency procedures", Context.MODE_PRIVATE);
//        SharedPreferences police_preferences = getSharedPreferences("police",Context.MODE_PRIVATE);
//        SharedPreferences location_preferences = getSharedPreferences("location",Context.MODE_PRIVATE);
//        SharedPreferences record_preferences = getSharedPreferences("record",Context.MODE_PRIVATE);


//        dismiss.setVisibility(View.INVISIBLE);
        profile_nav.setVisibility(View.INVISIBLE);
        settings_nav.setVisibility(View.INVISIBLE);
        emergency_nav.setVisibility(View.INVISIBLE);
        login_nav.setVisibility(View.INVISIBLE);
        harassmap_nav.setVisibility(View.INVISIBLE);


        EBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dismiss.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {

                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    //CANCEL ACTION
                    emergBool = false;
                    dismiss.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Canceling action", Toast.LENGTH_LONG).show();
                    onpress.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        EBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

//                if(event.getAction() == android.view.MotionEvent.ACTION_DOWN ) {
//
//
//                } else

                onpress.setVisibility(View.VISIBLE);

                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        if (!dismissRect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
                        //dismiss.setVisibility(View.INVISIBLE);
                        {
                        } else
                            Toast.makeText(getApplicationContext(), "Canceling Action", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Running Emergency Action", Toast.LENGTH_LONG).show();
                        dismiss.setVisibility(View.INVISIBLE);


                        //-> check the shared preferences/database first ??

                        onpress.setVisibility(View.INVISIBLE);


/********************************************************************************************************************************
 ***************************************   EXECUTING EMERGENCY PROCEDURES   ******************************************************
 ********************************************************************************************************************************/

                        //ALARM:    ---->>>>CHECK SHARED PREFS

//                        //checkboxMap order = police, loc, alarm, rec
                        if (shared_pref.getBoolean("alarm", false)) {
                            stopBtn.setVisibility(View.VISIBLE);
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();

                            stopBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    r.stop();
                                    stopBtn.setVisibility(View.INVISIBLE);
                                }
                            });

                        }


                        //SEND LOCATION TO EMERGENCY CONTACTS:       ---->>>>CHECK SHARED PREFS

                        if (shared_pref.getBoolean("location", false)) {
                        int latitude, longitude;    //int ??*
                            //contact = getSharedPreferences("contact number", MODE_PRIVATE);

                            String phoneNumberWithCountryCode = "+201211063345";  //contact.toString();
                            String message = "My GetSafe Application has detected that I am in danger at http://maps.google.com/maps?saddr=" + Latitude + "," + Longitude;

                            startActivity(
                                    new Intent(Intent.ACTION_VIEW,
                                            Uri.parse(
                                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, message)
                                            )
                                    )
                            );
                          }

//                        Uri uri = Uri.parse("smsto:01211063345");
//                        Intent sms = new Intent(Intent.ACTION_SENDTO, uri);
//                        sms.putExtra("sms_body",
//                                "My GetSafe Application has detected that I am in danger at the location http://maps.google.com/maps?saddr=" + Latitude + "," + Longitude);
//                        startActivity(sms);
                        //}

                        //  O   R
//                        //https://wa.me/+201211063345?text="My%20GetSafe%20Application%20has%20detected%20that%20I%20am%20in%20danger%20at%20http://maps.google.com/maps?saddr="
//                        //whatsapp://send?text=
//                        String whatsAppMessage = "My GetSafe Application has detected that I am in danger at http://maps.google.com/maps?saddr=";// + latitude + "," + longitude;
//                        Intent sendLocation = new Intent();
//                        sendLocation.setAction(Intent.ACTION_SEND);
//                        sendLocation.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
//                        sendLocation.setType("text/plain");
//                        sendLocation.setPackage("com.whatsapp");
//                        startActivity(sendLocation);
//
//                               // O R
//
//                        Uri uri = Uri.parse("smsto:" + "+201211063345");
//                        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
//                        i.setPackage("com.whatsapp");
//                        String text = "My GetSafe Application has detected that I am in danger at http://maps.google.com/maps?saddr=";// + latitude + "," + longitude;
//                        i.putExtra(Intent.EXTRA_TEXT, text);
//                        startActivity(Intent.createChooser(i, ""));


                        if (shared_pref.getBoolean("police", false)) {
//                            Intent call_911= new Intent(Intent.ACTION_CALL);
//                            call_911.setData(Uri.parse("tel:911"));
//                            startActivity(call_911);
//
//
//                            if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                               // return;
//                            }
//                            startActivity(call_911);


                            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);

                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        123);
                            } else {
                                Intent call_911= new Intent(Intent.ACTION_CALL);
                            call_911.setData(Uri.parse("tel:911"));
                            startActivity(call_911);
                               // startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:911")));
                            }
                        }
//                        if (shared_pref.getBoolean("record", false))
//                        {}
//                        if (checkboxMap.get("alarm").isChecked())  {
//                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//                            r.play();
//
//
//                       }

                    }

/********************************************************************************************************************************
 /*******************************************************************************************************************************/

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        dismiss.setVisibility(View.VISIBLE);
                    }
                    stopBtn.setVisibility(View.INVISIBLE);
                }
                return false;

            }
        });

        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (profile_nav.getVisibility() == View.VISIBLE) {
                    profile_nav.setVisibility(View.INVISIBLE);
                    settings_nav.setVisibility(View.INVISIBLE);
                    emergency_nav.setVisibility(View.INVISIBLE);
                    login_nav.setVisibility(View.INVISIBLE);
                    harassmap_nav.setVisibility(View.INVISIBLE);
                } else {
                    profile_nav.setVisibility(View.VISIBLE);
                    settings_nav.setVisibility(View.VISIBLE);
                    emergency_nav.setVisibility(View.VISIBLE);
                    login_nav.setVisibility(View.VISIBLE);
                    harassmap_nav.setVisibility(View.VISIBLE);
                }
            }
        });


        profile_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(i);

            }
        });
        settings_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);

            }
        });
        login_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).apply();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        harassmap_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HarassMap.class);
                startActivity(i);

            }
        });

        emergency_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_nav.setVisibility(View.INVISIBLE);
                settings_nav.setVisibility(View.INVISIBLE);
                emergency_nav.setVisibility(View.INVISIBLE);
                login_nav.setVisibility(View.INVISIBLE);
                harassmap_nav.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {


        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                sDirection = 3;
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                sDirection = 4;
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                sDirection = 2;
                break;
            case SimpleGestureFilter.SWIPE_UP:
                sDirection = 1;
                break;

        }
//        switch (direction) {
//
//            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
//                break;
//            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
//                break;
//            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
//                break;
//            case SimpleGestureFilter.SWIPE_UP :    if (emergBool){
//                dismiss.setVisibility(View.VISIBLE);
//            }else{
//                dismiss.setVisibility(View.INVISIBLE);
//            };
//                break;
//
//        }
        Toast.makeText(this, "DIR " + sDirection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
        Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }
}
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (checkboxMap != null) {
//            checkboxMap.clear();
//            checkboxMap = null;
//        }
//    }
//}