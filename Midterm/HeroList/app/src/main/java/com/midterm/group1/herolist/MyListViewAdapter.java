package com.midterm.group1.herolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter
        implements Filterable
                                 {
    private List<Hero> list;
    private ArrayList<Hero> fullList;
    private Context context;
    private MyFilter filter;

    public MyListViewAdapter(Context context, List<Hero> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.item_list_view, null);
            viewHolder = new ViewHolder();
            //viewHolder.src = (ImageView) convertView.findViewById(R.id.img_main_card);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_card_main_title);
            //viewHolder.location = (TextView) convertView.findViewById(R.id.tv_card_main_location);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        //viewHolder.type.setText(list.get(i).getType());
        //viewHolder.src.setImageResource(list.get(i).getIconid());
        viewHolder.name.setText(list.get(i).getName());
        //viewHolder.location.setText(list.get(i).getLocation());
        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        //public ImageView src;
        //public TextView type;
        public TextView name;
        //public TextView location;
    }

    public void setFilterText(String text) {
        if(fullList == null)
            fullList = new ArrayList<Hero>(list);
        if(list != null) list.clear();
        for(int i = 0; i < fullList.size(); i++) {
            if(fullList.get(i).getName().contains(text) ||
                    fullList.get(i).getLocation().contains(text) ||
                    fullList.get(i).getType().contains(text)) {
                list.add(fullList.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void clearTextFilter() {
        if(fullList == null)
            fullList = new ArrayList<Hero>(list);
        if(list != null) list.clear();
        for (int i = 0; i < fullList.size(); i++) {
             list.add(fullList.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
            filter = new MyFilter();
        return filter;
    }

    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constrain) {
            String tempstr = (String) constrain;
            String[] buff = tempstr.split(",");
            FilterResults results = new FilterResults();

            if(fullList == null)
                fullList = new ArrayList<Hero>(list);
            if(buff.length == 1 || buff[1] == null || buff[1].length() == 0) {
                ArrayList<Hero> temp = new ArrayList<Hero>(fullList);
                results.values = temp;
                results.count = temp.size();
            }
            else {
                ArrayList<Hero> temp = fullList;
                int count = temp.size();
                ArrayList<Hero> newlist = new ArrayList<Hero>(count);
                for(int i = 0; i < count; i++) {
                    Hero value = (Hero) temp.get(i);
                    if(valid(buff[0], buff[1], value)) {
                        newlist.add(value);
                    }
                }
                results.values = newlist;
                results.count = newlist.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List<Hero>) results.values;
            if(results.count > 0)
                notifyDataSetChanged();
            else
                notifyDataSetInvalidated();
        }

        private boolean valid(String category, String query, Hero value) {
            if(category.equals("Name")) {
                return value.getName().contains(query);
            }
            else if(category.equals("Type")) {
                return value.getType().contains(query);
            }
            else if(category.equals("Location")) {
                return  value.getLocation().contains(query);
            }
            return (value.getName().contains(query) ||
                    value.getLocation().contains(query) ||
                    value.getType().contains(query));
        }
    }
}

