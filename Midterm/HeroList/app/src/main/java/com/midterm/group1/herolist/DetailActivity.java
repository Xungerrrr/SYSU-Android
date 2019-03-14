package com.midterm.group1.herolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private MyData myData;
    private Boolean tags = true;
    private int suri_modify = 0;
    private int atta_modify = 0;
    private int skil_modify = 0;
    private int diff_modify = 0;
    private boolean isLikedChanged = false;
    private int position;
    private boolean isManager = false;
    private boolean isModified = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        myData = new MyData(getApplicationContext());

        TextView type = (TextView) findViewById(R.id.hero_type);
        TextView location = (TextView) findViewById(R.id.hero_location);
        ImageView image = (ImageView) findViewById(R.id.hero_img);
        final FloatingActionButton favorite = findViewById(R.id.favorite);
        final ProgressBar survivability = findViewById(R.id.survivability_progress);
        final ProgressBar attackDamage = findViewById(R.id.attack_damage_progress);
        final ProgressBar skillEffect = findViewById(R.id.skill_effect_progress);
        final ProgressBar difficulty = findViewById(R.id.difficulty_progress);

        final SeekBar survivability_modify = findViewById(R.id.survivability_progress_modify);
        final SeekBar attackDamage_modify = findViewById(R.id.attack_damage_progress_modify);
        final SeekBar skillEffect_modify = findViewById(R.id.skill_effect_progress_modify);
        final SeekBar difficulty_modify = findViewById(R.id.difficulty_progress_modify);

        TextView story = findViewById(R.id.story_content);
        final ImageView modify = findViewById(R.id.modify_btn);
        final Hero hero = (Hero) getIntent().getExtras().get("click");
        final String name = (String) getIntent().getExtras().get("name");
        if(TextUtils.equals(name, "manager")){
            isManager = true;
        }
        position = (int) getIntent().getExtras().get("position");

        type.setText(hero.getType());
        location.setText(hero.getLocation());
        ApplicationInfo appInfo = getApplicationInfo();
        int resID = getResources().getIdentifier(hero.getImage(), "drawable", appInfo.packageName);
        image.setImageResource(resID);

        suri_modify = hero.getViability();
        atta_modify = hero.getAttack();
        skil_modify = hero.getSkill();
        diff_modify = hero.getDifficulty();

        survivability.setProgress(hero.getViability());
        attackDamage.setProgress(hero.getAttack());
        skillEffect.setProgress(hero.getSkill());
        difficulty.setProgress(hero.getDifficulty());
        story.setText(hero.getStory());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(hero.getName());
        }
        if (TextUtils.isEmpty(name)) {
            favorite.hide();
        }
        else {
            favorite.show();
            if (myData.isLike(name, hero.getName())) {
                favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
            }
            else {
                favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myData.isLike(name, hero.getName())) {
                        myData.unlikeHero(name, hero.getName());
                        favorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    }
                    else {
                        myData.likeHero(name, hero.getName());
                        favorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                    }
                    isLikedChanged = !isLikedChanged;
                }
            });
        }

        //修改属性
        survivability_modify.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser == true){
                    suri_modify = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        attackDamage_modify.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser == true){
                    atta_modify = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        skillEffect_modify.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser == true){
                    skil_modify = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        difficulty_modify.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser == true){
                    diff_modify = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        if (!isManager) {
            modify.setVisibility(View.GONE);
        }
        else {
            modify.setVisibility(View.VISIBLE);
            //修改按钮
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tags == true){
                        modify.setImageResource(R.drawable.ic_check_black_24dp);
                        survivability.setVisibility(View.GONE);
                        attackDamage.setVisibility(View.GONE);
                        skillEffect.setVisibility(View.GONE);
                        difficulty.setVisibility(View.GONE);

                        survivability_modify.setVisibility(View.VISIBLE);
                        attackDamage_modify.setVisibility(View.VISIBLE);
                        skillEffect_modify.setVisibility(View.VISIBLE);
                        difficulty_modify.setVisibility(View.VISIBLE);

                        survivability_modify.setProgress(suri_modify);
                        attackDamage_modify.setProgress(atta_modify);
                        skillEffect_modify.setProgress(skil_modify);
                        difficulty_modify.setProgress(diff_modify);
                        tags = false;
                    } else {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailActivity.this);
                        alertDialog.setMessage("您确认修改吗？")
                                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        modify.setImageResource(R.drawable.ic_modify_black_24dp);
                                        survivability.setVisibility(View.VISIBLE);
                                        attackDamage.setVisibility(View.VISIBLE);
                                        skillEffect.setVisibility(View.VISIBLE);
                                        difficulty.setVisibility(View.VISIBLE);

                                        survivability.setProgress(suri_modify);
                                        attackDamage.setProgress(atta_modify);
                                        skillEffect.setProgress(skil_modify);
                                        difficulty.setProgress(diff_modify);

                                        survivability_modify.setVisibility(View.GONE);
                                        attackDamage_modify.setVisibility(View.GONE);
                                        skillEffect_modify.setVisibility(View.GONE);
                                        difficulty_modify.setVisibility(View.GONE);

                                        //在数据库中修改英雄
                                        hero.setViability(suri_modify);
                                        hero.setAttack(atta_modify);
                                        hero.setSkill(skil_modify);
                                        hero.setDifficulty(diff_modify);
                                        myData.updateHero(hero);
                                        isModified = true;
                                        tags = true;
                                    }
                                })
                                .setNegativeButton("我再想想", null)
                                .create()
                                .show();
                    }
                }
            });
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        if (isModified) {
            setResult(3);
        }
        else if (isLikedChanged && position == -1)
            setResult(1);
        else if (isLikedChanged)
            setResult(2, intent);
        else
            setResult(0);
        finish();
    }
}
