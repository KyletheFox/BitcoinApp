package edu.temple.bitcoinapp;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends Fragment {

    LinearLayout priceLayout, blockLayout, walletLayout;
    ListInterface listInterface;

    public MainListFragment() {
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
        View v = inflater.inflate(R.layout.bitcoin_list_fragment, container, false);

        Resources res = getResources();

        // Get Layouts
        priceLayout = (LinearLayout) v.findViewById(R.id.price_layout);
        walletLayout = (LinearLayout) v.findViewById(R.id.wallet_layout);
        blockLayout = (LinearLayout) v.findViewById(R.id.blockchain_layout);

        // Set onClickListeners
        priceLayout.setOnClickListener(clickListenerFactory(res.getString(R.string.price_name)));
        walletLayout.setOnClickListener(clickListenerFactory(res.getString(R.string.wallet_name)));
        blockLayout.setOnClickListener(clickListenerFactory(res.getString(R.string.blockchain_name)));

        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listInterface = (ListInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ListInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listInterface = null;
    }

    public View.OnClickListener clickListenerFactory(final String type) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listInterface.clickedOption(type);
            }
        };
    }

    public interface ListInterface {
        void clickedOption(String page);
    }

}
