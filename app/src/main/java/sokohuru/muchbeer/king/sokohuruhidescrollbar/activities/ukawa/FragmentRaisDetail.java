package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.AdapterSoko;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Constants;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Keys;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.L;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Soko;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.VolleySingleton;

/**
 * Created by muchbeer on 8/27/2015.
 */
public class FragmentRaisDetail extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String URL_SOKO = "http://sokouhuru.com/ukawa_rais.php";
    private static final String STATE_SOKO = "State Sokoni";
    private static final int SHARING_CODE = 1;
    private static final String TAG_POSITION = "position";

    private int position;

    String collapseTitle;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageLoader mImageLoader;
    private RequestQueue requestQueue;
    // private ArrayList<Soko> listMovies = new ArrayList<>();

    private RecyclerView listSokoni;
    private RecyclerView.LayoutManager sLayoutManager;

    // private OnFragmentInteractionListener mListener;
    private VolleySingleton volleySingleton;

    private AdapterSoko adapterSoko;
    private TextView mTextError;
    private TextView txtName;
    String result;
    //  private String position;
    String title;
    private NetworkImageView loadImageView;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView ukawaTitle;
    private TextView ukawaDesc, ukawaDate, ukawaUser, ukawaPlace;
    private String NULLIMAGE = "sokouhuru.com/image/sokohuru.png";
    private ImageView imgCall;
    //  private char[] title;


    public static FragmentRaisDetail newInstance(String mParam1, String mParam2){
        FragmentRaisDetail fragmentClick = new FragmentRaisDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
        args.putString(ARG_PARAM2, mParam2);
        fragmentClick.setArguments(args);

        return fragmentClick;
    }


    public FragmentRaisDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    public void sendJsonRequest(final int position) {
        // showing refresh animation before making http call
        // swipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL_SOKO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSONResponse(response, position);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        //    L.t(getActivity(),error.toString());
                        mTextError.setVisibility(View.VISIBLE);
                        //    btnRefresh.setVisibility(View.VISIBLE);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            mTextError.setText(R.string.error_timeout);

                        } else if (error instanceof AuthFailureError) {
                            mTextError.setText(R.string.error_auth_failure);
                            //TODO
                        } else if (error instanceof ServerError) {
                            mTextError.setText(R.string.error_auth_failure);
                            //TODO
                        } else if (error instanceof NetworkError) {
                            mTextError.setText(R.string.error_network);
                            //TODO
                        } else if (error instanceof ParseError) {
                            mTextError.setText(R.string.error_parser);
                            //TODO
                        }
                    }
                });
        requestQueue.add(request);
    }


    public void parseJSONResponse(final JSONObject response, int position) {

        if (response == null || response.length() == 0) {
            L.t(getActivity(), "Refresh data");
        } else if (response != null && response.length() > 0) {


            try {
                StringBuilder data = new StringBuilder();
                JSONArray arrayMoview = null;
                try {
                    arrayMoview = response.getJSONArray(Keys.EndpointBoxOffice.KEY_SOKO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                title = Constants.NA;
                String imaging = Constants.NA;

                String name = Constants.NA;
                String postdate = Constants.NA;
                String place=Constants.NA;
                String title = Constants.NA;

                String description = Constants.NA;

                JSONObject currentMarket = arrayMoview.getJSONObject(position);


                if (currentMarket.has(Keys.EndpointBoxOffice.KEY_USER) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_USER)) {
                    name = currentMarket.getString(Keys.EndpointBoxOffice.KEY_USER);
                }

                if(currentMarket.has(Keys.EndpointBoxOffice.KEY_IMAGE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_IMAGE)) {

                    imaging = currentMarket.getString(Keys.EndpointBoxOffice.KEY_IMAGE);

                }
                if (currentMarket.has(Keys.EndpointBoxOffice.KEY_DATE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_DATE)) {
                    postdate= currentMarket.getString(Keys.EndpointBoxOffice.KEY_DATE);
                }
                if (currentMarket.has(Keys.EndpointBoxOffice.KEY_DESCRIPTION) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_DESCRIPTION)) {
                    description = currentMarket.getString(Keys.EndpointBoxOffice.KEY_DESCRIPTION);
                }
                if (currentMarket.has(Keys.EndpointBoxOffice.KEY_TITLE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_TITLE)) {
                    title = currentMarket.getString(Keys.EndpointBoxOffice.KEY_TITLE);
                }

                if (currentMarket.has(Keys.EndpointBoxOffice.KEY_PLACE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_PLACE)) {
                    place = currentMarket.getString(Keys.EndpointBoxOffice.KEY_PLACE);
                }


                Soko sokoni = new Soko();

                //sokoni setTitle
                sokoni.setName(name);
                sokoni.setPlace(place);
                sokoni.setTitle(title);
                sokoni.setDescription(description);
                sokoni.setPostdate(postdate);
                sokoni.setImage(imaging);
                //   txtName.setText(title);



                ukawaPlace.setText(place);
                ukawaDate.setText(postdate);
                ukawaTitle.setText(title);
                ukawaUser.setText(name);
                //  txtUsername.setText(username);
                ukawaDesc.setText(description);

                collapsingToolbar.setTitle(place);

                mImageLoader = VolleySingleton.getsInstance().getImageLoader();

                if(imaging.length()>9) {
                    loadImageView.setImageUrl(imaging, mImageLoader);

                }else {
                    loadImageView.setBackground(getResources().getDrawable(R.drawable.loag));


                }
                // Toast.makeText(getActivity(), "Item is: " + title,Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                L.t(getActivity(), e.toString());
            }

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        //  mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // adapter = new ListAdapterHolder(mActivity);

        //   String tis = "george";

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);



        // txtName = (TextView) rootView.findViewById(R.id.name);
        ukawaUser = (TextView) rootView.findViewById(R.id.ukawaUser);
        ukawaDate = (TextView) rootView.findViewById(R.id.ukawaDate);
        ukawaPlace = (TextView) rootView.findViewById(R.id.ukawaPlace);
        ukawaDesc = (TextView) rootView.findViewById(R.id.ukawaDesc);
        ukawaTitle = (TextView) rootView.findViewById(R.id.ukawaTitle);
        //  txtUsername = (TextView) rootView.findViewById(R.id.sokoUser);



        mTextError = (TextView) rootView.findViewById(R.id.txtError);
        loadImageView = (NetworkImageView) rootView.findViewById(R.id.imaging);

        //Getting item details from Intent
        Intent collectDataIntent = getActivity().getIntent();
        position = collectDataIntent.getIntExtra(TAG_POSITION, -1);

        if(savedInstanceState !=null) {
            sendJsonRequest(position);
        }
        else {
            sendJsonRequest(position);
            //  MyApplication.getWritableDatabase().getAllItemFromMarket();
            //     adapterSoko.notifyDataSetChanged();
        }

        return rootView;
    }

    private void getPageTitile(String title) {

    }
}