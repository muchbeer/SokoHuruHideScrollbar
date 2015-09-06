package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.chat;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

import sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R;

/**
 * Created by muchbeer on 8/29/2015.
 */
public class FragmentChat extends ListFragment{

    // TODO: change this to your own Firebase URL
    private static final String FIREBASE_URL = "https://ukawatz.firebaseio.com";

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        // Make sure we have a mUsername
        setupUsername();

        getActivity().setTitle(mUsername);

        // Setup our Firebase mFirebaseRef
        mFirebaseRef = new Firebase(FIREBASE_URL).child("Sokoni");

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) view.findViewById(sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        view.findViewById(sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), getActivity(), sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(getActivity(), "Umeunganishwa na wenzako", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Tafadhari subiri kidogo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        SharedPreferences prefs = getActivity().getSharedPreferences("ChatPrefs", 0);
        mUsername = prefs.getString("username", null);
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            mUsername = "Ukawa" + r.nextInt(100000);
            prefs.edit().putString("username", mUsername).commit();
        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) getActivity().findViewById(sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }
}
