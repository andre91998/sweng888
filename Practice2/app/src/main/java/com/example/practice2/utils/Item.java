package com.example.practice2.utils;

import java.io.Serializable;

import lombok.Getter;

public class Item implements Serializable {

    @Getter
    private String title;
    @Getter
    private String subtitle;

    public Item(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }


}
