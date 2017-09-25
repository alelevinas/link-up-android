package com.fiuba.tdp.linkup.components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.NewMatchFragment.OnNewMatchListFragmentInteractionListener;
import com.fiuba.tdp.linkup.domain.LinkUpMatch;
import com.fiuba.tdp.linkup.util.DownloadImage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.fiuba.tdp.linkup.domain.LinkUpMatch} and makes a call to the
 * specified {@link OnNewMatchListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNewMatchRecyclerViewAdapter extends RecyclerView.Adapter<MyNewMatchRecyclerViewAdapter.ViewHolder> {

    private final List<LinkUpMatch> mValues;
    private final OnNewMatchListFragmentInteractionListener mListener;

    public MyNewMatchRecyclerViewAdapter(List<LinkUpMatch> items, OnNewMatchListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_newmatch_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        new DownloadImage(holder.mImageView).execute(holder.mItem.getPicture());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView mImageView;
        public LinkUpMatch mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (CircleImageView) view.findViewById(R.id.profile_picture);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getName() + "'";
        }
    }
}
