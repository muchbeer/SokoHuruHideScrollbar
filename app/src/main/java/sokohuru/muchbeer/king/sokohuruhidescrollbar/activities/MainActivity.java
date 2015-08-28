package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;



import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.swipeable.DetailActivityMain;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.swipeable.MoviesFragment;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa.ActivityError;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa.FragmentRais;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa.FragmentWabunge;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

            case R.id.action_settings:

                Intent startDetail = new Intent(this, ActivityError.class);
                startActivity(startDetail);
      Toast.makeText(getApplicationContext(), "Check met", Toast.LENGTH_LONG).show();
        }
     return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentRais(), "LOWASSA");
        adapter.addFragment(new FragmentWabunge(), "UBUNGE");
        adapter.addFragment(new CheeseListFragment(), "Category");
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        //Checking if the item is in checked state or not, if not make it in checked state
                        if(menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        //Closing drawer on item click
                        mDrawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()){


                            //Replacing the main content with ContentFragment Which is our Inbox View;
                            case R.id.nav_home:
                                Toast.makeText(getApplicationContext(),"Habari kuhusu Lowassa",Toast.LENGTH_SHORT).show();
                                FragmentRais fragment = new FragmentRais();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.viewpager,fragment);
                                fragmentTransaction.commit();
                                return true;

                            // For rest of the options we just show a toast on click

                            case R.id.nav_wabunge:
                                Toast.makeText(getApplicationContext(),"Habari kuhusu Wabunge",Toast.LENGTH_SHORT).show();
                                FragmentWabunge fragment2 = new FragmentWabunge();
                                android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction2.replace(R.id.tabs,fragment2);
                                fragmentTransaction2.commit();
                                return true;
                            case R.id.nav_video:
                                Toast.makeText(getApplicationContext(),"Angalia Video za lowassa",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.nav_chat:
                                Toast.makeText(getApplicationContext(),"Yalijori leo hii",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.nav_forum:
                                Toast.makeText(getApplicationContext(),"Wasiliana nasi kuhusu habari mototo",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.nav_about:
                                Toast.makeText(getApplicationContext(),"Ijue program yetu ya ukawa",Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                Toast.makeText(getApplicationContext(),"Tafadhari Jaribu Tena",Toast.LENGTH_SHORT).show();
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
}
