package com.prog_edu.airybuy;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    private String unit;
    private int price;
    private int amount;
    private float weight;
    private float calculatedPrice;
    private String code;

    public Product(String name, String code ,  int price, int amount, String unit) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.unit = unit;
        this.amount = amount;

    }


    protected Product(Parcel in) {
        name = in.readString();
        unit = in.readString();
        price = in.readInt();
        amount = in.readInt();
        weight = in.readFloat();
        calculatedPrice = in.readFloat();
        code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(unit);
        dest.writeInt(price);
        dest.writeInt(amount);
        dest.writeFloat(weight);
        dest.writeFloat(calculatedPrice);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getCalculatedPrice() {
        calculatedPrice = price*amount;
        return calculatedPrice;
    }

    public void setCalculatedPrice(float calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
