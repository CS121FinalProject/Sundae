package com.cs121.project.trollie;

import android.content.Context;
import android.view.Gravity;
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

//        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(650, ViewGroup.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams nicknameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        // Fills in the view.
        TextView tv = (TextView) newView.findViewById(R.id.info_text);
        TextView b = (Button) newView.findViewById(R.id.checkInBtn);
        ImageView iv = (ImageView) newView.findViewById(R.id.location_img);
        Log.i(LOG_TAG, "TEST");
        tv.setText(w.message);
//        b.setText(w.nickname);
        if ( (w.message).equals("Rosie McCann's") ) {
            iv.setImageResource(R.drawable.rosiemccannes);
        }
        if ( (w.message).equals("99 Bottles") ) {
            iv.setImageResource(R.drawable.ninetynine_logo1);
        }
        if ( (w.message).equals("Pono Hawaiian Grill") ) {
            iv.setImageResource(R.drawable.pono_logo);
        }

        // Pull message box LEFT or RIGHT
//        if ( (w.gravity).equals("left") ) {
////            tv.setBackgroundColor(0xffcccccc);
////            messageParams.gravity = Gravity.LEFT;
////            nicknameParams.gravity = Gravity.LEFT;
////            tv.setLayoutParams(messageParams);
////            b.setLayoutParams(nicknameParams);
//
//        }
//        else {
//            tv.setBackgroundColor(0xff0000ff);
//            tv.setTextColor(0xffffffff);
////            messageParams.gravity = Gravity.RIGHT;
////            nicknameParams.gravity = Gravity.RIGHT;
////            tv.setLayoutParams(messageParams);
////            b.setLayoutParams(nicknameParams);
//        }


        // Sets a listener for the button, and a tag for the button as well.
        b.setTag(new Integer(position));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reacts to a button press.
                // Gets the integer tag of the button.
                String s = v.getTag().toString();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, s, duration);
                toast.show();
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
    }
}
