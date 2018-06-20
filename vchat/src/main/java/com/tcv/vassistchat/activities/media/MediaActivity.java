/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:33 AM
 *
 */

package com.tcv.vassistchat.activities.media;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.adapters.others.TabsMediaAdapter;
import com.tcv.vassistchat.animations.AnimationsUtil;
import com.tcv.vassistchat.helpers.AppHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


/**
 * Created by Salman Saleem on 1/24/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class MediaActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar_title)
    EmojiconTextView toolbarTitle;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppHelper.isAndroid5()) {
            getWindow().setStatusBarColor(AppHelper.getColor(this, R.color.colorBlack));
        }
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);
        initializerView();

        if (getIntent().hasExtra("Username")) {
            toolbarTitle.setText(getIntent().getStringExtra("Username"));
        }

    }

    private void initializerView() {
        viewPager.setAdapter(new TabsMediaAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).setCustomView(R.layout.custom_tab_media);
        tabLayout.getTabAt(1).setCustomView(R.layout.custom_tab_documents);
       // tabLayout.getTabAt(2).setCustomView(R.layout.custom_tab_links);
        ((TextView) findViewById(R.id.title_tabs_media)).setTextColor(AppHelper.getColor(this, R.color.colorSelectedTab));
        ((TextView) findViewById(R.id.title_tabs_documents)).setTextColor(AppHelper.getColor(this, R.color.colorUnSelectedMedia));
       // ((TextView) findViewById(R.id.title_tabs_links)).setTextColor(AppHelper.getColor(this, R.color.colorUnSelectedTab));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        ((TextView) findViewById(R.id.title_tabs_media)).setTextColor(AppHelper.getColor(MediaActivity.this, R.color.colorSelectedTab));
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        ((TextView) findViewById(R.id.title_tabs_documents)).setTextColor(AppHelper.getColor(MediaActivity.this, R.color.colorSelectedTab));
                        break;
                   /* case 2:
                        viewPager.setCurrentItem(2);
                        ((TextView) findViewById(R.id.title_tabs_links)).setTextColor(AppHelper.getColor(MediaActivity.this, R.color.colorSelectedTab));
                        break;*/
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        ((TextView) findViewById(R.id.title_tabs_media)).setTextColor(AppHelper.getColor(MediaActivity.this, R.color.colorUnSelectedMedia));
                        break;
                    case 1:
                        ((TextView) findViewById(R.id.title_tabs_documents)).setTextColor(AppHelper.getColor(MediaActivity.this, R.color.colorUnSelectedMedia));
                        break;
                    /*case 2:
                        ((TextView) findViewById(R.id.title_tabs_links)).setTextColor(AppHelper.getColor(MediaActivity.this, R.color.colorUnSelectedTab));
                        break;*/
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.arrow_back)
    public void backPressed() {
        finish();
        AnimationsUtil.setSlideOutAnimation(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationsUtil.setSlideOutAnimation(this);
    }
}
