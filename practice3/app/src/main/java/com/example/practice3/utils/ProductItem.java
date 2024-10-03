package com.example.practice3.utils;

import lombok.Getter;
import lombok.Setter;

public class ProductItem {
    @Getter
    private String name;
    @Setter
    private boolean isSelected = false;

    public ProductItem(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
