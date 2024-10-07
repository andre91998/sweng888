package com.example.practice3.utils;

import android.os.Parcelable;

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
    @Setter
    private byte[] picture;

    public Product(int id, String name, String description, String seller, float price,
                   byte[] picture) {

    }
}
