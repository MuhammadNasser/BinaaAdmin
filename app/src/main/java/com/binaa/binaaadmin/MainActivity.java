package com.binaa.binaaadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.binaa.binaaadmin.models.Reservation;
import com.binaa.binaaadmin.server.ContentVolley;
import com.binaa.binaaadmin.utils.ApplicationBase;
import com.binaa.binaaadmin.utils.MyContextWrapper;
import com.binaa.binaaadmin.utils.ReservationsAdapter;
import com.binaa.binaaadmin.utils.SettingsManager;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TextView textViewTitle;
    private View viewLoading;
    private String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = getResources().getColor(R.color.colorPrimaryDark, null);
            } else {
                // noinspection deprecation
                color = getResources().getColor(R.color.colorPrimaryDark);
            }
            getWindow().setStatusBarColor(color);
        }
        SettingsManager settingsManager = new SettingsManager(getApplicationContext());
        if (!settingsManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);
            setToolBar();

            TabLayout tabLayout = findViewById(R.id.tabs);
            viewLoading = findViewById(R.id.viewLoading);

            ViewPager viewPager = findViewById(R.id.pager);

            viewPager.setOffscreenPageLimit(1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white, null));
                tabLayout.setTabTextColors(getResources().getColor(R.color.grayLight, null), getResources().getColor(R.color.white, null));
            } else {
                //noinspection deprecation
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
                //noinspection deprecation
                tabLayout.setTabTextColors(getResources().getColor(R.color.grayLight), getResources().getColor(R.color.white));
            }

            tabs = getResources().getStringArray(R.array.homeTabs);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(tabs);
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            for (int i = 0; i < tabs.length; i++) {
                tabLayout.getTabAt(i).setText(tabs[i]);
            }
            tabLayout.setOnTabSelectedListener(this);
            setActivityTitle(getString(R.string.app_name));

        }
    }

    public void setLoading(boolean isLoading) {
        viewLoading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("CutPasteId")
    private void setToolBar() {

        Toolbar toolBar = findViewById(R.id.toolbar);
        View actionBarView = getLayoutInflater().inflate(R.layout.toolbar_customview, toolBar, false);

        setSupportActionBar(toolBar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("");
        textViewTitle = actionBarView.findViewById(R.id.textViewActivityTitle);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
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
//        Locale languageType = ApplicationBase.getInstance().getLocale();

    @Override
    protected void attachBaseContext(Context newBase) {
        Locale languageType = new Locale("ar");
        super.attachBaseContext(MyContextWrapper.wrap(newBase, languageType));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setActivityTitle(tabs[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu_item:

                finish();
                ApplicationBase.getInstance().logOut();
                break;
        }
        return true;
    }


    public enum DetailsType {
        Properties, Hotels, Cars
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        private String[] tabs;

        public ViewPagerAdapter(String[] tabs) {
            this.tabs = tabs;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View view = inflater.inflate(R.layout.fragment_home_page, container, false);
            RecyclerView recycler = view.findViewById(R.id.recycler);

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recycler.setLayoutManager(layoutManager);
            recycler.setHasFixedSize(false);
            recycler.setItemAnimator(new DefaultItemAnimator());

            Content content;
            switch (position) {
                case 0:
                    content = new Content(recycler, DetailsType.Properties);
                    content.getProperties();
                    break;
                case 1:
                    content = new Content(recycler, DetailsType.Cars);
                    content.getCars();
                    break;
                case 2:
                    content = new Content(recycler, DetailsType.Hotels);
                    content.getHotels();
                    break;
            }

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public class Content extends ContentVolley {

        RecyclerView recyclerView;
        DetailsType type;

        public Content(RecyclerView recyclerView, DetailsType type) {
            super("", MainActivity.this);
            this.recyclerView = recyclerView;
            this.type = type;
        }

        @Override
        protected void onPreExecute(ActionType actionType) {
            setLoading(true);
        }

        @Override
        protected void onPostExecuteGetReservations(ActionType actionType, boolean success, String message, ArrayList<Reservation> reservations) {
            setLoading(false);
            if (success) {
                recyclerView.setAdapter(new ReservationsAdapter(MainActivity.this, reservations, type));
            } else {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
