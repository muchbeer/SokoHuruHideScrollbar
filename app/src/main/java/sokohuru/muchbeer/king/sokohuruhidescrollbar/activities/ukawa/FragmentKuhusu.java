package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

/**
 * Created by muchbeer on 8/27/2015.
 */
public class FragmentKuhusu extends Fragment {

    private String finalContact = "+255757022731";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kuhusu, container, false);
//Time settup
      //  final finalContact = "+255757022731";
    Button imgCall = (Button) rootView.findViewById(R.id.callMe);
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                        startActivity(new Intent(Intent.ACTION_CALL,
                                Uri.parse("tel:" + finalContact)));

                }catch (Exception e) {
                    Log.e("Soko Huru Dialer", "error: " +
                            e.getMessage(), e);
                }
            }
        });



        return rootView;
    }

}
