package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.testing;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Date;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

/**
 * Created by muchbeer on 12/7/2016.
 */
public class ShoppingListsFragment extends Fragment {

    private ListView mListView;
    private TextView mTextViewListName, mTextViewListOwner;
    private TextView mTextViewEditTime;
    private TextView mTextViewMkoa;

    public ShoppingListsFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static ShoppingListsFragment newInstance() {
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /**
         * Initialize UI elements
         */
        View rootView = inflater.inflate(R.layout.test_fragment_shopping_lists, container, false);


        initializeScreen(rootView);

        /**
         * Create Firebase references
         */
        Firebase refListName = new Firebase(Constants.FIREBASE_URL).child("activeList");

        /**
         * Add ValueEventListeners to Firebase references
         * to control get data and control behavior and visibility of elements
         */

        refListName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can use getValue to deserialize the data at dataSnapshot
                // into a ShoppingList.
                TestingList shoppingList = dataSnapshot.getValue(TestingList.class);

                // If there was no data at the location we added the listener, then
                // shoppingList will be null.
                if (shoppingList != null) {
                    // If there was data, take the TextViews and set the appropriate values.
                    mTextViewListName.setText(shoppingList.getName());
                    mTextViewListOwner.setText(shoppingList.getAddress());
                    if (shoppingList.getTimestampLastChanged() != null) {
                        mTextViewEditTime.setText(
                                Utils.SIMPLE_DATE_FORMAT.format(
                                        new Date(shoppingList.getTimestampLastChangedLong())));
                    } else {
                        mTextViewEditTime.setText("");
                    }
                    if (shoppingList.getDistrictz() != null) {
                        mTextViewMkoa.setText(shoppingList.getDistrictzLong());
                               /* Utils.SIMPLE_DATE_FORMAT.format(
                                        new Date(shoppingList.getTimestampLastChangedLong())));*/
                    } else {
                        mTextViewMkoa.setText("Namad");
                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        /**
         * Set interactive bits, such as click events and adapters
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_lists);
        // Get the TextViews in the single_active_list layout for list name, edit time and owner
        mTextViewListName = (TextView) rootView.findViewById(R.id.text_view_list_name);
        mTextViewListOwner = (TextView) rootView.findViewById(R.id.created_by_user);
        mTextViewEditTime = (TextView) rootView.findViewById(R.id.text_view_edit_time);

        mTextViewMkoa = (TextView) rootView.findViewById(R.id.text_view_mkoa);
    }
}
