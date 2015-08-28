package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.ukawa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Keys.EndpointBoxOffice.*;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.AdapterSoko;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Constants;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Keys;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.L;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.Soko;
import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.settings.VolleySingleton;

/**
 * Created by muchbeer on 8/27/2015.
 */
public class FragmentRais extends Fragment implements AdapterSoko.ClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "param2";

    //public static final String URL_SOKO = "http://sokouhuru.com/ccm/uchaguzi2.json";

    //public static final String URL_SOKO = "http://sokouhuru.com/kamaz_get_all_products.php";

    public static final String URL_SOKO = "http://sokouhuru.com/ukawa_rais.php";

    private static final String STATE_SOKO = "State Sokoni";
    private static final int SHARING_CODE = 1;
    private static final String TAG_POSITION = "position";
    private static final String TAG_POSITION2 = "position2";


    RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    public ArrayList<Soko> listMovies = new ArrayList<>();

    public ArrayList<Soko> listFilterBest = new ArrayList<>();


    public RecyclerView listSokoni;
    private RecyclerView.LayoutManager sLayoutManager;

    //Searchable

    // private SearchView mSearchView;



    // private OnFragmentInteractionListener mListener;
    private VolleySingleton volleySingleton;

    private AdapterSoko adapterSoko;
    private TextView mTextError;
    private TextView txtPosition;
    private String result;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnRefresh;

    private int positionSearch;
    private ProgressBar circularProgress;
    private TextView txtswipeRefresh;
    private int findPosition;
    private int enterSearchZone;


    // TODO: Rename and change types and number of parameters
    public static FragmentRais getInstance(int position ) {
        FragmentRais fragment = new FragmentRais();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_SOKO, listMovies);
    }

    public FragmentRais() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();

        // sendJsonRequest();


    }

    public void sendJsonRequest() {
        // showing refresh animation before making http call
        // swipeRefreshLayout.setRefreshing(true);
     JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
             URL_SOKO,
             new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                     listMovies = parseJSONResponse(response);
                     adapterSoko.setSokoList(listMovies);
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


    private ArrayList<Soko> parseJSONResponse(JSONObject response) {
        ArrayList<Soko> listMovies = new ArrayList<>();
        String matchFound = "N";

        if (response == null || response.length() == 0) {
            L.t(getActivity(), "Refresh data");
        }
        else   if (response != null && response.length() > 0) {



            try {

                StringBuilder data = new StringBuilder();
                JSONArray arrayMoview = response.getJSONArray(Keys.EndpointBoxOffice.KEY_SOKO);

                for (int i = 0; i < arrayMoview.length(); i++) {

                    String title = Constants.NA;
                    String imaging = Constants.NA;
                    String rating=Constants.NA;
                    String genre=Constants.NA;

                    //soko huru
                    String name = Constants.NA;
                    String postdate = Constants.NA;
                    String contact = Constants.NA;
                    String location = Constants.NA;

                    JSONObject currentMarket = null;
                    try {
                        currentMarket = arrayMoview.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(currentMarket.has(Keys.EndpointBoxOffice.KEY_USER) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_USER)) {
                        name = currentMarket.getString(Keys.EndpointBoxOffice.KEY_USER);
                    }

                    if(currentMarket.has(Keys.EndpointBoxOffice.KEY_IMAGE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_IMAGE)) {

                        imaging = currentMarket.getString(Keys.EndpointBoxOffice.KEY_IMAGE);

                    }// int releaseYear = currentMarket.getInt(KEY_YEAR);

                    if(currentMarket.has(Keys.EndpointBoxOffice.KEY_DATE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_DATE)) {

                        postdate = currentMarket.getString(Keys.EndpointBoxOffice.KEY_DATE);

                    }
                    if(currentMarket.has(Keys.EndpointBoxOffice.KEY_TITLE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_TITLE)) {

                        title   = currentMarket.getString(Keys.EndpointBoxOffice.KEY_TITLE);
                    }


                    data.append(name + "\n");

                    Soko sokoni = new Soko();
                    // sokoni.setId(id);
                    sokoni.setName(name);
                    sokoni.setImage(imaging);
                    //   sokoni.setRating(rating);
                    sokoni.setPostdate(postdate);
                    sokoni.setTitle(title);
                    //  sokoni.setReleaseYear(releaseYear);

                    listMovies.add(sokoni);
                  /*
                   Release year has someKind of problem try observe that has you move forward
                   sokoni.setReleaseYear(releaseYear);
                    */



                }


               //  L.t(getActivity(), "The date is: " +data.toString());
                   L.T(getActivity(), "SOKONI NOW" + listMovies.toString());

            } catch (JSONException e) {
                L.t(getActivity(), e.toString());
            }

        }
        //   MyApplication.getWritableDatabase().insertSokoOffice(listMovies, true);
        return listMovies;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rais, container, false);
        // Inflate the layout for this fragment



        Bundle bundle = getArguments();
        if(bundle !=null) {
            // Toast.makeText(getActivity(),"The selected page is: " + bundle.getInt("position"), Toast.LENGTH_LONG).show();
        }

        mTextError = (TextView) view.findViewById(R.id.textVolleyError);
        txtPosition = (TextView) view.findViewById(R.id.position);
        //   btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        circularProgress = (ProgressBar) view.findViewById(R.id.progressBar);


        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);


        //Swipe refresh
        //      swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);


        listSokoni = (RecyclerView) view.findViewById(R.id.listSokoni);
        listSokoni.setHasFixedSize(true);



        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        sLayoutManager = new LinearLayoutManager(getActivity());
        //   listSokoni.getAdapter().notifyDataSetChanged();

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
               new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
// Attach the layout manager to the recycler view
       listSokoni.setLayoutManager(gridLayoutManager);
    //   listSokoni.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterSoko = new AdapterSoko(getActivity());
        adapterSoko.setClickListener(this);
        listSokoni.setAdapter(adapterSoko);
        // listSearch = adapterSoko.setSokoList();
        if(savedInstanceState !=null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_SOKO);
            adapterSoko.setSokoList(listMovies);
        }
        else {
            sendJsonRequest();
            //  MyApplication.getWritableDatabase().getAllItemFromMarket();
            //     adapterSoko.notifyDataSetChanged();
        }

        if    (adapterSoko.getItemCount() == 0) {

            circularProgress.setVisibility(View.VISIBLE);

            //simulate doing something
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //    swipeRefreshLayout.setRefreshing(false);
                    circularProgress.setVisibility(View.GONE);
                }

            }, 10000);


        } else {

            circularProgress.setVisibility(View.GONE);
        }


        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        /**
         // TextFilter
         //     listSokoni.setTextFilterEnabled(true);
         //  swipeRefreshLayout.setOnRefreshListener(this);
         /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
      //  setHasOptionsMenu(true);

        return view;


    }



    @Override
    public void itemClicked(View view, int position) {
        // Toast.makeText(getActivity(), "Item Clicked at " + position, Toast.LENGTH_LONG).show();
        // result = String.valueOf(position);
        if(enterSearchZone != 1) {
            Intent startIntent = new Intent(getActivity(), ActivityRaisDetail.class);
            // positionSearch = getActivity().position;
            startIntent.putExtra(TAG_POSITION, position);
            //    startIntent.putExtra(TAG_POSITION2, positionSearch);
            startActivityForResult(startIntent, SHARING_CODE);

        }

    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener(){

        @Override
        public void onRefresh() {
            //textInfo.setText("WAIT: doing something");
            mTextError.setVisibility(View.GONE);
            sendJsonRequest();

            //simulate doing something
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    //  textInfo.setText("DONE");
                }

            }, 4000);
        }};



}