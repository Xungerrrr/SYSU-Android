package com.midterm.group1.herolist;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class AddHerosActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private List<Hero> data;
    private MyData myData = new MyData(this);
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mRecyclerView = findViewById(R.id.add_heros_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        username = (String) getIntent().getSerializableExtra("add");;
        mRecyclerView.setLayoutManager(linearLayoutManager);

        initData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add Hero");
        }
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(this, R.layout.add_item_recycler_view, data, username) {
            @Override
            public void convert(MyViewHolder holder, final Hero m) {
                ImageView image = holder.getView(R.id.rela_round);
                TextView title = holder.getView(R.id.add_heros_name);
                TextView location = holder.getView(R.id.add_heros_position);
                ImageView add = holder.getView(R.id.add_heros_add_btn);

                image.setImageResource(m.getIconid());
                title.setText(m.getName());
                location.setText(m.getLocation());


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //增加英雄……………………
                        myData.addHero(username,m.getName());
                        Intent intent = new Intent();
                        intent.putExtra("heroname", m.getName());
                        setResult(2, intent);
                        finish();
                    }

                });

            }
        };
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_recycler_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }

    private void initData(){
        data = new ArrayList<>();
        data = myData.userToAdd(username);

        for(int i = 0; i < data.size(); i++){
            ApplicationInfo appInfo = getApplicationInfo();
            int resID = getResources().getIdentifier(data.get(i).getImageIcon(), "drawable", appInfo.packageName);
            data.get(i).setIconid(resID);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}