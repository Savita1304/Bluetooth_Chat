package com.goo.vapps.bluetooth.chat.Adapters;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goo.vapps.bluetooth.chat.R;

import java.util.ArrayList;

/**
 * Created by compind-pc8 on 2/2/17.
 */
public class CustomAdapter extends ArrayAdapter<String> {



    public Activity context;
    public ArrayList<String> pairedDevicesArrayAdapter;
    public ArrayList<String> status;



    public CustomAdapter(Activity context, ArrayList<String> pairedDevicesArrayAdapter,ArrayList<String> status) {
        super(context, R.layout.mylist, pairedDevicesArrayAdapter);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.pairedDevicesArrayAdapter=pairedDevicesArrayAdapter;
        this.status=status;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView text1 = (TextView) rowView.findViewById(R.id.text1);
        TextView text2 = (TextView) rowView.findViewById(R.id.text2);
        LinearLayout layout=(LinearLayout)rowView.findViewById(R.id.layout1);


        String meCheck = pairedDevicesArrayAdapter.get(position);
        String meCheck1 = status.get(position);


//        text1.setText(meCheck);
//        text2.setText(meCheck1);

        if (meCheck1.startsWith("Online")){
            text1.setText(meCheck);
            text2.setText(meCheck1);
            text2.setTextColor(Color.BLUE);

        }else {
            text1.setText(meCheck);
            text2.setText(meCheck1);
            text2.setTextColor(Color.RED);
        }






//        Typeface custom_font = Typeface.createFromAsset(((Activity)getContext()).getAssets(), "fonts/ComicSansMS3.ttf");
//        text1.setTypeface(custom_font);
        int Screensize = ((Activity)getContext()).getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (Screensize == 4) {
            text1.setTextSize(35);
            text2.setTextSize(27);
        }

        if (Screensize == 3) {
            text1.setTextSize(33);
            text2.setTextSize(25);
        }

        if (Screensize == 2) {
            text1.setTextSize(20);
            text2.setTextSize(12);

        }
        if (Screensize == 1) {
            text1.setTextSize(15);
            text2.setTextSize(7);
        }




        return rowView;
    };

}
