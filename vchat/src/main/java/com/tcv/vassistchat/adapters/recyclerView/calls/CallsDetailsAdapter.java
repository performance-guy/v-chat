/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:33 AM
 *
 */

package com.tcv.vassistchat.adapters.recyclerView.calls;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.UtilsTime;
import com.tcv.vassistchat.models.calls.CallsInfoModel;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Salman Saleem on 12/3/16.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class CallsDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity mActivity;
    private List<CallsInfoModel> callsModelList;

    public CallsDetailsAdapter(@NonNull Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setCalls(List<CallsInfoModel> callsModelList) {
        this.callsModelList = callsModelList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.row_call_details, parent, false);
        return new CallsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final CallsViewHolder callsViewHolder = (CallsViewHolder) holder;
        final CallsInfoModel callsInfoModel = callsModelList.get(position);

        if (callsInfoModel.isReceived()) {
            callsViewHolder.showIcon();
            if (callsInfoModel.getType().equals(AppConstants.VIDEO_CALL))
                callsViewHolder.setCallType(mActivity.getString(R.string.missed_video_call));
            else
                callsViewHolder.setCallType(mActivity.getString(R.string.missed_voice_call));
        } else {
            callsViewHolder.hideIcon();
            if (callsInfoModel.getType().equals(AppConstants.VIDEO_CALL))
                callsViewHolder.setCallType(mActivity.getString(R.string.outgoing_video_call));
            else
                callsViewHolder.setCallType(mActivity.getString(R.string.outgoing_voice_call));
        }


        if (callsInfoModel.getDate() != null) {
            callsViewHolder.setCallDate(callsInfoModel.getDate());
        }

        if (callsInfoModel.getDuration() != null) {
            callsViewHolder.setDurationCall(callsInfoModel.getDuration());
        }
    }


    @Override
    public int getItemCount() {
        if (callsModelList != null) return callsModelList.size();
        return 0;
    }


    public class CallsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.call_type)
        TextView callType;

        @BindView(R.id.icon_made)
        AppCompatImageView IconMade;

        @BindView(R.id.icon_received)
        AppCompatImageView IconReceived;

        @BindView(R.id.date_call)
        TextView CallDate;

        @BindView(R.id.duration_call)
        TextView durationCall;

        public CallsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setTypeFaces();
        }

        private void setTypeFaces() {
            if (AppConstants.ENABLE_FONTS_TYPES) {
                durationCall.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
                CallDate.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
                callType.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
            }
        }

        void hideIcon() {
            IconMade.setVisibility(View.VISIBLE);
            IconReceived.setVisibility(View.GONE);
        }

        void showIcon() {
            IconMade.setVisibility(View.GONE);
            IconReceived.setVisibility(View.VISIBLE);
        }


        @SuppressLint("StaticFieldLeak")
        void setCallDate(String date) {
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    DateTime messageDate = UtilsTime.getCorrectDate(params[0]);
                    return UtilsTime.convertDateToString(mActivity, messageDate);
                }

                @Override
                protected void onPostExecute(String date) {
                    super.onPostExecute(date);
                    CallDate.setText(date);
                }
            }.execute(date);

        }

        void setDurationCall(String duration) {
            durationCall.setText(duration);
        }

        void setCallType(String type) {
            callType.setText(type);
        }


    }
}
