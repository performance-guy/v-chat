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
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tcv.vassistchat.R;
import com.tcv.vassistchat.activities.call.CallDetailsActivity;
import com.tcv.vassistchat.activities.profile.ProfilePreviewActivity;
import com.tcv.vassistchat.activities.settings.PreferenceSettingsManager;
import com.tcv.vassistchat.animations.AnimationsUtil;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.app.EndPoints;
import com.tcv.vassistchat.app.WhatsCloneApplication;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.Files.cache.ImageLoader;
import com.tcv.vassistchat.helpers.Files.cache.MemoryCache;
import com.tcv.vassistchat.helpers.UtilsPhone;
import com.tcv.vassistchat.helpers.UtilsTime;
import com.tcv.vassistchat.helpers.call.CallManager;
import com.tcv.vassistchat.models.calls.CallsModel;
import com.tcv.vassistchat.ui.ColorGenerator;
import com.tcv.vassistchat.ui.TextDrawable;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Salman Saleem on 12/3/16.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class CallsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private RealmList<CallsModel> callsModelList;
    private MemoryCache memoryCache;
    private RecyclerView callList;
    private String SearchQuery;


    public CallsAdapter(RecyclerView callList) {
        this.callList = callList;
        this.memoryCache = new MemoryCache();
        this.callsModelList = new RealmList<>();
    }

    public CallsAdapter() {
        this.memoryCache = new MemoryCache();
        this.callsModelList = new RealmList<>();
    }

    public void setCalls(RealmList<CallsModel> callsModelList) {
        this.callsModelList = callsModelList;
        notifyDataSetChanged();
    }


    //Methods for search start
    public void setString(String SearchQuery) {
        this.SearchQuery = SearchQuery;
        notifyDataSetChanged();
    }

    public void animateTo(List<CallsModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<CallsModel> newModels) {
        int arraySize = callsModelList.size();
        for (int i = arraySize - 1; i >= 0; i--) {
            final CallsModel model = callsModelList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CallsModel> newModels) {
        int arraySize = newModels.size();
        for (int i = 0; i < arraySize; i++) {
            final CallsModel model = newModels.get(i);
            if (!callsModelList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CallsModel> newModels) {
        int arraySize = newModels.size();
        for (int toPosition = arraySize - 1; toPosition >= 0; toPosition--) {
            final CallsModel model = newModels.get(toPosition);
            final int fromPosition = callsModelList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private CallsModel removeItem(int position) {
        final CallsModel model = callsModelList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, CallsModel model) {
        callsModelList.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final CallsModel model = callsModelList.remove(fromPosition);
        callsModelList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    //Methods for search end


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_calls, parent, false);
        return new CallsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final CallsViewHolder callsViewHolder = (CallsViewHolder) holder;
        final CallsModel callsModel = callsModelList.get(position);
        //  Context mActivity = callsViewHolder.itemView.getContext();
        Activity mActivity = (Activity) callsViewHolder.itemView.getContext();/*
        AppHelper.LogCat("getFrom " + callsModel.getFrom());
        AppHelper.LogCat("getTo " + callsModel.getTo());*/
        try {
            String Username;
            String name = UtilsPhone.getContactName(callsModel.getPhone());
            if (name != null) {
                Username = name;
            } else {
                Username = callsModel.getPhone();
            }

            SpannableString Message = SpannableString.valueOf(Username);
            if (SearchQuery != null) {
                int index = TextUtils.indexOf(Username.toLowerCase(), SearchQuery.toLowerCase());
                if (index >= 0) {
                    Message.setSpan(new ForegroundColorSpan(AppHelper.getColor(mActivity, R.color.colorSpanSearch)), index, index + SearchQuery.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    Message.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), index, index + SearchQuery.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                callsViewHolder.username.setText(Message, TextView.BufferType.SPANNABLE);
                callsViewHolder.username.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
            } else {
                callsViewHolder.username.setText(Username, TextView.BufferType.NORMAL);
                callsViewHolder.username.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
            }


            if (callsModel.isReceived()) {
                callsViewHolder.showIcon();
            } else {
                callsViewHolder.hideIcon();
            }
            if (callsModel.getType().equals(AppConstants.VIDEO_CALL)) {
                callsViewHolder.showVideoButton();
            } else if (callsModel.getType().equals(AppConstants.VOICE_CALL)) {
                callsViewHolder.hideVideoButton();

            }
            callsViewHolder.setUserImage(callsModel.getContactsModel().getImage(), callsModel.getContactsModel().getId(), Username);

            if (callsModel.getDate() != null) {
                callsViewHolder.setCallDate(callsModel.getDate());
            }

            if (callsModel.getCounter() != 0 && callsModel.getCounter() > 1)
                callsViewHolder.setCallCounter(callsModel.getCounter());
            else
                callsViewHolder.counterCall.setVisibility(View.GONE);


        callsViewHolder.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.CallVideoBtn:
                    if (callsModel.isReceived())
                        CallManager.callContact(mActivity, false, true, callsModel.getFrom());
                    else
                        CallManager.callContact(mActivity, false, true, callsModel.getTo());
                    break;
                case R.id.CallBtn:
                    if (callsModel.isReceived())
                        CallManager.callContact(mActivity, false, false, callsModel.getFrom());
                    else
                        CallManager.callContact(mActivity, false, false, callsModel.getTo());
                    break;
                case R.id.user_image:
                    if (AppHelper.isAndroid5()) {
                        if (callsModel.getContactsModel().isLinked() && callsModel.getContactsModel().isActivate()) {
                            Intent mIntent = new Intent(mActivity, ProfilePreviewActivity.class);
                            mIntent.putExtra("userID", callsModel.getContactsModel().getId());
                            mIntent.putExtra("isGroup", false);
                            mActivity.startActivity(mIntent);
                        }
                    } else {
                        if (callsModel.getContactsModel().isLinked() && callsModel.getContactsModel().isActivate()) {
                            Intent mIntent = new Intent(mActivity, ProfilePreviewActivity.class);
                            mIntent.putExtra("userID", callsModel.getContactsModel().getId());
                            mActivity.startActivity(mIntent);
                            mActivity.overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                        }
                    }

                    break;
                default:
                    Intent mIntent = new Intent(mActivity, CallDetailsActivity.class);
                    mIntent.putExtra("userID", callsModel.getContactsModel().getId());
                    mIntent.putExtra("callID", callsModel.getId());
                    mActivity.startActivity(mIntent);
                    AnimationsUtil.setSlideInAnimation(mActivity);
                    break;

            }
        });

        } catch (Exception e) {
            AppHelper.LogCat(e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        if (callsModelList != null) return callsModelList.size();
        return 0;
    }


    public CallsModel getItem(int position) {
        return callsModelList.get(position);
    }


    /**
     * method to check if a  call exist
     *
     * @param callId this is the first parameter for  checkIfCallExist method
     * @param realm  this is the second parameter for  checkIfCallExist  method
     * @return return value
     */
    private boolean checkIfCallExist(int callId, Realm realm) {
        RealmQuery<CallsModel> query = realm.where(CallsModel.class).equalTo("id", callId);
        return query.count() != 0;

    }

    public void addCallItem(int callId) {
        try {
            Realm realm = WhatsCloneApplication.getRealmDatabaseInstance();
            CallsModel callsModel = realm.where(CallsModel.class).equalTo("id", callId).findFirst();
            if (!isCallExistInList(callsModel.getId())) {
                addCallItem(0, callsModel);
            } else {
                return;
            }
            realm.close();

        } catch (Exception e) {
            AppHelper.LogCat("addCallItem Exception" + e);
        }
    }

    private void addCallItem(int position, CallsModel callsModel) {
        try {
            this.callsModelList.add(position, callsModel);
            notifyItemInserted(position);
        } catch (Exception e) {
            AppHelper.LogCat(e);
        }
    }

    private boolean isCallExistInList(int callId) {
        int arraySize = callsModelList.size();
        boolean exist = false;
        for (int i = 0; i < arraySize; i++) {
            CallsModel model = callsModelList.get(i);
            if (callId == model.getId()) {
                exist = true;
                break;
            }
        }
        return exist;
    }


    public void updateCallItem(int callId) {
        try {
            Realm realm = WhatsCloneApplication.getRealmDatabaseInstance();
            int arraySize = callsModelList.size();
            for (int i = 0; i < arraySize; i++) {
                CallsModel model = callsModelList.get(i);
                if (callId == model.getId()) {
                    CallsModel callsModel = realm.where(CallsModel.class).equalTo("id", callId).findFirst();
                    changeItemAtPosition(i, callsModel);
                    if (i != 0)
                        MoveItemToPosition(i, 0);
                    break;
                }

            }
            realm.close();
        } catch (Exception e) {
            AppHelper.LogCat(e);
        }
    }

    private void changeItemAtPosition(int position, CallsModel callsModel) {
        callsModelList.set(position, callsModel);
        notifyItemChanged(position);
    }

    private void MoveItemToPosition(int fromPosition, int toPosition) {
        CallsModel model = callsModelList.remove(fromPosition);
        callsModelList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
        callList.scrollToPosition(fromPosition);
    }

    public void removeCallItem(int position) {
        try {
            callsModelList.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e) {
            AppHelper.LogCat(e);
        }
    }

    public void DeleteCallItem(int callID) {
        try {
            int arraySize = callsModelList.size();
            for (int i = 0; i < arraySize; i++) {
                CallsModel model = callsModelList.get(i);
                if (model.isValid()) {
                    if (callID == model.getId()) {
                        removeCallItem(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            AppHelper.LogCat(e);
        }
    }

    public class CallsViewHolder extends RecyclerView.ViewHolder {

        Context mActivity;
        @BindView(R.id.user_image)
        ImageView userImage;
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.CallVideoBtn)
        AppCompatImageView CallVideoBtn;
        @BindView(R.id.CallBtn)
        AppCompatImageView CallBtn;
        @BindView(R.id.icon_made)
        AppCompatImageView IconMade;
        @BindView(R.id.icon_received)
        AppCompatImageView IconReceived;
        @BindView(R.id.date_call)
        TextView CallDate;
        @BindView(R.id.counter_call)
        TextView counterCall;

        public CallsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mActivity = itemView.getContext();
            setTypeFaces();
        }


        private void setTypeFaces() {
            if (AppConstants.ENABLE_FONTS_TYPES) {
                counterCall.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
                CallDate.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
                username.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
            }
        }


        @SuppressLint("StaticFieldLeak")
        void setUserImage(String ImageUrl, int recipientId, String name) {
            TextDrawable drawable = textDrawable(name);
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return ImageLoader.GetCachedBitmapImage(memoryCache, ImageUrl, mActivity, recipientId, AppConstants.USER, AppConstants.ROW_PROFILE);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap != null) {
                        ImageLoader.SetBitmapImage(bitmap, userImage);
                    } else {


                        BitmapImageViewTarget target = new BitmapImageViewTarget(userImage) {
                            @Override
                            public void onResourceReady(final Bitmap bitmap, GlideAnimation anim) {
                                super.onResourceReady(bitmap, anim);
                                userImage.setImageBitmap(bitmap);

                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                userImage.setImageDrawable(errorDrawable);
                            }

                            @Override
                            public void onLoadStarted(Drawable placeHolderDrawable) {
                                super.onLoadStarted(placeHolderDrawable);
                                userImage.setImageDrawable(placeHolderDrawable);
                            }
                        };

                        Glide.with(mActivity.getApplicationContext())
                                .load(EndPoints.ROWS_IMAGE_URL + ImageUrl)
                                .asBitmap()
                                .centerCrop()
                                .transform(new CropCircleTransformation(mActivity.getApplicationContext()))
                                .placeholder(drawable)
                                .error(drawable)
                                .override(AppConstants.ROWS_IMAGE_SIZE, AppConstants.ROWS_IMAGE_SIZE)
                                .into(target);
                    }
                }
            }.execute();
        }

        TextDrawable textDrawable(String name) {
            if (name == null) {
                name = mActivity.getString(R.string.app_name);
            }
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(name);
            String c = String.valueOf(name.toUpperCase().charAt(0));
            return TextDrawable.builder().buildRound(c, color);


        }

        void hideIcon() {
            IconMade.setVisibility(View.VISIBLE);
            IconReceived.setVisibility(View.GONE);
        }

        void showIcon() {
            IconMade.setVisibility(View.GONE);
            IconReceived.setVisibility(View.VISIBLE);
        }

        void showVideoButton() {
            CallVideoBtn.setVisibility(View.VISIBLE);
            CallBtn.setVisibility(View.GONE);
        }

        void hideVideoButton() {
            CallVideoBtn.setVisibility(View.GONE);
            CallBtn.setVisibility(View.VISIBLE);
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

        void setCallCounter(int counter) {
            counterCall.setVisibility(View.VISIBLE);
            counterCall.setText(String.format("(%d)", counter));
        }


        void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
            userImage.setOnClickListener(listener);
            CallVideoBtn.setOnClickListener(listener);
            CallBtn.setOnClickListener(listener);
        }


    }
}
