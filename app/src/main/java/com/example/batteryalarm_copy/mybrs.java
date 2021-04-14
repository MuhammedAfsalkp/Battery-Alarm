package com.example.batteryalarm_copy;

import  android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.batteryalarm_copy.App.CHANNEL_ID;


public class mybrs extends Service {
    MediaPlayer mp;
    int current_charge;
    boolean my_flag=false;
    int ui;
    boolean alarm;
    int c;




    private static BroadcastReceiver battery;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){

        Toast.makeText(getApplicationContext(),"Service created",Toast.LENGTH_SHORT).show();
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent,int flag,int startId){
        Toast.makeText(getApplicationContext(),"service started"+ui,Toast.LENGTH_SHORT).show();
        if(intent != null && intent.getExtras() != null){
            ui=intent.getIntExtra("UI",5);
            alarm=intent.getBooleanExtra("FLAG",false);
            c=intent.getIntExtra("C",1);
        }

        //Taping the notification-open app
        Intent activity=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,activity,0);

        //creating notification during the service
        Notification not=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(getText(R.string.app_name))
                .setSmallIcon(R.drawable.icon)
                .setContentText("Alarm when Charge is "+ui+"%")
                .setContentInfo("Alarm at charge"+ui+"%")
                .setContentIntent(pi).build();
        startForeground(1,not);



       if(!alarm){   register();   }
        return START_STICKY;

    }
    @Override
    public void onDestroy(){
        if(alarm){
        mp.stop();}
        stopForeground(true);
        super.onDestroy();
        unregisterReceiver(battery);
        Toast.makeText(getApplicationContext(),"Alarm Service Canceled",Toast.LENGTH_LONG).show();
         battery=null;
         alarm=false;
    }
    public void register(){
        battery=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                float cc = level * 100 / (float) scale;
                current_charge = (int) cc;
                if(current_charge==ui && !alarm) {
                   update();
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                    Toast.makeText(getApplicationContext(), "Alarming...", Toast.LENGTH_LONG).show();
                    mp.start();
                    my_flag=true;
                    alarm=true;
                }
                if(current_charge != ui) {

                    if(alarm || my_flag){
                    mp.stop();
                    alarm=false;
                    }if(c==1) {
                        Toast.makeText(getApplicationContext(), "Alarm when charge is" + ui + "%", Toast.LENGTH_SHORT).show();
                        c++;
                    }
                }
            }
        };
        registerReceiver(battery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }
    private void update(){
        Intent activity=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,activity,0);

        //creating notification during the service
        Notification not=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(getText(R.string.app_name))
                .setSmallIcon(R.drawable.icon)
                .setContentText("Alarming.....")
                .setContentInfo("Alarm at charge"+ui+"%")
                .setContentIntent(pi).build();
        startForeground(1,not);
    }


}
