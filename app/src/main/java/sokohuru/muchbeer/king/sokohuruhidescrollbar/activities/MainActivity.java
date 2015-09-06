package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;


import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.chat.FragmentChat;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Logger;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.MyApplication;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa.FragmentKuhusu;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa.FragmentRais;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa.FragmentRaisDetail;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa.FragmentWabunge;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.youtube.FragmentMain;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.youtube.FragmentYoutube;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pushbots.push.Pushbots;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Push message
        Pushbots.sharedInstance().init(this);

        try {
            Tracker t = ((MyApplication) getApplication()).getTracker(
                    MyApplication.TrackerName.APP_TRACKER);

            t.setScreenName("President Activity");

            t.send(new HitBuilders.AppViewBuilder().build());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.openDrawer(Gravity.LEFT);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }


        FragmentMain fragment = new FragmentMain();
        android.app.FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment);

        fragmentTransaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        mDrawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {


                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.nav_home:
                                Toast.makeText(getApplicationContext(), "Habari kuhusu Lowassa", Toast.LENGTH_SHORT).show();
                                FragmentRais fragment = new FragmentRais();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                return true;

                            // For rest of the options we just show a toast on click

                            case R.id.nav_wabunge:
                                Toast.makeText(getApplicationContext(), "Habari kuhusu Wabunge", Toast.LENGTH_SHORT).show();
                                FragmentWabunge fragment2 = new FragmentWabunge();
                                android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction2.replace(R.id.frame, fragment2);
                                fragmentTransaction2.addToBackStack(null);

                                fragmentTransaction2.commit();
                                return true;
                            case R.id.nav_video:
                                Toast.makeText(getApplicationContext(), "Angalia Video za lowassa", Toast.LENGTH_SHORT).show();
                                FragmentYoutube fragment3 = new FragmentYoutube();
                                android.support.v4.app.FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction3.replace(R.id.frame, fragment3);
                                fragmentTransaction3.addToBackStack(null);

                                fragmentTransaction3.commit();

                                return true;

                            case R.id.nav_chat:
                                Toast.makeText(getApplicationContext(), "Angalia Video za lowassa", Toast.LENGTH_SHORT).show();
                                FragmentChat fragment4 = new FragmentChat();
                                android.support.v4.app.FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction4.replace(R.id.frame, fragment4);

                                fragmentTransaction4.commit();

                                return true;

                            case R.id.nav_forum:

                                FragmentKuhusu fragment7 = new FragmentKuhusu();
                                android.support.v4.app.FragmentTransaction fragmentTransaction7 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction7.replace(R.id.frame, fragment7);
                                fragmentTransaction7.addToBackStack(null);

                                fragmentTransaction7.commit();

                                return true;
                               // Toast.makeText(getApplicationContext(), "Wasiliana nasi kuhusu habari mototo", Toast.LENGTH_SHORT).show();


                            case R.id.nav_about:
                                Toast.makeText(getApplicationContext(), "Tunakuletea Picha za matukio ya uchaguzi", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.nav_exit:
                                android.os.Process.killProcess(android.os.Process.myPid());
                                return true;


                            default:
                                Toast.makeText(getApplicationContext(), "Tafadhari Jaribu Tena", Toast.LENGTH_SHORT).show();
                                return true;

                        }
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        //  GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }


}