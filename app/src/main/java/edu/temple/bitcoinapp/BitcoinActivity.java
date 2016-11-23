package edu.temple.bitcoinapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * An activity representing a list of Bitcoins. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BitcoinActivity extends AppCompatActivity implements MainListFragment.ListInterface {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin);

        // See if the palette fragment has be init
        mTwoPane = (findViewById(R.id.bitcoin_detail_container) != null);

        // Get Fragment Manager
        fm = getFragmentManager();

        // Load The list fragment
        loadFragment(R.id.bitcoin_list_container, new MainListFragment(), false);

        if (mTwoPane) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            loadFragment(R.id.bitcoin_detail_container, new PriceFragment(), false);
            replaceFragment(R.id.bitcoin_list_container, new MainListFragment(), false);
        }
    }

    //  Load fragment in a specified frame
    private void loadFragment(int paneId, Fragment fragment, boolean placeOnBackstack){
        FragmentTransaction ft = fm.beginTransaction()
                .add(paneId, fragment);
        if (placeOnBackstack)
            ft.addToBackStack(null);
        ft.commit();

        //  Ensure fragment is attached before attempting to call its public methods
        fm.executePendingTransactions();
    }

    //  Load fragment in a specified frame
    private void replaceFragment(int paneId, Fragment fragment, boolean placeOnBackstack){
        FragmentTransaction ft = fm.beginTransaction()
                .replace(paneId, fragment);
        if (placeOnBackstack)
            ft.addToBackStack(null);
        ft.commit();

        //  Ensure fragment is attached before attempting to call its public methods
        fm.executePendingTransactions();
    }

    @Override
    public void clickedOption(String page) {

        if (!mTwoPane) {
            replaceFragment(R.id.bitcoin_list_container, getFragment(page), true);
        } else {
            replaceFragment(R.id.bitcoin_detail_container, getFragment(page), false);
        }
    }

    public Fragment getFragment(String page) {

        Resources res = getResources();

        if (res.getString(R.string.price_name) == page) {
            return new PriceFragment();
        } else if (res.getString(R.string.blockchain_name) == page) {
            return new BlockchainFragment();
        } else if (res.getString(R.string.wallet_name) == page) {
            return new WalletFragment();
        } else {
            // Shouldn't happen
            return null;
        }
    }
}
