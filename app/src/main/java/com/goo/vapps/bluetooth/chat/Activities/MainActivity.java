package com.goo.vapps.bluetooth.chat.Activities;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.goo.vapps.bluetooth.chat.Adapters.CustomAdapter1;
import com.goo.vapps.bluetooth.chat.Classes.Admob;
import com.goo.vapps.bluetooth.chat.Classes.ChatService;
import com.goo.vapps.bluetooth.chat.Database.ChatRecoedDataBase;
import com.goo.vapps.bluetooth.chat.Model.ChatData;
import com.goo.vapps.bluetooth.chat.R;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static android.content.ContentValues.TAG;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity  {
    private ConsentInformation consentInformation;
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);


    public void createCMP(){








        // Set tag for under age of consent. false means users are not under age
        // of consent.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }
    }
    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this);

        // TODO: Request an ad.
        // InterstitialAd.load(...);
    }
    int height;
    LinearLayout header, footer;

    RelativeLayout relative_bg;


    boolean isConnected = false;



    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;


    public static final String DEVICE_NAME = "device_name";


    //new
    public static final String DEVICE_ADDRESS1 = "device_address1";
    public static final String TOAST = "toast";

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static int CODE;
    TextView textheader, textinfo;

    ListView lvMainChat;

    //    EditText emojiconEditText;
    ImageView optionmenu;

//    TextView btnSend;

  ImageView uploadimage;

    ImageView btnSend;


    public static int stat1;


    private String connectedDeviceName = null;

    private String connectedDeviceAddress = null;

    private StringBuffer outStringBuffer;
    private BluetoothAdapter bluetoothAdapter = null;
    private ChatService chatService = null;


    //new own define

    ArrayList<String> chatlist = new ArrayList<>();

    public static boolean backpressed = false;


    static CustomAdapter1 customAdapter1;



    String time;


    public static final String MyPREFERENCES = "MyPrefs";


    //    public boolean isplayingSound=false;
    public int sound = 2;

    public static int istype = 0;

    MediaPlayer mp1;
    MediaPlayer mp3;


    MediaPlayer mp2;

    View layout;

    String address;
    String name1;

    //new location
    int locationMode;
    public final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    public boolean checkbluetooth = false;

    public boolean datafound = false;


    public static final String MyPREFERENCES1 = "MyPrefs1";
    SharedPreferences sp;

    TextInputEditText emojiconEditText;





    public boolean option_check = false;







    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case ChatService.STATE_CONNECTED:
                            textheader.setText(connectedDeviceName);
                            setStatus("Online");
                            optionmenu.setVisibility(View.GONE);
                            optionmenu.setVisibility(View.INVISIBLE);


                            if (sound == 1) {
                                //Log.i("check sound","===");
                                try {
                                    mp3.stop();
                                    mp3.reset();
                                    mp3.release();

                                } catch (Exception e) {
                                }

                            } else if (sound == 2) {
                                //Log.i("check sound111","===");
                                try {
                                    mp3.start();
                                } catch (Exception e) {
                                }
                            }


                            fetchChatData(connectedDeviceAddress);    //check for the first time chat

