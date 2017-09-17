package com.fiuba.tdp.linkup.components;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;

/**
 * Created by alejandro on 9/16/17.
 */

public class ExploreUserViewHolder extends RecyclerView.ViewHolder {

    private final Button sendMessageButton;
    private final ImageButton closeImageButton;
    private final ImageButton favoriteImageButton;
    private final ImageButton superLikeImageButton;
    public String userId;
    public ImageView picture;
    public TextView name;
    public TextView description;
    boolean favoriteImageButtonChecked = false;
    boolean superLikeImageButtonChecked = false;
    private ViewGroup parent;


    public ExploreUserViewHolder(LayoutInflater inflater, final ViewGroup parent) {
        super(inflater.inflate(R.layout.item_card, parent, false));

        this.parent = parent;

        picture = (ImageView) itemView.findViewById(R.id.card_image);
        name = (TextView) itemView.findViewById(R.id.card_title);
        description = (TextView) itemView.findViewById(R.id.card_text);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Snackbar.make(v, "Ver Perfil de " + name.getText().toString(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        // Adding Snackbar to Action Button inside card
        sendMessageButton = (Button) itemView.findViewById(R.id.message_button);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Snackbar.make(v, "Enviar mensaje a " + name.getText().toString(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        closeImageButton = (ImageButton) itemView.findViewById(R.id.closeButton);
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Eliminaste de tu listado a " + name.getText().toString(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        favoriteImageButton = (ImageButton) itemView.findViewById(R.id.favorite_button);
        favoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressLikeButton();
            }
        });


        superLikeImageButton = (ImageButton) itemView.findViewById(R.id.superlike_button);
        superLikeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Le pusiste un SUPER like a " + name.getText().toString(),
                        Snackbar.LENGTH_LONG).show();
                if (superLikeImageButtonChecked) {
                    superLikeImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), R.color.button_grey));
                } else {
                    superLikeImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), holo_orange_light));
                }
                superLikeImageButtonChecked = !superLikeImageButtonChecked;
            }
        });
    }

    private void pressLikeButton() {
        if (favoriteImageButtonChecked) {
            favoriteImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), R.color.button_grey));
        } else {
            favoriteImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), holo_red_light));

            new UserService().postLikeToUser(userId, new Callback<ServerResponse<String>>() {
                String LOG_LIKE = "LIKE USER";

                @Override
                public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                    Log.d(LOG_LIKE, "message = " + response.message());
                    if (response.isSuccessful()) {
                        Log.d(LOG_LIKE, "-----isSuccess----");
                    } else {
                        Log.d(LOG_LIKE, "-----isFalse-----");
                        this.onFailure(call, null);
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                    favoriteImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), R.color.button_grey));
                    favoriteImageButtonChecked = !favoriteImageButtonChecked;
                }
            });
        }
        favoriteImageButtonChecked = !favoriteImageButtonChecked;
    }
}
