package com.prog_edu.airybuy;

import android.content.Context;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final Context context;
    private List<Product> items;
    //private Listener listener;


    public ProductsListAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
    }

    public interface OnEditJobListener {
        void onEditClick(Product product);
    }

//    public interface Listener{
//        void onOpenJobPropertiesListFragment(JobItem jobItem);
//    }
//
//    private void setListener(Listener listener) {
//        this.listener = listener;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.row_product, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int backRes = position%2 != 0 ? R.color.colorBackground : R.color.colorLightBlue;
            Product ps = items.get(position);
            ProductViewHolder serviceViewHolder = (ProductViewHolder) holder;
            serviceViewHolder.setProduct(ps);
            serviceViewHolder.setBackground(backRes);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void removeItem(Product product){
        if (items.contains(product)){
            int index = items.indexOf(product);
            items.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, items.size()+1);
        }
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    public void insertItem(Product product, int index){
        if (!items.contains(product)){
            items.add(index, product);
            notifyItemInserted(index);
            notifyItemRangeChanged(index, items.size());
        }
    }

    public void update(@Nullable List<Product> list){
        items.clear();
        if (list != null){
            items = new ArrayList<>(list);
        }
        notifyDataSetChanged();
    }

    public void update(Product product){
        if (items.contains(product)){
            notifyItemChanged(items.indexOf(product));
            notifyItemChanged(items.size());
        }
    }

    public void addItems(final List<Product> list){
        items.addAll(list);
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                ProductsListAdapter.this.notifyItemRangeInserted(items.size() - list.size(), list.size());
                ProductsListAdapter.this.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    public void addItem(Product product){

        for (int i = 0; i < items.size(); i++) {
            if(product.getCode().equals(items.get(i).getCode())){
                items.get(i).setAmount(items.get(i).getAmount()+1);
                ProductsListAdapter.this.notifyDataSetChanged();
                return;
            }
        }

        items.add(product);
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            @Override
            public void run() {

                ProductsListAdapter.this.notifyItemInserted(items.size() - 1);
                ProductsListAdapter.this.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        private final TextView productNameTextField;
        private final TextView codeTextField;
        private final TextView mainPriceTextField;
        private final TextView amountTextField;
        private final TextView unitsTextField;
        private final TextView calculatedPriceTextField;

        private final Button incrementButton;
        private final Button decrementButton;
        private final ConstraintLayout rootLo;





        ProductViewHolder(View itemView) {
            super(itemView);
            productNameTextField = itemView.findViewById(R.id.product_name);
            codeTextField = itemView.findViewById(R.id.code);
            mainPriceTextField = itemView.findViewById(R.id.main_price);
            amountTextField = itemView.findViewById(R.id.amount);
            unitsTextField = itemView.findViewById(R.id.units);
            calculatedPriceTextField = itemView.findViewById(R.id.calculated_price);
            incrementButton = itemView.findViewById(R.id.increment_button);
            decrementButton = itemView.findViewById(R.id.decrement_button);
            rootLo = itemView.findViewById(R.id.rootLo);

        }

        private  void setProduct(final Product product){

//            menuButton.setOnClickListener(view -> {
//                PopupMenu popupMenu = new PopupMenu(context, menuButton);
//                popupMenu.getMenuInflater().inflate(R.menu.menu_jobs, popupMenu.getMenu());
//                popupMenu.show();
//
//                popupMenu.setOnMenuItemClickListener(item -> {
//
//                    switch (item.getItemId()){
//
//                        case R.id.action_delete:{
//
//                            new AlertDialog.Builder(context)
//                                    .setTitle("Удалить?")
//                                    .setNegativeButton("Нет", null)
//                                    .setPositiveButton("Да", (dialogInterface, i) -> CostingJobListAdapter.this.removeItem(costingJob))
//                                    .create()
//                                    .show();
//                        }
//                        return true;
//
//                        case R.id.action_edit:{
//                            editListener.onEditClick(costingJob);
//                        }
//                        return true;
//                    }
//                    return false;
//                });
//            });



            productNameTextField.setText(product.getName()!=null ? product.getName() : "Наименование товара");
            codeTextField.setText(product.getCode()!=null ? "#" + product.getCode() : "");
            mainPriceTextField.setText(product.getPrice()!=0 ? product.getPrice() + "р" : "0р");
            amountTextField.setText(Integer.toString(product.getAmount()));
            unitsTextField.setText(product.getUnit()!=null ? product.getUnit() : "шт.");
            String calculatedPriceText = product.getCalculatedPrice() + " р.";
            calculatedPriceTextField.setText(calculatedPriceText);

            incrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.setAmount(product.getAmount()+1);
                    ProductsListAdapter.this.notifyDataSetChanged();
                }
            });

            decrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(product.getAmount()>1){
                        product.setAmount(product.getAmount()-1);
                        ProductsListAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }

        void setBackground(int res){
            rootLo.setBackgroundResource(res);
        }
    }
    public List<Product> getItems() {
        return items;
    }

}
