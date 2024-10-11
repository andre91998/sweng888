package com.example.practice3.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

public class Product implements Parcelable {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String seller;
    @Getter
    private float price;
    @Getter
    private byte[] picture;

    protected Product(Parcel in) {

        id = in.readInt();
        name = in.readString();
        description = in.readString();
        seller = in.readString();
        price = in.readFloat();
        picture = in.readBlob();
    }

    public Product(int id, String name, String description, String seller, float price,
                   byte[] picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.picture = picture;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(seller);
        dest.writeFloat(price);
        dest.writeBlob(picture);
    }

    public String getProductString() {
        return "Product Name: " + name + " | Product Description" + description +
                " | Product Seller: " + seller + " | Prodcut Price: " + price;
    }
}
