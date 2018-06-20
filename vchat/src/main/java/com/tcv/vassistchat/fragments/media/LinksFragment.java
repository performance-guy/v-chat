/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.fragments.media;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.adapters.recyclerView.media.LinksAdapter;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.models.messages.MessagesModel;
import com.tcv.vassistchat.presenters.users.ProfilePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salman Saleem on 1/25/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class LinksFragment extends Fragment {

    private View mView;
    private LinksAdapter linksAdapter;


    @BindView(R.id.linksList)
    RecyclerView linksList;


    private ProfilePresenter mProfilePresenter;

    public static LinksFragment newInstance(String tag) {
        LinksFragment linksFragment = new LinksFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        linksFragment.setArguments(args);
        return linksFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_links, container, false);
        ButterKnife.bind(this, mView);
        initializerView();

        mProfilePresenter = new ProfilePresenter(this);
        mProfilePresenter.onCreate();
        return mView;
    }


    public void initializerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linksList.setLayoutManager(linearLayoutManager);
        linksAdapter = new LinksAdapter(getActivity());
        linksList.setAdapter(linksAdapter);
    }

    public void ShowMedia(List<MessagesModel> messagesModel) {
        AppHelper.LogCat("messagesModel " + messagesModel.size());
        if (messagesModel.size() != 0) {
            linksAdapter.setMessages(messagesModel);
        }

    }

    public void onErrorLoading(Throwable throwable) {
        AppHelper.LogCat("MediaFragment throwable " + throwable.getMessage());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mProfilePresenter.onDestroy();
    }

}
