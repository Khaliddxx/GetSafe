package com.roseik.getsafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roseik.getsafe.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class Settings extends AppCompatActivity {

    FloatingActionButton nav_btn2;
    Button profile_nav2, settings_nav2, emergency_nav2, login_nav2, harassmap_nav2;
    CheckBox police_cb, location_cb, alarm_cb, record_cb;

    ListView listView;
    List<Map<String, String>> dataC = new ArrayList<Map<String, String>>();

    Button addC, removeC;
    SharedPreferences contactNo = null;

    //TRY
    boolean myBoolVariable = false;
    private static final String police = "police";
    private static final String location = "location";
    private static final String alarm = "alarm";
    private static final String record = "record";

    SharedPreferences sharedPref = null;

    ArrayList<Integer> markedList;


//get vals from shared pref n put into map
    public void loadInitialValues(Map<String, CheckBox> checkboxMap) {
        for (Map.Entry<String, CheckBox> checkboxEntry: checkboxMap.entrySet()) {
            Boolean checked = sharedPref.getBoolean(checkboxEntry.getKey(), false);
            checkboxEntry.getValue().setChecked(checked);
        }
    }

    public void setupCheckedChangeListener(Map<String, CheckBox> checkboxMap) {
        for (final Map.Entry<String, CheckBox> checkboxEntry: checkboxMap.entrySet()) {
            checkboxEntry.getValue().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    final SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(checkboxEntry.getKey(), isChecked);
                    editor.apply();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nav_btn2 = (FloatingActionButton) findViewById(R.id.navi2);
        profile_nav2 = (Button) findViewById(R.id.profile_nav2);
        emergency_nav2 = (Button) findViewById(R.id.emergency_nav2);
        login_nav2 = (Button) findViewById(R.id.logout_nav2);
        settings_nav2 = (Button) findViewById(R.id.settings_nav2);
        harassmap_nav2 = (Button) findViewById(R.id.harassmap_nav2);

        police_cb = (CheckBox) findViewById(R.id.callpolice);
        location_cb = (CheckBox) findViewById(R.id.sendliveL);
        alarm_cb = (CheckBox) findViewById(R.id.loudAlarm);
        record_cb = (CheckBox) findViewById(R.id.record);

        sharedPref = getSharedPreferences("emergency procedures", Context.MODE_PRIVATE);

        Map<String, CheckBox> checkboxMap = new HashMap();
        checkboxMap.put(police, police_cb);
        checkboxMap.put(location, location_cb);
        checkboxMap.put(alarm, alarm_cb);
        checkboxMap.put(record, record_cb);

        loadInitialValues(checkboxMap);
        setupCheckedChangeListener(checkboxMap);

        addC = (Button) findViewById(R.id.addBtn);


        addC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestContactPermission();
            }
        });

//        itemClicked(police_cb, "police");
//        itemClicked(location_cb, "location");
//        itemClicked(alarm_cb, "alarm");
//        itemClicked(record_cb, "record");

        markedList = new ArrayList<Integer>();
        removeC = (Button) findViewById(R.id.removeBtn);

        removeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int  i : markedList){

                    dataC.remove(markedList.indexOf(i));
                }

                updateList();
            }
        });

        listView = (ListView) findViewById(R.id.contactList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                adapterView.getItemAtPosition(position);

                try {


                    if (!markedList.contains(position)) {
                        listView.setItemChecked(position, true);
                        view.setBackgroundColor(Color.RED);
                        markedList.add(position);
                    } else {
                        listView.setItemChecked(position, false);
                        view.setBackgroundColor(Color.TRANSPARENT);
                        for (int i : markedList) {
                            if (markedList.indexOf(i) == position)
                                markedList.remove(i);
                        }
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Couldn't find item", Toast.LENGTH_LONG).show();
                }
//                dataC.remove(markedList.remove(position));
//
//
//                ((BaseAdapter)adapter).notifyDataSetChanged();
            }
        });



        nav_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (profile_nav2.getVisibility() == View.VISIBLE) {
                    profile_nav2.setVisibility(View.INVISIBLE);
                    settings_nav2.setVisibility(View.INVISIBLE);
                    emergency_nav2.setVisibility(View.INVISIBLE);
                    login_nav2.setVisibility(View.INVISIBLE);
                    harassmap_nav2.setVisibility(View.INVISIBLE);
                } else {
                    profile_nav2.setVisibility(View.VISIBLE);
                    settings_nav2.setVisibility(View.VISIBLE);
                    emergency_nav2.setVisibility(View.VISIBLE);
                    login_nav2.setVisibility(View.VISIBLE);
                    harassmap_nav2.setVisibility(View.VISIBLE);
                }
            }
        });


        profile_nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(i);

            }
        });
        login_nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).apply();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

            }
        });
        emergency_nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

        harassmap_nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), HarassMap.class);
                    startActivity(i);
            }
        });
        settings_nav2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_nav2.setVisibility(View.INVISIBLE);
                settings_nav2.setVisibility(View.INVISIBLE);
                emergency_nav2.setVisibility(View.INVISIBLE);
                login_nav2.setVisibility(View.INVISIBLE);
                harassmap_nav2.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void getContact(){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, 1);
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        //                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , 100);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            100);
                }
            } else {
                getContact();
            }
        } else {
            getContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        String name = "";
        String cNumber = "";
        super.onActivityResult(reqCode, resultCode, data);

        switch(reqCode)
        {
            case (1):
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst())
                    {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            phones.moveToFirst();

                            name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                        }
                        cNumber = retrieveContactNumber(name);

                        contactNo = getSharedPreferences(name, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = contactNo.edit();
                        editor.putString("contact number", cNumber);
                        editor.commit();

//                        Toast.makeText(getApplicationContext(),"Added" + name + ": " + cNumber, Toast.LENGTH_SHORT).show();

                        Map<String, String> datum = new HashMap<>(2);
                        datum.put("titleKey", name);
                        datum.put("detailKey", cNumber);
                        dataC.add(datum);

                        updateList();
                    }
                }
        }
    }


    public void updateList(){

        SimpleAdapter adapter = new SimpleAdapter(this, dataC,
                android.R.layout.simple_list_item_2,
                new String[] {"titleKey","detailKey"},
                new int[] {android.R.id.text1, android.R.id.text2 });

        listView.setAdapter(adapter);

    }

    private String retrieveContactNumber(String contactName) {

        String contactNumber = null;

        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactName},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();


        return contactNumber;
    }
//
}
//    public void itemClicked(View v, String name) {
//        if (((CheckBox) v).isChecked()) {
//            SharedPreferences preferences = getSharedPreferences(name, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(name, true);
//            editor.commit();
//        }
//        else{
//            SharedPreferences preferences = getSharedPreferences(name, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(name, false);
//            editor.commit();
//        }
//    }
