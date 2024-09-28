package com.example.practice2.utils;

import lombok.Getter;

public class Item {

    @Getter
    private String Title;
    @Getter
    private String Subtitle;

    public Item(String title, String subtitle) {
        this.Title = title;
        this.Subtitle = subtitle;
    }


}
