package com.goo.vapps.bluetooth.chat.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goo.vapps.bluetooth.chat.R;


public class Splash extends AppCompatActivity {
    RelativeLayout header,footer;
    TextView headerlogo;
    ImageView imgview1;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /*header=(RelativeLayout)findViewById(R.id.header);
        footer=(RelativeLayout)findViewById(R.id.footer);
        headerlogo=(TextView)findViewById(R.id.headerlogo);*/
        text=(TextView)findViewById(R.id.text);




        imgview1=(ImageView)findViewById(R.id.myimg);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        Splash.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightLcl = (int) (displayMetrics.heightPixels*0.30f);

        imgview1.getLayoutParams().width = heightLcl;
        imgview1.getLayoutParams().height = heightLcl;


        fade(imgview1);
        fade1(text);


      /*  DisplayMetrics matrices=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrices);
        int width=matrices.widthPixels;
        int height=matrices.heightPixels;

        int header_height=height*7/100;
        header.getLayoutParams().height=header_height;
        footer.getLayoutParams().height=header_height;

        int Screensize=getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (Screensize==4){
            headerlogo.setTextSize(35);
            text.setTextSize(33);
        }
        if (Screensize==3){

            headerlogo.setTextSize(33);
            text.setTextSize(30);
        }
        if (Screensize==2){
            headerlogo.setTextSize(20);
            text.setTextSize(17);

        }
        if (Screensize==1){
            headerlogo.setTextSize(15);
            text.setTextSize(12);

        }
*/




        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("splash","splash");
                startActivity(i);
                finish();
            }
        },2000);

    }


    public void fade(View view){
        ImageView image = (ImageView)findViewById(R.id.myimg);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        image.startAnimation(animation1);
    }



    public void fade1(View view){
        TextView text = (TextView) findViewById(R.id.text);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        text.startAnimation(animation1);
    }

}
