package com.fiuba.tdp.linkup.views;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.fiuba.tdp.linkup.R;
import com.fiuba.tdp.linkup.domain.ServerResponse;
import com.fiuba.tdp.linkup.services.UserManager;
import com.fiuba.tdp.linkup.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PremiumPaymentFragment extends Fragment {

    public static final String TAG = "PREMIUM PAY";
    Spinner card_type;
    EditText card_nmbr;
    EditText card_name;
    EditText card_date;
    EditText card_cvc;
    Button pay_button;

    String type;

    private OnPremiumPaymentFragmentInteractionListener mListener;

    public PremiumPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_premium_payment, container, false);

        card_type = (Spinner) v.findViewById(R.id.cards_spinner);
        card_nmbr = (EditText) v.findViewById(R.id.card_nmbr);
        card_name = (EditText) v.findViewById(R.id.card_name);
        card_date = (EditText) v.findViewById(R.id.card_date);
        card_cvc = (EditText) v.findViewById(R.id.card_cvc);
        pay_button = (Button) v.findViewById(R.id.button_pay);


        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCardInput()) {
                    upgradeToPremium();
                }
            }
        });


        setUpSpinner();


        return v;
    }

    private void upgradeToPremium() {
        Log.d(TAG, "Upgrading to PREMIUM");
        new UserService(getContext()).postUpgradeToPremium(new Callback<ServerResponse<String>>() {
            @Override
            public void onResponse(Call<ServerResponse<String>> call, Response<ServerResponse<String>> response) {
                showAlert("¡El pago se ha procesado con éxito!", "");
                UserManager.getInstance().getMyUser().setPremium(true);
            }

            @Override
            public void onFailure(Call<ServerResponse<String>> call, Throwable t) {
                showAlert("Nos hemos encontrado con un error", "Por favor intenta más tarde");
            }
        });
    }

    private boolean checkCardInput() {
        if (card_nmbr.getText().toString().length() < 16) {
            showAlert("Atención", "Ingrese los 16 dígitos de la tarjeta sin espacios");
            return false;
        }
        if (card_name.getText().toString().length() < 3) {
            showAlert("Atención", "Ingrese su nombre completo");
            return false;
        }
        if (card_date.getText().toString().length() < 5) {
            showAlert("Atención", "Ingrese la fecha de vencimiento de la forma MM/YY");
            return false;
        }
        if (card_cvc.getText().toString().length() < 3) {
            showAlert("Atención", "Ingrese los 3 números al dorso de la tarjeta");
            return false;
        }
        return true;
    }

    private void setUpSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.credit_cards, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        card_type.setAdapter(adapter);

        card_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, (String) adapterView.getItemAtPosition(i));
                type = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type = "";
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPremiumPaymentFragmentInteractionListener) {
            mListener = (OnPremiumPaymentFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPremiumPaymentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showAlert(final String title, String message) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
                .setTitle(title);

        // 3. Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                if (title.compareTo("¡El pago se ha procesado con éxito!") == 0) {
                    mListener.onPremiumPaymentFragmentInteraction();
                }
            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public interface OnPremiumPaymentFragmentInteractionListener {
        void onPremiumPaymentFragmentInteraction();
    }
}
