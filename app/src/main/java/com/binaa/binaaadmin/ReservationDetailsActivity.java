package com.binaa.binaaadmin;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.binaaadmin.models.Car;
import com.binaa.binaaadmin.models.Hotel;
import com.binaa.binaaadmin.models.Property;
import com.binaa.binaaadmin.models.Reservation;
import com.binaa.binaaadmin.server.ContentVolley;

/**
 * Created by Muhammad on 5/8/2018
 */

public class ReservationDetailsActivity extends AppCompatActivity {

    public static String TYPE = "item_type";
    public static String ID = "id";

    private TextView textViewTitle;
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private TextView textViewCountry;
    private TextView textViewPassport;
    private TextView textViewCode;
    private TextView textViewArrival;
    private TextView textViewLeave;
    private TextView textViewWantCar;
    private TextView textViewPickupFromAirport;
    private TextView textViewWantTravel;
    private TextView textViewWantDriver;
    private View viewLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_reservation_details);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setToolBar();

        MainActivity.DetailsType type = (MainActivity.DetailsType) getIntent().getSerializableExtra(TYPE);
        int id = getIntent().getIntExtra(ID, 0);

        viewLoading = findViewById(R.id.viewLoading);

        LinearLayout linearLayoutWantCar = findViewById(R.id.linearLayoutWantCar);
        LinearLayout linearLayoutPickupFromAirport = findViewById(R.id.linearLayoutPickupFromAirport);
        LinearLayout linearLayoutWantTravel = findViewById(R.id.linearLayoutWantTravel);
        LinearLayout linearLayoutWantDriver = findViewById(R.id.linearLayoutWantDriver);

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewCountry = findViewById(R.id.textViewCountry);
        textViewPassport = findViewById(R.id.textViewPassport);
        textViewCode = findViewById(R.id.textViewCode);
        textViewArrival = findViewById(R.id.textViewArrival);
        textViewLeave = findViewById(R.id.textViewLeave);
        textViewWantCar = findViewById(R.id.textViewWantCar);
        textViewPickupFromAirport = findViewById(R.id.textViewPickupFromAirport);
        textViewWantTravel = findViewById(R.id.textViewWantTravel);
        textViewWantDriver = findViewById(R.id.textViewWantDriver);
        TextView textViewCodeTitle = findViewById(R.id.textViewCodeTitle);

        textViewCode.setPaintFlags(textViewCode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewPhone.setPaintFlags(textViewPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewEmail.setPaintFlags(textViewEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Content content = new Content();
        switch (type) {
            case Properties:
                textViewCodeTitle.setText(getString(R.string.apartment));
                content.getPropertyDetails(id);
                break;
            case Hotels:
                textViewCodeTitle.setText(getString(R.string.hotel));
                content.getHotelDetails(id);
                break;
            case Cars:
                linearLayoutWantDriver.setVisibility(View.VISIBLE);
                linearLayoutPickupFromAirport.setVisibility(View.GONE);
                linearLayoutWantTravel.setVisibility(View.GONE);
                linearLayoutWantCar.setVisibility(View.GONE);
                textViewCodeTitle.setText(getString(R.string.car));
                content.getCarDetails(id);
                break;
        }

        setActivityTitle(getString(R.string.app_name));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


    public void setLoading(boolean isLoading) {
        viewLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void setToolBar() {

        Toolbar toolBar = findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);
        textViewTitle = actionBarView.findViewById(R.id.textViewActivityTitle);

        setSupportActionBar(toolBar);

        toolBar.setPadding(0, getStatusBarHeight(), 0, 0);


        // Set up the drawer.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void setActivityTitle(String title) {
        if (textViewTitle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textViewTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>", Html.FROM_HTML_OPTION_USE_CSS_COLORS));
            } else {
                //noinspection deprecation
                textViewTitle.setText(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
            }
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = (int) getResources().getDimension(resourceId);
        }
        return result;
    }

    public class Content extends ContentVolley {

        public Content() {
            super("", ReservationDetailsActivity.this);
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            setLoading(true);
        }

        @Override
        protected void onPostExecuteReservationDetails(ActionType actionType, boolean succsess, String message, final Reservation reservation) {
            setLoading(false);
            if (succsess) {

                final String phone = reservation.getPhone();
                final String email = reservation.getEmail();

                textViewName.setText(reservation.getName());
                textViewEmail.setText(email);
                textViewPhone.setText(phone);
                textViewCountry.setText(reservation.getCountry());
                textViewPassport.setText(reservation.getPassport());
                textViewArrival.setText(reservation.getArrival_date());
                textViewLeave.setText(reservation.getLeave_date());
                textViewWantCar.setText(reservation.getCar_rent());
                textViewPickupFromAirport.setText(reservation.getAirport_pick());
                textViewWantTravel.setText(reservation.getTravel_prog());

                if (phone != null && !phone.isEmpty()) {
                    textViewPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intentCall = new Intent(Intent.ACTION_DIAL);
                            intentCall.setData(Uri.parse("tel:" + phone));
                            startActivity(intentCall);
                        }
                    });
                }

                if (email != null && !email.isEmpty()) {
                    textViewEmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent sendIntent = new Intent(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Binaa Reservation");
                            sendIntent.setType("plain/text");
                            startActivity(sendIntent);
                        }
                    });
                }

                switch (actionType) {
                    case GetPropertyDetails:
                        final Property property = reservation.getApartment();
                        if (property != null) {
                            textViewCode.setText(property.getCode());
                        }

                        textViewCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ReservationDetailsActivity.this, DetailsActivity.class);
                                intent.putExtra(TYPE, MainActivity.DetailsType.Properties);
                                intent.putExtra("property", property);
                                startActivity(intent);
                            }
                        });
                        break;
                    case GetHotelDetails:
                        final Hotel hotel = reservation.getHotel();
                        if (hotel != null) {
                            textViewCode.setText(hotel.getCode());
                        }
                        textViewCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ReservationDetailsActivity.this, DetailsActivity.class);
                                intent.putExtra(TYPE, MainActivity.DetailsType.Hotels);
                                intent.putExtra("hotel", hotel);
                                startActivity(intent);
                            }
                        });
                        break;
                    case GetCarDetails:
                        final Car car = reservation.getCar();
                        if (car != null) {
                            textViewCode.setText(car.getModel());
                        }
                        textViewWantDriver.setText(reservation.getDriver());
                        textViewCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ReservationDetailsActivity.this, DetailsActivity.class);
                                intent.putExtra(TYPE, MainActivity.DetailsType.Cars);
                                intent.putExtra("car", car);
                                startActivity(intent);
                            }
                        });
                        break;
                }

            } else {
                Toast.makeText(ReservationDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
