package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.chat.ChatPreviewViewHolder;
import com.fiuba.tdp.linkup.domain.ChatPreview;
import com.fiuba.tdp.linkup.services.UserManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class ActiveChatsFragment extends Fragment {

    private static final String TAG = "ActiveChatsFragment";
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;


    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<ChatPreview, ChatPreviewViewHolder> mFirebaseAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    private String mUsername;
    private String mUserId;
    private LinearLayout mEmptyListMessage;

    public ActiveChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_chats, container, false);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Log In activity
            // TODO: avisar que hubo un error, que se loguee devuelta
            return view;
        } else {
            mUsername = UserManager.getInstance().getMyUser().getName();
            mUserId = UserManager.getInstance().getMyUser().getId();
        }

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mEmptyListMessage = (LinearLayout) view.findViewById(R.id.emptyList);

        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.previewsRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        // New message child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        attachRecyclerViewAdapter();

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        return view;
    }

    private FirebaseRecyclerAdapter<ChatPreview, ChatPreviewViewHolder> getFirebaseAdapter() {
        return new FirebaseRecyclerAdapter<ChatPreview,
                ChatPreviewViewHolder>(
                ChatPreview.class,
                R.layout.item_chat_preview,
                ChatPreviewViewHolder.class,
                mFirebaseDatabaseReference.child("users/" + mUserId + "/conversations")) {

            @Override
            protected void populateViewHolder(final ChatPreviewViewHolder viewHolder,
                                              ChatPreview chatPreview, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.bind(chatPreview, mFirebaseDatabaseReference);
                Log.e(TAG, String.format("position: %d, Item count: %d", position, getItemCount()));
                if (getItemCount() - 1 == position) {
                    viewHolder.hideDelimiter();
                } else {
                    viewHolder.showDelimiter();
                }
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }

    private void attachRecyclerViewAdapter() {

        mFirebaseAdapter = getFirebaseAdapter();

//        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
//                int lastVisiblePosition =
//                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                // If the recycler view is initially being loaded or the
//                // user is at the bottom of the list, scroll to the bottom
//                // of the list to show the newly added message.
//                if (lastVisiblePosition == -1 ||
//                        (positionStart >= (friendlyMessageCount - 1) &&
//                                lastVisiblePosition == (positionStart - 1))) {
//                    mMessageRecyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
