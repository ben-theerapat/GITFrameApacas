package com.admin.gitframeapacas.Views;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.admin.gitframeapacas.Fragment.FeedAboutAQIFragment;
import com.admin.gitframeapacas.Fragment.FeedFavoriteFragment;
import com.admin.gitframeapacas.Fragment.FeedHomeFragment;
import com.admin.gitframeapacas.Fragment.FeedMapNavigateFragment;
import com.admin.gitframeapacas.Fragment.FeedMapRealtimeFragment;
import com.admin.gitframeapacas.R;
import com.admin.gitframeapacas.SQLite.DBUser;
import com.admin.gitframeapacas.Service.GetGasService;
import com.admin.gitframeapacas.Service.SetGasService;
import com.arlib.floatingsearchview.FloatingSearchView;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static boolean MQTTRunning = false;
    static FloatingSearchView searchDistrict;
    static FloatingSearchView searchNavigate;
    private static DrawerLayout mDrawerLayout;
    private static String TAG = "BENHomeActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Snackbar snackbar;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "my Profile", Toast.LENGTH_SHORT).show();
            }
        });

        Fragment myFragment = getSupportFragmentManager().findFragmentById(R.id.searchfragment);
        searchDistrict = (FloatingSearchView) myFragment.getView().findViewById(R.id.search_district);
        searchDistrict.attachNavigationDrawerToMenuButton(mDrawerLayout);

        searchNavigate = (FloatingSearchView) myFragment.getView().findViewById(R.id.search_navigate);
        searchNavigate.attachNavigationDrawerToMenuButton(mDrawerLayout);
        final ConstraintLayout view2 = (ConstraintLayout) findViewById(R.id.constaint_home);


        startService(new Intent(getApplicationContext(), SetGasService.class));


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        DBUser db = new DBUser(getApplicationContext());
        if (db.getCheckSensor() == 0) {
            alertDialogBuilder.setTitle("APARCAS System");
            alertDialogBuilder
                    .setMessage("คุณมีอุปกรณ์เซ็นเซอร์ตรวจจับสภาพอากาศหรือไม่")
                    .setCancelable(false)
                    .setPositiveButton("มี", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Log.i(TAG, "มีเซ็นเซอร์");
                            //gauge.setVisibility(View.GONE);
                            //txtAQI.setVisibility(View.GONE);
                            dialog.cancel();
                            DBUser db = new DBUser(getApplicationContext());
                            db.updateCheckSensor(1);

                        }
                    })
                    .setNegativeButton("ไม่มี", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "ไม่มีเซ็เนซอร์");
                            dialog.cancel();
                            snackbar = Snackbar.make(view2, "หากคุณมีเซ็นเซอร์ คุณสามารถเข้าไปเปิดการใช้งานที่ตั้งค่าได้ในภายหลัง", Snackbar.LENGTH_LONG)
                                    .setAction("ปิด", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            snackbar.dismiss();
                                        }
                                    });
                            snackbar.show();
                            DBUser db = new DBUser(getApplicationContext());
                            db.updateCheckSensor(1);


                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }


    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void initUI() {

        Log.i(TAG, "initUI");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_home),
                        Color.parseColor(colors[0]))
                        //.selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("Home")
                        .badgeTitle("Home Sweet Home")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_favorite),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Favorite")
                        .badgeTitle("My Favorite :D")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_navigate),
                        Color.parseColor(colors[2]))
                        // .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Navigate")
                        .badgeTitle("go somewhere?")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_realtime),
                        Color.parseColor(colors[2]))
                        // .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("Real time")
                        .badgeTitle("Real time !!")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_about),
                        Color.parseColor(colors[3]))
                        // .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("About")
                        .badgeTitle("AQI")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setBehaviorEnabled(true);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
                switch (position) {
                    case 0:

                        if (searchNavigate.getVisibility() == View.VISIBLE) {
                            searchNavigate.setVisibility(View.GONE);
                        }
                        if (searchDistrict.getVisibility() == View.GONE) {
                            searchDistrict.setVisibility(View.VISIBLE);
                        }
                        if (isMyServiceRunning(GetGasService.class) == true) {
                            stopService(new Intent(getApplicationContext(), GetGasService.class));
                            Log.i(TAG, "is service running: " + isMyServiceRunning(GetGasService.class));
                        }
                        break;

                    case 1:
                        if (searchNavigate.getVisibility() == View.VISIBLE) {
                            searchNavigate.setVisibility(View.GONE);
                        }
                        if (searchDistrict.getVisibility() == View.GONE) {
                            searchDistrict.setVisibility(View.VISIBLE);
                        }
                        if (isMyServiceRunning(GetGasService.class) == true) {
                            stopService(new Intent(getApplicationContext(), GetGasService.class));
                            Log.i(TAG, "is service running: " + isMyServiceRunning(GetGasService.class));
                        }
                        break;

                    case 2:

                        if (searchDistrict.getVisibility() == View.VISIBLE) {
                            searchDistrict.setVisibility(View.GONE);
                            searchNavigate.setVisibility(View.VISIBLE);
                        }

                        if (isMyServiceRunning(GetGasService.class) == true) {
                            stopService(new Intent(getApplicationContext(), GetGasService.class));
                            Log.i(TAG, "is service running: " + isMyServiceRunning(GetGasService.class));
                        }
                        break;

                    case 3://map
                        if (searchNavigate.getVisibility() == View.VISIBLE) {
                            searchNavigate.setVisibility(View.GONE);
                        }
                        if (searchDistrict.getVisibility() == View.GONE) {
                            searchDistrict.setVisibility(View.VISIBLE);
                        }

                        if (isMyServiceRunning(GetGasService.class) == false) {
                            startService(new Intent(getApplicationContext(), GetGasService.class));
                            Log.i(TAG, "is service running: " + isMyServiceRunning(GetGasService.class));
                        }

                        break;
                    case 4:
                        if (searchNavigate.getVisibility() == View.VISIBLE) {
                            searchNavigate.setVisibility(View.GONE);
                        }
                        if (searchDistrict.getVisibility() == View.GONE) {
                            searchDistrict.setVisibility(View.VISIBLE);
                        }
                        if (isMyServiceRunning(GetGasService.class) == true) {
                            stopService(new Intent(getApplicationContext(), GetGasService.class));
                            Log.i(TAG, "is service running: " + isMyServiceRunning(GetGasService.class));
                        }
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notification) {
            Toast.makeText(this, "NotificationActivity", Toast.LENGTH_SHORT).show();

        } /*else if (id == R.id.nav_reward) {
            Toast.makeText(this, "LocationActivity", Toast.LENGTH_SHORT).show();
        }*/ else if (id == R.id.nav_setting) {

            Toast.makeText(this, "SettingActivity", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {

            Toast.makeText(this, "HelpActivity", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent2);
        } else if (id == R.id.nav_logout) {

            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            DBUser db = new DBUser(getApplicationContext());
            db.updateStatus(0);
            db.updateName("");
            db.updateCheckSensor(0);
            if (isMyServiceRunning(SetGasService.class) == true) {
                stopService(new Intent(getApplicationContext(), SetGasService.class));
            }
            if (isMyServiceRunning(GetGasService.class) == true) {
                stopService(new Intent(getApplicationContext(), GetGasService.class));
            }
            Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_help:
                Intent intent2 = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent2);
                return true;
            case R.id.action_logout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {


        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    Log.i(TAG, "FeedHomeFragment");
                    return new FeedHomeFragment();
                case 1:
                    Log.i(TAG, "FeedFavoriteFragment");
                    return new FeedFavoriteFragment();
                case 2:
                    Log.i(TAG, "FeedFavoriteFragment");
                    return new FeedMapNavigateFragment();
                case 3:
                    Log.i(TAG, "FeedMapRealtimeFragment");
                    return new FeedMapRealtimeFragment();
                case 4:
                    Log.i(TAG, "FeedAboutAQIFragment");
                    return new FeedAboutAQIFragment();


            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

    }


}