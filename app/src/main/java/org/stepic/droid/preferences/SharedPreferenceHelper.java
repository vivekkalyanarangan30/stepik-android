package org.stepic.droid.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.stepic.droid.base.MainApplication;
import org.stepic.droid.model.Profile;
import org.stepic.droid.util.RWLocks;
import org.stepic.droid.web.AuthenticationStepicResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferenceHelper {
    private Context mContext;

    @Inject
    public SharedPreferenceHelper() {
        mContext = MainApplication.getAppContext();
    }

    public enum PreferenceType {
        LOGIN("login preference"),
        WIFI("wifi_preference");

        private String description;

        PreferenceType(String description) {
            this.description = description;
        }

        private String getStoreName() {
            return description;
        }
    }

    public void storeProfile(Profile profile) {
        //todo save picture of user profile
        //todo validate profile from the server with cached profile and make restore to cache. make
        //todo query when nav drawer is occurred?
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        put(PreferenceType.LOGIN, PROFILE_JSON, json);
    }

    public Profile getProfile() {
        String json = getString(PreferenceType.LOGIN, PROFILE_JSON);
        if (json == null) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        Profile result = gson.fromJson(json, Profile.class);
        return result;
    }

    public void storeAuthInfo(AuthenticationStepicResponse response) {
        Gson gson = new Gson();
        String json = gson.toJson(response);
        put(PreferenceType.LOGIN, AUTH_RESPONSE_JSON, json);

    }

    public void deleteAuthInfo() {
        RWLocks.AuthLock.writeLock().lock();
        try {
            clear(PreferenceType.LOGIN);
        } finally {
            RWLocks.AuthLock.writeLock().unlock();
        }
    }

    public AuthenticationStepicResponse getAuthResponseFromStore() {
        String json = getString(PreferenceType.LOGIN, AUTH_RESPONSE_JSON);
        if (json == null) {
            return null;
        }

        Gson gson = new GsonBuilder().create();
        AuthenticationStepicResponse result = gson.fromJson(json, AuthenticationStepicResponse.class);
        return result;
    }


    public boolean isMobileInternetAlsoAllowed() {
        return getBoolean(PreferenceType.WIFI, WIFI_KEY);
    }

    public void setMobileInternetAndWifiAllowed(boolean isOnlyWifi) {
        put(PreferenceType.WIFI, WIFI_KEY, isOnlyWifi);
    }

    private void put(PreferenceType type, String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(type.getStoreName(), Context.MODE_PRIVATE).edit();
        editor.putString(key, value).apply();
    }

    private void put(PreferenceType type, String key, Boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(type.getStoreName(), Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value).apply();
    }

    private void clear(PreferenceType type) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(type.getStoreName(), Context.MODE_PRIVATE).edit();
        editor.clear().apply();
    }


    private String getString(PreferenceType preferenceType, String key) {
        return mContext.getSharedPreferences(preferenceType.getStoreName(), Context.MODE_PRIVATE)
                .getString(key, null);
    }

    private boolean getBoolean(PreferenceType preferenceType, String key) {
        return mContext.getSharedPreferences(preferenceType.getStoreName(), Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }


    private final String AUTH_RESPONSE_JSON = "auth_response_json";
    private final String PROFILE_JSON = "profile_json";
    private final String WIFI_KEY = "wifi_key";

}