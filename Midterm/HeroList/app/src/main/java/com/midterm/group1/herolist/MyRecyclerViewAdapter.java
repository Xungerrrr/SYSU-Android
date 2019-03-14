package com.midterm.group1.herolist;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>  {
    private Context context;
    private int layoutId;
    public List<Hero> allData;
    public List<Hero> data;
    private OnItemClickListener onItemClickListener;
    private ViewGroup parentView;
    private MyData myData;
    public String name;

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    // Provide a reference to the views for each data item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views;
        private View view;

        public MyViewHolder(Context _context, View _view, ViewGroup _viewGroup) {
            super(_view);
            view = _view;
            views = new SparseArray<View>();
        }
        public static MyViewHolder get(Context _context, ViewGroup _viewGroup, int _layoutId) {
            View _view = LayoutInflater.from(_context).inflate(_layoutId, _viewGroup, false);
            return new MyViewHolder(_context, _view, _viewGroup);
        }
        public <T extends View> T getView(int _viewId) {
            View _view = views.get(_viewId);
            if (_view == null) {
                // 创建view
                _view = view.findViewById(_viewId);
                // 将view存入views
                views.put(_viewId, _view);
            }
            return (T)_view;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerViewAdapter(Context _context, int _layoutId, List _data, String _name) {
        context = _context;
        layoutId = _layoutId;
        allData = _data;
        data = new LinkedList<>();
        myData = new MyData(context);
        name = _name;
        for (int i = 0; i < allData.size(); i++) {
            data.add(allData.get(i));
        }
    }

    public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
        onItemClickListener = _onItemClickListener;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        parentView = parent;
        MyViewHolder vh = MyViewHolder.get(context, parent, layoutId);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        convert(holder, data.get(position)); // convert函数需要重写
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
        final MyViewHolder recyclerViewHolder = (MyViewHolder) holder;

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.recycler_item_show);
        recyclerViewHolder.view.startAnimation(animation);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public abstract void convert(MyViewHolder holder, Hero t);

    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(data, fromPosition, toPosition);
        Collections.swap(allData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void onItemDelete(final int position) {
        final Hero deleted = data.get(position);
        data.remove(position);
        allData.remove(position);
        if (!TextUtils.isEmpty(name)) {
            myData.unAddHero(name, deleted.getName());
        }
        notifyItemRemoved(position);

        Snackbar.make(parentView, "Deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.add(position, deleted);
                        allData.add(position, deleted);
                        if (!TextUtils.isEmpty(name)) {
                            myData.addHero(name, deleted.getName());
                        }
                        notifyItemInserted(position);
                    }
                }).show();
    }

    public void setFilterText(String text) {
        data.clear();
        for (int i = 0; i < allData.size(); i++) {
            if(allData.get(i).getName().contains(text) ||
                    allData.get(i).getLocation().contains(text) ||
                    allData.get(i).getType().contains(text)) {
                data.add(allData.get(i));
            }
        }
        notifyDataSetChanged();
    }
    public void clearTextFilter() {
        onDataSetChanged();
        notifyDataSetChanged();
    }
    public void onDataSetChanged() {
        data.clear();
        for (int i = 0; i < allData.size(); i++) {
            data.add(allData.get(i));
        }
    }
}
