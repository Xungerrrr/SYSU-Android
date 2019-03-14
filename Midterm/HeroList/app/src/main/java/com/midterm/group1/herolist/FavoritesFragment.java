package com.midterm.group1.herolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private List<Hero> Likes = new ArrayList<Hero>();
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ItemTouchHelper mItemTouchHelper;
    private MyData myData;
    private String name = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConstraintLayout constraintLayout = (ConstraintLayout)inflater.inflate(R.layout.content_main, container, false);
        mRecyclerView = constraintLayout.findViewById(R.id.recyclerView);
        return constraintLayout;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myData = new MyData(getActivity().getApplicationContext());

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        mRecyclerViewAdapter = new MyRecyclerViewAdapter(getContext(), R.layout.item_recycler_view, Likes, name) {
            @Override
            public void convert(final MyViewHolder holder, final Hero m) {
                ImageView image = holder.getView(R.id.img_main_card);
                TextView title = holder.getView(R.id.tv_card_main_title);
                TextView location = holder.getView(R.id.tv_card_main_location);
                final ImageView favorite = holder.getView(R.id.img_main_card_favorite);
                TextView type = holder.getView(R.id.tv_card_main_type);

                image.setImageResource(m.getIconid());
                title.setText(m.getName());
                location.setText(m.getLocation());
                type.setText(m.getType());
                favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Hero unliked = m;
                        final int position = holder.getAdapterPosition();
                        myData.unlikeHero(name, m.getName());
                        Likes = myData.userLike(name);
                        ((MyRecyclerViewAdapter)mRecyclerViewAdapter).allData = Likes;
                        ((MyRecyclerViewAdapter)mRecyclerViewAdapter).data.remove(position);
                        notifyItemRemoved(position);
                        ((MainActivity)getActivity()).heroListFragment.mRecyclerViewAdapter.notifyDataSetChanged();
                        Snackbar.make(mRecyclerView, "Removed from favorites.", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myData.likeHero(name, m.getName());
                                    Likes = myData.userLike(name);
                                    ((MyRecyclerViewAdapter)mRecyclerViewAdapter).allData = Likes;
                                    ((MyRecyclerViewAdapter)mRecyclerViewAdapter).data.add(position, unliked);
                                    notifyItemInserted(position);
                                    ((MainActivity)getActivity()).heroListFragment.mRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            }).show();
                    }
                });
            }
        };
        ((MyRecyclerViewAdapter) mRecyclerViewAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("click", ((MyRecyclerViewAdapter)mRecyclerViewAdapter).data.get(position));
                bundle.putString("name", name);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, 1);
            }

            @Override
            public void onLongClick(final int position) { }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                // If the 2 items are not the same type, no dragging
                if (viewHolder.getItemViewType() != viewHolder1.getItemViewType()) {
                    return false;
                }
                ((MyRecyclerViewAdapter) mRecyclerViewAdapter).onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                ((MyRecyclerViewAdapter) mRecyclerViewAdapter).onItemDelete(viewHolder.getAdapterPosition());
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("username", getActivity().MODE_PRIVATE);
        name = sharedPreferences.getString("username", "");
        if (!TextUtils.isEmpty(name)) {
            refresh(name);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    public void refresh(String name) {
        this.name = name;
        Likes = myData.userLike(name);
        if (Likes == null) {
            Likes = new LinkedList<>();
        }
        ((MyRecyclerViewAdapter)mRecyclerViewAdapter).allData = Likes;
        ((MyRecyclerViewAdapter)mRecyclerViewAdapter).name = name;
        ((MyRecyclerViewAdapter)mRecyclerViewAdapter).onDataSetChanged();
    }
}
