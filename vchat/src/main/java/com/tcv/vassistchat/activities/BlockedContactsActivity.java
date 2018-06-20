/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:38 AM
 *
 */

package com.tcv.vassistchat.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;


import com.tcv.vassistchat.adapters.recyclerView.contacts.BlockedContactsAdapter;
import com.tcv.vassistchat.animations.AnimationsUtil;
import com.tcv.vassistchat.helpers.PreferenceManager;
import com.tcv.vassistchat.models.users.contacts.UsersBlockModel;
import com.tcv.vassistchat.presenters.users.SelectContactsPresenter;
import com.tcv.vassistchat.ui.RecyclerViewFastScroller;
import com.tcv.vassistchat.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 *
 */

public class BlockedContactsActivity extends AppCompatActivity {
    @BindView(R.id.ContactsList)
    RecyclerView ContactsList;
    @BindView(R.id.fastscroller)
    RecyclerViewFastScroller fastScroller;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.empty)
    LinearLayout emptyContacts;
    private List<UsersBlockModel> usersBlockModels;
    private BlockedContactsAdapter mSelectContactsAdapter;
    private SelectContactsPresenter mContactsPresenter;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        initializerView();
        mContactsPresenter = new SelectContactsPresenter(this);
        mContactsPresenter.onCreate();
    }

    /**
     * method to initialize the view
     */
    private void initializerView() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_blocked_contacts));

        }
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSelectContactsAdapter = new BlockedContactsAdapter(this, usersBlockModels);
        ContactsList.setLayoutManager(mLinearLayoutManager);
        ContactsList.setAdapter(mSelectContactsAdapter);

        // set recyclerView to fastScroller
        fastScroller.setRecyclerView(ContactsList);
        fastScroller.setViewsToUse(R.layout.contacts_fragment_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        AnimationsUtil.setSlideOutAnimation(this);
        super.onOptionsItemSelected(item);
        return true;
    }

    /**
     * method to show blocked contacts list
     *
     * @param usersBlockModels this is parameter for ShowContacts  method
     */
    public void ShowContacts(List<UsersBlockModel> usersBlockModels) {
        this.usersBlockModels = usersBlockModels;
        if (getSupportActionBar() != null)
            getSupportActionBar().setSubtitle("" + usersBlockModels.size() + getResources().getString(R.string.of) + PreferenceManager.getContactSize(this));


        if (usersBlockModels.size() != 0) {
            fastScroller.setVisibility(View.VISIBLE);
            ContactsList.setVisibility(View.VISIBLE);
            emptyContacts.setVisibility(View.GONE);
            mSelectContactsAdapter.setContacts(usersBlockModels);
        } else {
            fastScroller.setVisibility(View.GONE);
            ContactsList.setVisibility(View.GONE);
            emptyContacts.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mContactsPresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationsUtil.setSlideOutAnimation(this);
    }
}
