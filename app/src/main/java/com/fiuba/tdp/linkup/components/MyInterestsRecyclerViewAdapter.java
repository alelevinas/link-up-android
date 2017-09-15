package com.fiuba.tdp.linkup.components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.components.InterestsFragment.OnListFragmentInteractionListener;
import com.fiuba.tdp.linkup.domain.LinkUpUser;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link LinkUpUser.LinkUpLike} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyInterestsRecyclerViewAdapter extends RecyclerView.Adapter<MyInterestsRecyclerViewAdapter.ViewHolder> {

    private final List<LinkUpUser.LinkUpLike> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyInterestsRecyclerViewAdapter(List<LinkUpUser.LinkUpLike> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_interests_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public LinkUpUser.LinkUpLike mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
