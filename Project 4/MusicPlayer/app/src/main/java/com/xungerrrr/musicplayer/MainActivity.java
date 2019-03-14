package com.xungerrrr.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private de.hdodenhof.circleimageview.CircleImageView albumArt;
    private TextView title;
    private TextView artist;
    private TextView currentTime;
    private TextView totalTime;
    private SeekBar seekBar;
    private ImageButton file;
    private ImageButton quit;
    private de.hdodenhof.circleimageview.CircleImageView play;
    private de.hdodenhof.circleimageview.CircleImageView stop;
    private ServiceConnection serviceConnection;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private IBinder iBinder;
    private io.reactivex.Observer<Integer> observer;
    private io.reactivex.Observable<Integer> observable;
    private boolean isPlaying;
    private boolean isTrackingTouch;
    private boolean isForeground;
    private int duration;
    private final int  PLAY_CODE = 1;
    private final int STOP_CODE = 0;
    private final int GET_POSITION_CODE = 2;
    private final int LOAD_CODE = 3;
    private final int SEEK_TO_CODE = 4;
    private final int GET_DURATION_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkPermission();

        albumArt = findViewById(R.id.album_art);
        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        file = findViewById(R.id.file);
        quit = findViewById(R.id.quit);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        isPlaying = false;
        isForeground = true;

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iBinder = service;
                try {
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    iBinder.transact(GET_DURATION_CODE, data, reply,0);
                    duration = reply.readInt();
                    seekBar.setProgress(0);
                    seekBar.setMax(duration);
                    totalTime.setText(time.format(duration));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        observer = new io.reactivex.Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer position) {
                if (duration - position <= 500) {
                    seekBar.setProgress(0);
                    currentTime.setText(time.format(0));
                    play.setImageResource(R.drawable.play);
                    albumArt.setRotation(0);
                    isPlaying = false;
                }
                else {
                    String current = time.format(position);
                    albumArt.setRotation(albumArt.getRotation() + 0.2f);
                    if (!TextUtils.equals(currentTime.getText(), current) && !isTrackingTouch) {
                        currentTime.setText(current);
                        seekBar.setProgress(position);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable = io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                while (isPlaying && isForeground) {
                    Thread.sleep(10);
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    iBinder.transact(GET_POSITION_CODE, data, reply,0);
                    int position = reply.readInt();
                    emitter.onNext(position);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentTime.setText(time.format(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Parcel data = Parcel.obtain();
                    data.writeInt(seekBar.getProgress());
                    Parcel reply = Parcel.obtain();
                    iBinder.transact(SEEK_TO_CODE, data, reply,0);
                    Thread.sleep(10);
                    isTrackingTouch = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    if (isPlaying) {
                        play.setImageResource(R.drawable.play);
                        isPlaying = false;
                    } else {
                        play.setImageResource(R.drawable.pause);
                        isPlaying = true;
                        observable.subscribe(observer);
                    }
                    iBinder.transact(PLAY_CODE, data, reply, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    iBinder.transact(STOP_CODE, data, reply, 0);
                    Thread.sleep(10);
                    isPlaying = false;
                    seekBar.setProgress(0);
                    currentTime.setText(time.format(0));
                    play.setImageResource(R.drawable.play);
                    albumArt.setRotation(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = false;
                unbindService(serviceConnection);
                try {
                    MainActivity.this.finish();
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*"); //选择音频
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                Uri uri = data.getData();
                Parcel d = Parcel.obtain();
                d.writeString(uri.toString());
                Parcel reply = Parcel.obtain();
                iBinder.transact(LOAD_CODE, d, reply, 0);
                duration = reply.readInt();
                play.setImageResource(R.drawable.play);
                isPlaying = false;
                albumArt.setRotation(0);
                seekBar.setProgress(0);
                seekBar.setMax(duration);
                totalTime.setText(time.format(duration));
                currentTime.setText(time.format(0));

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(getApplicationContext(), uri);
                title.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                artist.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                byte[] art = mmr.getEmbeddedPicture();
                Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
                albumArt.setImageBitmap(bitmap);
                mmr.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        if (isPlaying) {
            observable.subscribe(observer);
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {}
}
