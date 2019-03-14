package com.xungerrrr.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MusicService extends Service {
    public final IBinder binder = new MyBinder();
    public MediaPlayer mediaPlayer = new MediaPlayer();
    private final int STOP_CODE = 0;
    private final int  PLAY_CODE = 1;
    private final int GET_POSITION_CODE = 2;
    private final int LOAD_CODE = 3;
    private final int SEEK_TO_CODE = 4;
    private final int GET_DURATION_CODE = 5;

    public MusicService() {}

    @Override
    public IBinder onBind(Intent intent) {
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() + "/data/山高水长.mp3");
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return binder;
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case PLAY_CODE: // 播放和暂停
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                    break;
                case STOP_CODE: // 停止
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        try {
                            mediaPlayer.prepare();
                            mediaPlayer.seekTo(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GET_POSITION_CODE: // 获取当前播放进度
                    reply.writeInt(mediaPlayer.getCurrentPosition());
                    break;
                case LOAD_CODE: // 选歌
                    Uri uri = Uri.parse(data.readString());
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mediaPlayer.setDataSource(getApplicationContext(), uri);
                            mediaPlayer.prepare();
                            mediaPlayer.seekTo(0);
                            reply.writeInt(mediaPlayer.getDuration());
                        }
                        catch(IOException s) {
                            Log.e("TAG", "Failed to open the audio file.");
                        }
                    }
                    break;
                case SEEK_TO_CODE:  // 改变进度
                    mediaPlayer.seekTo(data.readInt());
                    break;
                case GET_DURATION_CODE: // 获取音乐时长
                    reply.writeInt(mediaPlayer.getDuration());
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
