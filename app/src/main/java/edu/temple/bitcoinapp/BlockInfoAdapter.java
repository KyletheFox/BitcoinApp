package edu.temple.bitcoinapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kyleoneill on 11/23/16.
 */

public class BlockInfoAdapter extends BaseAdapter {

    private String[] blockMapKeys = {"nb","hash","version","nb_txs","next_block_nb",
            "prev_block_nb", "fee", "size", "difficulty"};
    private String[] blockMapValues = {"Block Number","Hash","Version", "Number of Transactions",
            "Next Block","Previous Block","Fee","Size","Difficulty"};
    private JSONObject json;
    private Activity activity;

    public BlockInfoAdapter(JSONObject json, Activity activity) {
        super();
        this.json = json;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return blockMapKeys.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        TextView tv = (TextView) inflater.inflate(R.layout.text_view, null);

        try {
            Log.d("Block Data", json.getString(blockMapKeys[position]));
            tv.setText(blockMapValues[position] + ": " + json.getString(blockMapKeys[position]));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tv;
    }
}
