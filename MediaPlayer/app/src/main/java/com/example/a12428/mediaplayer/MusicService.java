package com.example.a12428.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;

import static android.R.attr.data;

public class MusicService extends Service {
    public static MediaPlayer mp = new MediaPlayer();
    private MyBinder myBinder = new MyBinder();
    static int i=1;
    private String currentState="";

    public MusicService(){
        try{
            mp.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/documents/melt.mp3");
            mp.prepare();
            mp.setLooping(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return myBinder;
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data,Parcel reply, int flags) throws RemoteException{
            switch (code)
            {
                case 101:
                    if(mp.isPlaying())mp.pause();
                    else
                        mp.start();
                    break;
                case 102:
                    mp.stop();
                    try{
                        mp.prepare();
                        mp.seekTo(0);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 103:
                    break;
                case 104:
                    reply.writeInt(mp.getCurrentPosition());
                    reply.writeInt(mp.getDuration());
                    break;
                case 105:
                    mp.seekTo(data.readInt());
                    break;
                default:break;
            }
            return super.onTransact(code,data,reply,flags);
        }

    }

}
