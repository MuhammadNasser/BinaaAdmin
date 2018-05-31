package com.binaa.binaaadmin;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binaa.binaaadmin.models.Car;
import com.binaa.binaaadmin.models.Hotel;
import com.binaa.binaaadmin.models.Image;
import com.binaa.binaaadmin.models.Property;
import com.binaa.binaaadmin.utils.ApplicationBase;
import com.binaa.binaaadmin.utils.MyContextWrapper;
import com.binaa.binaaadmin.views.PagerSlidingTabStrip;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import static com.binaa.binaaadmin.ReservationDetailsActivity.TYPE;


/**
 * Created by Muhammad on 8/6/2017
 */

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewDescription;
    private TextView textViewPrice;
    private TextView textViewCode;
    private TextView textViewArea;
    private TextView textViewBedrooms;
    private TextView textViewBathrooms;
    private ViewPager viewPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;
    private MainActivity.DetailsType detailsType;
    private RelativeLayout relativeLayoutDetails;
    private Property property;
    private Hotel hotel;
    private Car car;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setToolBar();

        textViewDescription = findViewById(R.id.textViewDescription);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewCode = findViewById(R.id.textViewCode);
        textViewBathrooms = findViewById(R.id.textViewBathrooms);
        textViewBedrooms = findViewById(R.id.textViewBedrooms);
        textViewArea = findViewById(R.id.textViewArea);
        relativeLayoutDetails = findViewById(R.id.relativeLayoutDetails);

        viewPager = findViewById(R.id.viewPager);
        pagerSlidingTabStrip = findViewById(R.id.pagerSlidingTabStrip);

        detailsType = (MainActivity.DetailsType) getIntent().getSerializableExtra(TYPE);
        property = (Property) getIntent().getSerializableExtra("property");
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        car = (Car) getIntent().getSerializableExtra("car");

        initializeData();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale languageType = ApplicationBase.getInstance().getLocale();
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }

    private void initializeData() {
        switch (detailsType) {
            case Properties:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(property.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(property.getDescription()));
                }

                textViewPrice.setText(String.format("%s - %s %s", property.getPrice(), property.getPriceMonth(), getResources().getString(R.string.egp)));
                textViewCode.setText(String.format("%s %s", getResources().getString(R.string.property_code), property.getCode()));
                textViewArea.setText(String.format("%s%s%s", getString(R.string.area), property.getArea(), getString(R.string.m_)));
                textViewBedrooms.setText(String.format("%s %s", getString(R.string.bedrooms), property.getBedrooms()));
                textViewBathrooms.setText(String.format("%s %s", getString(R.string.bathrooms), property.getBathrooms()));

                viewPager.setAdapter(new PageAdapter(property.getImagesLinks()));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
                break;
            case Hotels:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(hotel.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(hotel.getDescription()));
                }

                textViewPrice.setText(String.format("%s - %s %s", hotel.getPrice(), hotel.getPriceMonth(), getResources().getString(R.string.egp)));
                textViewCode.setText(String.format("%s %s", getResources().getString(R.string.property_code), hotel.getCode()));
                textViewArea.setText(String.format("%s%s%s", getString(R.string.area), hotel.getArea(), getString(R.string.m_)));
                textViewBedrooms.setText(String.format("%s %s", getString(R.string.bedrooms), hotel.getBedrooms()));
                textViewBathrooms.setText(String.format("%s %s", getString(R.string.bathrooms), hotel.getBathrooms()));

                viewPager.setAdapter(new PageAdapter(hotel.getImagesLinks()));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
                break;
            case Cars:
                relativeLayoutDetails.setVisibility(View.GONE);
                textViewPrice.setVisibility(View.INVISIBLE);
                textViewCode.setVisibility(View.INVISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textViewDescription.setText(Html.fromHtml(car.getDescription(), Html.FROM_HTML_OPTION_USE_CSS_COLORS));
                } else {
                    //noinspection deprecation
                    textViewDescription.setText(Html.fromHtml(car.getDescription()));
                }
                viewPager.setAdapter(new PageAdapter(car.getImages()));

                if (Build.VERSION.SDK_INT > 22) {
                    pagerSlidingTabStrip.setIndicatorColor(getResources().getColor(android.R.color.transparent, null));
                } else {
                    // noinspection deprecation
                    getResources().getColor(android.R.color.transparent);
                }
                pagerSlidingTabStrip.setSelectedTabSrc(R.drawable.circle_gray_dark);
                pagerSlidingTabStrip.setViewPager(viewPager);
                break;
        }
    }

    private void setToolBar() {

        Toolbar toolBar = findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);

        setSupportActionBar(toolBar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        TextView textViewActivityTitle = actionBarView.findViewById(R.id.textViewActivityTitle);


        // Set up the drawer.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        textViewActivityTitle.setText(getString(R.string.details));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PageAdapter extends PagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        private LayoutInflater inflater;
        private ArrayList<Image> images;

        public PageAdapter(ArrayList<Image> images) {
            this.inflater = getLayoutInflater();
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public float getPageWidth(int position) {
            return 1f;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = inflater.inflate(R.layout.header_item, container, false);

            ImageView imageViewCover = itemView.findViewById(R.id.imageViewCover);

            Picasso.with(DetailsActivity.this).load(images.get(position).getImageUrl()).
                    placeholder(R.drawable.placeholder).fit().centerCrop().
                    error(R.drawable.ic_warning).
                    into(imageViewCover);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public int getPageIconResId(int position) {
            return R.drawable.circle_gray_dark_stroke;
        }
    }

}