//                            String text1=" has joined chat.";
//                            chatlist.add("\u2709" + connectedDeviceName + text1);
//                            customAdapter1.notifyDataSetChanged();

                            //new

                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = new Date();
                            String date1 = dateFormat.format(date);


                            if (chatlist.isEmpty()) {
                                //Log.i("empty chatlist","===");

                                chatlist.add("\u0020" + date1);
                                customAdapter1.notifyDataSetChanged();

                                ChatRecoedDataBase db = new ChatRecoedDataBase(getApplicationContext());
                                db.addChatRecord(new ChatData("\u0020" + date1, connectedDeviceAddress));
                            } else {


                                //Log.i("chatlist element",""+chatlist);
                                if (chatlist.contains("\u0020" + date1)) {
//                                    Toast.makeText(MainActivity.this, "if bl1", Toast.LENGTH_SHORT).show();
                                } else {
//                                    Toast.makeText(MainActivity.this, "else bl1", Toast.LENGTH_SHORT).show();
                                    chatlist.add("\u0020" + date1);       //u2755
                                    customAdapter1.notifyDataSetChanged();
                                    ChatRecoedDataBase db = new ChatRecoedDataBase(getApplicationContext());
                                    db.addChatRecord(new ChatData("\u0020" + date1, connectedDeviceAddress));
                                }


                            }

                            break;
                        case ChatService.STATE_CONNECTING:
                            setStatus("Connecting....");
                            break;
                        case ChatService.STATE_LISTEN:
                            if (ChatService.devicenotfound == true) {
                                //Log.i("when click on device","===="+address);
                                SharedPreferences sp1 = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                time = sp1.getString(address, null);
                                //Log.i("sp value","==="+time);

                                if (time == null) {
                                    chatlist.clear();
//                                    Toast.makeText(MainActivity.this, "time null", Toast.LENGTH_SHORT).show();
//                                    //Log.i("sp value","===");
                                    String text = " No message found";
                                    chatlist.add("\u0020" + text);     //u2755
                                    customAdapter1.notifyDataSetChanged();
                                    textheader.setText("Bluetooth Chat");
                                    setStatus("Offline");

                                } else {
                                    //Log.i("sp value","===");
                                    String[] str1 = time.split("!");
                                    String t1 = str1[0];
                                    String name = str1[1];
                                    textheader.setText(name);
                                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                                    textinfo.setAnimation(animation1);
                                    setStatus("last chat at " + t1);


                                    if (address == null) {

                                    } else {
                                        fetchChatData1(address);
                                    }


                                }

                            } else {


                                //Log.i("boolean value","==="+ChatService.connectionfailed);
//                                lvMainChat.setStackFromBottom(false);
                                if (ChatService.connectionfailed == false) {
                                    //Log.i("boolean value111","==="+ChatService.connectionfailed);
                                    String text = "Start chat by tapping option menu at top right corner. ";
                                    chatlist.add("\u0020" + text);   //start  u2757
                                    customAdapter1.notifyDataSetChanged();
                                }

                                //Log.i("sp value else","===");
                                textheader.setText("Bluetooth Chat");
                                setStatus("Not Available");

                            }

                            break;
                        case ChatService.STATE_NONE:
                            setStatus("Not Available");
                            break;
                    }
                    break;

                case MESSAGE_WRITE:

                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);

                    //new timr
                    DateFormat df1 = new SimpleDateFormat("HH:mm");
                    Calendar calobj1 = Calendar.getInstance();
                    String timestamp1 = df1.format(calobj1.getTime());
                    //new time

                    chatlist.add("Me[" + timestamp1 + "]: " + writeMessage);
                    customAdapter1.notifyDataSetChanged();

                    break;
                case MESSAGE_READ:


                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    //new timr
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    Calendar calobj = Calendar.getInstance();
                    String timestamp = df.format(calobj.getTime());
                    //new time

                    chatlist.add(connectedDeviceName + "[" + timestamp + "]" + ":" + readMessage);
                    customAdapter1.notifyDataSetChanged();


                    if (sound == 1) {
                        try {
                            mp1.stop();
                            mp1.reset();
                            mp1.release();

                        } catch (Exception e) {
                        }

                    } else if (sound == 2) {
                        try {
                            mp1 = MediaPlayer.create(getApplicationContext(), R.raw.message);
                            mp1.start();

                        } catch (Exception e) {
//                            System.out.println("sound exception");
                        }

                    }


                    ChatRecoedDataBase db1 = new ChatRecoedDataBase(getApplicationContext());
                    db1.addChatRecord(new ChatData(connectedDeviceName + "[" + timestamp + "]" + ":" + readMessage, connectedDeviceAddress));




                    break;
                case MESSAGE_DEVICE_NAME:
                    connectedDeviceName = msg.getData().getString(DEVICE_NAME);
//                    Toast.makeText(getApplicationContext(), "Connected to " + connectedDeviceName, Toast.LENGTH_SHORT).show();
                    connectedDeviceAddress = msg.getData().getString(DEVICE_ADDRESS1);
                    break;

                case MESSAGE_TOAST:          //if application not installed on mobile
