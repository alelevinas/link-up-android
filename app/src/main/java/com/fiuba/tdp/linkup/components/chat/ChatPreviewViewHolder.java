package com.fiuba.tdp.linkup.components.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ChatPreview;
import com.fiuba.tdp.linkup.util.DownloadImage;
import com.fiuba.tdp.linkup.views.ChatActivity;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by alejandro on 10/7/17.
 */

public class ChatPreviewViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "ChatPreviewViewHolder";
    private final TextView mNameField;
    private final TextView mPreviewField;
    private final ImageView mProfilePicture;

    public ChatPreviewViewHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.name_text);
        mPreviewField = (TextView) itemView.findViewById(R.id.preview_text);
        mProfilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
    }

    public void bind(final ChatPreview chat, final DatabaseReference chatRef) {
        setName(chat.getmName());
        setPreview(chat.getmLastMessage());
        setProfilePicture("");

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBundle = new Intent(itemView.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ChatActivity.CHAT_WITH_USER_ID, chat.getmOtherUserId());
                bundle.putString(ChatActivity.CHAT_WITH_USER_NAME, chat.getmName());
                intentBundle.putExtras(bundle);
                itemView.getContext().startActivity(intentBundle);
            }
        });
    }

    private void setProfilePicture(String s) {
        new DownloadImage(mProfilePicture).execute(s);
    }

    private void setName(String name) {
        mNameField.setText(name);
    }

    private void setPreview(String text) {
        mPreviewField.setText(text);
    }
}
