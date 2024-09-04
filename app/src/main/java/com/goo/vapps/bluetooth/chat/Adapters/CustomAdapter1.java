package com.goo.vapps.bluetooth.chat.Adapters;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goo.vapps.bluetooth.chat.R;

import java.util.ArrayList;

/**
 * Created by compind-pc8 on 2/2/17.
 */
public class CustomAdapter1 extends ArrayAdapter<String> {



    public Activity context;
    public  ArrayList<String> chatlist=new ArrayList<>();




    public CustomAdapter1(Activity context, ArrayList<String> chatlist) {
        super(context, R.layout.mylist1, chatlist);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.chatlist=chatlist;


    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist1, null,true);

        TextView text1 = (TextView) rowView.findViewById(R.id.text1);
        LinearLayout layout=(LinearLayout)rowView.findViewById(R.id.layout1);




        String meCheck = chatlist.get(position);



        if (meCheck.startsWith("Me")){
            text1.setText(meCheck);
            layout.setGravity(Gravity.END);
            text1.setBackgroundResource(R.drawable.bubble_a1);



            Animation animation1 = AnimationUtils.loadAnimation((Activity)getContext(), R.anim.bounce);
            text1.startAnimation(animation1);

            int Screensize = ((Activity)getContext()).getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            if (Screensize == 4) {
                text1.setTextSize(35);
            }

            if (Screensize == 3) {
                text1.setTextSize(33);
            }

            if (Screensize == 2) {
                text1.setTextSize(20);

            }
            if (Screensize == 1) {
                text1.setTextSize(15);
            }


        }

        else if (meCheck.startsWith("\u0020")){
            text1.setText(meCheck);
            layout.setGravity(Gravity.CENTER);
            text1.setTextColor(Color.parseColor("#FFFFFF"));
//            text1.setBackgroundColor(Color.parseColor("#FF524D4B"));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) text1.getLayoutParams();
            params.setMargins(20,20,20,0);
            text1.setBackgroundResource(R.drawable.bgxml1);

            int Screensize = ((Activity)getContext()).getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            if (Screensize == 4) {
                text1.setTextSize(30);
            }

            if (Screensize == 3) {
                text1.setTextSize(27);
            }

            if (Screensize == 2) {
                text1.setTextSize(15);

            }
            if (Screensize == 1) {
                text1.setTextSize(10);
            }

        }


        else {

            text1.setText(meCheck);
            layout.setGravity(Gravity.START);
            text1.setBackgroundResource(R.drawable.bubble_a2);


            Animation animation1 = AnimationUtils.loadAnimation((Activity)getContext(), R.anim.bounce);
            text1.startAnimation(animation1);


            int Screensize = ((Activity)getContext()).getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
            if (Screensize == 4) {
                text1.setTextSize(35);
            }

            if (Screensize == 3) {
                text1.setTextSize(33);
            }

            if (Screensize == 2) {
                text1.setTextSize(20);

            }
            if (Screensize == 1) {
                text1.setTextSize(15);
            }

        }


//        Typeface custom_font = Typeface.createFromAsset(((Activity)getContext()).getAssets(), "fonts/ComicSansMS3.ttf");
//        text1.setTypeface(custom_font);





        return rowView;
    };



}
