package com.binaa.binaaadmin.server;

import android.content.Context;

import com.android.volley.Request;
import com.binaa.binaaadmin.models.Reservation;
import com.binaa.binaaadmin.models.UserModel;
import com.binaa.binaaadmin.utils.SettingsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Muhammad on 07/29/2017
 */
public abstract class ContentVolley extends BaseVolley {

    private final String url = Constants.getApiUrl();
    private String token;

    public ContentVolley(String TAG, Context context) {
        super(TAG, VolleySingleton.getInstance(context));
        SettingsManager settingsManager = new SettingsManager(context);
        token = settingsManager.getUserToken();
    }

    public void logIn(String email, String password) {
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        actionType = ActionType.LogIn;
        requestAction(Request.Method.POST, url + "auth/user/login", false);
    }

    public void getProperties() {
        params = new HashMap<>();
        params.put("token", token);

        actionType = ActionType.GetApartments;
        requestAction(Request.Method.GET, url + "get/apartments/reservations", false);
    }

    public void getHotels() {
        params = new HashMap<>();
        params.put("token", token);

        actionType = ActionType.GetHotels;
        requestAction(Request.Method.GET, url + "get/hotels/reservations", false);
    }

    public void getCars() {
        params = new HashMap<>();
        params.put("token", token);

        actionType = ActionType.GetCars;
        requestAction(Request.Method.GET, url + "get/cars/reservations", false);
    }

    public void getPropertyDetails(int id) {
        params = new HashMap<>();
        params.put("id", id + "");
        params.put("token", token);

        actionType = ActionType.GetPropertyDetails;
        requestAction(Request.Method.POST, url + "apartment/reservation/details", false);
    }

    public void getCarDetails(int id) {
        params = new HashMap<>();
        params.put("id", id + "");
        params.put("token", token);

        actionType = ActionType.GetCarDetails;
        requestAction(Request.Method.POST, url + "car/reservation/details", false);
    }

    public void getHotelDetails(int id) {
        params = new HashMap<>();
        params.put("id", id + "");
        params.put("token", token);

        actionType = ActionType.GetHotelDetails;
        requestAction(Request.Method.POST, url + "hotel/reservation/details", false);
    }

    public void addRegistration(String token) {

        params = new HashMap<>();
        params.put("token", token);

        actionType = ActionType.addRegistration;

        requestAction(Request.Method.POST, url + "store/admin/token", false);
    }

    @Override
    protected void onPreExecute(BaseVolley.ActionType actionType) {
        ActionType action = (ActionType) actionType;
        onPreExecute(action);
    }

    protected abstract void onPreExecute(ActionType actionType);

    @Override
    protected void getResponseParameters(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);
        JSONArray dataArray;
        JSONObject dataObject;

        String message = jsonObject.optString("error", null);
        boolean success = message == null;
        if (success) {
            message = jsonObject.optString("success", "");
        }

        ActionType action = (ActionType) actionType;

        switch (action) {
            case LogIn:
                dataObject = jsonObject.getJSONObject("data");
                UserModel userModel = new UserModel(dataObject);
                onPostExecuteUser(action, success, message, userModel);
                break;
            case GetApartments:
            case GetHotels:
            case GetCars:
                ArrayList<Reservation> reservations = new ArrayList<>();
                dataArray = jsonObject.optJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject reservationItem = dataArray.getJSONObject(i);
                    Reservation reservation = new Reservation(reservationItem);
                    reservations.add(reservation);
                }
                onPostExecuteGetReservations(action, success, message, reservations);
                break;
            case GetPropertyDetails:
            case GetHotelDetails:
            case GetCarDetails:
                dataObject = jsonObject.optJSONObject("data");

                Reservation reservation = new Reservation(dataObject);
                onPostExecuteReservationDetails(action, success, message, reservation);
                break;
            case addRegistration:
                onPostExecute(action, success, message);
                break;
        }
    }

    @Override
    protected void onPostExecuteError(boolean success, String message, BaseVolley.ActionType actionType) {

        ActionType action = (ActionType) actionType;

        switch (action) {
            case LogIn:
                onPostExecuteUser(action, false, message, null);
                break;
            case GetApartments:
            case GetHotels:
            case GetCars:
                onPostExecuteGetReservations(action, false, message, null);
                break;
            case GetPropertyDetails:
            case GetHotelDetails:
            case GetCarDetails:
                onPostExecuteReservationDetails(action, false, message, null);
                break;
            case addRegistration:
                onPostExecute(action, success, message);
                break;
        }
    }

    protected void onPostExecute(ActionType actionType, boolean success, String message) {
    }

    protected void onPostExecuteGetReservations(ActionType actionType, boolean success, String message, ArrayList<Reservation> reservations) {
    }

    protected void onPostExecuteUser(ActionType actionType, boolean succsess, String message, UserModel user) {
    }

    protected void onPostExecuteReservationDetails(ActionType actionType, boolean succsess, String message, Reservation reservation) {
    }

    public enum ActionType implements BaseVolley.ActionType {
        GetApartments, GetPropertyDetails, GetHotels, GetHotelDetails, GetCars, GetCarDetails, addRegistration, LogIn
    }
}
