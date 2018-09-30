package com.example.abdulaziz.stockmanagement;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomListView extends ArrayAdapter<String> {

    Activity context;
    /*String[] itemName;
    String[] ItemQuantity;
    String[] category;
    String[] company;
    String[] StockInDate;*/
    ArrayList itemName, ItemQuantity, category, company, stockOutType, StockInDate;

    public CustomListView(@NonNull Activity context, ArrayList itemName, ArrayList quantity, ArrayList company, ArrayList date) {
        super(context, R.layout.stock_in_list_table,itemName);
        this.context = context;
        this.itemName = itemName;
        this.ItemQuantity = quantity;
        this.category = category;
        this.company = company;
        this.StockInDate = date;
    }
    public CustomListView(@NonNull Activity context, ArrayList itemName, ArrayList quantity, ArrayList company,ArrayList stockOutType, ArrayList date) {
        super(context, R.layout.stock_in_list_table,itemName);
        this.context = context;
        this.itemName = itemName;
        this.ItemQuantity = quantity;
        this.category = category;
        this.company = company;
        this.stockOutType = stockOutType;
        this.StockInDate = date;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.stock_in_list_table,null,true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.itemName.setText(itemName.get(position).toString());
        viewHolder.quantity.setText(ItemQuantity.get(position).toString());
        viewHolder.company.setText(company.get(position).toString());
        viewHolder.date.setText(StockInDate.get(position).toString());

        return view;
    }

    class ViewHolder{
        TextView itemName;
        TextView quantity;
        TextView company;
        TextView date;

        ViewHolder(View view){
            itemName = view.findViewById(R.id.item_name);
            quantity = view.findViewById(R.id.quantity);
            company = view.findViewById(R.id.compnay_name);
            date = view.findViewById(R.id.date);
        }
    }
}
