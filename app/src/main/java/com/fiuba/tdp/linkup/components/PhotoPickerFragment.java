package com.fiuba.tdp.linkup.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.fiuba.tdp.linkup.R;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link PhotoPickerFragment.OnPhotoPickerFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoPickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoPickerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "photoUrl";

    private String photoUrl;

    private ImageButton mAddImageButton;
    private ImageButton mRemoveImageButton;

    private OnPhotoPickerFragmentInteractionListener mListener;
    private int number;

    public PhotoPickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param photoUrl Parameter 1.
     * @return A new instance of fragment PhotoPickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoPickerFragment newInstance(String photoUrl) {
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, photoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            photoUrl = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_picker, container, false);

        //Find the image button
        mAddImageButton = (ImageButton) view.findViewById(R.id.photo);
        mAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromFacebook();
            }
        });

        //Find the image button
        mRemoveImageButton = (ImageButton) view.findViewById(R.id.removeButton);
        mRemoveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddImageButton.setImageBitmap(null);
                if (photoUrl != null) {
                    onImageRemoved(photoUrl);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setImage(String photoUrl) {
//        new DownloadImage(mAddImageButton).execute(photoUrl);

        Glide.with(this)
                .load(photoUrl)
                .into(mAddImageButton);


        this.photoUrl = photoUrl;
    }

    public void onImageChanged(String photoUrl) {
        if (mListener != null) {
            mListener.onImageChanged(number, photoUrl);
        }
    }

    public void onImageRemoved(String photoUrl) {
        if (mListener != null) {
            mListener.onImageRemoved(number, photoUrl);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPhotoPickerFragmentInteractionListener) {
            mListener = (OnPhotoPickerFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        photoUrl = null;
        mAddImageButton = null;
    }

    private void pickImageFromFacebook() {
        Intent main = new Intent(getActivity(), FacebookAlbumListActivity.class);
        startActivityForResult(main, 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String photoURL = data.getStringExtra(FacebookPhotoGridActivity.ARG_PHOTO_URL);
//            new DownloadImage(mAddImageButton).execute(photoURL);

            Glide.with(this)
                    .load(photoURL)
                    .into(mAddImageButton);

            onImageChanged(photoURL);
        }
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPhotoPickerFragmentInteractionListener {
        void onImageChanged(int number, String photoUrl);

        void onImageRemoved(int number, String photoUrl);
    }

}
