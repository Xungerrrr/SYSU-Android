package com.xungerrrr.healthyfood;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListView mListView;
    private BaseAdapter mListViewAdapter;
    private FloatingActionButton mFloatingActionButton;

    public static class MessageEvent {
        public int position;
        public int requestCode;
        MessageEvent(int p, int r) {
            position = p;
            requestCode = r;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mListView = findViewById(R.id.my_list_view);
        mFloatingActionButton = findViewById(R.id.float_btn);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("删除").create();
        List<Map<String, Object>> data = new ArrayList<>();
        String[] foodName =
                new String[] {"大豆", "十字花科蔬菜", "牛奶", "海鱼", "菌菇类", "番茄", "胡萝卜", "荞麦", "鸡蛋"};
        String[] foodCategoryFirst =
                new String[] {"粮", "蔬", "饮", "肉", "蔬", "蔬", "蔬", "粮", "杂"};
        String[] foodCategory =
                new String[] {"粮食", "蔬菜", "饮品", "肉食", "蔬菜", "蔬菜", "蔬菜", "粮食", "杂"};
        String[] foodNutrient =
                new String[] {"蛋白质", "维生素C", "钙", "蛋白质", "微量元素", "番茄红素", "胡萝卜素", "膳食纤维", "几乎所有营养物质"};
        String[] foodColor =
                new String[] {"#BB4C3B", "#C48D30", "#4469B0", "#20A17B", "#BB4C3B", "#4469B0", "#20A17B", "#BB4C3B", "#C48D30"};
        for (int i = 0; i < foodName.length; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("name", foodName[i]);
            temp.put("first", foodCategoryFirst[i]);
            temp.put("category", foodCategory[i]);
            temp.put("nutrient", foodNutrient[i]);
            temp.put("color", foodColor[i]);
            data.add(temp);
        }
        final String STATICACTION = "com.xungerrrr.healthyfood.MyStaticFilter";
        Random random = new Random();
        int position = random.nextInt(9); //返回一个0到n-1的整数
        Intent intentBroadcast = new Intent(STATICACTION); //定义Intent
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("name", data.get(position).get("name").toString());
        bundle.putString("category", data.get(position).get("category").toString());
        bundle.putString("nutrient", data.get(position).get("nutrient").toString());
        bundle.putString("color", data.get(position).get("color").toString());
        bundle.putInt("requestCode", 1);
        intentBroadcast.putExtras(bundle);
        intentBroadcast.setComponent(new ComponentName(getPackageName(),getPackageName() + ".StaticReceiver"));
        sendBroadcast(intentBroadcast);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(this, R.layout.food_item, data) {
            @Override
            public void convert(MyViewHolder holder, Map m) {
                TextView name = holder.getView(R.id.name);
                name.setText(m.get("name").toString());
                Button first = holder.getView(R.id.category_first);
                first.setText(m.get("first").toString());
            }
        };
        ((MyRecyclerViewAdapter) mRecyclerViewAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Map<String, Object> item = ((MyRecyclerViewAdapter) mRecyclerViewAdapter).data.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("name", item.get("name").toString());
                intent.putExtra("category", item.get("category").toString());
                intent.putExtra("nutrient", item.get("nutrient").toString());
                intent.putExtra("color", item.get("color").toString());
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(int position) {
                String name = ((MyRecyclerViewAdapter) mRecyclerViewAdapter).data.get(position).get("name").toString();
                ((MyRecyclerViewAdapter) mRecyclerViewAdapter).data.remove(position);
                mRecyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "删除" + name,
                        Toast.LENGTH_SHORT).show();
            }
        });
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(mRecyclerViewAdapter);
        scaleInAnimationAdapter.setDuration(1000);
        mRecyclerView.setAdapter((scaleInAnimationAdapter));
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());

        mListViewAdapter = new MyListViewAdapter(this);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Map<String, Object> item = ((MyListViewAdapter) mListViewAdapter).data.get(position);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("name", item.get("name").toString());
                    intent.putExtra("category", item.get("category").toString());
                    intent.putExtra("nutrient", item.get("nutrient").toString());
                    intent.putExtra("color", item.get("color").toString());
                    intent.putExtra("requestCode", 2);
                    startActivityForResult(intent, 2);
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position != 0) {
                    Map<String, Object> item = ((MyListViewAdapter) mListViewAdapter).data.get(position);
                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MyListViewAdapter)mListViewAdapter).data.remove(position);
                            mListViewAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("取消", null).setMessage(
                            "确定删除" + item.get("name").toString() + "?").show();
                    return true;
                }
                return false;
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mListView.setVisibility(View.VISIBLE);
                    mFloatingActionButton.setImageResource(R.mipmap.mainpage);
                }
                else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    mFloatingActionButton.setImageResource(R.mipmap.collect);
                }
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onRestart() {
        final String WIDGETSTATICACTION= "com.xungerrrr.healtyfood.MyWidgetStaticFilter";
        super.onRestart();
        List<Map<String, Object>> data = ((MyRecyclerViewAdapter) mRecyclerViewAdapter).data;
        Random random = new Random();
        int position = random.nextInt(9); //返回一个0到n-1的整数
        Intent widgetIntentBroadcast = new Intent(WIDGETSTATICACTION); //定义Intent
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("name", data.get(position).get("name").toString());
        bundle.putString("category", data.get(position).get("category").toString());
        bundle.putString("nutrient", data.get(position).get("nutrient").toString());
        bundle.putString("color", data.get(position).get("color").toString());
        bundle.putInt("requestCode", 1);
        widgetIntentBroadcast.putExtras(bundle);
        widgetIntentBroadcast.setComponent(new ComponentName(getPackageName(),getPackageName() + ".HealthyFoodWidget"));
        sendBroadcast(widgetIntentBroadcast);
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().unregister(this);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        int position = event.position;
        Map<String, Object> item = null;
        if (event.requestCode == 1) {
            item = ((MyRecyclerViewAdapter) mRecyclerViewAdapter).data.get(position);
        }
        else if (event.requestCode == 2) {
            item = ((MyListViewAdapter) mListViewAdapter).data.get(position);
        }
        ((MyListViewAdapter)mListViewAdapter).data.add(item);
        mListViewAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.VISIBLE);
    }
}
