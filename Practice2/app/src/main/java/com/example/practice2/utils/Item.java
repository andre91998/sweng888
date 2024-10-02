package com.example.practice2.utils;

import java.io.Serializable;

import lombok.Getter;

public class Item implements Serializable {

    @Getter
    private String title;
    @Getter
    private String subtitle;
    @Getter
    private String description;

    public Item(String title, String subtitle, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }


}
