package edu.temple.bitcoinapp;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {

    RequestQueue queue;
    View v;
    TextView walletBalance;
    AutoCompleteTextView walletBalanceAddress;
    Button getBalanceFromText;
    Button qrCode;
    ArrayList<String> savedAddresses;
    ArrayAdapter<String> adapter, spinnerAdapter;
    SharedPreferences sharedPreferences;
    String preferenceName;
    Spinner spinner;
    BitcoinActivity activity;

    public final static int QR_KEY = 9585;


    public WalletFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.bitcoin_wallet_fragment, container, false);

        activity = (BitcoinActivity) getActivity();

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
            }
        }
        
        preferenceName = getString(R.string.prefernce_key);

        walletBalance = (TextView) v.findViewById(R.id.wallet_balance);
        getBalanceFromText = (Button) v.findViewById(R.id.get_balance_btn);
        walletBalanceAddress = (AutoCompleteTextView) v.findViewById(R.id.wallet_address_text);
        qrCode = (Button) v.findViewById(R.id.qr_btn);
        spinner = (Spinner) v.findViewById(R.id.address_spinner);

        walletBalanceAddress.setThreshold(0);

        walletBalanceAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    activity.hideKeyboard(v);
                }
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, QRScannerActivity.class);
                startActivityForResult(intent, QR_KEY);
            }
        });

        queue = Volley.newRequestQueue(v.getContext());

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(savedAddresses);
        Log.d("SavedAddress onPause", json);
        editor.putString(preferenceName, json);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        savedAddresses = new ArrayList<>();

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if (sharedPreferences.getString("SavedAddress", null) != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString(preferenceName, null);
            Log.d("json", json);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            savedAddresses = gson.fromJson(json, type);
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,
                    savedAddresses.toArray(new String[savedAddresses.size()]));
            spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,
                    savedAddresses.toArray(new String[savedAddresses.size()]));
            walletBalanceAddress.setAdapter(adapter);
            spinner.setAdapter(spinnerAdapter);
        } else {
            Log.d("SavedAddressOnResume", "empty");
        }

        getBalanceFromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = walletBalanceAddress.getText().toString();
                if (!savedAddresses.contains(address)) {
                    savedAddresses.add(address);
                }
                Log.d("Saved Addresses", savedAddresses.toString());;
                queue.add(getBalance(address));

                if (sharedPreferences.getString(preferenceName, null) != null) {
                    adapter.clear();
                    adapter.addAll(savedAddresses);
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                walletBalanceAddress.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public JsonObjectRequest getBalance(String address) {
        String updateUrl;
        if (address != null)
            updateUrl = getString(R.string.address_balance_url) + address;
        else
            updateUrl = getString(R.string.address_balance_url) + "last";

        return new JsonObjectRequest(Request.Method.GET, updateUrl, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    double balance = response.getJSONObject("data").getDouble("balance");
                    Log.d("Address Balance", String.valueOf(balance));
                    walletBalance.setText(String.valueOf(balance) + " BTC");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_KEY && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra("address");
            queue.add(getBalance(address));
            Log.d("QR Text", address);
        }
    }
}
