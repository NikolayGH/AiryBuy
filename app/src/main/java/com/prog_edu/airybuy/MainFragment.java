package com.prog_edu.airybuy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;


public class MainFragment extends Fragment {

    private Listener listener;

    private ArrayList<Product> products;
    private ProductsListAdapter productsListAdapter;
    private RecyclerView productRecycler;

    public static MainFragment newInstance(ArrayList<Product> products) {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        args.putParcelableArrayList("products", products);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.products = getArguments().getParcelableArrayList("products");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Listener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton addProductButton = view.findViewById(R.id.add_product_button);
        productRecycler = view.findViewById(R.id.products_recycler_view);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addProduct();
            }
        });

        Button addPackButton = view.findViewById(R.id.add_pack_button);
        addPackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.addPack();
            }
        });

        Button finishShoppingButton = view.findViewById(R.id.finish_shopping_button);
        finishShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.finishShopping();
            }
        });

        if(products!=null){
            productsListAdapter = new ProductsListAdapter(this.getActivity(), products);
            productRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            productRecycler.setAdapter(productsListAdapter);
            productsListAdapter.notifyDataSetChanged();
        }

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();


                //productsListAdapter.notifyItemRemoved(position);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Удаление товара")
                        .setMessage("Вы действительно хотите удалить товар?")
                        .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //listener.deleteAccountBySwipe(accountGuid);
                                productsListAdapter.removeItem(products.get(position));
                            }
                        })
                        .setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //listener.getCurrentAccountListBySwipeRefresh();
                                productsListAdapter.notifyDataSetChanged();
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //listener.getCurrentAccountListBySwipeRefresh();
                                productsListAdapter.notifyDataSetChanged();
                            }
                        })
                        .create()
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(productRecycler);

        return view;
    }

    public interface Listener{
        void addProduct();
        void addPack();
        void finishShopping();
    }

    public void addProductToList(Product product){
        Log.d("AIR", "FRAGMENT_addProductToList");
        if(productsListAdapter==null){
            productsListAdapter = new ProductsListAdapter(this.getActivity(), products);
            productRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            productRecycler.setAdapter(productsListAdapter);
            productsListAdapter.notifyDataSetChanged();
        }
        productsListAdapter.addItem(product);
    }
}
