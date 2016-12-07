package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.testing;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

/**
 * Created by muchbeer on 12/7/2016.
 */
public class MealsFragment extends Fragment {

    public MealsFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static MealsFragment newInstance() {
        MealsFragment fragment = new MealsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         * Initialize UI elements
         */
        View rootView = inflater.inflate(R.layout.test_fragment_meal_lists, container, false);
        return rootView;
    }
}
