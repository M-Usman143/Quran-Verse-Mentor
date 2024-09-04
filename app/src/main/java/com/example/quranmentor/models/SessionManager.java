package com.example.quranmentor.models;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String SESSION_PREFS = "SessionPrefs";
    private static final String SESSION_TOKEN_KEY = "SessionToken";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE);
    }

    public void saveSessionToken(String sessionToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_TOKEN_KEY, sessionToken);
        editor.apply();
    }

    public String getSessionToken() {
        return sharedPreferences.getString(SESSION_TOKEN_KEY, null);
    }

    public void setLogin(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SESSION_TOKEN_KEY);
        editor.remove(KEY_IS_LOGGED_IN);
        editor.apply();
    }
}
