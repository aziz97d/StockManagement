package com.example.abdulaziz.stockmanagement;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class StockInListActivity extends AppCompatActivity {

    private ListView listView;
    private SqliteDB sqliteDB;
    ArrayList itemName, stockInQuantity, categoryName, companyName, stockInDate;
    //String[] itemName, StockInQuantity, categoryName, companyName, StockInDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        sqliteDB = new SqliteDB(StockInListActivity.this);
        Cursor getAllStockInData = sqliteDB.getAllStockInData();

        itemName = new ArrayList();
        stockInQuantity = new ArrayList();
        //categoryName = new ArrayList();
        companyName = new ArrayList();
        stockInDate = new ArrayList();

        while (getAllStockInData.moveToNext()){
            int i = 0;
           /* itemName[i] = getAllStockInData.getString(3);
            StockInQuantity[i] = getAllStockInData.getString(4);
            categoryName[i] = getAllStockInData.getString(1);
            companyName[i] = getAllStockInData.getString(2);
            StockInDate[i] = getAllStockInData.getString(5);*/

            itemName.add(getAllStockInData.getString(getAllStockInData.getColumnIndex("ItemName")));
            stockInQuantity.add(getAllStockInData.getString(getAllStockInData.getColumnIndex("Quantity")));
            //categoryName.add(getAllStockInData.getString(getAllStockInData.getColumnIndex("CategoryName")));
            companyName.add(getAllStockInData.getString(getAllStockInData.getColumnIndex("CompanyName")));
            stockInDate.add(getAllStockInData.getString(getAllStockInData.getColumnIndex("Date")));

            i++;
        }
       /* do {
            int i = 0;
            itemName[i] = getAllStockInData.getString(3);
            stockInQuantity[i] = getAllStockInData.getString(4);
            categoryName[i] = getAllStockInData.getString(1);
            companyName[i] = getAllStockInData.getString(2);
            stockInDate[i] = getAllStockInData.getString(5);
            i++;
        }while (getAllStockInData.moveToNext());*/



        listView = findViewById(R.id.stock_list_view);
        CustomListView customListView = new CustomListView(this,itemName,stockInQuantity,companyName,stockInDate);
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
