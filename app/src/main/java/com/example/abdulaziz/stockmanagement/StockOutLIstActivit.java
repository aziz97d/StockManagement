package com.example.abdulaziz.stockmanagement;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class StockOutLIstActivit extends AppCompatActivity {

    private ListView listView;
    private SqliteDB sqliteDB;
    ArrayList itemName, stockOutQuantity, companyName, stockOutType, stockOutDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out_list);

        //adding back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        itemName = new ArrayList();
        stockOutQuantity = new ArrayList();
        companyName = new ArrayList();
        stockOutType = new ArrayList();
        stockOutDate = new ArrayList();
        sqliteDB = new SqliteDB(this);
        Cursor allStockOutData = sqliteDB.getAllStockOutData();

        while (allStockOutData.moveToNext()){
            itemName.add(allStockOutData.getString(3));
            stockOutQuantity.add(allStockOutData.getString(4));
            companyName.add(allStockOutData.getString(2));
            stockOutType.add(allStockOutData.getString(5));
            stockOutDate.add(allStockOutData.getString(6));

        }

        listView = findViewById(R.id.stock_list_view);
        CustomStockOutListView customListView = new CustomStockOutListView(this,itemName,stockOutQuantity,companyName,stockOutType,stockOutDate);
        listView.setAdapter(customListView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