//                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();


                    if (ChatService.connectionfailed == true) {
                        Date date = new Date();
                        String stringDate = DateFormat.getDateTimeInstance().format(date);
                        SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(connectedDeviceAddress, stringDate + "!" + connectedDeviceName);
                        editor.commit();


                    }


                    int Screensize = getApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

                    //Creating the LayoutInflater instance
                    LayoutInflater li = getLayoutInflater();
                    //Getting the View object as defined in the customtoast.xml file
                    layout = li.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
                    TextView toast_text = (TextView) layout.findViewById(R.id.toast_text);
                    toast_text.setText(msg.getData().getString(TOAST));
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 280);
                    toast.setView(layout);
                    toast.show();

                    if (Screensize == 4) {
                        toast_text.setTextSize(35);
                    }

                    if (Screensize == 3) {
                        toast_text.setTextSize(33);
                    }

                    if (Screensize == 2) {
                        toast_text.setTextSize(20);

                    }
                    if (Screensize == 1) {
                        toast_text.setTextSize(15);

                    }


                    optionmenu.setVisibility(View.VISIBLE);
                    break;


            }
            return false;
        }
    });








    boolean marshmallowPermission = false;








    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    marshmallowPermission = true;
                } else {
                    marshmallowPermission = false;
                }
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.e("GRGR","Grant bluetooth :");
                    checkbluetooth = true;
                   //  connectAdapter();
                    setBluetoothConnection();
                } else {
                    checkbluetooth = false;
                }

            case 3:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkbluetooth = true;
                } else {

                }
                break;

        }


    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);

        setContentView(R.layout.layoutrg);

        createCMP();

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);











        mp1 = MediaPlayer.create(MainActivity.this, R.raw.message);
        mp3 = MediaPlayer.create(MainActivity.this, R.raw.connect);
        mp2 = MediaPlayer.create(MainActivity.this, R.raw.buttonclick);


        header = findViewById(R.id.header);


        DisplayMetrics matrices = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrices);
        final int width = matrices.widthPixels;
        height = matrices.heightPixels;









        getWidgetReferences();
        bindEventHandler();








        optionmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.start();

                showoptionmenu();
            }
        });



        requestLocation();



        int Screensize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (Screensize == 4) {
            textheader.setTextSize(32);
            textinfo.setTextSize(26);
        }
        if (Screensize == 3) {

            textheader.setTextSize(30);
            textinfo.setTextSize(24);
        }
        if (Screensize == 2) {
            textheader.setTextSize(22);
            textinfo.setTextSize(11);

        }
        if (Screensize == 1) {
            textheader.setTextSize(12);
            textinfo.setTextSize(6);

        }


    }




    public void requestLocation(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {

            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)  && (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED ) {




                setBluetoothConnection();





                }
            else{
                //ask for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN},
                        PERMISSION_ACCESS_COARSE_LOCATION);
            }



        }

        else{
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_GRANTED)  ) {




                setBluetoothConnection();





            }
            else{
                //ask for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH},
                        PERMISSION_ACCESS_COARSE_LOCATION);
            }
        }

    }


    public void checkLocation() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if (locationMode == 0) {


              /*  int myVersion = Build.VERSION.SDK_INT;
                if (myVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    showalertDialog();
                }*/

            }

            checkMarshmallowPermission();
            addLocationPermiision();

        }
        else{
            marshmallowPermission = true;
        }


        checkBlutoothPermission();
       // checkExternalStoragePermission();

    }


    public void addLocationPermiision() {
        int myVersion = Build.VERSION.SDK_INT;
        ////Log.e(TAG, "myVersion -> " + myVersion);

        if (myVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_ACCESS_COARSE_LOCATION);
                //Log.e("RRRS","SF :"+marshmallowPermission);
                // //Log.e("addlocation permission ","1111 ");
                marshmallowPermission = false;
            }
        }
    }

    public void checkMarshmallowPermission() {
        int myVersion = Build.VERSION.SDK_INT;
        if (myVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                ////Log.e("checkMarshmallow Permission ","1111 ");
                marshmallowPermission = true;
            }
        }
    }


    public void checkBlutoothPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R){
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
                checkbluetooth = true;
                setBluetoothConnection();
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN},2);
            }
        }
        else{
            //Log.e("RRROL","ch :"+Build.VERSION.SDK_INT);
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_GRANTED)) {
                checkbluetooth = true;
                setBluetoothConnection();
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.BLUETOOTH},2);
            }
        }



    }

