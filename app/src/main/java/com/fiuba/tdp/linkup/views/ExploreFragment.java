package com.fiuba.tdp.linkup.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.domain.UserAround;
import com.fiuba.tdp.linkup.domain.UsersAround;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.util.DownloadImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;

public class ExploreFragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                    R.layout.recycler_view, container, false);
            ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView picture;
            public TextView name;
            public TextView description;
            boolean favoriteImageButtonChecked = false;
            boolean superLikeImageButtonChecked = false;
            public ViewHolder(LayoutInflater inflater, final ViewGroup parent) {
                super(inflater.inflate(R.layout.item_card, parent, false));
                picture = (ImageView) itemView.findViewById(R.id.card_image);
                name = (TextView) itemView.findViewById(R.id.card_title);
                description = (TextView) itemView.findViewById(R.id.card_text);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Snackbar.make(v, "Ver Perfil de "+name.getText().toString(),
                                Snackbar.LENGTH_LONG).show();
                    }
                });

                // Adding Snackbar to Action Button inside card
                Button button = (Button)itemView.findViewById(R.id.action_button);
                button.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {
                        Snackbar.make(v, "Enviar mensaje a "+name.getText().toString(),
                                Snackbar.LENGTH_LONG).show();
                    }
                });

                ImageButton closeImageButton =
                        (ImageButton) itemView.findViewById(R.id.closeButton);
                closeImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Eliminaste de tu listado a "+name.getText().toString(),
                                Snackbar.LENGTH_LONG).show();
                    }
                });

                final ImageButton favoriteImageButton =
                        (ImageButton) itemView.findViewById(R.id.favorite_button);
                favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Le pusiste un like a "+name.getText().toString(),
                                Snackbar.LENGTH_LONG).show();
                        if(favoriteImageButtonChecked)
                            favoriteImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), R.color.button_grey));
                        else
                            favoriteImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), holo_red_light));
                        favoriteImageButtonChecked = !favoriteImageButtonChecked;
                    }
                });

                final ImageButton superLikeImageButton = (ImageButton) itemView.findViewById(R.id.superlike_button);
                superLikeImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Le pusiste un SUPER like a "+name.getText().toString(),
                                Snackbar.LENGTH_LONG).show();
                        if(superLikeImageButtonChecked)
                            superLikeImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), R.color.button_grey));
                        else
                            superLikeImageButton.setImageTintList(ContextCompat.getColorStateList(parent.getContext(), holo_orange_light));
                        superLikeImageButtonChecked = !superLikeImageButtonChecked;
                    }
                });
            }
        }

        public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
            private static int LENGTH = 0;

            private final Drawable[] mUsersPictures;
            private UserService userService;
            private static UsersAround usersAround;

            public ContentAdapter(Context context) {
                userService = new UserService();

                userService.getUsersCompatible(Profile.getCurrentProfile().getId(), new Callback<ServerResponse<ArrayList<UserAround>>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<ArrayList<UserAround>>> call, Response<ServerResponse<ArrayList<UserAround>>> response) {
                        usersAround = new UsersAround(response.body().data);
                        LENGTH = usersAround.getSize();
                    }

                    @Override
                    public void onFailure(Call<ServerResponse<ArrayList<UserAround>>> call, Throwable t) {
                        Log.w("ERROR", "Error getting users around!");
                    }
                });
                mUsersPictures = new Drawable[1];
                mUsersPictures[0] = ContextCompat.getDrawable(context, R.drawable.a);
                notifyDataSetChanged();
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                new DownloadImage(holder.picture).execute(usersAround.getPictures().get(position));
                holder.name.setText(usersAround.getNames().get(position));
                holder.description.setText(usersAround.getDescriptions().get(position));
            }

            @Override
            public int getItemCount() {
                return LENGTH;
            }
        }
}

