package com.prog_edu.airybuy;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SelectionPayTypeFragment extends Fragment {

    private Listener listener;
    private int finalAmount;
    private float finalPrice;

    public static SelectionPayTypeFragment newInstance(int finalAmount, float finalPrice) {
        Bundle args = new Bundle();

        args.putInt("finalAmount", finalAmount);
        args.putFloat("finalPrice", finalPrice);

        SelectionPayTypeFragment fragment = new SelectionPayTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.finalAmount = getArguments().getInt("finalAmount");
            this.finalPrice = getArguments().getFloat("finalPrice");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection_pay_type, container, false);

        Button cardButton = view.findViewById(R.id.card_button);
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPaymentType(1);
            }
        });

        Button cashButton = view.findViewById(R.id.cash_button);
        cashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setPaymentType(2);
            }
        });

        TextView amountAllProducts = view.findViewById(R.id.amount_all_products);
        TextView allProductsPrice = view.findViewById(R.id.all_products_price);


        amountAllProducts.setText(String.valueOf(finalAmount));
        allProductsPrice.setText(String.valueOf(finalPrice));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SelectionPayTypeFragment.Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Listener");
        }
    }

    public interface Listener{
        void setPaymentType(int typeIndex);
    }
}
