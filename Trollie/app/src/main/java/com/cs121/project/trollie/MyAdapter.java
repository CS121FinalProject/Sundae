package com.cs121.project.trollie;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

/**
 * Created by shobhit on 1/24/16.
 * Copied from Prof. Luca class code
 */
public class MyAdapter extends ArrayAdapter<ListElement> {
    public static String LOG_TAG = "MY LOG TAG IN MyAdapter:";

    int resource;
    Context context;

    public MyAdapter(Context _context, int _resource, List<ListElement> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;

        //Must initialize Firebase library
        Firebase.setAndroidContext(context);

        // Get users information from MainActivity
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String user_name = settings.getString("user_name", null);
        final String user_gender = settings.getString("user_gender", null);
        Log.i(LOG_TAG, user_name);
        Log.i(LOG_TAG, user_gender);

        ListElement w = getItem(position);

        // Inflate a new view if necessary.
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource,  newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        // Fills in the view.
        TextView tv = (TextView) newView.findViewById(R.id.info_text);
        TextView mc = (TextView) newView.findViewById(R.id.maleCount);
        TextView fc = (TextView) newView.findViewById(R.id.femaleCount);
        TextView b = (Button) newView.findViewById(R.id.checkInBtn);
        ImageView iv = (ImageView) newView.findViewById(R.id.location_img);
        ImageView mi = (ImageView) newView.findViewById(R.id.maleIcon);
        ImageView fi = (ImageView) newView.findViewById(R.id.femaleIcon);

        if ( (w.nickname) == null ) {
            w.nickname = "0";
        }
        if ( (w.gravity) == null ) {
            w.gravity = "0";
        }

        tv.setText(w.message);
        mc.setText(w.nickname);
        fc.setText(w.gravity);

        int mcNum = Integer.parseInt(w.nickname);
        int fcNum = Integer.parseInt(w.gravity);

        if ( mcNum > fcNum ) {
            mi.setImageResource(R.drawable.male_user_green);
        } else if ( fcNum > mcNum ) {
            fi.setImageResource(R.drawable.female_user_green);
        }

        // Set images at the top of each card
        if ( (w.message).equals("99 Bottles") ) {
            iv.setImageResource(R.drawable.ninetynine_logo1);
        }
        if ( (w.message).equals("Rosie McCann's") ) {
            iv.setImageResource(R.drawable.rosiemccannes);
        }
        if ( (w.message).equals("Pono Hawaiian Grill") ) {
            iv.setImageResource(R.drawable.pono_logo);
        }

        // Sets a listener for Check-In button
        b.setTag(new Integer(position));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reacts to a button press.
                // Gets the integer tag of the button.
                String s = v.getTag().toString();

                // If user chooses to check-in at 99 Bottles
                if ( (s).equals("0") ) {
                    Log.i(LOG_TAG, s);

                    // Update 99 Bottles Table
                    final Firebase genRef = new Firebase("https://glaring-fire-674.firebaseio.com/99Bottles");

                    if (user_gender != null) {
                        if ( (user_gender).equals("male") ) {
                            // Update the 'male' count for 99 Bottles
                            obtainMaleCount99(genRef);
                        }
                        else if ( (user_gender).equals("female") ) {
                            // Update the female count for 99 Bottles
                            obtainFemaleCount99(genRef);
                        }
                    }

                }

                // If user chooses to check-in at Rosie McCann's
                if ( (s).equals("1") ) {
                    Log.i(LOG_TAG, s);
                    // Update Rosie McCann's Table
                    final Firebase genRef = new Firebase("https://glaring-fire-674.firebaseio.com/Rosie McCanns");

                    if (user_gender != null) {
                        if ( (user_gender).equals("male") ) {
                            // Update the 'male' count for Pono
                            obtainMaleCountRosies(genRef);
                        }
                        else if ( (user_gender).equals("female") ) {
                            // Update the female count for Pono
                            obtainFemaleCountRosies(genRef);
                        }
                    }
                }

                // If user chooses to check-in at Pono Hawaiian Grill
                if ( (s).equals("2") ) {
                    Log.i(LOG_TAG, s);
                    // Update Pono Hawaiian Grill Table
                    final Firebase genRef = new Firebase("https://glaring-fire-674.firebaseio.com/Pono Hawaiian Grill");

                    if (user_gender != null) {
                        if ( (user_gender).equals("male") ) {
                            // Update the 'male' count for Pono
                            obtainMaleCountPono(genRef);
                        }
                        else if ( (user_gender).equals("female") ) {
                            // Update the female count for Pono
                            obtainFemaleCountPono(genRef);
                        }
                    }
                }

                // Display pop when user checks in
                int ss = Integer.parseInt(s);
                if (ss == 0) {
                    // Display message when user checks in
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "You've checked in at 99 Bottles", duration);
                    toast.show();
                } else if (ss == 1) {
                    // Display message when user checks in
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "You've checked in at Rosie McCann's", duration);
                    toast.show();
                } else if (ss == 2) {
                    // Display message when user checks in
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "You've checked in at Pono", duration);
                    toast.show();
                }
            }
        });

        // Set a listener for the whole list item.
        newView.setTag(w.message);
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = v.getTag().toString();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, s, duration);
                toast.show();
            }
        });

        return newView;

    } // End of getView()

    public void obtainMaleCount99(Firebase ref){
        ref.child("Male count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/99Bottles");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                int i = Integer.parseInt(snapshot.getValue().toString());
                //String j = String.valueOf(i + 1);
                thisOne.child("Male count").setValue(i + 1);

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }

    public void obtainFemaleCount99(Firebase ref){
        ref.child("Female count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/99Bottles");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                int i = Integer.parseInt(snapshot.getValue().toString());
                //String j = String.valueOf(i + 1);
                thisOne.child("Female count").setValue(i + 1 );

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }

    public void obtainMaleCountRosies(Firebase ref){
        ref.child("Male count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Rosie McCanns");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                int i = Integer.parseInt(snapshot.getValue().toString());
                //String j = String.valueOf(i + 1);
                thisOne.child("Male count").setValue(i + 1);

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }

    public void obtainFemaleCountRosies(Firebase ref){
        ref.child("Female count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Rosie McCanns");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                int i = Integer.parseInt(snapshot.getValue().toString());
                //String j = String.valueOf(i + 1);
                thisOne.child("Female count").setValue(i + 1 );

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }

    public void obtainMaleCountPono(Firebase ref){
        ref.child("Male count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Pono Hawaiian Grill");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                int i = Integer.parseInt(snapshot.getValue().toString());
                //String j = String.valueOf(i + 1);
                thisOne.child("Male count").setValue(i + 1);

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }

    public void obtainFemaleCountPono(Firebase ref){
        ref.child("Female count").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Firebase thisOne = new Firebase("https://glaring-fire-674.firebaseio.com/Pono Hawaiian Grill");
                Log.i(LOG_TAG, snapshot.getValue().toString());  //prints "Do you have data? You'll love Firebase."
                int i = Integer.parseInt(snapshot.getValue().toString());
                //String j = String.valueOf(i + 1);
                thisOne.child("Female count").setValue(i + 1 );

            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }

}