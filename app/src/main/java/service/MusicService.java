package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;

import com.cerveauroyal.R;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        int musicUrl = bundle.getInt("musicUrl");
        boolean isLoop = bundle.getBoolean("isLoop");
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, musicUrl);
            mediaPlayer.setLooping(isLoop);
            mediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}