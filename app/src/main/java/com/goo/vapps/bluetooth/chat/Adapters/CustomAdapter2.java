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
public class CustomAdapter2 extends ArrayAdapter<String> {


    public Activity context;
    public ArrayList<String> newDevicesArrayAdapter;
    public ArrayList<String> status1;



    public CustomAdapter2(Activity context, ArrayList<String> newDevicesArrayAdapter,ArrayList<String> status1) {
        super(context, R.layout.mylist2, newDevicesArrayAdapter);
//         TODO Auto-generated constructor stub

        this.context=context;
        this.newDevicesArrayAdapter=newDevicesArrayAdapter;
        this.status1=status1;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist2, null,true);

        TextView text3 = (TextView) rowView.findViewById(R.id.text3);
        TextView text4 = (TextView) rowView.findViewById(R.id.text4);
        LinearLayout layout=(LinearLayout)rowView.findViewById(R.id.layout2);


        String meCheck = newDevicesArrayAdapter.get(position);
        String meCheck1 = status1.get(position);

//        text3.setText(meCheck);
//        text4.setText(meCheck1);


        if (meCheck1.startsWith("Online")){
            text3.setText(meCheck);
            text4.setText(meCheck1);
            text4.setTextColor(Color.BLUE);

        }else {
            text3.setText(meCheck);
            text4.setText(meCheck1);
            text4.setTextColor(Color.parseColor("#FF0000"));
        }



//        Typeface custom_font = Typeface.createFromAsset(((Activity)getContext()).getAssets(), "fonts/ComicSansMS3.ttf");
//        text1.setTypeface(custom_font);
        int Screensize = ((Activity)getContext()).getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (Screensize == 4) {
            text3.setTextSize(35);
            text4.setTextSize(27);
        }

        if (Screensize == 3) {
            text3.setTextSize(33);
            text4.setTextSize(25);
        }

        if (Screensize == 2) {
            text3.setTextSize(20);
            text4.setTextSize(12);

        }
        if (Screensize == 1) {
            text3.setTextSize(15);
            text4.setTextSize(7);
        }




        return rowView;
    };

}
