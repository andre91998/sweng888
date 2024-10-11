package com.example.practice3.utils;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Map<Integer, Boolean> mProductSelectedMap = new HashMap<>();
    private List<Product> mProductList;

    public RecyclerViewAdapter(List<Product> productList) {
        mProductList = productList;
        for (int i = 0; i < mProductList.size(); i++) {
            mProductSelectedMap.put(mProductList.get(i).getId(), false);
        }
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_row,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Product product = mProductList.get(position);
        holder.textView.setText(product.getName());
        holder.view.setBackgroundColor(mProductSelectedMap.get(product.getId()) ? Color.CYAN :
                Color.WHITE);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductSelectedMap.put(product.getId(), !mProductSelectedMap.get(product.getId()));
                holder.view.setBackgroundColor(mProductSelectedMap.get(product.getId()) ? Color.CYAN : Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductSelectedMap.size();
    }

    public int getSelectedItemCount() {
       return (int) mProductSelectedMap.values().stream().filter(b -> b).count();
    }

    public Map<Integer, Boolean> getProductSelectedMap() {
        return mProductSelectedMap;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

        private MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.textView = (TextView) view.findViewById(R.id.text_view);
        }
    }
}
