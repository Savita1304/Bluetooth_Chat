package com.goo.vapps.bluetooth.chat.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.goo.vapps.bluetooth.chat.Adapters.CustomAdapter;
import com.goo.vapps.bluetooth.chat.Adapters.CustomAdapter2;
import com.goo.vapps.bluetooth.chat.Classes.Admob;
import com.goo.vapps.bluetooth.chat.R;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity1 extends AppCompatActivity {

    LinearLayout header,footer;

    TextView infodevice,textpair,textsearch,textheader;

    public boolean refreshlist=false;


    TextView addtextView;
    boolean isConnected = false;
    public String addtext = "";
    public String addlink = "";



//    private TextView tvDeviceListPairedDeviceTitle, tvDeviceListNewDeviceTitle;
    private ListView lvDeviceListPairedDevice, lvDeviceListNewDevice;
//    private Button btnDeviceListScan;

    private BluetoothAdapter bluetoothAdapter;
//    private ArrayAdapter<String> pairedDevicesArrayAdapter;
//    private ArrayAdapter<String> newDevicesArrayAdapter;


    public ArrayList<String> pairedDevicesArrayAdapter=new ArrayList<>();
    public ArrayList<String> status=new ArrayList<>();



    public ArrayList<String> newDevicesArrayAdapter=new ArrayList<>();
    public ArrayList<String> status1=new ArrayList<>();

    CustomAdapter customAdapter;
    CustomAdapter2 customAdapter2;

    Handler handler=new Handler();




    public static String DEVICE_ADDRESS = "deviceAddress";


    public static String DEVICE_NAME1 = "deviceName1";



    ImageView imageview,imagesearch,share;


     MediaPlayer mp2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_device_list1);
        mp2=MediaPlayer.create(DeviceListActivity1.this,R.raw.buttonclick);

        header=findViewById(R.id.header);
        footer=findViewById(R.id.footer);




        checknet();



        textheader=(TextView)findViewById(R.id.headerlogo1);
        infodevice=(TextView)findViewById(R.id.infodevice);
        textpair=(TextView)findViewById(R.id.pairdevice);
        textsearch=(TextView)findViewById(R.id.newdevice);
        imageview=(ImageView)findViewById(R.id.backimg);
//        share=(ImageView)findViewById(R.id.share);
       // addtextView=(TextView)findViewById(R.id.ads);




        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(i);
                finish();
            }
        });





     setResult(Activity.RESULT_CANCELED);

        getWidgetReferences();
        bindEventHandler();
        initializeValues();

        //new
        startDiscovery();

        DisplayMetrics matrices=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrices);
        int width=matrices.widthPixels;
        int height=matrices.heightPixels;

        int header_height=height*7/100;
       // header.getLayoutParams().height=header_height;


        /*=========admob================*/
   /*     checknet();

        AdView mAdView = findViewById(R.id.adView);
        Admob.adMob3(getApplicationContext(),
                mAdView,footer,isConnected);*/

        /*=================admob=============*/


        checknet();

        AdView mAdView = findViewById(R.id.adView);
        Admob.adMob1(getApplicationContext(),
                mAdView,isConnected);


        int Screensize=getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (Screensize==4){
            textheader.setTextSize(32);
            infodevice.setTextSize(26);
            textpair.setTextSize(30);
            textsearch.setTextSize(30);
        }
        if (Screensize==3){

            textheader.setTextSize(30);
            infodevice.setTextSize(24);
            textpair.setTextSize(28);
            textsearch.setTextSize(28);
        }
        if (Screensize==2){
            textheader.setTextSize(17);
            infodevice.setTextSize(11);
            textpair.setTextSize(15);
            textsearch.setTextSize(15);

        }
        if (Screensize==1){
            textheader.setTextSize(12);
            infodevice.setTextSize(6);
            textpair.setTextSize(10);
            textsearch.setTextSize(10);

        }


    }





    public void checknet(){
//		Toast.makeText(getApplicationContext(), "Checking Internet Connection",
//				   Toast.LENGTH_LONG).show();
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
//		 Toast.makeText(getApplicationContext(), ""+isConnected,
//				   Toast.LENGTH_LONG).show();
    }








    public void refreshlist(){




        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
//            Toast.makeText(getApplicationContext(),"paired device available",Toast.LENGTH_SHORT).show();
//            tvDeviceListPairedDeviceTitle.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
//                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());



                for(int i=0;i<pairedDevicesArrayAdapter.size();i++){
                    if(newDevicesArrayAdapter.contains(pairedDevicesArrayAdapter.get(i))){
//                        //Log.e("list1==","=====");
//                        status.add("\n"+""+"Available");
                        status.set(i,"Online");



                    }
                    else{
//                        //Log.e("list2==","=========");
//                        status.add("\n"+""+"Not Available");
                        status.set(i,"Offline");


                    }
                }


                if (pairedDevicesArrayAdapter.size() > 0){
                    customAdapter=new CustomAdapter(DeviceListActivity1.this,pairedDevicesArrayAdapter,status);
                    lvDeviceListPairedDevice.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                  //  lvDeviceListPairedDevice.smoothScrollToPosition(pairedDevicesArrayAdapter.size()-1);
                }





            }
        }



    }


