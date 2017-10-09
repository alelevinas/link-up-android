package com.fiuba.tdp.linkup.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alejandro on 10/9/17.
 */

public class TextInputDialog extends DialogFragment {
    private static final String TAG = "TEXT INPUT DIALOG";
    private String otherUserId;

    public DialogFragment setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        final View viewInflated = inflater.inflate(R.layout.dialog_text_input, null);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewInflated)
                // Add action buttons
                .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendReport(input.getText().toString());
                    }
                });

        return builder.create();

    }

    private void sendReport(String reportReason) {

        final Context context = getContext();

        new UserService(context).postReportUser(otherUserId, reportReason, new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                Toast.makeText(context, "Gracias, nuestros moderadores ya están analizando el caso", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo bien");
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                Toast.makeText(context, "Nos hemos encontrado con un error, por favor intente más tarde", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "todo mal");
            }
        });
    }
}