package com.prog_edu.airybuy;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        QrResultFragment.Listener,
        MainFragment.Listener,
        QrScannerFragment.Listener,
        SelectionPayTypeFragment.Listener{

    private List<Product> products;
    private ArrayList <Product>fakeProducts = new ArrayList<>();
    private ArrayList <Product>controlListProducts;
    private MainFragment mainFragment;
    private int paymentType;
    private int pack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fakeProducts = generateFakeProducts();
        controlListProducts = generateFakeProducts();
        showMainFragment(fakeProducts);

    }


    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount()> 1 ){
            getFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Закрыть приложение?")
                    .setNegativeButton("Нет", null)
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //exit
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void showMainFragment(ArrayList<Product> fakeProducts) {
        mainFragment = MainFragment.newInstance(fakeProducts);
        getFragmentManager().beginTransaction()
                .add(R.id.content, mainFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("MainFragment")
                .commit();
    }

    private void showQrResultFragment(String code){
        getFragmentManager().popBackStack("MainFragment",0);
        QrResultFragment qrResultFragment = QrResultFragment.newInstance(code);
        getFragmentManager().beginTransaction()

                .add(R.id.content, qrResultFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(String.valueOf(R.id.content))
                .commit();
    }

    private void showSelectionPayTypeFragment(int finalAmount, float finalPrice){
        SelectionPayTypeFragment selectionPayTypeFragment = SelectionPayTypeFragment.newInstance(finalAmount, finalPrice);
        getFragmentManager().beginTransaction()
                .add(R.id.content, selectionPayTypeFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("SelectionPayTypeFragment")
                .commit();
    }

    private void showQrScannerFragment() {
        QrScannerFragment qrScannerFragment = QrScannerFragment.newInstance();
        getFragmentManager().beginTransaction()
                .add(R.id.content, qrScannerFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(String.valueOf(R.id.content))
                .commit();
    }

    @Override
    public void goBackToShopping() {
        getFragmentManager().popBackStack("MainFragment",0);
        //onBackPressed();
    }

    @Override
    public void setPaymentType(int typeIndex) {
        paymentType = typeIndex;

        if(fakeProducts!=null){
            StringBuilder purchaseJson = new StringBuilder("{\"ID\":\"shoppinglist\",\"goods\":{");

            for (int i = 0; i < fakeProducts.size(); i++) {
                if(i!=fakeProducts.size()-1){
                    purchaseJson.append("\"").append(fakeProducts.get(i).getCode()).append("\":\"").append(fakeProducts.get(i).getAmount()).append("\",");
                }else{
                    purchaseJson.append("\"").append(fakeProducts.get(i).getCode()).append("\":\"").append(fakeProducts.get(i).getAmount()).append("\"");
                }
            }
            purchaseJson.append("},\"clientcard\":\"1234123412341000\",\"payment\":\""+paymentType+"\"}");
            showQrResultFragment(String.valueOf(purchaseJson));
        }
    }

    @Override
    public void addProduct() {
        Intent myIntent = new Intent(MainActivity.this, QrCodeScannerActivity.class);
        startActivityForResult(myIntent, 1);
    }

    @Override
    public void addPack() {

        Product product = null;
        for (int i = 0; i < controlListProducts.size(); i++) {
            if(controlListProducts.get(i).getCode().equals("0000000000001")){
                product = controlListProducts.get(i);
            }
        }

        if(product==null){
            product = new Product("Пакет (майка)", "0000000000001", 5,1, "шт.");
        }

        mainFragment.addProductToList(product);

    }

    @Override
    public void finishShopping() {
        int finalAmount = 0;
        float finalPrice = 0;
        for (int i = 0; i < fakeProducts.size(); i++) {
            finalAmount = finalAmount + fakeProducts.get(i).getAmount();
            finalPrice = finalPrice + fakeProducts.get(i).getCalculatedPrice();
        }
        showSelectionPayTypeFragment(finalAmount, finalPrice);

    }


    @Override
    public void addProductToList(String code) {

        Toast.makeText(this, "finishShopping()" + code, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("AIR", "ACTIVITY_onActivityResult");
        if (data == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        String code = data.getStringExtra("code");
        Product product = null;

        for (int i = 0; i < controlListProducts.size(); i++) {
            if(controlListProducts.get(i).getCode().equals(code)){
                product = controlListProducts.get(i);
            }
        }

        if(product==null){
            product = new Product(null, code, 0,1, "шт.");
        }

        mainFragment.addProductToList(product);
    }

    private ArrayList <Product> generateFakeProducts(){
        ArrayList<Product> listProducts = new ArrayList<Product>();
        //чистая линия 8714100718070
        Product product_1 = new Product("яйцо \"желтое и белое\" куриное отборное с омега-3 кислотами, 10 шт.", "4607033231126", 149, 1, "шт.");
        listProducts.add(product_1);
        Product product_2 = new Product("Сок j-7 апельсин с мякотью tetrapak 0.97л", "4601512005291", 99, 1, "шт.");
        listProducts.add(product_2);
        Product product_3 = new Product("Кофе растворимый bushido Original натуральный 100г швейцария", "7610121710318", 699, 1, "шт.");
        listProducts.add(product_3);
        Product product_4 = new Product("МАКАРОН.ИЗДЕЛИЯ \"BARILLA\" \"ПИПЕ РИГАТЕ\" 500Г", "8076809576086", 150, 1, "шт.");
        listProducts.add(product_4);
        Product product_5 = new Product("Яблоки Голден (Окей), 1.352кг", "2904069013521", 0, 1, "шт.");
        listProducts.add(product_5);
        Product product_6 = new Product("Яблоки Голден (Мегамарт), 0.53кг", "2200004005300", 0,1, "шт.");
        listProducts.add(product_6);
        Product product_7 = new Product("Картон альт мультики цветной 8цв 8л", "4606016184756", 400, 1, "шт.");
        listProducts.add(product_7);
        Product product_8 = new Product("Клей-карандаш Erich Krause ек2368 21г", "4041485023685", 250, 1, "шт.");
        listProducts.add(product_8);
        Product product_9 = new Product("КРАСКИ ГУАШЕВЫЕ \"ПРЕСТИЖ\" 12 ЦВ., АРТ. 28С 1684-08", "4601185014316", 600,1, "шт.");
        listProducts.add(product_9);
        Product product_10 = new Product("*ПИВО \"ВЕЛКОПОПОВЕЦКИЙ КОЗЕЛ\" ТЕМНОЕ 0,45Л 3,7% Ж/Б", "4605664012015", 98,1, "шт.");
        listProducts.add(product_10);
        Product product_11 = new Product("Напиток пивной реддс 0.33ст/б 4.5%", "4605664002658", 81,1, "шт.");
        listProducts.add(product_11);
        Product product_12 = new Product("ИНДЕЙКА \"КОТЛЕТА НАТУРАЛЬНАЯ\" ИНДИЛАЙТ ОХЛ. ГВУ 0,5 КГ", "4670012885436", 450, 1, "шт.");
        listProducts.add(product_12);
        Product product_13 = new Product("КОЛБАСА КУРИНАЯ СЫР/КОПЧ 300ГР СИБКОЛ", "4620007114980", 700,1, "шт.");
        listProducts.add(product_13);

        return listProducts;
    }
}