//    @Override
//    public void onBackPressed() {
//        Intent i=new Intent(DeviceListActivity1.this,MainActivity.class);
//        startActivity(i);
//    }

    private void getWidgetReferences() {
//        tvDeviceListPairedDeviceTitle = (TextView) findViewById(R.id.tvDeviceListPairedDeviceTitle);
//        tvDeviceListNewDeviceTitle = (TextView) findViewById(R.id.tvDeviceListNewDeviceTitle);

        lvDeviceListPairedDevice = (ListView) findViewById(R.id.lvDeviceListPairedDevice);
        lvDeviceListNewDevice = (ListView) findViewById(R.id.lvDeviceListNewDevice);

        imagesearch=(ImageView)findViewById(R.id.refresh);
        imagesearch.setVisibility(View.INVISIBLE);

//        btnDeviceListScan = (Button) findViewById(R.id.btnDeviceListScan);

    }

    private void bindEventHandler() {


//        lvDeviceListPairedDevice.setOnItemClickListener(mDeviceClickListener);
//        lvDeviceListNewDevice.setOnItemClickListener(mDeviceClickListener);


        lvDeviceListPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();
//            Toast.makeText(DeviceListActivity.this, "click on device", Toast.LENGTH_SHORT).show();

                mp2.start();

                String info = pairedDevicesArrayAdapter.get(i);
               //Log.e("info===","in "+info);
                String address = info.substring(info.length() - 17);
                //Log.e("info===","in11 "+address);
                Intent intent = new Intent();
                intent.putExtra(DEVICE_ADDRESS, address);
                intent.putExtra("splash",address);


                setResult(Activity.RESULT_OK, intent);
                finish();

          /*      Intent backintent = new Intent(getApplicationContext(),MainActivity.class);
                backintent.putExtra(DEVICE_ADDRESS, address);
                backintent.putExtra("splash",address);
                startActivity(backintent);
                finish();*/
            }
        });
        lvDeviceListNewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();
                mp2.start();

                String info = newDevicesArrayAdapter.get(i);
//                //Log.e("info===",""+info);
                String address = info.substring(info.length() - 17);

                Intent intent = new Intent();
                intent.putExtra(DEVICE_ADDRESS, address);
                intent.putExtra("splash",address);



                setResult(Activity.RESULT_OK, intent);
                finish();


           /*     Intent backintent = new Intent(getApplicationContext(),MainActivity.class);
                backintent.putExtra(DEVICE_ADDRESS, address);
                backintent.putExtra("splash",address);
                startActivity(backintent);
                finish();
*/
            }
        }) ;


        imagesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvDeviceListNewDevice.setAdapter(null);
                newDevicesArrayAdapter.clear();
                startDiscovery();
                mp2.start();
                imagesearch.setVisibility(View.GONE);

                if (lvDeviceListPairedDevice!=null){
                    refreshlist();
                }
            }
        });