/*    public void checkExternalStoragePermission() {
        int myVersion = Build.VERSION.SDK_INT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){


            createDestination();
        }
        else{
            //Log.e("RRROL","ch :"+Build.VERSION.SDK_INT);
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
                checkbluetooth = true;
            }
            else {
                requestPermission();
            }
        }


    }*/
    public void showalertDialog() {

        // Toast.makeText(this,"1111"+height,Toast.LENGTH_LONG).show();

        //final Dialog alertdialog=new Dialog(this);
        final Dialog alertdialog = new Dialog(this);
        alertdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertdialog.setContentView(R.layout.alert_dialog);
        alertdialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, height * 40 / 100);

        //alertdialog.setCancelable(false);
        alertdialog.show();

        int screensize = getApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        Button yesbtn = (Button) alertdialog.findViewById(R.id.yesbtn);
        Button nobtn = (Button) alertdialog.findViewById(R.id.nobtn);
        TextView alerttext = (TextView) alertdialog.findViewById(R.id.alerttextview);

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

                alertdialog.dismiss();
            }
        });
        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertdialog.dismiss();
                //finish();
            }
        });

        if (screensize >= 4) {
            alerttext.setTextSize(38);
        }
        if (screensize == 3) {
            alerttext.setTextSize(36);
        }
        if (screensize == 2) {
            alerttext.setTextSize(23);
        }
        if (screensize == 1) {
            alerttext.setTextSize(18);
        }
    }



 /*   private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                3);
    }

    private void requestPermission1() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_AUDIO,Manifest.permission.READ_MEDIA_VIDEO},
                3);
    }*/






    public void setBluetoothConnection() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Log.e("BNN","dev :"+bluetoothAdapter.isEnabled());
        if (bluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled :)

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            // Bluetooth is enabled
            connectAdapter();
        }


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
//
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//           // connectAdapter();
//
//        }



//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//         /*   if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R){
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN}, 2);
//                    return;
//                }
//            }
//            else {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 2);
//
//                }*/
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//
//            }
//
//
//
//        else {
//
//           /* if (checkbluetooth) {
//
//
//            }*/
//            connectAdapter();
//
//    }

    }

    public void connectAdapter(){
        if (chatService == null) {
            //Log.e("RRRS","SF :"+checkbluetooth+"/"+chatService);
            setupChat();
            if (chatService.getState() == ChatService.STATE_NONE) {
                chatService.start();
            }

        } else if (chatService != null) {
            if (chatService.getState() == ChatService.STATE_NONE) {
                chatService.start();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (marshmallowPermission) {
//
//            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//            if (!bluetoothAdapter.isEnabled()) {
//                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R){
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN}, 2);
//                        return;
//                    }
//                }
//                else {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
//
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 2);
//
//                    }
//
//                }
//
//                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//
//            } else {
//
//                if (checkbluetooth) {
//
//                    if (chatService == null) {
//                        //Log.e("RRRS","SF :"+checkbluetooth+"/"+chatService);
//                        setupChat();
//                        if (chatService.getState() == ChatService.STATE_NONE) {
//                            chatService.start();
//                        }
//
//                    } else if (chatService != null) {
//                        if (chatService.getState() == ChatService.STATE_NONE) {
//                            chatService.start();
//                        }
//                    }
//                }
//
//            }
//
//
//
//          /*  if (bluetoothAdapter == null) {
//                Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
//                finish();
//                return;
//            }*/
//        }
    }

    public void checknet() {

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }


    public void fetchChatData(String device) {
        ChatRecoedDataBase db2 = new ChatRecoedDataBase(getApplicationContext());
        chatlist = db2.getChat(device);
        customAdapter1 = new CustomAdapter1(MainActivity.this, chatlist);
        lvMainChat.setAdapter(customAdapter1);
        customAdapter1.notifyDataSetChanged();

    }


    public void fetchChatData1(String device) {
        ChatRecoedDataBase db3 = new ChatRecoedDataBase(getApplicationContext());
        chatlist = db3.getChat1(device);
        if (!chatlist.isEmpty()) {
//            Toast.makeText(MainActivity.this, "if toast", Toast.LENGTH_SHORT).show();
            datafound = false;
            customAdapter1 = new CustomAdapter1(MainActivity.this, chatlist);
            lvMainChat.setAdapter(customAdapter1);
        } else {
//            Toast.makeText(MainActivity.this, "else toast", Toast.LENGTH_SHORT).show();
            datafound = true;

        }

//        customAdapter1.notifyDataSetChanged();
    }








    @Override
    public void onBackPressed() {
//        final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);




        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog1);  //before1

