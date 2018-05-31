package com.binaa.binaaadmin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.binaa.binaaadmin.R;
import com.binaa.binaaadmin.models.UserModel;


/**
 * Created by Muhammad on 2/5/2018
 */
public class SettingsManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SettingsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setIsRegistered(Boolean value) {
        setSettings(Settings.IsRegistered, value);
    }

    public void updateUser(UserModel user) {
        setLoggedIn(true);
        setUserId(user.getId());
        setUserToken(user.getToken());
        setFullName(user.getName());
        setEmail(user.getEmail());
    }

    public UserModel getUser() {

        UserModel user = new UserModel();
        user.setId(getUserId());
        user.setToken(getUserToken());
        user.setName(getFullName());
        user.setEmail(getEmail());
        return user;
    }

    public void resetAccount() {
        setUserToken("");
        setEmail("");
        setPassword("");
        setFullName("");
        setIsRegistered(false);
    }

    private void setSettings(Settings setting, Object value) {
        if (value instanceof String) {
            editor.putString(setting.name(), (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(setting.name(), (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(setting.name(), (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(setting.name(), (Boolean) value);
        }

        editor.apply();
    }


    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Settings.IsLoggedIn.name(), false);
    }

    public void setLoggedIn(Boolean value) {
        if (!value) {
            resetAccount();
        }
        setSettings(Settings.IsLoggedIn, value);
    }

    public boolean isRegistered() {
        return sharedPreferences.getBoolean(Settings.IsRegistered.name(), false);
    }

    public String getUserToken() {
        return sharedPreferences.getString(Settings.UserToken.name(), "");
    }

    public void setUserToken(String value) {
        setSettings(Settings.UserToken, value);
    }

    public int getUserId() {
        return sharedPreferences.getInt(Settings.UserId.name(), 0);
    }

    public void setUserId(int value) {
        setSettings(Settings.UserId, value);
    }

    public String getFullName() {
        return sharedPreferences.getString(Settings.FullName.name(), "");
    }

    public void setFullName(String value) {
        setSettings(Settings.FullName, value);
    }

    public String getEmail() {
        return sharedPreferences.getString(Settings.Email.name(), "");
    }

    public void setEmail(String value) {
        setSettings(Settings.Email, value);
    }

    public String getPassword() {
        return sharedPreferences.getString(Settings.Password.name(), "");
    }

    public void setPassword(String value) {
        setSettings(Settings.Password, value);
    }

    private enum Settings {
        IsLoggedIn, IsRegistered, UserToken, UserId, FullName, Email, Password
    }
}
