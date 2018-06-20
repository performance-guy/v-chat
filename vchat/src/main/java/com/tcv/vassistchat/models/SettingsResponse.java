/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.models;

/**
 * Created by Salman Saleem on 03/05/2016.
 *
 */
public class SettingsResponse {
    private boolean adsVideoStatus;
    private boolean adsBannerStatus;
    private boolean adsInterstitialStatus;
    private String unitBannerID;
    private String unitVideoID;
    private String unitInterstitialID;
    private String appID;
    private int appVersion;

    public boolean isAdsVideoStatus() {
        return adsVideoStatus;
    }

    public void setAdsVideoStatus(boolean adsVideoStatus) {
        this.adsVideoStatus = adsVideoStatus;
    }

    public boolean isAdsBannerStatus() {
        return adsBannerStatus;
    }

    public void setAdsBannerStatus(boolean adsBannerStatus) {
        this.adsBannerStatus = adsBannerStatus;
    }

    public boolean isAdsInterstitialStatus() {
        return adsInterstitialStatus;
    }

    public void setAdsInterstitialStatus(boolean adsInterstitialStatus) {
        this.adsInterstitialStatus = adsInterstitialStatus;
    }

    public String getUnitBannerID() {
        return unitBannerID;
    }

    public void setUnitBannerID(String unitBannerID) {
        this.unitBannerID = unitBannerID;
    }

    public String getUnitVideoID() {
        return unitVideoID;
    }

    public void setUnitVideoID(String unitVideoID) {
        this.unitVideoID = unitVideoID;
    }

    public String getUnitInterstitialID() {
        return unitInterstitialID;
    }

    public void setUnitInterstitialID(String unitInterstitialID) {
        this.unitInterstitialID = unitInterstitialID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }
}
