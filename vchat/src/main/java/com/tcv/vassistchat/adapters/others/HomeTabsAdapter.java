/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.adapters.others;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tcv.vassistchat.fragments.home.CallsFragment;
import com.tcv.vassistchat.fragments.home.ContactsFragment;
import com.tcv.vassistchat.fragments.home.ConversationsFragment;

/**
 * Created by Salman Saleem on 27/02/2016.
 *
 */
public class HomeTabsAdapter extends FragmentStatePagerAdapter {


    public HomeTabsAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ConversationsFragment();
            case 1:
                return new CallsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}