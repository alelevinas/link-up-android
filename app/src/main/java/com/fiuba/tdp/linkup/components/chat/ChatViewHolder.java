package com.fiuba.tdp.linkup.components.chat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ChatMessage;
import com.fiuba.tdp.linkup.services.UserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alejandro on 9/29/17.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "ChatHolder";
    private final TextView mNameField;
    private final TextView mTextField;
    private final FrameLayout mLeftArrow;
    private final FrameLayout mRightArrow;
    private final ImageView mLeftStarred;
    private final ImageView mRightStarred;
    private final RelativeLayout mMessageContainer;
    private final LinearLayout mMessage;
    private final int mGreen300;
    private final int mGray300;

    public ChatViewHolder(View itemView) {
        super(itemView);
        mLeftStarred = (ImageView) itemView.findViewById(R.id.message_left_starred);
        mRightStarred = (ImageView) itemView.findViewById(R.id.message_right_starred);

        mNameField = (TextView) itemView.findViewById(R.id.name_text);
        mTextField = (TextView) itemView.findViewById(R.id.message_text);
        mLeftArrow = (FrameLayout) itemView.findViewById(R.id.left_arrow);
        mRightArrow = (FrameLayout) itemView.findViewById(R.id.right_arrow);
        mMessageContainer = (RelativeLayout) itemView.findViewById(R.id.message_container);
        mMessage = (LinearLayout) itemView.findViewById(R.id.message);
        mGreen300 = ContextCompat.getColor(itemView.getContext(), R.color.material_green_300);
        mGray300 = ContextCompat.getColor(itemView.getContext(), R.color.material_gray_300);
    }

    public void bind(final ChatMessage chat, final DatabaseReference chatRef) {
        setName(chat.getName());
        setText(chat.getMessage());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setIsSender(currentUser != null && chat.getUid().equals(UserManager.getInstance().getMyUser().getId()), chat);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  User can only like other user's messages.
                if (chat.getUid().compareTo(UserManager.getInstance().getMyUser().getId()) != 0) {
                    Log.e(TAG, "LIKING MESSAGE");
                    chat.setLiked(!chat.getLiked());
                    Map<String, Object> chatUpdate = new HashMap<String, Object>();
                    chatUpdate.put(chat.getMessageId() + "/liked", chat.getLiked());

                    chatRef.updateChildren(chatUpdate);
                }
            }
        });


    }

    private void setName(String name) {
        mNameField.setText(name);
    }

    private void setText(String text) {
        mTextField.setText(text);
    }

    private void setStarred(ImageView mStarred, Boolean isStarred) {
        if (mStarred == mLeftStarred && isStarred == false) {
            mStarred.setVisibility(View.INVISIBLE);
            return;
        } else {
            mStarred.setVisibility(View.VISIBLE);
        }
        String resourceId = "";
        if (isStarred) {
            resourceId = "@android:drawable/btn_star_big_on"; // where myResourceName is the name of your resource file, minus the file extension
        } else {
            resourceId = "@drawable/chat_icon_star_white"; // where myResourceName is the name of your resource file, minus the file extension
        }

        int imageResource = itemView.getResources().getIdentifier(resourceId, null, itemView.getContext().getPackageName());
        Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), imageResource); // For API 21+, gets a drawable styled for theme of passed Context

        mStarred.setImageDrawable(drawable);
    }

    private void setIsSender(boolean isSender, ChatMessage chat) {
        final int color;
        if (isSender) {
            color = mGreen300;
            mMessage.setBackgroundResource(R.drawable.ic_chat_message_background_owner);

            mLeftArrow.setVisibility(View.INVISIBLE);
            mRightArrow.setVisibility(View.VISIBLE);
            mMessageContainer.setGravity(Gravity.END);
            mRightStarred.setVisibility(View.GONE);
            setStarred(mLeftStarred, chat.getLiked());

            ViewGroup.MarginLayoutParams parameter = (RelativeLayout.LayoutParams) mMessage.getLayoutParams();
            parameter.setMargins(140, 0, 0, 0); // left, top, right, bottom
            mMessage.setLayoutParams(parameter);

        } else {
            color = mGray300;
            mLeftArrow.setVisibility(View.VISIBLE);
            mRightArrow.setVisibility(View.INVISIBLE);
            mMessageContainer.setGravity(Gravity.START);
            mLeftStarred.setVisibility(View.GONE);
            setStarred(mRightStarred, chat.getLiked());

            ViewGroup.MarginLayoutParams parameter = (RelativeLayout.LayoutParams) mMessage.getLayoutParams();
            parameter.setMargins(50, 0, 150, 0); // left, top, right, bottom
            mMessage.setLayoutParams(parameter);

        }

        ((GradientDrawable) mMessage.getBackground()).setColor(color);
        ((RotateDrawable) mLeftArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
        ((RotateDrawable) mRightArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
    }
}
