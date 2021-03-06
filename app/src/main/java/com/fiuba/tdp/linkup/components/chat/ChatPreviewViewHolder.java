package com.fiuba.tdp.linkup.components.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ChatPreview;
import com.fiuba.tdp.linkup.domain.LinkUpUser;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.views.ChatActivity;
import com.google.firebase.database.DatabaseReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alejandro on 10/7/17.
 */

public class ChatPreviewViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "ChatPreviewViewHolder";
    private final TextView mNameField;
    private final TextView mPreviewField;
    private final ImageView mProfilePicture;
    private final ImageView mNotification;

    public ChatPreviewViewHolder(View itemView) {
        super(itemView);
        mNameField = (TextView) itemView.findViewById(R.id.name_text);
        mPreviewField = (TextView) itemView.findViewById(R.id.preview_text);
        mProfilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
        mNotification= (ImageView) itemView.findViewById(R.id.notification);
    }

    public void bind(final ChatPreview chat, final DatabaseReference chatRef) {
        setName(chat.getName());
        setPreview(chat.getLastMessage());
        setNotification(chat.getIsRead());

        new UserService(itemView.getContext()).getUser(chat.getOtherUserId(), new Callback<ServerResponse<LinkUpUser>>() {
            @Override
            public void onResponse(Call<ServerResponse<LinkUpUser>> call, Response<ServerResponse<LinkUpUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setProfilePicture(response.body().data.getPicture());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<LinkUpUser>> call, Throwable t) {

            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBundle = new Intent(itemView.getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ChatActivity.CHAT_WITH_USER_ID, chat.getOtherUserId());
                bundle.putString(ChatActivity.CHAT_WITH_USER_NAME, chat.getName());
                bundle.putString(ChatActivity.LAST_CHAT_ID, chat.getMessageId());
                bundle.putString(ChatActivity.LAST_CHAT_TEXT, chat.getLastMessage());
                bundle.putBoolean(ChatActivity.LAST_CHAT_BLOCKED, chat.getBlocked_by_me() || chat.getBlocked_by_other());
                intentBundle.putExtras(bundle);
                itemView.getContext().startActivity(intentBundle);
            }
        });
    }

    private void setNotification(Boolean isRead) {
        if (!isRead) {
            mNotification.setVisibility(View.VISIBLE);
        } else {
            mNotification.setVisibility(View.GONE);
        }
    }

    private void setProfilePicture(String s) {
        //new DownloadImage(mProfilePicture).execute(s);
        Glide.with(itemView.getContext())
                .load(s)
                .into(mProfilePicture);
    }

    private void setName(String name) {
        mNameField.setText(name);
    }

    private void setPreview(String text) {
        mPreviewField.setText(text);
    }

    public void hideDelimiter() {
        itemView.findViewById(R.id.chat_preview_delimiter).setVisibility(View.GONE);
    }

    public void showDelimiter() {
        itemView.findViewById(R.id.chat_preview_delimiter).setVisibility(View.VISIBLE);
    }
}
