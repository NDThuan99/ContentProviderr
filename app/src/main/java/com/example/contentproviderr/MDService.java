package com.example.contentproviderr;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MDService extends Service {
    private MediaPlayer mp = new MediaPlayer();
    private List<String> songs = new ArrayList<String>();
    private int currentPosition;

    private NotificationManager nm;
    private static final int NOTIFY_ID = 1;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