//        dialog.getWindow().getAttributes().windowAnimations = R.style.AppTheme;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        int Screensize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText("Do you want to close chat?");
        final TextView b1 = (TextView) dialog.findViewById(R.id.btn_yes);
        b1.setText("Yes");
        final TextView b2 = (TextView) dialog.findViewById(R.id.btn_no);
        b2.setText("No");
//        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ComicSansMS3.ttf");
//        text.setTypeface(custom_font);
//        b1.setTypeface(custom_font,Typeface.BOLD);
//        b2.setTypeface(custom_font,Typeface.BOLD);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                b1.setTextColor(Color.parseColor("#111111"));


                ChatService.connectionfailed = false;
                ChatService.devicenotfound = false;

                backpressed = true;
                dialog.cancel();
                dialog.dismiss();
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b2.setTextColor(Color.parseColor("#111111"));
                dialog.cancel();
                dialog.dismiss();
            }
        });
        if (Screensize == 4) {
            text.setTextSize(35);
            b1.setTextSize(35);
            b2.setTextSize(35);
        }

        if (Screensize == 3) {
            text.setTextSize(33);
            b1.setTextSize(33);
            b2.setTextSize(33);
        }

        if (Screensize == 2) {
            text.setTextSize(20);
            b1.setTextSize(20);
            b2.setTextSize(20);
        }
        if (Screensize == 1) {
            text.setTextSize(15);
            b1.setTextSize(15);
            b2.setTextSize(15);
        }
        dialog.show();



    }


    @Override
    public void finish() {
        backpressed = false;
//        Log.e("service stop1","=====");
        if (chatService != null) {
            chatlist.clear();
//            Log.e("service stop","=====");
            chatService.stop();


        }

        super.finish();
    }


    public void showoptionmenu() {
        PopupMenu popup = new PopupMenu(MainActivity.this, optionmenu);
        popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Intent serverIntent = null;
                mp2.start();
                int id = item.getItemId();

                if (id == R.id.secure_connect_scan){
                    perm();
                }
               else if (id == R.id.insecure_connect_scan){

                   perm();
                }

                else if (id == R.id.discoverable){
                    ensureDiscoverable();
                }
                else if (id == R.id.theme){
                    showpopupmenu();
                }

                else if (id == R.id.support){
                    suppurt();
                }

                else if (id == R.id.share){
                    share();
                }

                return false;
            }
        });
        popup.show();
    }





    public void perm() {
        int myVersion1 = Build.VERSION.SDK_INT;
        try {
            locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


        if (myVersion1 > Build.VERSION_CODES.LOLLIPOP_MR1) {

            //permission
            if ((ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && locationMode != 0) {
                //Log.e("QQQ","rq :"+REQUEST_CONNECT_DEVICE_INSECURE);
                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity1.class);
          /*      startActivity(serverIntent);
                finish();*/

                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);

            } else if ((ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) && locationMode == 0) {
                showalertDialog();
                addLocationPermiision();
            } else if ((ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) && locationMode != 0) {
                addLocationPermiision();
            } else if ((ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && locationMode == 0) {
                showalertDialog();
            }

          /*  else if ((ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {


                requestPermission();


            }*/

        } else {




            Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity1.class);
           /* startActivity(serverIntent);
            finish();*/
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
        }
    }


    public void showpopupmenu() {

        mp2.start();

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_color);  //before1
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        int Screensize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        DisplayMetrics matrices = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrices);
        final int width = matrices.widthPixels;
        final int height = matrices.heightPixels;

        int image_width = height * 10 / 100;

        ImageView im1 = (ImageView) dialog.findViewById(R.id.image1);
        im1.setBackgroundColor(Color.parseColor("#CCFFCC"));
        im1.getLayoutParams().height = image_width;

        ImageView im2 = (ImageView) dialog.findViewById(R.id.image2);
        im2.setBackgroundColor(Color.parseColor("#ADB6B5"));
        im2.getLayoutParams().height = image_width;

        ImageView im3 = (ImageView) dialog.findViewById(R.id.image3);
        im3.setBackgroundColor(Color.parseColor("#A0AEC1"));
        im3.getLayoutParams().height = image_width;

        ImageView im4 = (ImageView) dialog.findViewById(R.id.image4);
        im4.setBackgroundColor(Color.parseColor("#627894"));
        im4.getLayoutParams().height = image_width;

        ImageView im5 = (ImageView) dialog.findViewById(R.id.image5);
        im5.setBackgroundColor(Color.parseColor("#FFF0BA"));
        im5.getLayoutParams().height = image_width;

        ImageView im6 = (ImageView) dialog.findViewById(R.id.image6);
        im6.setBackgroundColor(Color.parseColor("#ECECEC"));
        im6.getLayoutParams().height = image_width;


        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_bg.setBackgroundColor(Color.parseColor("#CCFFCC"));
                String color = "#CCFFCC";
                dialog.dismiss();
                dialog.cancel();
                sp = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor12 = sp.edit();
                editor12.putString("ColorCode", color);
                editor12.commit();
                showToast();
            }
        });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_bg.setBackgroundColor(Color.parseColor("#ADB6B5"));
                String color = "#ADB6B5";
                dialog.dismiss();
                dialog.cancel();
                sp = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor12 = sp.edit();
                editor12.putString("ColorCode", color);
                editor12.commit();
                showToast();
            }
        });

        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_bg.setBackgroundColor(Color.parseColor("#A0AEC1"));
                String color = "#A0AEC1";
                dialog.dismiss();
                dialog.cancel();
                sp = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor12 = sp.edit();
                editor12.putString("ColorCode", color);
                editor12.commit();
                showToast();
            }
        });

        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_bg.setBackgroundColor(Color.parseColor("#627894"));
                String color = "#627894";
                dialog.dismiss();
                dialog.cancel();
                sp = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor12 = sp.edit();
                editor12.putString("ColorCode", color);
                editor12.commit();
                showToast();
            }
        });

        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_bg.setBackgroundColor(Color.parseColor("#FFF0BA"));
                String color = "#FFF0BA";
                dialog.dismiss();
                dialog.cancel();
                sp = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor12 = sp.edit();
                editor12.putString("ColorCode", color);
                editor12.commit();
                showToast();
            }
        });

        im6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_bg.setBackgroundColor(Color.parseColor("#ECECEC"));
                String color = "#ECECEC";
                dialog.dismiss();
                dialog.cancel();
                sp = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor12 = sp.edit();
                editor12.putString("ColorCode", color);
                editor12.commit();
                showToast();
            }
        });

        dialog.show();

    }


    public void showToast() {
        int Screensize = getApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        //Creating the LayoutInflater instance
        LayoutInflater li = getLayoutInflater();
        //Getting the View object as defined in the customtoast.xml file
        layout = li.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView toast_text = (TextView) layout.findViewById(R.id.toast_text);
        toast_text.setText("you have changed theme");
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 280);
        toast.setView(layout);
        toast.show();

        if (Screensize == 4) {
            toast_text.setTextSize(35);
        }

        if (Screensize == 3) {
            toast_text.setTextSize(33);
        }

        if (Screensize == 2) {
            toast_text.setTextSize(20);

        }
        if (Screensize == 1) {
            toast_text.setTextSize(15);

        }
    }





    RelativeLayout r1,r2,r3,r11,r22;



    private void getWidgetReferences() {



        lvMainChat = (ListView) findViewById(R.id.lvMainChat);

        textheader = (TextView) findViewById(R.id.headerlogo);
        textinfo = (TextView) findViewById(R.id.info);
        optionmenu = findViewById(R.id.optionmenu);






        emojiconEditText = findViewById(R.id.etMain);
        emojiconEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        btnSend = (ImageView) findViewById(R.id.btnSend);
        uploadimage = (ImageView) findViewById(R.id.attachsymbol);



        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r11 = findViewById(R.id.r11);
        r22 = findViewById(R.id.r22);



        checknet();

        AdView mAdView = findViewById(R.id.adView);
        Admob.adMob1(getApplicationContext(),
                mAdView,isConnected);





        relative_bg = findViewById(R.id.relative_bg);
        sp = getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        String color1 = sp.getString("ColorCode", null);
        if (color1 != null) {
            relative_bg.setBackgroundColor(Color.parseColor(color1));

        }


        DisplayMetrics matrices = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrices);
        final int width = matrices.widthPixels;
        final int height = matrices.heightPixels;


        r1.getLayoutParams().height = (int) (height*0.10f);


        r3.getLayoutParams().height = (int) (height*0.10f);

        r2.getLayoutParams().height = (int) (height*0.80f);



        r11.getLayoutParams().height = (int) (r2.getLayoutParams().height*0.87f);

        r22.getLayoutParams().height = (int) (r2.getLayoutParams().height*0.13f);






        RelativeLayout footer = findViewById(R.id.footer);




   /*     DisplayMetrics matrices = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrices);
        final int width = matrices.widthPixels;
        final int height = matrices.heightPixels;*/




      /*  int heights_content = height * 5 / 100;
        int width_content = width * 10 / 100;


        btnSend.getLayoutParams().width = width_content;
        btnSend.getLayoutParams().height = heights_content;*/


        emojiconEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (emojiconEditText.getText().toString().trim().equals("")) {
                  //  btnSend.setImageResource(R.drawable.am1);
                    btnSend.setColorFilter(getResources().getColor(R.color.gray));

                } else {
                  //  btnSend.setBackgroundResource(R.drawable.send1);
                    btnSend.setColorFilter(getResources().getColor(R.color.header));


                }
            }
        });


    }


    private void bindEventHandler() {
//        emojiconEditText.setOnEditorActionListener(mWriteListener);


        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (emojiconEditText.getText().toString().equals("")) {

                } else {
                    //new timr
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    Calendar calobj = Calendar.getInstance();
                    String timestamp = df.format(calobj.getTime());
                    //new time
                    String message = emojiconEditText.getText().toString();        //store database message here
                    sendMessage(message);


                    if (sound == 1) {
                        //Log.i("check sound","===");
                        try {
                            mp1.stop();
                            mp1.reset();
                            mp1.release();

                        } catch (Exception e) {
                        }


                    } else if (sound == 2) {
                        //Log.i("check sound111","===");
                        try {
                            mp1 = MediaPlayer.create(MainActivity.this, R.raw.message);
                            mp1.start();

                        } catch (Exception e) {
//                            System.out.println("Sound error");
                        }


                    }


                    ChatRecoedDataBase db = new ChatRecoedDataBase(getApplicationContext());
                    db.addChatRecord(new ChatData("Me[" + timestamp + "]: " + message, connectedDeviceAddress));
                }

            }
        });


        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Integer.parseInt(uploadimage.getTag().toString()) == 1) {
//                    isplayingSound=true;
                    sound = 1;
                    uploadimage.setBackgroundResource(0);
                    uploadimage.setBackgroundResource(R.drawable.sa);
                    uploadimage.setTag(2);

                } else {
//                    isplayingSound=false;
                    sound = 2;
                    uploadimage.setBackgroundResource(0);
                    uploadimage.setBackgroundResource(R.drawable.sound1);
                    uploadimage.setTag(1);

                }

            }
        });







    }












    public void suppurt() {
        mp2.start();

//                final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click);
//                mediaPlayer.start();
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog8);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        int Screensize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView b4 = (TextView) dialog.findViewById(R.id.txt_dia5);
        b4.setText("Brought to you by Primosoft\nInc,USA,please provide any\nfeedback or bug report to\nsupport@primosoft.us");

        TextView b3 = (TextView) dialog.findViewById(R.id.txt_dia4);
        b3.setText("Close");


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        mediaPlayer.start();
                dialog.dismiss();
                dialog.cancel();
            }
        });


        if (Screensize == 4) {
            b3.setTextSize(33);
            b4.setTextSize(35);
        }

        if (Screensize == 3) {
            b3.setTextSize(30);
            b4.setTextSize(33);
        }

        if (Screensize == 2) {
            b3.setTextSize(20);
            b3.setTextSize(20);
        }
        if (Screensize == 1) {
            b3.setTextSize(15);
            b3.setTextSize(15);
        }
        dialog.show();
    }


    public void share() {
        mp2.start();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.goo.vapps.bluetooth.chat");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }







    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChat();
                } else {
                    Toast.makeText(this, "Bluetooth was not enabled", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }






    private void connectDevice(Intent data, boolean secure) {
        //check for device is connected or not if not show data base value

        //String addr = data.getStringExtra("splash");
        address = data.getExtras().getString(DeviceListActivity1.DEVICE_ADDRESS);
        //Log.e("PPPP","add :"+address+"/"+bluetoothAdapter);
        if (address != null){

           /* r2.setVisibility(View.VISIBLE);
            r4.setVisibility(View.GONE);



            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) r3.getLayoutParams();
            layoutParams.addRule(RelativeLayout.BELOW, r2.getId());
            r3.setLayoutParams(layoutParams);*/



            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
            chatService.connect(device, secure);

        }



    }





    private void ensureDiscoverable() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);

            return;
        }
        else{
            if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            }
        }

    }

    private void sendMessage(String message) {
        if (chatService.getState() != ChatService.STATE_CONNECTED) {
//            Toast.makeText(this, "you are not connected", Toast.LENGTH_SHORT).show();
            int Screensize = getApplicationContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

            //Creating the LayoutInflater instance
            LayoutInflater li = getLayoutInflater();
            //Getting the View object as defined in the customtoast.xml file
            layout = li.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
            TextView toast_text = (TextView) layout.findViewById(R.id.toast_text);
            toast_text.setText("you are not connected!");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 280);
            toast.setView(layout);
            toast.show();

            if (Screensize == 4) {
                toast_text.setTextSize(35);
            }

            if (Screensize == 3) {
                toast_text.setTextSize(33);
            }

            if (Screensize == 2) {
                toast_text.setTextSize(20);

            }
            if (Screensize == 1) {
                toast_text.setTextSize(15);

            }

            emojiconEditText.setText(null);

            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            chatService.write(send);
            outStringBuffer.setLength(0);
            emojiconEditText.setText(outStringBuffer);

        }
    }


    private final void setStatus(CharSequence subTitle) {
        textinfo.setText(subTitle);
    }

    private void setupChat() {

        customAdapter1 = new CustomAdapter1(MainActivity.this, chatlist);
        lvMainChat.setAdapter(customAdapter1);
        customAdapter1.notifyDataSetChanged();
        chatService = new ChatService(this, handler);
        outStringBuffer = new StringBuffer("");

    }

    @Override
    public synchronized void onPause() {    //do not finish activity here


        if (backpressed==true){
            finish();
        }

        else{


        }
        super.onPause();

    }

    @Override
    public void onStop() {
        if (backpressed==true){
            finish();
        }else{


        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatService != null)
            chatService.stop();

    }


}