//        btnDeviceListScan.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                startDiscovery();
//                mp2.start();
//                btnDeviceListScan.setVisibility(View.GONE);
//            }
//        });
    }

    private void initializeValues() {
//        pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
//        newDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);





//        lvDeviceListPairedDevice.setAdapter(pairedDevicesArrayAdapter);
//        lvDeviceListNewDevice.setAdapter(newDevicesArrayAdapter);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryFinishReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishReceiver, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
//            Toast.makeText(getApplicationContext(),"paired device available",Toast.LENGTH_SHORT).show();
//            tvDeviceListPairedDeviceTitle.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                status.add("\n"+""+"Not Available");



                if (pairedDevicesArrayAdapter.size() > 0){
                    customAdapter=new CustomAdapter(this,pairedDevicesArrayAdapter,status);
                    lvDeviceListPairedDevice.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();
                    //lvDeviceListPairedDevice.smoothScrollToPosition(pairedDevicesArrayAdapter.size()-1);
                }




            }
        }


        else {
            String noDevices="no new Devices found";
//            String noDevices = getResources().getText(R.string.none_paired).toString();
            pairedDevicesArrayAdapter.add(noDevices);
            status.add("");

            if (pairedDevicesArrayAdapter.size() > 0){
                customAdapter=new CustomAdapter(this,pairedDevicesArrayAdapter,status);
                lvDeviceListPairedDevice.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
              //  lvDeviceListPairedDevice.smoothScrollToPosition(pairedDevicesArrayAdapter.size()-1);
            }


        }
    }

    private void startDiscovery() {
        setProgressBarIndeterminateVisibility(true);
//        setTitle(R.string.scanning);
        infodevice.setText("scanning....");

//        tvDeviceListNewDeviceTitle.setVisibility(View.VISIBLE);

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            bluetoothAdapter.cancelDiscovery();
//            Toast.makeText(DeviceListActivity.this, "click on device", Toast.LENGTH_SHORT).show();

            mp2.start();

            String info = ((TextView) v).getText().toString();
//            //Log.e("info===",""+info);
            String address = info.substring(info.length() - 17);

            Intent intent = new Intent();
            intent.putExtra(DEVICE_ADDRESS, address);
            intent.putExtra("splash",address);

            setResult(Activity.RESULT_OK, intent);
            finish();

       /*     Intent backintent = new Intent(getApplicationContext(),MainActivity.class);
            backintent.putExtra(DEVICE_ADDRESS, address);
            backintent.putExtra("splash",address);
            startActivity(backintent);
            finish();*/
        }
    };

    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                if (device.getBondState()<1){

                    newDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    status1.add("\n"+""+"Available");

                    if (newDevicesArrayAdapter.size() > 0){
                        customAdapter2=new CustomAdapter2(DeviceListActivity1.this,newDevicesArrayAdapter,status1);
                        lvDeviceListNewDevice.setAdapter(customAdapter2);
                        customAdapter2.notifyDataSetChanged();
                      //  lvDeviceListNewDevice.smoothScrollToPosition(newDevicesArrayAdapter.size()-1);
                    }


                if (lvDeviceListPairedDevice!=null){
                    refreshlist();
                }







//                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                imagesearch.setVisibility(View.VISIBLE);
                setProgressBarIndeterminateVisibility(false);
//                setTitle(R.string.select_device);
                infodevice.setText("select a device to connect");
                if (newDevicesArrayAdapter.isEmpty()) {
                    String noDevices="no new Devices found";
//                    String noDevices = getResources().getText(R.string.none_found).toString();
                    newDevicesArrayAdapter.add(noDevices);
                    status1.add("");

                    if (newDevicesArrayAdapter.size() > 0){
                        customAdapter2=new CustomAdapter2(DeviceListActivity1.this,newDevicesArrayAdapter,status1);
                        lvDeviceListNewDevice.setAdapter(customAdapter2);
                        customAdapter2.notifyDataSetChanged();
                      //  lvDeviceListNewDevice.smoothScrollToPosition(newDevicesArrayAdapter.size()-1);

                    }




                }
            }
        }
    };

    @Override
    protected void onDestroy() {
//        Log.i("on Destroy call","====");
        super.onDestroy();

        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(discoveryFinishReceiver);
    }
}
