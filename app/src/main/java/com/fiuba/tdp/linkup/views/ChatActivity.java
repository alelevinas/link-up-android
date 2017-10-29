package com.fiuba.tdp.linkup.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.BlockDialog;
import com.fiuba.tdp.linkup.components.ReportDialog;
import com.fiuba.tdp.linkup.components.chat.ChatViewHolder;
import com.fiuba.tdp.linkup.domain.ChatMessage;
import com.fiuba.tdp.linkup.domain.ChatPreview;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, BlockDialog.OnBlockDialogFragmentInteractionListener {
    public static final String CHATS_CHILD = "chats";
    public static final String MESSAGES_CHILD = "messages";
    public static final String LAST_CHAT_ID = "LAST_CHAT_ID";
    private static final String TAG = "ChatActivity";
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    public static final String LAST_CHAT_TEXT = "LAST_CHAT_TEXT";
    public static final String LAST_CHAT_BLOCKED = "LAST_CHAT_BLOCKED";
    public static String CHAT_WITH_USER_ID = "CHAT_WITH_USER_ID";
    public static String CHAT_WITH_USER_NAME = "CHAT_WITH_USER_NAME";
    private String otherUserId;
    private String otherUserName;
    private String myUserId;
    private String conversationId;
    private String firebaseChatLocation;

    private LinkUpUser otherUser;

    private String mUsername;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;

    private LinearLayout mEmptyListMessage;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>
            mFirebaseAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String myUserLastChatReference;
    private String otherUserLastChatReference;
    private Toolbar toolbar;
    private String lastChatPreviewId;
    private String lastChatText;
    private boolean lastChatBlocked;
    private LinearLayout mBlockedConversation;
    private Menu menu;
    private TextView mBlockedConversationMessage;

    public static String getConversationId(String id, String otherUserId) {
        if (otherUserId.compareTo(id) < 0) {
            return otherUserId + "_" + id;
        } else {
            return id + "_" + otherUserId;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getOtherUserId();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Log In activity
            // TODO: avisar que hubo un error, que se loguee devuelta
            startActivity(new Intent(this, LogInActivity.class));
            finish();
            return;
        } else {
//            mUsername = mFirebaseUser.getDisplayName();
            mUsername = UserManager.getInstance().getMyUser().getName();
        }

        // Initialize ProgressBar and RecyclerView.
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mEmptyListMessage = (LinearLayout) findViewById(R.id.emptyChat);
        mBlockedConversation = (LinearLayout) findViewById(R.id.blockedChat);
        mBlockedConversationMessage = (TextView) findViewById(R.id.blockedChatMessage);

        // New message child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        attachRecyclerViewAdapter();

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = (Button) findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Conversation data
                DatabaseReference newChatRef = mFirebaseDatabaseReference.child(firebaseChatLocation).push();
                String messageId = newChatRef.getKey();

                ChatMessage chatMessage = new ChatMessage(messageId, UserManager.getInstance().getMyUser().getId(), mUsername, mMessageEditText.getText().toString(), false);
                newChatRef.setValue(chatMessage);

                //my chats and last message data
                DatabaseReference myLastChatRef = mFirebaseDatabaseReference.child(myUserLastChatReference);
                HashMap<String, Object> lastChat = new HashMap<String, Object>();
                lastChat.put("name", otherUserName);
                lastChat.put("lastMessage", mMessageEditText.getText().toString());
                lastChat.put("otherUserId", otherUserId);
                lastChat.put("conversationId", messageId);
                lastChat.put("isRead", true);

                myLastChatRef.setValue(lastChat);

                //OTHER chats and last message data
                DatabaseReference otherLastChatRef = mFirebaseDatabaseReference.child(otherUserLastChatReference);
                HashMap<String, Object> otherLastChat = new HashMap<String, Object>();
                otherLastChat.put("name", UserManager.getInstance().getMyUser().getName());
                otherLastChat.put("lastMessage", mMessageEditText.getText().toString());
                otherLastChat.put("otherUserId", myUserId);
                otherLastChat.put("conversationId", messageId);
                otherLastChat.put("isRead", false);

                otherLastChatRef.setValue(otherLastChat);


                mMessageEditText.setText("");
            }
        });

        checkIfConversationBlocked();
    }

    private void checkIfOtherUserIsDisabled() {
        if (otherUser.isDisable()) {
            mBlockedConversation.setVisibility(View.VISIBLE);
            mBlockedConversationMessage.setText(R.string.blocked_by_admin_msg);
            mMessageEditText.setEnabled(false);
            mSendButton.setEnabled(false);
            mMessageRecyclerView.setEnabled(false);
        }
    }

    private void checkIfConversationBlocked() {
        DatabaseReference myLastChatRef = mFirebaseDatabaseReference.child(myUserLastChatReference);

        myLastChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatPreview preview = dataSnapshot.getValue(ChatPreview.class);
                if (preview == null)
                    return;
                if (mBlockedConversation.getVisibility() == View.VISIBLE) {
                    // ya esta bloqueada la conv
                    return;
                }
                if (preview.getBlocked_by_me() || preview.getBlocked_by_other()) {
                    mBlockedConversation.setVisibility(View.VISIBLE);
                    mBlockedConversationMessage.setText(R.string.blocked_conv_msg);
                    mMessageEditText.setEnabled(false);
                    mSendButton.setEnabled(false);
                } else {
                    mBlockedConversation.setVisibility(View.GONE);
                    mMessageEditText.setEnabled(true);
                    mSendButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void markConversationRead() {
        // mark last message as read
        //my chats and last message data
        DatabaseReference myLastChatRef = mFirebaseDatabaseReference.child(myUserLastChatReference);
        Map<String, Object> isReadUpdate = new HashMap<String, Object>();
//        isReadUpdate.put("/name", otherUserName);
//        isReadUpdate.put("/lastMessage", lastChatText);
//        isReadUpdate.put("/otherUserId", otherUserId);
//        isReadUpdate.put("/conversationId", lastChatPreviewId);
        isReadUpdate.put("/isRead", true);

        myLastChatRef.updateChildren(isReadUpdate);
    }

    private void getOtherUserId() {
        Intent intentExtras = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();
        if (!extrasBundle.isEmpty()) {
            otherUserId = extrasBundle.getString(CHAT_WITH_USER_ID);
            otherUserName = extrasBundle.getString(CHAT_WITH_USER_NAME);
            lastChatPreviewId = extrasBundle.getString(LAST_CHAT_ID);
            lastChatText = extrasBundle.getString(LAST_CHAT_TEXT);
            lastChatBlocked = extrasBundle.getBoolean(LAST_CHAT_BLOCKED);
            myUserId = UserManager.getInstance().getMyUser().getId();

            getOtherUser();

            conversationId = getConversationId(myUserId, otherUserId);
            /*if (otherUserId.compareTo(myUserId) < 0) {
                conversationId = otherUserId + "_" + myUserId;
            } else {
                conversationId = myUserId + "_" + otherUserId;
            }
*/
            firebaseChatLocation = CHATS_CHILD + "/" + conversationId + "/" + MESSAGES_CHILD;

            myUserLastChatReference = "users/" + myUserId + "/conversations/" + conversationId;
            otherUserLastChatReference = "users/" + otherUserId + "/conversations/" + conversationId;


        } else {
            otherUserId = "";
            otherUserName = "Error";
            myUserId = "";
            finish();
            // hubo un error, ir para atras
        }
    }

    private void getOtherUser() {
        new UserService(this).getUser(otherUserId, new Callback<ServerResponse<LinkUpUser>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    otherUser = response.body().data;
                    setUpToolbarTitle();
                    checkIfOtherUserIsDisabled();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {

            }
        });

        LinearLayout linearLayout = (LinearLayout) toolbar.findViewById(R.id.toolbar_action);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                Intent intent = new Intent(view.getContext(), OtherProfileActivity.class);
                intent.putExtra(OtherProfileActivity.ID_USER, Long.parseLong(otherUserId));
                view.getContext().startActivity(intent);
            }
        });

    }

    private void setUpToolbarTitle() {
        ((TextView) toolbar.findViewById(R.id.other_title_name)).setText(otherUserName);

        final ImageView profileImage = (ImageView) toolbar.findViewById(R.id.other_profile_picture);

        Glide.with(getBaseContext())
                .load(otherUser.getPicture())
                .into(profileImage);
    }

    private FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> getFirebaseAdapter() {
        return new FirebaseRecyclerAdapter<ChatMessage,
                ChatViewHolder>(
                ChatMessage.class,
                R.layout.item_message,
                ChatViewHolder.class,
                mFirebaseDatabaseReference.child(firebaseChatLocation)) {

            @Override
            protected void populateViewHolder(final ChatViewHolder viewHolder,
                                              ChatMessage chatMessage, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.bind(chatMessage, mFirebaseDatabaseReference.child(firebaseChatLocation));
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);

                if (mSendButton.isShown()) {
                    markConversationRead();
                }
            }
        };
    }

    private void attachRecyclerViewAdapter() {

        mFirebaseAdapter = getFirebaseAdapter();

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        this.menu = menu;
        onBlockDialogFragmentInteraction(UserManager.getInstance().isBlocked(otherUserId));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.block:
                if (item.getTitle().equals("Bloquear"))
                    new BlockDialog().setOtherUserId(otherUserId).attach(this).show(getFragmentManager().beginTransaction(), "bloquear");
                else
                    new BlockDialog().setOtherUserId(otherUserId).unblock().attach(this).show(getFragmentManager().beginTransaction(), "desbloquear");
                return true;

            case R.id.report:
                new ReportDialog().setOtherUserId(otherUserId).show(getFragmentManager().beginTransaction(), "denunciar");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlockDialogFragmentInteraction(Boolean isBlocked) {
        MenuItem blockMenuItem = menu.findItem(R.id.block);
        if (isBlocked) {
            // cambiar el menu a Desbloquear
            blockMenuItem.setTitle("Desbloquear");
        } else {
            // pooner el menu en Bloquear
            blockMenuItem.setTitle("Bloquear");
        }
    }
}
