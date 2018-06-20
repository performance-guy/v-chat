/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.activities.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.activities.popups.WallpaperSelector;
import com.tcv.vassistchat.animations.AnimationsUtil;
import com.tcv.vassistchat.helpers.AppHelper;

/**
 * Created by Salman Saleem on 8/17/16.
 *
 *
 *
 */

public class ChatsSettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.chats_settings);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.app_bar, root, false);
        View view = LayoutInflater.from(this).inflate(R.layout.shadow_view, root, false);
        root.addView(toolbar, 0);
        root.addView(view, 1);
        root.setBackgroundColor(AppHelper.getColor(this, R.color.colorWhite));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        initializer();
        toolbar.setTitle(R.string.chats);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initializer() {
        Preference preference1 = findPreference(getString(R.string.key_wallpaper_message));
        preference1.setOnPreferenceClickListener(preference -> {
            AppHelper.LaunchActivity(this, WallpaperSelector.class);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            AnimationsUtil.setSlideOutAnimation(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationsUtil.setSlideOutAnimation(this);
    }
}
