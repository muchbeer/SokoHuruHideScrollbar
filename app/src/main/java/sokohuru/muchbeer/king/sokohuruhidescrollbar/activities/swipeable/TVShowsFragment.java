package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.swipeable;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;


public class TVShowsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tv_shows_fragment, container, false);
        return rootView;
    }
}
