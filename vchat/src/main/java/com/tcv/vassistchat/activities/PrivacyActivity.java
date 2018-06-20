/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.animations.AnimationsUtil;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.presenters.users.ContactsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salman Saleem on 3/13/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class PrivacyActivity extends AppCompatActivity {

    @BindView(R.id.privacy_termes)
    TextView privacyTermes;

    @BindView(R.id.app_bar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_help);
        ButterKnife.bind(this);
        setTypeFaces();
        setupToolbar();
        ContactsPresenter contactsPresenter = new ContactsPresenter(this);
        contactsPresenter.onCreate();
    }


    /**
     * method to setup the toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_privacy);
    }

    private void setTypeFaces() {

        if (AppConstants.ENABLE_FONTS_TYPES)
            privacyTermes.setTypeface(AppHelper.setTypeFace(this, "Futura"));
    }

    public void showPrivcay(String privacy) {
        //String htmlText = "<html><body style=\"text-align:left\"> %s </body></Html>";
        privacyTermes.setText(privacy);
        // privacyTermes.setText(Html.fromHtml(String.format(htmlText, privacy)));
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
