package com.binaa.binaaadmin.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 17/7/2017
 */
public class UserModel implements Serializable {

    private int id;
    private String name;
    private String token;
    private String email;
    private String password;

    public UserModel(JSONObject json) throws JSONException {

        id = json.optInt("id");
        name = json.optString("full_name");
        email = json.optString("email");

        JSONObject meta = json.optJSONObject("meta");
        if (meta != null) {
            token = meta.optString("token");
        }
    }

    public UserModel() {
        token = null;
        id = 0;
        name = "";
        email = "";
        password = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UserModel && ((UserModel) obj).getId() == id;
    }
}
