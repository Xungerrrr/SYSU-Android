package com.xungerrrr.storage2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> data;
    private myDB db;
    private String name;

    public MyAdapter(Context _context, List<Map<String, Object>> list, String _name) {
        context = _context;
        data = list;
        db = new myDB(_context);
        name = _name;
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
        final ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = convertView.findViewById(R.id.comment_avatar);
            viewHolder.name = convertView.findViewById(R.id.comment_name);
            viewHolder.time = convertView.findViewById(R.id.time);
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.num = convertView.findViewById(R.id.number_of_likes);
            viewHolder.like = convertView.findViewById(R.id.like);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        final int _id = (int)data.get(i).get("_id");
        final int tag = (int)data.get(i).get("tag");
        BitmapDrawable bitmapDrawable = new BitmapDrawable((Bitmap)data.get(i).get("avatar"));
        viewHolder.avatar.setImageDrawable(bitmapDrawable);
        viewHolder.name.setText(data.get(i).get("name").toString());
        viewHolder.time.setText(data.get(i).get("time").toString());
        viewHolder.content.setText(data.get(i).get("content").toString());
        viewHolder.num.setText(data.get(i).get("number").toString());
        if (tag == 0) {
            viewHolder.like.setBackgroundResource(R.mipmap.white);
        }
        else {
            viewHolder.like.setBackgroundResource(R.mipmap.red);
        }
        viewHolder.like.setTag(tag);
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int)v.getTag() == 0) {
                    v.setBackgroundResource(R.mipmap.red);
                    v.setTag(1);
                    int num = Integer.parseInt(viewHolder.num.getText().toString()) + 1;
                    viewHolder.num.setText(Integer.toString(num));
                    db.like(name, _id);
                }
                else {
                    v.setBackgroundResource(R.mipmap.white);
                    v.setTag(0);
                    int num = Integer.parseInt(viewHolder.num.getText().toString()) - 1;
                    viewHolder.num.setText(Integer.toString(num));
                    db.unlike(name, _id);
                }
            }
        });
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public ImageView avatar;
        public TextView name;
        public TextView time;
        public TextView content;
        public TextView num;
        public ImageView like;
    }
}
