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
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.csc309.arspace.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.support.constraint.motion.MotionScene.TAG;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout relLayout;
    private SimpleItemRecyclerViewAdapter adapter;
    private static int save = 0;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (@NonNull MenuItem item) -> {
        EditText search = findViewById(R.id.search);
        switch (item.getItemId()) {
            case R.id.navigation_home:
                save = 1;
                search.setVisibility(View.INVISIBLE);
                ProductsContent.removeAll();
                adapter.notifyDataSetChanged();
                Thread thread = new Thread(() -> {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        String currentUid = null;
                        if (currentUser != null) {
                            currentUid = currentUser.getUid();
                        }
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        assert currentUid != null;
                        db.collection(currentUid)
                                .get().addOnCompleteListener(task -> {
                            try {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        Map<String, Object> prodFields = document.getData();
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        Product product = new Product(Objects.requireNonNull(prodFields.get("id")).toString(),
                                                Objects.requireNonNull(prodFields.get("title")).toString(),
                                                Objects.requireNonNull(prodFields.get("type")).toString(),
                                                (Double) prodFields.get("width"),
                                                (Double) prodFields.get("height"),
                                                (Double) prodFields.get("length"),
                                                Objects.requireNonNull(prodFields.get("imgURL")).toString(),
                                                (Double) prodFields.get("price"),
                                                Objects.requireNonNull(prodFields.get("info")).toString(),
                                                Objects.requireNonNull(prodFields.get("productURL")).toString());
                                        Thread t = new Thread(() -> {
                                                URL url;
                                                try {
                                                    url = new URL(product.getImgURL());
                                                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                                    product.addBitmap(bmp);
                                                    ProductsContent.addItem(product);
                                                }catch(MalformedURLException ex){
                                                    Log.d(TAG, "Malformed URL ", task.getException());
                                                }catch (IOException e) {
                                                    Log.d(TAG, "IO Exception ", task.getException());

                                                }

                                                runOnUiThread(() ->
                                                        adapter.notifyDataSetChanged()
                                                );
                                            });
                                        t.start();

                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ");
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "Error in database: ", e);
                            }
                        });
                    });
                thread.start();
                relLayout.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigation_search:
                save = 0;

                ProductsContent.removeAll();
                adapter.notifyDataSetChanged();
                relLayout.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                return true;
            case R.id.navigation_settings:
                relLayout.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                return true;
            default:
                return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        MainActivity parent = this;
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        EditText search = findViewById(R.id.search);
        RecyclerView recyclerView = findViewById(R.id.product_list);
        search.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Thread thread = new Thread(() ->{
                            try  {
                                ProductsContent.removeAll();
                                String[] values = search.getText().toString().split(" ");
                                ArrayList<Product> products = Search.searchProduct(values);
                                for(int i = 0; i < products.size(); i++) {

                                    Product prod = products.get(i);
                                    URL url = new URL(prod.getImgURL());
                                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    prod.addBitmap(bmp);
                                    ProductsContent.addItem(prod);
                                }
                                runOnUiThread(() -> adapter.notifyDataSetChanged());

                            } catch (Exception e) {

                                Log.d(TAG, "Error getting documents: ", e);

                            }
                    });
                    InputMethodManager imm = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    thread.start();
                    return true;
                }
                return false;
            });
        search.setOnKeyListener((View v, int keyCode, KeyEvent event) -> event.getAction() == KeyEvent.ACTION_DOWN &&

                        keyCode == KeyEvent.KEYCODE_ENTER);
        relLayout = findViewById(R.id.frameLayout);

        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new MainActivity.SimpleItemRecyclerViewAdapter(ProductsContent.ITEMS);
        recyclerView.setAdapter(adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ProductViewHolder> {

        private final List<Product> mValues;
        private final View.OnClickListener mOnClickListener = (View view) -> {

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
                viewProduct.setOnClickListener((View v) -> {
                        String url = item.getProductUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                });

                Button arView = popupView.findViewById(R.id.arView);
                arView.setOnClickListener((View v) -> {
                        Intent intent = new Intent(context, SceneformActivity.class);
                        intent.putExtra("width", item.getWidth());
                        intent.putExtra("height", item.getHeight());
                        intent.putExtra("length", item.getLength());

                        context.startActivity(intent);
                });
                Button saveBtn = popupView.findViewById(R.id.save);
                if(save == 1) {
                    saveBtn.setText("Unsave");
                }
                saveBtn.setOnClickListener((View v) -> {

                    item.addProduct(item.getId());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Item Saved")
                            .setTitle("Item Saved");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setOnDismissListener((DialogInterface dialogInterface) ->
                            popupWindow.dismiss()
                    );
                });

                Button share = popupView.findViewById(R.id.share);

                share.setOnClickListener( (View v) -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Action not supported at this time")
                            .setTitle("Error");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setOnDismissListener((DialogInterface dialogInterface) ->
                            popupWindow.dismiss()
                    );
                });

                // dismiss the popup window when touched
                popupView.setOnTouchListener((View v, MotionEvent event) -> {
                    popupWindow.dismiss();
                    return true;
                });
        };

        SimpleItemRecyclerViewAdapter(List<Product> items) {
            mValues = items;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductViewHolder holder, int position) {

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

        class ProductViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mImgView;
            final TextView dimensions;
            final TextView price;
            ProductViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
                mImgView = view.findViewById(R.id.prodImg);
                dimensions = view.findViewById(R.id.dimensions);
                price = view.findViewById(R.id.price);
            }
        }
    }
}
