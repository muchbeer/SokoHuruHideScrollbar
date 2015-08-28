package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.swipeable;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

public class MoviesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_fragment, container, false);
        return rootView;
    }
}
