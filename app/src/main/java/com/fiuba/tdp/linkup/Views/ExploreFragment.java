package com.fiuba.tdp.linkup.Views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;

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
            public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
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

                ImageButton favoriteImageButton =
                        (ImageButton) itemView.findViewById(R.id.favorite_button);
                favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Le pusiste un like a "+name.getText().toString(),
                                Snackbar.LENGTH_LONG).show();
                    }
                });

                ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
                shareImageButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Le pusiste un SUPER like a "+name.getText().toString(),
                                Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }

        /**
         * Adapter to display recycler view.
         */
        public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
            // Set numbers of Card in RecyclerView.
            private static final int LENGTH = 1;

            //private final String[] mUsers;
            //private final String[] mUsersDesc;
            private final Drawable[] mUsersPictures;

            public ContentAdapter(Context context) {
                mUsersPictures = new Drawable[1];
                mUsersPictures[0] = ContextCompat.getDrawable(context, R.drawable.a);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                holder.picture.setImageDrawable(mUsersPictures[0]);
                holder.name.setText("Sabrina");
                holder.description.setText("Me gusta hacer piruetas, soy muy piola, y me gustaria conocer chicoss!! Hablame!");
            }

            @Override
            public int getItemCount() {
                return LENGTH;
            }
        }
}
