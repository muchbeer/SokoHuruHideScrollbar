package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.R;


public class AddMainActivity extends AppCompatActivity {


    static final int NUM_ITEMS = 4;
    PlanetFragmentPagerAdapter planetFragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_main);

        planetFragmentPagerAdapter = new PlanetFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(planetFragmentPagerAdapter);


        // makevisibilty = (Button) findViewById(R.id.goto_previous);
        Button button  = (Button)findViewById(R.id.goto_next);
        button.setOnClickListener(btnListener);

        button = (Button)findViewById(R.id.goto_next);
        button.setOnClickListener(btnListener);

        button = (Button)findViewById(R.id.goto_next);
        button.setOnClickListener(btnListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean tickInvisibility() {
        viewPager.setCurrentItem(0) ;
        return true;
    }
    public boolean checkVisibility(int a) {

        viewPager.setCurrentItem(a);

        return true;
    }


    private View.OnClickListener btnListener = new View.OnClickListener() {
        int check=0;
        public void onClick(View v) {
            //     viewPager.setCurrentItem(0)
            switch(v.getId()) {

                case R.id.goto_next:
                    //  makeVisiblity.setEnabled(true);

                    //  makevisibilty.setVisibility(View.VISIBLE);
                    //                        setVisible(true);
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);

                    break;




            }
        }
    };

    public static class PlanetFragmentPagerAdapter extends FragmentPagerAdapter {
        public PlanetFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SwipeFragment extends Fragment {

        EditText nameItem;
        EditText dataItem;

        String firtPage_et1, firstPage_et2, secondPage_et1, secondPage_et2;
        String thirdPage_et1, thirdPage_et2, fourthPage_et1, fourthPage_et2;
        public SwipeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_add, container, false);

            // TextView tv = (TextView) rootView.findViewById(R.id.text);

            nameItem = (EditText) rootView.findViewById(R.id.et_onetext);
            dataItem =(EditText) rootView.findViewById(R.id.et_twotext);

            ImageView img = (ImageView)rootView.findViewById(R.id.imageView);
            Bundle args = getArguments();
            int position = args.getInt("position");
            String planet = AddFragmentItem.SOKONI[position];
            int imgResId = getResources().getIdentifier(planet, "drawable", "toast.muchbeer.king.swipingfragmentbest");
            img.setImageResource(imgResId);

            nameItem.setHint(AddFragmentItem.SOKONI_DETAIL.get(planet));
            dataItem.setHint(AddFragmentItem.SOKONI_DETAIL_BELOW.get(planet));

            // Hint(AddFragmentItem.SOKONI_DETAIL.get(planet));
            //tv.setText(AddFragmentItem.SOKONI_DETAIL.get(planet)+" - Wikipedia.");

            switch(AddFragmentItem.SOKONI_DETAIL_TEXT.get(planet)) {
                case "1":
                    //   viewPager.setCurrentItem(0);
                    //      nameterm = nameItem.getText().toString();
                    //    dataterm = dataItem.getText().toString();

                    firtPage_et1 = nameItem.getText().toString();
                    firstPage_et2 = dataItem.getText().toString();

                    break;
                case "2":
                    //  viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                    secondPage_et1 = nameItem.getText().toString();
                    secondPage_et2 = dataItem.getText().toString();
                    //Toast.makeText(getActivity(),firtPage_et1+"-"+ firstPage_et2 ,Toast.LENGTH_LONG).show();

                    break;
                case "3":
                    thirdPage_et1 = nameItem.getText().toString();
                    thirdPage_et2 = dataItem.getText().toString();

                    //  Toast.makeText(getActivity(),secondPage_et1+"-" + secondPage_et2,Toast.LENGTH_LONG).show();
                    break;
                case "4":
                    //  viewPager.setCurrentItem(NUM_ITEMS - 1);
                    //    desc = nameItem.getText().toString();
                    //  contact=dataItem.getText().toString();
                    fourthPage_et1 = nameItem.getText().toString();
                    fourthPage_et2 = dataItem.getText().toString();

                    // Toast.makeText(getActivity(),thirdPage_et1+"-" + thirdPage_et2,Toast.LENGTH_LONG).show();
                    break;
            }
            return rootView;
        }

        public static Fragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            swipeFragment.setArguments(args);
            return swipeFragment;
        }
    }
}
