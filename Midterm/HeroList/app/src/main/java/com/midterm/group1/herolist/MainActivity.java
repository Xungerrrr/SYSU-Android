package com.midterm.group1.herolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private MyData myData = new MyData(this);
    private boolean login = false;
    private String name = "";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    public HeroListFragment heroListFragment;
    public FavoritesFragment favoritesFragment;
    private SearchView searchView;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All Heroes");
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login) {
                    Intent intent = new Intent(MainActivity.this, AddHerosActivity.class);
                    intent.putExtra("add", name);
                    startActivityForResult(intent, 2);
                }
                else {
                    Snackbar.make(view, "Please log in first.", Snackbar.LENGTH_LONG)
                            .setAction("LOG IN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this, LogActivity.class);
                                    startActivityForResult(intent, 0);
                                }
                            }).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View navigationViewHeader = navigationView.getHeaderView(0);
        navigationViewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login) {
                    alertDialog.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        login = false;
                        sharedPreferences.edit().putString("username", "").commit();
                        de.hdodenhof.circleimageview.CircleImageView avatar = navigationViewHeader.findViewById(R.id.imageView);
                        TextView username = navigationViewHeader.findViewById(R.id.name);
                        TextView textView = navigationViewHeader.findViewById(R.id.textView);
                        avatar.setImageResource(R.drawable.me);
                        username.setText(R.string.nav_header_title);
                        textView.setText(R.string.nav_header_subtitle);
                        name = "";
                        heroListFragment.onLogOut();
                        favoritesFragment.refresh("");
                        favoritesFragment.mRecyclerViewAdapter.notifyDataSetChanged();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        mTabLayout.getTabAt(0).select();
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle("All Heroes");
                        }
                        }
                    }).setNegativeButton("Cancel", null).setTitle(
                            "Log out or not?").setMessage("").create().show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, LogActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        List<String> titles = new ArrayList<>();
        titles.add("Hero List");
        titles.add("Favorites");

        List<Fragment> fragments = new ArrayList<>();
        heroListFragment = new HeroListFragment();
        favoritesFragment = new FavoritesFragment();
        fragments.add(heroListFragment);
        fragments.add(favoritesFragment);
        mViewPager.setOffscreenPageLimit(2);

        MyFragmentAdapter mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_format_list_bulleted_white_24dp);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_favorite_white_24dp);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    fab.show();
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                        ((MyRecyclerViewAdapter)favoritesFragment.mRecyclerViewAdapter).setFilterText("");
                    }
                    if(login) {
                        getSupportActionBar().setTitle("My Heroes");
                        heroListFragment.mItemTouchHelper.attachToRecyclerView(heroListFragment.mRecyclerView);
                    }
                    else {
                        getSupportActionBar().setTitle("All Heroes");
                    }
                    MenuItem item = navigationView.getMenu().findItem(R.id.nav_hero);
                    item.setChecked(true);
                }
                else {
                    getSupportActionBar().setTitle("Favorites");
                    fab.hide();
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                        ((MyRecyclerViewAdapter)heroListFragment.mRecyclerViewAdapter).setFilterText("");
                    }
                    navigationView.setCheckedItem(R.id.favorite);
                    MenuItem item = navigationView.getMenu().findItem(R.id.nav_favorites);
                    item.setChecked(true);
                }
                searchView.setIconified(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
        sharedPreferences = getSharedPreferences("username", MODE_PRIVATE);
        name = sharedPreferences.getString("username", "");
        if (TextUtils.isEmpty(name)) {
            login = false;
        }
        else {
            User user = myData.getByUsername(name);
            de.hdodenhof.circleimageview.CircleImageView avatar = navigationViewHeader.findViewById(R.id.imageView);
            TextView username = navigationViewHeader.findViewById(R.id.name);
            Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
            avatar.setImageDrawable(bitmapDrawable);
            username.setText(name);
            login = true;
            mTabLayout.getTabAt(0).select();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Heroes");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            if (resultCode == 1) {
                Bundle bundle = data.getExtras();
                name = bundle.getString("name");
                sharedPreferences.edit().putString("username", name).commit();
                User user = myData.getByUsername(name);
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View navigationViewHeader = navigationView.getHeaderView(0);
                de.hdodenhof.circleimageview.CircleImageView avatar = navigationViewHeader.findViewById(R.id.imageView);
                TextView username = navigationViewHeader.findViewById(R.id.name);
                Bitmap bmp = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
                avatar.setImageDrawable(bitmapDrawable);
                username.setText(name);
                heroListFragment.onLogIn(name);
                favoritesFragment.refresh(name);
                favoritesFragment.mRecyclerViewAdapter.notifyDataSetChanged();
                login = true;
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                mTabLayout.getTabAt(0).select();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("My Heroes");
                }
            }
        }
        if (requestCode == 1) {
            if (resultCode == 1) {
                favoritesFragment.refresh(name);
                favoritesFragment.mRecyclerViewAdapter.notifyDataSetChanged();
                heroListFragment.mRecyclerViewAdapter.notifyDataSetChanged();
            }
            else if (resultCode == 2) {
                Bundle bundle = data.getExtras();
                int position = bundle.getInt("position");
                ((MyRecyclerViewAdapter)favoritesFragment.mRecyclerViewAdapter).data.remove(position);
                favoritesFragment.mRecyclerViewAdapter.notifyItemRemoved(position);
                heroListFragment.mRecyclerViewAdapter.notifyDataSetChanged();
            }
            else if (resultCode == 3) {
                favoritesFragment.refresh(name);
                heroListFragment.onLogIn(name);
                favoritesFragment.mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2){
            if(resultCode == 2){
                Bundle bundle = data.getExtras();
                String heroname = bundle.getString("heroname");
                heroListFragment.addHero(heroname);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_hero) {
            mTabLayout.getTabAt(0).select();
        } else if (id == R.id.nav_favorites) {
            mTabLayout.getTabAt(1).select();
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SearchBar.class);
            if(login) intent.putExtra("username", name);
            startActivity(intent);
        } else if (id == R.id.nav_add) {
            if (login) {
                Intent intent = new Intent(MainActivity.this, AddHerosActivity.class);
                intent.putExtra("add", name);
                startActivityForResult(intent, 2);
            }
            else {
                Snackbar.make(getCurrentFocus(), "Please log in first.", Snackbar.LENGTH_LONG)
                        .setAction("LOG IN", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                                startActivityForResult(intent, 0);
                            }
                        }).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        int position = mTabLayout.getSelectedTabPosition();
        if (position == 0) {
            // 搜索英雄列表
            if(TextUtils.isEmpty(newText)) {
                ((MyRecyclerViewAdapter)heroListFragment.mRecyclerViewAdapter).clearTextFilter();
                if (login)
                    heroListFragment.mItemTouchHelper.attachToRecyclerView(heroListFragment.mRecyclerView);
            }
            else {
                ((MyRecyclerViewAdapter)heroListFragment.mRecyclerViewAdapter).setFilterText(newText);
                heroListFragment.mItemTouchHelper.attachToRecyclerView(null);
            }
        }
        else if (position == 1) {
            // 搜索收藏列表
            if(TextUtils.isEmpty(newText)) {
                ((MyRecyclerViewAdapter)favoritesFragment.mRecyclerViewAdapter).clearTextFilter();
                if (login)
                    favoritesFragment.mItemTouchHelper.attachToRecyclerView(favoritesFragment.mRecyclerView);
            }
            else {
                ((MyRecyclerViewAdapter)favoritesFragment.mRecyclerViewAdapter).setFilterText(newText);
                favoritesFragment.mItemTouchHelper.attachToRecyclerView(null);
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
