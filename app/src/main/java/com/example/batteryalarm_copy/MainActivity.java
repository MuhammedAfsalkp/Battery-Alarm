package com.example.batteryalarm_copy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button set;
    private ImageView b;
    private Button cb;
    private SeekBar sb;
    private TextView tv;
    boolean flag = false;
    private int charge;
    private int intermediate;
    private TextView tvc;
    private int userinput;
    private BroadcastReceiver  battery=new BroadcastReceiver() {
        @Override
        public void onReceive (Context context, Intent intent){
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            float cc = level * 100 / (float) scale;
            charge = (int) cc;
            tvc.setText(String.valueOf(charge)+"%");
            int img=charge/10;
            switch (img){
                case 10:
                    b.setImageResource(R.drawable.charge100);
                    tvc.setTextColor(Color.BLACK);
                    break;
                case 9:
                    b.setImageResource(R.drawable.charge95);
                    tvc.setTextColor(Color.BLACK);
                    break;
                case 8:
                    b.setImageResource(R.drawable.charge80);
                    tvc.setTextColor(Color.BLACK);
                    break;
                case 7:
                case 6:
                    b.setImageResource(R.drawable.charge65);
                    tvc.setTextColor(Color.BLACK);
                    break;

                case 5:
                    b.setImageResource(R.drawable.charge55);
                    tvc.setTextColor(Color.BLACK);
                    break;
                    case 4:
                case 3:
                    b.setImageResource(R.drawable.charge40);
                        tvc.setTextColor(Color.BLACK);
                    break;

                case 2:
                    b.setImageResource(R.drawable.charge30);
                    tvc.setTextColor(Color.RED);
                    break;
                case 1:
                case 0:
                    b.setImageResource(R.drawable.charge15);
                    tvc.setTextColor(Color.RED);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        registerReceiver(battery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        set = (Button) findViewById(R.id.set);
        cb = (Button) findViewById(R.id.cb);
        sb = (SeekBar) findViewById(R.id.sb);
        tv = (TextView) findViewById(R.id.tv);
        tvc = (TextView) findViewById(R.id.tvc);
        b =(ImageView) findViewById(R.id.b);


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(getApplicationContext(), "charge:" + progress, Toast.LENGTH_SHORT).show();
                tv.setText(progress + "%");
                userinput = intermediate = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Charge changing...", Toast.LENGTH_SHORT).show();
                userinput = intermediate;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "Charge Selected", Toast.LENGTH_SHORT).show();
                userinput = intermediate;


            }
        });

    }

    public void remove(View v) {
        flag=true;
        Intent service=new Intent(getBaseContext(),mybrs.class);
        stopService(service);
        Toast.makeText(getApplicationContext(),"The Alarm is not set",Toast.LENGTH_SHORT).show();
    }


    public void onClick(View v) {
        int btn=userinput;
        flag=false;
       // Toast.makeText(getApplicationContext(),"Button clicked",Toast.LENGTH_SHORT).show();
        Intent service=new Intent(getBaseContext(),mybrs.class);
        service.putExtra("UI",btn);
        service.putExtra("FLAG",flag);
        service.putExtra("C",1);
        startService(service);
        Toast.makeText(getApplicationContext(),"Alarm setting",Toast.LENGTH_SHORT).show();

    }
}
