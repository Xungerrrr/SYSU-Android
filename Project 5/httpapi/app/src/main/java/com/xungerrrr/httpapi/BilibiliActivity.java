package com.xungerrrr.httpapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BilibiliActivity extends AppCompatActivity {
    private EditText id;
    private Button search;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<RecyclerObj> data;
    private io.reactivex.Observer<RecyclerObj> observer;
    private io.reactivex.Observable<RecyclerObj> observable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilibili);
        id = findViewById(R.id.search_src_text);
        search = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.recycler_view);
        data = new LinkedList<>();
        ImageView image = findViewById(R.id.cover);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerViewAdapter<RecyclerObj>(getApplicationContext(), R.layout.item, data) {
            @Override
            public void convert(final MyViewHolder holder, final RecyclerObj obj) {
                final ImageView cover = holder.getView(R.id.cover);
                final ProgressBar progress = holder.getView(R.id.progress);
                final SeekBar seek = holder.getView(R.id.seek);
                TextView play = holder.getView(R.id.play);
                TextView video_review = holder.getView(R.id.video_review);
                TextView duration = holder.getView(R.id.duration);
                TextView create = holder.getView(R.id.create);
                TextView title = holder.getView(R.id.title);
                TextView content = holder.getView(R.id.content);

                io.reactivex.Observer<Bitmap[]> observer = new Observer<Bitmap[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap[] bitmaps) {
                        holder.bitmaps = bitmaps;
                        cover.setImageDrawable(new BitmapDrawable(bitmaps[0]));
                        progress.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
                io.reactivex.Observable<Bitmap[]> observable = Observable.create(new ObservableOnSubscribe<Bitmap[]>() {
                    @Override
                    public void subscribe(ObservableEmitter<Bitmap[]> emitter) throws Exception {
                        URL url = new URL(obj.getData().getCover());
                        url.openConnection();

                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        url = new URL("https://api.bilibili.com/pvideo?aid=" + obj.getData().getAid());
                        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        httpURLConnection.setReadTimeout(3000);
                        httpURLConnection.setConnectTimeout(3000);
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.connect();
                        int responseCode = httpURLConnection.getResponseCode();
                        if (responseCode != HttpURLConnection.HTTP_OK) {
                            throw new IOException("HTTP error code:" + responseCode);
                        }
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        String jsonString = sb.toString();
                        PreviewObj previewObj = new Gson().fromJson(jsonString, PreviewObj.class);
                        int x = previewObj.getData().getImg_x_len();
                        int y = previewObj.getData().getImg_y_len();
                        int width = previewObj.getData().getImg_x_size();
                        int height = previewObj.getData().getImg_y_size();
                        Bitmap[] bitmaps = new Bitmap[x * y + 1];
                        seek.setMax(x * y - 1);
                        bitmaps[0] = image;
                        url = new URL(previewObj.getData().getImage()[0]);
                        Bitmap preview = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        if (preview != null && preview.getWidth() == x * width && preview.getHeight() == y * height) {
                            for (int i = 0; i < y; i++) {
                                for (int j = 0; j < x; j++) {
                                    Bitmap bitmap = Bitmap.createBitmap(preview, j * width, i * height, width, height);
                                    bitmaps[i * x + j + 1] = bitmap;
                                }
                            }
                        }
                        else {
                            seek.setEnabled(false);
                        }
                        emitter.onNext(bitmaps);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

                seek.setProgress(0);
                play.setText("播放：" + obj.getData().getPlay());
                video_review.setText("评论：" + obj.getData().getVideo_review());
                duration.setText("时长：" + obj.getData().getDuration());
                create.setText("创建时间：" + obj.getData().getCreate());
                title.setText(obj.getData().getTitle());
                content.setText(obj.getData().getContent());
                observable.subscribe(observer);

                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            cover.setImageDrawable(new BitmapDrawable(holder.bitmaps[progress + 1]));
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        cover.setImageDrawable(new BitmapDrawable(holder.bitmaps[1]));
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress(0);
                        cover.setImageDrawable(new BitmapDrawable(holder.bitmaps[0]));
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        observer = new Observer<RecyclerObj>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(RecyclerObj obj) {
                if (obj.getStatus()) {
                    data.add(obj);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(), "数据库中不存在记录", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable = Observable.create(new ObservableOnSubscribe<RecyclerObj>() {
            @Override
            public void subscribe(ObservableEmitter<RecyclerObj> emitter) throws Exception {
                String text = "https://space.bilibili.com/ajax/top/showTop?mid=" + id.getText();
                URL url = new URL(text);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code:" + responseCode);
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                String jsonString = sb.toString();
                RecyclerObj recyclerObj = new RecyclerObj();
                if (TextUtils.equals(jsonString.substring(10, 15), "false")) {
                    recyclerObj.setStatus(false);

                }
                else {
                    recyclerObj = new Gson().fromJson(jsonString, RecyclerObj.class);
                }
                emitter.onNext(recyclerObj);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    try {
                        int integer = Integer.parseInt(id.getText().toString());
                        observable.subscribe(observer);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "需要整数类型数据", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
