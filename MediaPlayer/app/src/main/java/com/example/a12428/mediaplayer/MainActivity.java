package com.example.a12428.mediaplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StateSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Objects;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import static android.R.attr.duration;
import static android.R.attr.name;
import static android.R.attr.rotation;
import static android.R.attr.seekBarStyle;
import static android.R.attr.toAlpha;
import static com.example.a12428.mediaplayer.R.id.button1;
import static com.example.a12428.mediaplayer.R.id.text;


public class MainActivity extends AppCompatActivity {
    private MusicService.MyBinder mBinder;
    //private ServiceConnection sc;
    private Parcel sParcel,sParcel1,sParcel2;
    private Parcel rParcel,rParcel1,rParcel2;
    private boolean right = false;
    private ObjectAnimator mObjectanimator;
    static private int b1flag = 0;
    private TextView textPlay;
    private TextView textEndTime;
    private TextView textStartTime;
    private SeekBar seekbar;
    private Button button1;
    private Button button2 ;
    private Button button3;

    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private ProgressBar mProgressBar;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MusicService.MyBinder) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = (ImageView) findViewById(R.id.Image);
        textPlay=(TextView) findViewById(R.id.playTextView) ;
        textStartTime=(TextView)findViewById(R.id.starttime);
        textEndTime=(TextView)findViewById(R.id.endTime);
        seekbar = (SeekBar)findViewById(R.id.SeekBar);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        mObjectanimator = ObjectAnimator.ofFloat(image, "rotation", 0f, 359f);
        mObjectanimator.setDuration(30000).setInterpolator(new LinearInterpolator());//  匀速
        mObjectanimator.setRepeatCount(-1);//永久旋转
        verifyStoragePermissions(MainActivity.this);

        if (right) {
            Intent StartService = new Intent(MainActivity.this, MusicService.class);
            bindService(StartService, sc, BIND_AUTO_CREATE);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sParcel = Parcel.obtain();
                rParcel = Parcel.obtain();
                if (b1flag == 0) {
                    textPlay.setText("正在播放");
                    button1.setText("暂停");
                    mObjectanimator.start();
                    b1flag = 1;
                } else if (b1flag == 1) {
                    textPlay.setText("开始");
                    button1.setText("开始");
                    mObjectanimator.pause();
                    b1flag = 2;
                } else {
                    textPlay.setText("正在播放");
                    button1.setText("暂停");
                    mObjectanimator.resume();
                    b1flag = 1;
                }
                try {
                    mBinder.transact(101, sParcel, rParcel, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sParcel = Parcel.obtain();
                rParcel = Parcel.obtain();
                textPlay.setText("未播放");
                button1.setText("开始");
                mObjectanimator.end();
                try {
                    mBinder.transact(102, sParcel, rParcel, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(sc);
                Intent StopService = new Intent(MainActivity.this,MusicService.class);
                stopService(StopService);
                finish();
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sParcel2 = Parcel.obtain();
                sParcel2.writeInt(seekBar.getProgress());
                rParcel2=Parcel.obtain();
                try {
                    mBinder.transact(105,sParcel2,rParcel2,0);
                }catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        final Handler mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        sParcel1 = Parcel.obtain();
                        rParcel1 = Parcel.obtain();
                        try{
                            mBinder.transact(104,sParcel1,rParcel1,0);
                        }catch(RemoteException e){
                            e.printStackTrace();
                        }
                        rParcel1.setDataPosition(0);
                        int currentposition=rParcel1.readInt();
                        rParcel1.setDataPosition(4);
                        int Duration = rParcel1.readInt();
                        if(right) {   //&&!pullseekbar
                            textStartTime.setText(time.format(currentposition));
                            textEndTime.setText(time.format(Duration));
                            seekbar.setProgress(currentposition);
                            seekbar.setMax(Duration);
                        }
                        break;
                    default:break;

                }

            }
        };
        Thread mThread = new Thread(){
            @Override
            public void run(){
                while(true)
                {
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if(sc!=null && mBinder!=null){
                        Message mMessage = new Message();
                        mMessage.what=1;
                        mHandler.sendMessage(mMessage);
                    }
                }

            }

        };
        mThread.start();
    }

    public void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                right = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                right = true;
            } else {
                finish();
                System.exit(0);
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}


