package com.binaa.binaaadmin.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Muhammad on 8/19/2017
 */

public class Reservation implements Serializable {

    private int id;
    private String name;
    private String email;
    private String country;
    private String phone;
    private String passport;
    private String arrival_date;
    private String leave_date;
    private String airport_pick;
    private String car_rent;
    private String travel_prog;
    private String driver;
    private Property apartment;
    private Car car;
    private Hotel hotel;


    public Reservation(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.optInt("id");
        this.name = jsonObject.optString("name");
        this.email = jsonObject.optString("email");
        this.country = jsonObject.optString("country");
        this.phone = jsonObject.optString("phone_number");
        this.passport = jsonObject.optString("passport_number");
        this.arrival_date = jsonObject.optString("arrival_date");
        this.leave_date = jsonObject.optString("leave_date");
        this.airport_pick = jsonObject.optString("airport_pick");
        this.car_rent = jsonObject.optString("car_rent");
        this.travel_prog = jsonObject.optString("travel_prog");
        this.driver = jsonObject.optString("driver");

        JSONObject apartment = jsonObject.optJSONObject("apartment");
        if (apartment != null) {
            this.apartment = new Property(apartment);
        }

        JSONObject hotel = jsonObject.optJSONObject("hotel");
        if (hotel != null) {
            this.hotel = new Hotel(hotel);
        }

        JSONObject car = jsonObject.optJSONObject("car");
        if (car != null) {
            this.car = new Car(car);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassport() {
        return passport;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public String getLeave_date() {
        return leave_date;
    }

    public String getAirport_pick() {
        return airport_pick;
    }

    public String getCar_rent() {
        return car_rent;
    }

    public String getTravel_prog() {
        return travel_prog;
    }

    public String getDriver() {
        return driver;
    }

    public Property getApartment() {
        return apartment;
    }

    public Car getCar() {
        return car;
    }

    public Hotel getHotel() {
        return hotel;
    }
}
