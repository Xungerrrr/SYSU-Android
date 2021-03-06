package com.xungerrrr.healthyfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    public List<Map<String, Object>> data = new ArrayList<>();

    public MyListViewAdapter(Context _context) {
        context = _context;
        Map<String, Object> init = new LinkedHashMap<>();
        init.put("name", "收藏夹");
        init.put("first", "*");
        data.add(init);
    }
    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if (data == null) {
            return null;
        }
        return data.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHolder变量,ViewHolder类在下面定义。
        View convertView;
        ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.food_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.first = convertView.findViewById(R.id.category_first);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.name.setText(data.get(i).get("name").toString());
        viewHolder.first.setText(data.get(i).get("first").toString());
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public Button first;
        public TextView name;
    }
}
