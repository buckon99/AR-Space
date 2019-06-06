package com.csc309.arspace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.csc309.arspace.dummy.ProductsContent;
import com.csc309.arspace.models.Product;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivity parent;
    private TextView mTextMessage;
    private RelativeLayout relLayout;
    private SimpleItemRecyclerViewAdapter adapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    relLayout.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_search:
                    relLayout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_settings:
                    relLayout.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Search();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        parent = this;
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (findViewById(R.id.product_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        EditText search = findViewById(R.id.search);
        RecyclerView recyclerView = findViewById(R.id.product_list);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try  {
                                String[] values = search.getText().toString().split(" ");
                                ArrayList<Product> products = Search.searchProduct(values);
                                for(int i = 0; i < products.size(); i++) {

                                    Product prod = products.get(i);
                                    URL url = new URL(prod.getImgURL());
                                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    prod.addBitmap(bmp);
                                    ProductsContent.addItem(prod);
                                }

                                adapter.notifyDataSetChanged();
                                //recyclerView.setAdapter(new MainActivity.SimpleItemRecyclerViewAdapter(parent, products, mTwoPane));
                                //recyclerView.setAdapter(new MainActivity.SimpleItemRecyclerViewAdapter(parent, products, mTwoPane));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    InputMethodManager imm = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    thread.start();
                    return true;
                }
                return false;
            }
        });
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    return true;
                }
                return false;
            }
        });
        relLayout = findViewById(R.id.frameLayout);

        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new MainActivity.SimpleItemRecyclerViewAdapter(this, ProductsContent.ITEMS, mTwoPane);
        recyclerView.setAdapter(adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MainActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final MainActivity mParentActivity;
        private final List<Product> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Product item = (Product) view.getTag();

                Intent intent = new Intent(context, SceneformActivity.class);
                intent.putExtra("width", item.getWidth());
                intent.putExtra("height", item.getHeight());
                intent.putExtra("length", item.getLength());

                context.startActivity(intent);

                /*
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ProductDetailFragment.ARG_ITEM_ID, item.id);
                    ProductDetailFragment fragment = new ProductDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.product_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(ProductDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }*/
            }
        };

        SimpleItemRecyclerViewAdapter(MainActivity parent,
                                      List<Product> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public MainActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new MainActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MainActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {

            Product prod = mValues.get(position);
            holder.mIdView.setText(prod.getTitle());
            holder.mContentView.setText(prod.getType());
            holder.mImgView.setImageBitmap(prod.getBitmap());
            DecimalFormat df = new DecimalFormat("0");
            holder.dimensions.setText("Dimensions: " + df.format(prod.getLength()) + "\" x "
                    + df.format(prod.getWidth()) + "\" x "
                    + df.format(prod.getHeight()) + "\"");

            df = new DecimalFormat("0.##");
            holder.price.setText("$" + df.format(prod.getPrice()));
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mImgView;
            final TextView dimensions;
            final TextView price;
            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                mImgView = (ImageView) view.findViewById(R.id.prodImg);
                dimensions = (TextView) view.findViewById(R.id.dimensions);
                price = (TextView) view.findViewById(R.id.price);
            }
        }
    }
}
