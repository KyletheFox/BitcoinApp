package edu.temple.bitcoinapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * An activity representing a list of Bitcoins. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NOTUSED_BitcoinDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BitcoinActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setActionBar(toolbar);
//        toolbar.setTitle(getTitle());

        // See if the palette fragment has be init
        mTwoPane = (findViewById(R.id.bitcoin_detail_container) != null);

        loadFragment(R.id.bitcoin_list_container, new BitcoinListFragment(), false);

        if (mTwoPane) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.

            loadFragment(R.id.bitcoin_detail_container, new BitcoinDetailFragment(), false);
        }
    }

    //  Load fragment in a specified frame
    private void loadFragment(int paneId, Fragment fragment, boolean placeOnBackstack){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction()
                .add(paneId, fragment);
        if (placeOnBackstack)
            ft.addToBackStack(null);
        ft.commit();

        //  Ensure fragment is attachecd before attempting to call its public methods
        fm.executePendingTransactions();
    }

//    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
//    }
//
//    public class SimpleItemRecyclerViewAdapter
//            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
//
//        private final List<DummyContent.DummyItem> mValues;
//
//        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
//            mValues = items;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.notused_bitcoin_list_content, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, int position) {
//            holder.mItem = mValues.get(position);
//            //holder.mIdView.setText(mValues.get(position).id);
//            holder.mContentView.setText(mValues.get(position).content);
//
//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putString(BitcoinDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                        BitcoinDetailFragment fragment = new BitcoinDetailFragment();
//                        fragment.setArguments(arguments);
////                        getSupportFragmentManager().beginTransaction()
////                                .replace(R.id.bitcoin_detail_container, fragment)
////                                .commit();
//                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, NOTUSED_BitcoinDetailActivity.class);
//                        intent.putExtra(BitcoinDetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                        context.startActivity(intent);
//                    }
//                }
//            });
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public final View mView;
//            //public final TextView mIdView;
//            public final TextView mContentView;
//            public DummyContent.DummyItem mItem;
//
//            public ViewHolder(View view) {
//                super(view);
//                mView = view;
//                //mIdView = (TextView) view.findViewById(R.id.id);
//                mContentView = (TextView) view.findViewById(R.id.content);
//            }
//
//            @Override
//            public String toString() {
//                return super.toString() + " '" + mContentView.getText() + "'";
//            }
//        }
//    }
}
