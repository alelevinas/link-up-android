package com.fiuba.tdp.linkup.components;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;
import com.fiuba.tdp.linkup.views.ChatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alejandro on 10/9/17.
 */

public class BlockDialog extends DialogFragment {

    final static String TAG = "BLOCK DIALOG";

    String otherUserId;

    String[] options;

    Boolean unblock = false;

    private DatabaseReference dbRef;

    private OnBlockDialogFragmentInteractionListener mListener;

    public BlockDialog() {
    }

    public BlockDialog setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
        return this;
    }

    public BlockDialog unblock() {
        this.unblock = true;
        return this;
    }

    public BlockDialog attach(OnBlockDialogFragmentInteractionListener listener) {
        mListener = listener;
        return this;
    }

    private String getConversationId() {
        return ChatActivity.getConversationId(UserManager.getInstance().getMyUser().getId(), otherUserId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        dbRef = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        options = getResources().getStringArray(R.array.report_options);

        final int otherReason = options.length - 1;

        builder.setTitle("¿Seguro quieres bloquear a este usuario?");

        if (unblock)
            builder.setTitle("¿Seguro quieres desbloquear a este usuario?");

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (unblock)
                    sendUnblock();
                else
                    sendBlock();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

        return builder.create();
    }

    private void sendBlock() {

        final Context context = getContext();

        new UserService(context).postBlockUser(UserManager.getInstance().getMyUser().getId(), otherUserId, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Toast.makeText(context, "El usuario ha sido bloqueado", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo bien");

                String convId = getConversationId();

                Map<String, Object> chatBlockedByMe = new HashMap<String, Object>();
                chatBlockedByMe.put("users/" + UserManager.getInstance().getMyUser().getId() + "/conversations/" + convId + "/blocked_by_me", true);

                dbRef.updateChildren(chatBlockedByMe);



                Map<String, Object> chatBlockedByOther = new HashMap<String, Object>();
                chatBlockedByOther.put("users/" + otherUserId + "/conversations/" + convId + "/blocked_by_other", true);

                dbRef.updateChildren(chatBlockedByOther);

                UserManager.getInstance().updateMyBlockedUsers(context);

                mListener.onBlockDialogFragmentInteraction(true);
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                Toast.makeText(context, "Nos hemos encontrado con un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo mal");

                mListener.onBlockDialogFragmentInteraction(false);
            }
        });
    }

    /*--------------------------------------- UNBLOCK --------------------------------------------*/

    private void sendUnblock() {
        final Context context = getContext();
        new UserService(context).postUnblockUser(UserManager.getInstance().getMyUser().getId(), otherUserId, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Toast.makeText(context, "El usuario ha sido DESbloqueado", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo bien");

                String convId = getConversationId();

                Map<String, Object> chatBlockedByMe = new HashMap<String, Object>();
                chatBlockedByMe.put("users/" + UserManager.getInstance().getMyUser().getId() + "/conversations/" + convId + "/blocked_by_me", false);

                dbRef.updateChildren(chatBlockedByMe);



                Map<String, Object> chatBlockedByOther = new HashMap<String, Object>();
                chatBlockedByOther.put("users/" + otherUserId + "/conversations/" + convId + "/blocked_by_other", false);

                dbRef.updateChildren(chatBlockedByOther);

                UserManager.getInstance().updateMyBlockedUsers(context);

                mListener.onBlockDialogFragmentInteraction(false);
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                Toast.makeText(context, "Nos hemos encontrado con un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo mal");

                mListener.onBlockDialogFragmentInteraction(true);
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBlockDialogFragmentInteractionListener {
        void onBlockDialogFragmentInteraction(Boolean isBlocked);
    }
}
