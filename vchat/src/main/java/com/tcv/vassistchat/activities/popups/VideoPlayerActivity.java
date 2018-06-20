/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:32 AM
 *
 */

package com.tcv.vassistchat.activities.popups;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcv.vassistchat.R;
import com.tcv.vassistchat.animations.AnimationsUtil;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.Files.FilesManager;
import com.tcv.vassistchat.helpers.UtilsTime;
import com.tcv.vassistchat.ui.CustomTextureView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Salman Saleem on 6/6/17.
 *
 *
 * @Author : https://twitter.com/Ben__Cherif
 * @Skype :
 */

public class VideoPlayerActivity extends Activity {


    @BindView(R.id.video_layout)
    FrameLayout videoLayout;

    @BindView(R.id.feed_thumbnail)
    ImageView feedThumbnail;

    @BindView(R.id.feed_video)
    CustomTextureView feedVideo;

    @BindView(R.id.video_time)
    TextView videoTime;

    @BindView(R.id.video_total_time)
    TextView videoTimeTotal;

    @BindView(R.id.video_loading)
    TextView videoLoading;

    @BindView(R.id.feed_video_play_btn)
    AppCompatImageButton feedVideoPlayBtn;

    @BindView(R.id.img_vol)
    AppCompatImageView img_vol;

    private boolean isMuted;
    private Handler durationHandler = new Handler();


    private File videoUrl = null;
    //String videoThumbUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppHelper.isAndroid5()) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setStatusBarColor(AppHelper.getColor(this, R.color.colorPrimaryDark));
        }

        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);


        if (getIntent().hasExtra("Identifier")) {
            boolean isSent = getIntent().getExtras().getBoolean("isSent");
            if (isSent) {
                videoUrl = FilesManager.getFileVideoSent(this, getIntent().getExtras().getString("Identifier"));
            } else {
                videoUrl = FilesManager.getFileVideo(this, getIntent().getExtras().getString("Identifier"));
            }
            Bitmap thumbnailBitmap = ThumbnailUtils.createVideoThumbnail(videoUrl.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
            setFeedThumbnail(thumbnailBitmap);
        }
        feedVideoPlayBtn.setOnClickListener(view -> playVideoUrl(videoUrl));
        feedVideo.setOnClickListener(v -> {
            if (isMuted) {
                feedVideo.unMuteVideo();
                img_vol.setImageResource(R.drawable.ic_volume_up_white_24dp);
            } else {
                feedVideo.muteVideo();
                img_vol.setImageResource(R.drawable.ic_volume_off_white_24dp);
            }
            isMuted = !isMuted;
        });

        feedVideoPlayBtn.post(() -> playVideoUrl(videoUrl));

    }


    void setFeedThumbnail(Bitmap feed_file) {
        feedThumbnail.setImageBitmap(feed_file);
    }

    void playVideoUrl(File videoUrl) {
        if (feedVideo.isPlaying()) {
            stopVideo();
        }
        playVideo(videoUrl);
    }


    void stopVideo() {
        if (feedVideo.getmMediaPlayer() == null) return;
        feedVideo.setVisibility(View.GONE);
        AppHelper.LogCat("stopVideo");
        feedVideoPlayBtn.setVisibility(View.VISIBLE);
        feedVideoPlayBtn.setEnabled(true);
        feedThumbnail.setVisibility(View.VISIBLE);
        videoTimeTotal.setVisibility(View.GONE);
        videoTime.setVisibility(View.GONE);
        feedVideo.stopVideo();
    }

    void playVideo(File videoUrl) {

        feedVideo.setSource(FilesManager.getFile(videoUrl));
        feedVideo.setLooping(true);
        feedVideo.setVisibility(View.VISIBLE);
        feedVideoPlayBtn.setEnabled(false);
        feedVideo.initVideo();
        feedVideo.setOnInfoListener(() -> {
            feedVideoPlayBtn.setVisibility(View.GONE);
            videoLoading.setVisibility(View.GONE);
            feedThumbnail.setVisibility(View.GONE);
            feedVideo.setVisibility(View.VISIBLE);
        });
        feedVideo.setOnPreparedListener(() -> {
            videoTime.setVisibility(View.VISIBLE);
            videoTimeTotal.setVisibility(View.VISIBLE);
            feedVideo.startVideo();
            durationHandler.postDelayed(mUpdateTimeTask, 100);
            videoTimeTotal.setText(UtilsTime.getFileTime(feedVideo.getDuration()));
            feedVideoPlayBtn.setEnabled(true);

        });
        feedVideo.setOnErrorListener(() -> {
            feedVideo.setVisibility(View.GONE);
            feedVideoPlayBtn.setEnabled(true);
            feedVideoPlayBtn.setVisibility(View.VISIBLE);
            feedThumbnail.setVisibility(View.VISIBLE);
            videoTime.setVisibility(View.GONE);
            videoTimeTotal.setVisibility(View.GONE);
            feedVideo.stopVideo();

        });
        feedVideo.setOnCompletionListener(() -> {
            feedVideoPlayBtn.setEnabled(true);
            feedVideoPlayBtn.setVisibility(View.VISIBLE);
            feedThumbnail.setVisibility(View.VISIBLE);
            feedVideo.setVisibility(View.GONE);
            videoTimeTotal.setVisibility(View.GONE);
            videoTime.setVisibility(View.GONE);
            feedVideo.stopVideo();
        });
    }


    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                if (feedVideo.isPlaying()) {
                    long currentDuration = feedVideo.getCurrentPosition();
                    videoTime.setText(UtilsTime.getFileTime(currentDuration));
                    durationHandler.postDelayed(this, 100);
                }
            } catch (Exception e) {
                AppHelper.LogCat("Exception mUpdateTimeTask " + e.getMessage());
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationsUtil.setSlideOutAnimation(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.shareBtn)
    void ShareContent() {
        AppHelper.shareIntent(videoUrl, this, null, AppConstants.SENT_VIDEOS);
        finish();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.backBtn)
    void oBack() {
        finish();
        AnimationsUtil.setSlideOutAnimation(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            AnimationsUtil.setSlideOutAnimation(this);
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}