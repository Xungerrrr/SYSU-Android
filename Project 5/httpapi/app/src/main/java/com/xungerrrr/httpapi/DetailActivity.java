package com.xungerrrr.httpapi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private EditText token;
    private EditText title;
    private EditText body;
    private Button add;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Issue> data;
    private String user;
    private String repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getString("user");
        repo = bundle.getString("repo");

        token = findViewById(R.id.token);
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        add = findViewById(R.id.add_issue);
        recyclerView = findViewById(R.id.recycler_view);
        data = new LinkedList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerViewAdapter<Issue>(getApplicationContext(), R.layout.item2, data) {
            @Override
            public void convert(MyViewHolder holder, Issue t) {
                TextView title = holder.getView(R.id.repo);
                TextView time = holder.getView(R.id.repo_id);
                TextView state = holder.getView(R.id.issue_num);
                TextView body = holder.getView(R.id.repo_description);

                title.setText("标题：" + t.title);
                time.setText("创建时间：" + t.created_at);
                state.setText("问题状态：" + t.state);
                body.setText("问题描述：" + t.body);
            }
        };
        recyclerView.setAdapter(adapter);
        OkHttpClient build = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();
        final GitHubService gitHubService = retrofit.create(GitHubService.class);

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Observable<List<Issue>> observable = gitHubService.getIssue(user, repo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            observable.subscribe(new Observer<List<Issue>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<Issue> issues) {
                    if (issues.size() == 0) {
                        Toast.makeText(getApplicationContext(), "该repo不存在任何issue", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < issues.size(); i++) {
                        data.add(issues.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(body.getText())) {
                    Toast.makeText(getApplicationContext(), "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    Issue issue = new Issue();
                    issue.title = title.getText().toString();
                    issue.body = body.getText().toString();
                    Observable<Issue> observable = gitHubService.postIssue(issue, user, repo)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    observable.subscribe(new Observer<Issue>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Issue issue) {
                            data.add(0, issue);
                            adapter.notifyItemInserted(0);
                            Toast.makeText(getApplicationContext(), "增加成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), "增加失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
