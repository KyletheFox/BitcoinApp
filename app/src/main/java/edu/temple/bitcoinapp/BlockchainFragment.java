package edu.temple.bitcoinapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlockchainFragment extends Fragment {

    RequestQueue queue;
    ListView listView;
    Button blockBtn;
    EditText blockEdit;
    BlockInfoAdapter adapter;

    public BlockchainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bitcoin_blockchain_fragment, container, false);

        listView = (ListView) v.findViewById(R.id.block_info_list);
        blockBtn = (Button) v.findViewById(R.id.block_btn);
        blockEdit = (EditText) v.findViewById(R.id.block_edit);

        queue = Volley.newRequestQueue(v.getContext());

        queue.add(getBlockData("last"));

        blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = blockEdit.getText().toString();
                queue.add(getBlockData(text));
            }
        });

        return v;
    }

    public JsonObjectRequest getBlockData(String block) {
        String updateUrl = getString(R.string.block_info_url) + block;
        return new JsonObjectRequest(Request.Method.GET, updateUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            adapter = new BlockInfoAdapter(data, getActivity());
                            listView.setAdapter(adapter);
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
