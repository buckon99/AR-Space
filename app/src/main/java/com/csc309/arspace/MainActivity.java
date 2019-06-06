package com.csc309.arspace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.*;
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

                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                Button viewProduct = popupView.findViewById(R.id.view);
                viewProduct.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        String url = item.getProductUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                });

                Button arView = popupView.findViewById(R.id.arView);
                arView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(context, SceneformActivity.class);
                        intent.putExtra("width", item.getWidth());
                        intent.putExtra("height", item.getHeight());
                        intent.putExtra("length", item.getLength());

                        context.startActivity(intent);
                    }
                });
                Button save = popupView.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                        item.addProduct(item.getProductUrl());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Item Saved")
                                .setTitle("Item Saved");

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                popupWindow.dismiss();
                            }
                        });
                    }
                });

                Button share = popupView.findViewById(R.id.share);

                share.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Action not supported at this time")
                                .setTitle("Error");

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                popupWindow.dismiss();
                            }
                        });
                    }
                });

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
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
