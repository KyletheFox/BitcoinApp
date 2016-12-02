package edu.temple.bitcoinapp;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class PriceFragment extends Fragment {

    RequestQueue queue;
    TextView bitcoinPrice;
    ImageView chartImg;
    String chartRange;
    Button oneDay, fiveDay, oneMonth, sixMonth, oneYear, twoYear;
    Timer timer;

    public PriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bitcoin_price_fragment, container, false);

        // Get view references
        bitcoinPrice = (TextView) v.findViewById(R.id.bitcoin_price_cost);
        chartImg = (ImageView) v.findViewById(R.id.chart_img);
        oneDay = (Button) v.findViewById(R.id.one_day_btn);
        fiveDay = (Button) v.findViewById(R.id.five_day_btn);
        oneMonth = (Button) v.findViewById(R.id.one_month_btn);
        sixMonth = (Button) v.findViewById(R.id.six_month_btn);
        oneYear = (Button) v.findViewById(R.id.one_year_btn);
        twoYear = (Button) v.findViewById(R.id.two_year_btn);

        // Set Click Listeners for Buttons
        oneDay.setOnClickListener(createClickListener(getString(R.string.one_day)));
        fiveDay.setOnClickListener(createClickListener(getString(R.string.five_day)));
        oneMonth.setOnClickListener(createClickListener(getString(R.string.one_month)));
        sixMonth.setOnClickListener(createClickListener(getString(R.string.six_month)));
        oneYear.setOnClickListener(createClickListener(getString(R.string.one_year)));
        twoYear.setOnClickListener(createClickListener(getString(R.string.two_year)));

        // Set Default Chart range
        chartRange = getResources().getString(R.string.one_day);

        // Intitalize RequestQueue
        queue = Volley.newRequestQueue(v.getContext());

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                queue.add(updatePrice());
                queue.add(updateChart());
            }
        }, 0, 2000);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        timer.cancel();
    }

    public JsonObjectRequest updatePrice() {
        String updateUrl = getResources().getString(R.string.price_update_url);

        return new JsonObjectRequest(Request.Method.GET, updateUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject data;
                        double bitcoinRate;
                        NumberFormat nf = NumberFormat.getCurrencyInstance();
                        try {
                            data = response.getJSONObject(getString(R.string.currency_json));
                            bitcoinRate = data.getDouble(getString(R.string.rate_json));
                            bitcoinPrice.setText(nf.format(bitcoinRate));
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

    public ImageRequest updateChart() {
        String updateUrl = getResources().getString(R.string.chart_url);

        // Add date
        updateUrl += "&t=" + chartRange;
        Log.d("url", updateUrl);

        return new ImageRequest(updateUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                chartImg.setImageBitmap(response);
            }
        }, 0, 0, null, null);
    }

    public View.OnClickListener createClickListener(final String range) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartRange = range;
                queue.add(updateChart());
            }
        };
    }

}
