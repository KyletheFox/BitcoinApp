package edu.temple.bitcoinapp;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment {

    RequestQueue queue;
    View v;
    TextView walletBalance;
    AutoCompleteTextView walletBalanceAddress;
    Button getBalanceFromText;
    ArrayList<String> savedAddresses;
    ArrayAdapter<String> adapter;
    SharedPreferences sharedPreferences;


    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.bitcoin_wallet_fragment, container, false);

        walletBalance = (TextView) v.findViewById(R.id.wallet_balance);
        getBalanceFromText = (Button) v.findViewById(R.id.get_balance_btn);
        walletBalanceAddress = (AutoCompleteTextView) v.findViewById(R.id.wallet_address_text);

        walletBalanceAddress.setThreshold(0);

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
        editor.putString("SavedAddress", json);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        savedAddresses = new ArrayList<>();

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        if (sharedPreferences.getString("SavedAddress", null) != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("SavedAddress", null);
            Log.d("json", json);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            savedAddresses = gson.fromJson(json, type);
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,
                    savedAddresses.toArray(new String[savedAddresses.size()]));
            walletBalanceAddress.setAdapter(adapter);
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

                if (sharedPreferences.getString("SavedAddress", null) != null) {
                    adapter.clear();
                    adapter.addAll(savedAddresses);
                }
            }
        });

    }


    public JsonObjectRequest getBalance(String address) {
        String updateUrl = getString(R.string.address_balance_url) + address;
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
}
