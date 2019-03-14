package com.xungerrrr.httpapi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitHubActivity extends AppCompatActivity {
    private EditText username;
    private Button search;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Repo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_git_hub);
        username = findViewById(R.id.search_src_text);
        search = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.recycler_view);
        data = new LinkedList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerViewAdapter<Repo>(getApplicationContext(), R.layout.item2, data) {
            @Override
            public void convert(MyViewHolder holder, Repo t) {
                TextView repo = holder.getView(R.id.repo);
                TextView id = holder.getView(R.id.repo_id);
                TextView num = holder.getView(R.id.issue_num);
                TextView description = holder.getView(R.id.repo_description);

                repo.setText("项目名：" + t.name);
                id.setText("项目id：" + Integer.toString(t.id));
                num.setText("存在问题：" + Integer.toString(t.open_issues));
                description.setText("项目描述：" + t.description);
            }
        };
        ((MyRecyclerViewAdapter) adapter).setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(GitHubActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user", username.getText().toString());
                bundle.putString("repo", data.get(position).name);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
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



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    Observable<List<Repo>> observable = gitHubService.getRepo(username.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    observable.subscribe(new Observer<List<Repo>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(List<Repo> repos) {
                            data.clear();
                            adapter.notifyDataSetChanged();
                            if (repos.size() == 0) {
                                Toast.makeText(getApplicationContext(), "该用户没有任何repo", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (int i = 0; i < repos.size(); i++) {
                                if (repos.get(i).has_issues) {
                                    data.add(repos.get(i));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            data.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "用户不存在", Toast.LENGTH_SHORT).show();
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
