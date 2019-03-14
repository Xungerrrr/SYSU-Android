package com.xungerrrr.healthyfood;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity {
    private int position, requestCode;
    private String name, category, nutrient, color;
    private TextView foodName, foodCategory, foodNutrient;
    private LinearLayout topPanel;
    private ImageButton back, collect, star;
    private ListView bottom_list;
    private static final String DYNAMICACTION = "com.xungerrrr.healthyfood.MyDynamicFilter";
    private static final String WIDGETDYNAMICACTION = "com.xungerrrr.healthyfood.MyWidgetDynamicFilter";
    private DynamicReceiver dynamicReceiver = new DynamicReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        foodName = findViewById(R.id.food_name);
        foodCategory = findViewById(R.id.food_category);
        foodNutrient = findViewById(R.id.food_nutrient);
        topPanel = findViewById(R.id.top_panel);
        back = findViewById(R.id.back);
        collect = findViewById(R.id.collect);
        star = findViewById(R.id.star);
        bottom_list = findViewById(R.id.bottom_panel);
        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            position = extras.getInt("position");
            name = extras.getString("name");
            category = extras.getString("category");
            nutrient = extras.getString("nutrient");
            color = extras.getString("color");
            requestCode = extras.getInt("requestCode");
        }
        foodName.setText(name);
        foodCategory.setText(category);
        foodNutrient.setText("富含 " + nutrient);
        topPanel.setBackgroundColor(Color.parseColor(color));
        collect.setTag(0);
        star.setTag(0);

        String[] operations = new String[] {"分享信息", "不感兴趣", "查看更多的信息", "出错反馈"};
        ArrayAdapter<String> bottomArrayAdapter = new ArrayAdapter<String>(this, R.layout.bottom_item, operations);
        bottom_list.setAdapter(bottomArrayAdapter);

        IntentFilter dynamicFilter = new IntentFilter();
        dynamicFilter.addAction(DYNAMICACTION);    //添加动态广播的Action
        dynamicFilter.addAction(WIDGETDYNAMICACTION);
        registerReceiver(dynamicReceiver, dynamicFilter);    //注册自定义动态广播消息

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(dynamicReceiver);
                finish();
            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect.setTag(1);
                Intent intentBroadcast = new Intent();   //定义Intent
                intentBroadcast.setAction(DYNAMICACTION);
                intentBroadcast.putExtras(extras);
                sendBroadcast(intentBroadcast);
                intentBroadcast.setAction(WIDGETDYNAMICACTION);
                sendBroadcast(intentBroadcast);

                EventBus.getDefault().post(new MainActivity.MessageEvent(position, requestCode));
                Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (star.getTag().equals(0)) {
                    star.setTag(1);
                    star.setImageResource(R.mipmap.full_star);
                }
                else {
                    star.setTag(0);
                    star.setImageResource(R.mipmap.empty_star);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        unregisterReceiver(dynamicReceiver);
        finish();
    }
}
