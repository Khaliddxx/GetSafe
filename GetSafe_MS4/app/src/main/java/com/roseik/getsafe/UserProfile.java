package com.roseik.getsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roseik.getsafe.data.LoginRepository;
import com.roseik.getsafe.ui.login.LoginActivity;

public class UserProfile extends AppCompatActivity {

    FloatingActionButton nav_btn1;
    Button profile_nav1, settings_nav1, emergency_nav1, login_nav1, harassmap_nav1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nav_btn1 =(FloatingActionButton) findViewById(R.id.floaterBtn);
        settings_nav1 = (Button) findViewById(R.id.settings_nav1);
        emergency_nav1 = (Button) findViewById(R.id.emergency_nav1);
        profile_nav1 = (Button) findViewById(R.id.profile_nav1);
        login_nav1 = (Button) findViewById(R.id.logout_nav1);
        harassmap_nav1 = (Button) findViewById(R.id.harassmap_nav1);

        nav_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settings_nav1.getVisibility() == View.VISIBLE){
                    profile_nav1.setVisibility(View.INVISIBLE);
                    settings_nav1.setVisibility(View.INVISIBLE);
                    emergency_nav1.setVisibility(View.INVISIBLE);
                    login_nav1.setVisibility(View.INVISIBLE);
                    harassmap_nav1.setVisibility(View.INVISIBLE);
                }else {
                    profile_nav1.setVisibility(View.VISIBLE);
                    settings_nav1.setVisibility(View.VISIBLE);
                    emergency_nav1.setVisibility(View.VISIBLE);
                    login_nav1.setVisibility(View.VISIBLE);
                    harassmap_nav1.setVisibility(View.VISIBLE);
                }
            }
        });

        settings_nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);

            }
        });


        login_nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).apply();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

            }
        });
        emergency_nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });
        harassmap_nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HarassMap.class);
                startActivity(i);

            }
        });

        profile_nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_nav1.setVisibility(View.INVISIBLE);
                settings_nav1.setVisibility(View.INVISIBLE);
                emergency_nav1.setVisibility(View.INVISIBLE);
                login_nav1.setVisibility(View.INVISIBLE);
                harassmap_nav1.setVisibility(View.INVISIBLE);
            }
        });

    }
}