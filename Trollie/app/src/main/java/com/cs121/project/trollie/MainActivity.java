package com.cs121.project.trollie;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    public static String LOG_TAG = "MY LOG TAG:";
    private CallbackManager callbackManager;
    private TextView info;
    private ImageView profileImgView;
    private LoginButton loginButton;

    private PrefUtil prefUtil;
    private IntentUtil intentUtil;

    String name;
    String gender;
    String maleCount99;
    String femaleCount99;
    String maleCountRosies;
    String femaleCountRosies;
    String maleCountPono;
    String femaleCountPono;

    private MyAdapter aa;
    private ArrayList<ListElement> aList;

    // Global SharedPreferences instance
    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Must initialize Firebase library
        Firebase.setAndroidContext(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        prefUtil = new PrefUtil(this);
        intentUtil = new IntentUtil(this);

        info = (TextView) findViewById(R.id.info);
       // profileImgView = (ImageView) findViewById(R.id.profile_img);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        // Ask user permission to access birthday
        //loginButton.setReadPermissions("user_birthday");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                info.setText(message(profile));

                String userId = loginResult.getAccessToken().getUserId();
                String accessToken = loginResult.getAccessToken().getToken();

                // save accessToken to SharedPreference
                prefUtil.saveAccessToken(accessToken);

                String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";

//                Glide.with(MainActivity.this)
//                        .load(profileImgUrl)
//                        .into(profileImgView);

                // Calling Facebook Graph API for user info
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    Log.i("OBJ", object.toString());
                                    name = object.getString("name");
                                    Log.i("FB Name: ", name);
                                    gender = object.getString("gender");
                                    Log.i("FB Gender: ", gender);
                                    String age = object.getString("age_range");
                                    Log.i("FB Age:", age);
                                    //String rel_status = object.getString("relationship_status");
                                    //Log.i("FB Relation: ", rel_status);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,age_range,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                info.setText("Login attempt failed.");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_show_access_token:
                intentUtil.showAccessToken();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayInfo(View v) {
        // Store user's information in SP so we can access it in MyAdapter
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user_name", name);
        editor.commit();
        editor.putString("user_gender", gender);
        editor.commit();

        final Firebase genRef99 = new Firebase("https://glaring-fire-674.firebaseio.com/99Bottles");
        String male_count_99 = obtainMaleCount99(genRef99);
        String female_count_99 = obtainFemaleCount99(genRef99);

        final Firebase genRefPono = new Firebase("https://glaring-fire-674.firebaseio.com/Pono Hawaiian Grill");
        String male_count_Pono = obtainMaleCountPono(genRefPono);
        String female_count_Pono = obtainFemaleCountPono(genRefPono);

        final Firebase genRefRosies = new Firebase("https://glaring-fire-674.firebaseio.com/Rosie McCanns");
        String male_count_Rosies = obtainMaleCountRosies(genRefRosies);
        String female_count_Rosies = obtainFemaleCountRosies(genRefRosies);

        aList.clear();
        aList.add(new ListElement("99 Bottles", male_count_99, female_count_99));
        aList.add(new ListElement("Rosie McCann's", male_count_Rosies, female_count_Rosies));
        aList.add(new ListElement("Pono Hawaiian Grill", male_count_Pono, female_count_Pono));
        Log.i(LOG_TAG, "RIGHT BEFORE aa.notifyData()");
        aa.notifyDataSetChanged();
    }

    // Returns the male count for 99 Bottles
    public String obtainMaleCount99(Firebase ref){

        ref.child("Male count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/99Bottles");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                maleCount99 = snapshot.getValue().toString();
//                int i = Integer.parseInt(snapshot.getValue().toString());
//                //String j = String.valueOf(i + 1);
////                thisOne.child("Male count").setValue(i + 1);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        return (maleCount99);
    }

    // Returns the female count for 99 Bottles
    public String obtainFemaleCount99(Firebase ref){
        ref.child("Female count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/99Bottles");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                femaleCount99 = snapshot.getValue().toString();
//                int i = Integer.parseInt(snapshot.getValue().toString());
//                //String j = String.valueOf(i + 1);
//                thisOne.child("Female count").setValue(i + 1 );
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        return (femaleCount99);
    }

    public String obtainMaleCountRosies(Firebase ref){
        ref.child("Male count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Rosie McCanns");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                maleCountRosies = snapshot.getValue().toString();
//                int i = Integer.parseInt(snapshot.getValue().toString());
//                //String j = String.valueOf(i + 1);
//                thisOne.child("Male count").setValue(i + 1);

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        return (maleCountRosies);
    }

    public String obtainFemaleCountRosies(Firebase ref){
        ref.child("Female count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Rosie McCanns");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                femaleCountRosies = snapshot.getValue().toString();
//                int i = Integer.parseInt(snapshot.getValue().toString());
//                //String j = String.valueOf(i + 1);
//                thisOne.child("Female count").setValue(i + 1 );

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        return (femaleCountRosies);
    }

    //
    public String obtainMaleCountPono(Firebase ref){
        ref.child("Male count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Pono Hawaiian Grill");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                maleCountPono = snapshot.getValue().toString();
//                int i = Integer.parseInt(snapshot.getValue().toString());
//                //String j = String.valueOf(i + 1);
//                thisOne.child("Male count").setValue(i + 1);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        return (maleCountPono);
    }

    public String obtainFemaleCountPono(Firebase ref){
        ref.child("Female count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Pono Hawaiian Grill");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                femaleCountPono = snapshot.getValue().toString();
//                int i = Integer.parseInt(snapshot.getValue().toString());
//                //String j = String.valueOf(i + 1);
//                thisOne.child("Female count").setValue(i + 1 );

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });

        return (femaleCountPono);
    }

    @Override
    public void onResume() {
        super.onResume();
        deleteAccessToken();
        Profile profile = Profile.getCurrentProfile();
        info.setText(message(profile));

        // Initialize List View
        aList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private String message(Profile profile) {
        StringBuilder stringBuffer = new StringBuilder();
        if (profile != null) {
            stringBuffer.append("Welcome ").append(profile.getName());
        }
        return stringBuffer.toString();
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    prefUtil.clearToken();
                    clearUserArea();
                }
            }
        };
    }

    private void clearUserArea() {
        info.setText("You've Successfully Logge Out");
//        profileImgView.setImageDrawable(null);
    }

}