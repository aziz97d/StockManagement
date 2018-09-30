package com.example.abdulaziz.stockmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SqliteDB sqliteDB;
    private CardView categoryButton, companyButton, itemButton, stockInButton, stockOutButton, stockInListButton, stockOutListButton, summaryButton;
    private boolean isFirstSelect = true;
    int itemQuantity;
    String stockOutType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                AlertDialog.Builder feedbackAlertDialog = new AlertDialog.Builder(MainActivity.this);
                View feedbackView = getLayoutInflater().inflate(R.layout.feedback_dialog,null);
                final EditText feedbackPersonName = feedbackView.findViewById(R.id.feedback_person_name);
                final EditText feedbackEmail = feedbackView.findViewById(R.id.feedback_email);
                final EditText feedbackMessage = feedbackView.findViewById(R.id.feedback_message);
                ImageView close_image_view = feedbackView.findViewById(R.id.close_image_button);

                Button feedbackSendButton = feedbackView.findViewById(R.id.btn_send_feedback);
                Button feedbackClearButton = feedbackView.findViewById(R.id.btn_clear_feedback);

                feedbackAlertDialog.setView(feedbackView);
                final AlertDialog feedbackDialog = feedbackAlertDialog.create();
                feedbackDialog.show();

                close_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedbackDialog.dismiss();
                    }
                });
                feedbackSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            String name = feedbackPersonName.getText().toString();
                            String email = feedbackEmail.getText().toString();
                            String message = feedbackMessage.getText().toString();

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/email");
                            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"aziz12d97@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback from the app");
                            intent.putExtra(Intent.EXTRA_TEXT,"Name : "+name+"\n Email : "+email+"\n Message : "+message);

                            startActivity(Intent.createChooser(intent,"Feedback with"));

                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "Exception "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                feedbackClearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedbackPersonName.setText("");
                        feedbackEmail.setText("");
                        feedbackMessage.setText("");
                    }
                });
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sqliteDB = new SqliteDB(this);

        categoryButton = findViewById(R.id.category_cardViewId);
        companyButton = findViewById(R.id.company_cardViewId);
        itemButton = findViewById(R.id.item_cardViewId);
        stockInButton = findViewById(R.id.stock_in_cardViewId);
        stockOutButton = findViewById(R.id.stock_out_cardViewId);
        stockInListButton = findViewById(R.id.stock_in_list_cardViewId);
        stockOutListButton = findViewById(R.id.stock_out_list_cardViewId);
        summaryButton = findViewById(R.id.summary_cardViewId);

        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder categoryAlertDialog = new AlertDialog.Builder(MainActivity.this);
                View categoryView = getLayoutInflater().inflate(R.layout.add_category_dialog,null);
                ImageView close_image_view = categoryView.findViewById(R.id.close_image_button);
                final EditText categoryName = categoryView.findViewById(R.id.edt_add_category);
                Button saveCategoryButton = categoryView.findViewById(R.id.btn_save_category);
                final Button showCategoryButton = categoryView.findViewById(R.id.btn_show_category);


                categoryAlertDialog.setView(categoryView);
                final AlertDialog alertDialog = categoryAlertDialog.create();
                alertDialog.show();

                close_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                saveCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txtCategory = categoryName.getText().toString();
                        if(!txtCategory.isEmpty()){

                            if (sqliteDB.saveCategory(txtCategory)){
                                Toast.makeText(MainActivity.this, "Category Save Successful", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Category Save Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            categoryName.setError("Category Required.");
                            //Toast.makeText(MainActivity.this, "Please Enter Category Name", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                showCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor allCategory = sqliteDB.getAllCategory();

                        if (allCategory.getCount()==0){
                            Toast.makeText(MainActivity.this, "Category Not Found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append("SL \t Name \n");
                            while (allCategory.moveToNext()){
                                stringBuffer.append(allCategory.getString(0)+" \t ");
                                stringBuffer.append(allCategory.getString(1)+"\n");
                            }
                            AlertDialog.Builder showCategoryDialog = new AlertDialog.Builder(MainActivity.this);
                            showCategoryDialog.setTitle("All Compnay");
                            showCategoryDialog.setMessage(stringBuffer.toString());
                            AlertDialog alertDialog1 = showCategoryDialog.create();
                            alertDialog1.show();
                        }
                    }
                });
            }
        });

        //Company Button Listener

        companyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder companyAlertDialog = new AlertDialog.Builder(MainActivity.this);
                View companyView = getLayoutInflater().inflate(R.layout.add_company_dialog,null);
                ImageView close_image_view = companyView.findViewById(R.id.close_image_button);
                final EditText companyName = companyView.findViewById(R.id.edt_add_company);
                Button saveCompanyButton = companyView.findViewById(R.id.btn_save_company);
                Button showCompanyButton = companyView.findViewById(R.id.btn_show_company);


                companyAlertDialog.setView(companyView);
                final AlertDialog alertDialog = companyAlertDialog.create();
                alertDialog.show();

                close_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                saveCompanyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txtCompany = companyName.getText().toString();
                        if(!txtCompany.isEmpty()){

                            if (sqliteDB.saveCompany(txtCompany)){
                                Toast.makeText(MainActivity.this, "Company Save Successful", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Company Save Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            companyName.setError("Company Name Required.");
                            //Toast.makeText(MainActivity.this, "Please Enter Company Name", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                showCompanyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Cursor allCompany = sqliteDB.getAllCompany();

                        if (allCompany.getCount()==0){
                            Toast.makeText(MainActivity.this, "Company Not Found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append("SL \t Name \n");
                            while (allCompany.moveToNext()){
                                stringBuffer.append(allCompany.getString(0)+" \t ");
                                stringBuffer.append(allCompany.getString(1)+"\n");
                            }
                            AlertDialog.Builder showCompanyDialog = new AlertDialog.Builder(MainActivity.this);
                            showCompanyDialog.setTitle("All Company");
                            showCompanyDialog.setMessage(stringBuffer.toString());
                            AlertDialog alertDialog1 = showCompanyDialog.create();
                            alertDialog1.show();
                        }
                    }
                });

            }
        });

        //Item Setup .........................
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder itemDialog = new AlertDialog.Builder(MainActivity.this);
                View itemView = getLayoutInflater().inflate(R.layout.add_item_dialog,null);
                ImageView close_image_view = itemView.findViewById(R.id.close_image_button);
                final Spinner categorySpinner = itemView.findViewById(R.id.category_spinner);
                final Spinner companySpinner = itemView.findViewById(R.id.company_spinner);
                final EditText itemName = itemView.findViewById(R.id.item_name_id);
                final EditText reorderLevel = itemView.findViewById(R.id.reorder_id);
                Button btnSave = itemView.findViewById(R.id.btn_save_item);


                ArrayList<String> categorys = new ArrayList<>();
                categorys.add("Select Category");
                Cursor allCategory = sqliteDB.getAllCategory();
                while (allCategory.moveToNext()){
                    categorys.add(allCategory.getString(1));
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,categorys);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(arrayAdapter);

                ArrayList<String> companys = new ArrayList<>();
                companys.add("Select Company");
                Cursor allCompany = sqliteDB.getAllCompany();
                while (allCompany.moveToNext()){
                    companys.add(allCompany.getString(1));
                }
                allCompany.close();

                ArrayAdapter companyAdapter = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,companys);
                companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                companySpinner.setAdapter(companyAdapter);


                itemDialog.setView(itemView);
                final AlertDialog itemD = itemDialog.create();
                itemD.show();


                close_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemD.dismiss();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txtItemName = itemName.getText().toString();
                        String txtReorderLevel = reorderLevel.getText().toString();
                        String txtCategory = categorySpinner.getSelectedItem().toString();
                        String txtCompany = companySpinner.getSelectedItem().toString();
                        if (txtReorderLevel.isEmpty()){
                            txtReorderLevel="0";
                        }

                        if (!txtCategory.equals("Select Category") && !txtCompany.equals("Select Company") && !txtItemName.isEmpty() && !txtReorderLevel.isEmpty()){

                            if (sqliteDB.isExistsItem(txtCompany,txtCategory,txtItemName)){

                                Toast.makeText(MainActivity.this, "This Data Already Exists", Toast.LENGTH_SHORT).show();
                            }else{

                                boolean result = sqliteDB.saveItem(txtCompany,txtCategory,txtItemName,txtReorderLevel);

                                if (result){
                                    Toast.makeText(MainActivity.this, "Item Saved", Toast.LENGTH_SHORT).show();
                                    itemD.dismiss();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Item Save Failed", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                        else {
                            if (txtItemName.isEmpty()){
                                itemName.setError("Item Name Required.");
                            }
                            if(txtCompany.equals("Select Company")){
                                ((TextView)companySpinner.getSelectedView()).setError("Error message");
                            }
                            if (txtCategory.equals("Select Category")){
                                ((TextView)categorySpinner.getSelectedView()).setError("Error message");
                            }
                        }

                    }
                });
            }
        });

        //Stock In .........................................
        stockInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder stockInAlertDialog = new AlertDialog.Builder(MainActivity.this);
                View stockInView = getLayoutInflater().inflate(R.layout.stock_in_dialog,null);
                ImageView close_image_view = stockInView.findViewById(R.id.close_image_button);
                final Spinner itemSpinner = stockInView.findViewById(R.id.stock_in_item_spinner_id);
                final Spinner companySpinner  = stockInView.findViewById(R.id.stock_in_company_spinner_id);
                final TextView reorderTextView = stockInView.findViewById(R.id.stock_in_reorder_level_id);
                final TextView availableQuantityTextView = stockInView.findViewById(R.id.stock_in_available_quantity_id);
                final EditText stockInQuantityEditText = stockInView.findViewById(R.id.stock_in_quantity_id);
                Button stockInButton = stockInView.findViewById(R.id.btn_stock_in_id);


                //Company Spinner

                final ArrayList<String> companys = new ArrayList<>();
                companys.add("Select Company");
                final ArrayList<String> itemArrayList = new ArrayList<String>();
                itemArrayList.add("Select Item");
                Cursor allCompany = sqliteDB.getAllCompany();
                while (allCompany.moveToNext()){
                    companys.add(allCompany.getString(1));
                }
                allCompany.close();
                ArrayAdapter companyAdapter = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,companys);
                companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                companySpinner.setAdapter(companyAdapter);


                //Reload Item after selected company

                companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String companyName = companys.get(i);

                        itemArrayList.clear();
                        itemArrayList.add("Select Item");
                        //for problem solve
                        isFirstSelect = true;
                        Cursor result = sqliteDB.getItemByCompany(companyName);

                        while (result.moveToNext()){
                            itemArrayList.add(result.getString(3));
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,itemArrayList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        itemSpinner.setAdapter(arrayAdapter);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (isFirstSelect ==true){
                            isFirstSelect = false;
                        }
                        else {
                            String itemName = itemArrayList.get(i);
                            Cursor result = sqliteDB.getItemReorder(itemName);
                            String reorder="0";
                            while (result.moveToNext()){
                                reorder = result.getString(4);
                            }
                            reorderTextView.setText(reorder);

                            String companyName = companySpinner.getSelectedItem().toString();
                            //check here
                            itemQuantity = sqliteDB.getItemQuantity(companyName,itemName);

                            availableQuantityTextView.setText(""+itemQuantity);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                stockInAlertDialog.setView(stockInView);
                final AlertDialog alertDialog = stockInAlertDialog.create();
                alertDialog.show();

                close_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });


                stockInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String getCompanyName = companySpinner.getSelectedItem().toString();
                        String getItemName = itemSpinner.getSelectedItem().toString();
                        String getReorder = reorderTextView.getText().toString();
                        String quantity = stockInQuantityEditText.getText().toString();

                        if (getCompanyName.equals("Select Company") || getItemName.equals("Select Item") || quantity.isEmpty() ){
                            if (quantity.isEmpty()){
                                stockInQuantityEditText.setError("Input Quantity");
                            }
                            if(getItemName.equals("Select Item")){
                                ((TextView)itemSpinner.getSelectedView()).setError("Item Required");
                            }
                            if (getCompanyName.equals("Select Company")){
                                ((TextView)companySpinner.getSelectedView()).setError("Company Required");
                            }
                        }
                        else {

                            //Update Quantity if company Name and item name added before

                            if (sqliteDB.isExistsItemAndCompany(getCompanyName,getItemName)){
                                int getQuantity = Integer.parseInt(quantity);
                                itemQuantity+=getQuantity;

                                boolean result = sqliteDB.updateQuantity(getCompanyName,getItemName,itemQuantity);
                                if (result){
                                    Toast.makeText(MainActivity.this, "Stock In Successful.", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }

                            }
                            else{
                                //Insert data in Quantity table
                                int getQuantity = Integer.parseInt(quantity);
                                boolean result = sqliteDB.saveStockIn(getCompanyName,getItemName,getQuantity);
                                if (result){
                                    Toast.makeText(MainActivity.this, "Stock In Successful", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Stock In Failed", Toast.LENGTH_SHORT).show();
                                }

                                alertDialog.dismiss();
                            }

                        }

                    }
                });


            }
        });

        //Stock Out .......................................................

        stockOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder stockOutAlertDialog = new AlertDialog.Builder(MainActivity.this);
                View stockOutView = getLayoutInflater().inflate(R.layout.stock_out_dialog,null);
                ImageView close_image_view = stockOutView.findViewById(R.id.close_image_button);
                final Spinner itemSpinner =  stockOutView.findViewById(R.id.stock_out_item_spinner_id);
                final Spinner companySpinner = stockOutView.findViewById(R.id.stock_out_company_spinner_id);
                final TextView availableQuantityTextView = stockOutView.findViewById(R.id.stock_out_available_quantity_id);
                final TextView reorderTextView = stockOutView.findViewById(R.id.stock_out_reorder_level_id);
                final EditText stockOutQuantityEditText = stockOutView.findViewById(R.id.stock_out_quantity_id);
                Button sellButton = stockOutView.findViewById(R.id.stock_out_sell_button);
                Button damageButton = stockOutView.findViewById(R.id.stock_out_damage_button);
                Button lostButton = stockOutView.findViewById(R.id.stock_out_lost_button);



                Cursor getCompanyFromStockInTable = sqliteDB.getCompanyFromStockIn();
                final ArrayList<String> companyList = new ArrayList();
                companyList.add("Select Company");
                while (getCompanyFromStockInTable.moveToNext()){
                    companyList.add(getCompanyFromStockInTable.getString(getCompanyFromStockInTable.getColumnIndex("CompanyName")));
                }
                getCompanyFromStockInTable.close();

                ArrayAdapter CompanyAdapter = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,companyList);
                CompanyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                companySpinner.setAdapter(CompanyAdapter);

                final ArrayList<String> itemList = new ArrayList<>();
                itemList.add("Select Item");
                companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        itemList.clear();
                        itemList.add("Select Item");
                        isFirstSelect=true;
                        String getCompanyName = companyList.get(i);
                        Cursor getItems = sqliteDB.getItemFromStockInByCompanyName(getCompanyName);

                        while (getItems.moveToNext()){
                            itemList.add(getItems.getString(getItems.getColumnIndex("ItemName")));
                        }
                        getItems.close();

                        ArrayAdapter itemAdapter = new ArrayAdapter(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,itemList);
                        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        itemSpinner.setAdapter(itemAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (isFirstSelect ==true){
                            isFirstSelect = false;
                        }
                        else {
                            String itemName = itemList.get(i);
                            Cursor result = sqliteDB.getItemReorder(itemName);
                            String reorder="0";
                            while (result.moveToNext()){
                                reorder = result.getString(result.getColumnIndex("ReorderLevel"));
                            }
                            reorderTextView.setText(reorder);

                            String companyName = companySpinner.getSelectedItem().toString();

                            itemQuantity = sqliteDB.getItemQuantity(companyName,itemName);

                            availableQuantityTextView.setText(""+itemQuantity);
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                stockOutAlertDialog.setView(stockOutView);
                final AlertDialog stockOutDialog = stockOutAlertDialog.create();
                stockOutDialog.show();

                close_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stockOutDialog.dismiss();
                    }
                });

                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String companyName = companySpinner.getSelectedItem().toString();
                        String itemName = itemSpinner.getSelectedItem().toString();
                        String availableQuantitytxt = availableQuantityTextView.getText().toString();
                        String stockOutQuantity = stockOutQuantityEditText.getText().toString();

                        if (companyName.equals("Select Company") || itemName.equals("Select Item") || stockOutQuantity.equals("")){
                            if (stockOutQuantity.equals("")){
                                stockOutQuantityEditText.setError("Quantity Required.");
                            }
                            if (companyName.equals("Select Company")){

                                ((TextView)companySpinner.getSelectedView()).setError("Error message");
                            }
                            if (itemName.equals("Select Item")){
                                ((TextView)itemSpinner.getSelectedView()).setError("Error message");
                            }
                        }
                        else {

                            itemQuantity = Integer.parseInt(availableQuantitytxt);
                            int getItemQuantity = Integer.parseInt(stockOutQuantity);
                            if (getItemQuantity<= sqliteDB.getItemQuantity(companyName,itemName)){

                                stockOutType = "Sell";


                                boolean result = sqliteDB.saveStockOut(companyName, itemName, getItemQuantity, stockOutType);
                                if (result){
                                    itemQuantity -= getItemQuantity;
                                    sqliteDB.updateQuantity(companyName,itemName,itemQuantity);
                                    Toast.makeText(MainActivity.this, stockOutType+" Successful", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, stockOutType+" Failed", Toast.LENGTH_SHORT).show();
                                }

                                availableQuantityTextView.setText(""+sqliteDB.getItemQuantity(companyName,itemName));

                            }
                            else {
                                stockOutQuantityEditText.setError("Product out of Stock");
                                //Toast.makeText(MainActivity.this, "Product out of Stock", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                });

                damageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String companyName = companySpinner.getSelectedItem().toString();
                        String itemName = itemSpinner.getSelectedItem().toString();
                        String availableQuantitytxt = availableQuantityTextView.getText().toString();
                        String stockOutQuantity = stockOutQuantityEditText.getText().toString();

                        if (companyName.equals("Select Company") || itemName.equals("Select Item") || stockOutQuantity.equals("")){
                            if (stockOutQuantity.equals("")){
                                stockOutQuantityEditText.setError("Quantity Required.");
                            }
                            if (companyName.equals("Select Company")){

                                ((TextView)companySpinner.getSelectedView()).setError("Error message");
                            }
                            if (itemName.equals("Select Item")){
                                ((TextView)itemSpinner.getSelectedView()).setError("Error message");
                            }
                        }
                        else {

                            itemQuantity = Integer.parseInt(availableQuantitytxt);
                            int getItemQuantity = Integer.parseInt(stockOutQuantity);

                            if (getItemQuantity <= sqliteDB.getItemQuantity(companyName, itemName)) {

                                stockOutType = "Damage";


                                boolean result = sqliteDB.saveStockOut(companyName, itemName, getItemQuantity, stockOutType);
                                if (result) {
                                    itemQuantity -= getItemQuantity;
                                    sqliteDB.updateQuantity(companyName, itemName, itemQuantity);
                                    Toast.makeText(MainActivity.this, stockOutType + " Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, stockOutType + " Failed", Toast.LENGTH_SHORT).show();
                                }

                                availableQuantityTextView.setText("" + sqliteDB.getItemQuantity(companyName, itemName));
                            } else {
                                stockOutQuantityEditText.setError("Product out of Stock");
                                //Toast.makeText(MainActivity.this, "Product out of Stock", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                lostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String companyName = companySpinner.getSelectedItem().toString();
                        String itemName = itemSpinner.getSelectedItem().toString();
                        String availableQuantitytxt = availableQuantityTextView.getText().toString();
                        String stockOutQuantity = stockOutQuantityEditText.getText().toString();

                        if (companyName.equals("Select Company") || itemName.equals("Select Item") || stockOutQuantity.equals("")){
                            if (stockOutQuantity.equals("")){
                                stockOutQuantityEditText.setError("Quantity Required.");
                            }
                            if (companyName.equals("Select Company")){

                                ((TextView)companySpinner.getSelectedView()).setError("Error message");
                            }
                            if (itemName.equals("Select Item")){
                                ((TextView)itemSpinner.getSelectedView()).setError("Error message");
                            }

                        }
                        else {

                            itemQuantity = Integer.parseInt(availableQuantitytxt);
                            int getItemQuantity = Integer.parseInt(stockOutQuantity);


                            if (getItemQuantity <= sqliteDB.getItemQuantity(companyName, itemName)) {

                                stockOutType = "Lost";


                                boolean result = sqliteDB.saveStockOut(companyName, itemName, getItemQuantity, stockOutType);
                                if (result) {
                                    itemQuantity -= getItemQuantity;
                                    sqliteDB.updateQuantity(companyName, itemName, itemQuantity);
                                    Toast.makeText(MainActivity.this, stockOutType + " Successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, stockOutType + " Failed", Toast.LENGTH_SHORT).show();
                                }

                                availableQuantityTextView.setText("" + sqliteDB.getItemQuantity(companyName, itemName));
                            } else {
                                stockOutQuantityEditText.setError("Product out of Stock");
                                //Toast.makeText(MainActivity.this, "Product out of Stock", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        //Stock In List
        stockInListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent stockInListViewIntent = new Intent(getApplicationContext(),StockInListActivity.class);
                startActivity(stockInListViewIntent);

            }
        });

        //Stock Out List

        stockOutListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent stockOutListIntent = new Intent(getApplicationContext(),StockOutLIstActivit.class);
                startActivity(stockOutListIntent);
            }
        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SummaryActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (this!=MainActivity.this){
                startActivity(new Intent(this,MainActivity.class));
            }

        } else if (id == R.id.nav_share) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            intent.putExtra(Intent.EXTRA_SUBJECT,"Stock Management App");
            intent.putExtra(Intent.EXTRA_TEXT,"This app help to manage a stock or shop or godown.");

            startActivity(intent.createChooser(intent,"Share With"));

        } else if (id == R.id.nav_feedback) {
            AlertDialog.Builder feedbackAlertDialog = new AlertDialog.Builder(MainActivity.this);
            View feedbackView = getLayoutInflater().inflate(R.layout.feedback_dialog,null);
            final EditText feedbackPersonName = feedbackView.findViewById(R.id.feedback_person_name);
            final EditText feedbackEmail = feedbackView.findViewById(R.id.feedback_email);
            final EditText feedbackMessage = feedbackView.findViewById(R.id.feedback_message);
            ImageView close_image_view = feedbackView.findViewById(R.id.close_image_button);

            Button feedbackSendButton = feedbackView.findViewById(R.id.btn_send_feedback);
            Button feedbackClearButton = feedbackView.findViewById(R.id.btn_clear_feedback);

            feedbackAlertDialog.setView(feedbackView);
            final AlertDialog feedbackDialog = feedbackAlertDialog.create();
            feedbackDialog.show();

            close_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedbackDialog.dismiss();
                }
            });
            feedbackSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        String name = feedbackPersonName.getText().toString();
                        String email = feedbackEmail.getText().toString();
                        String message = feedbackMessage.getText().toString();

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/email");
                        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"aziz12d97@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback from the app");
                        intent.putExtra(Intent.EXTRA_TEXT,"Name : "+name+"\n Email : "+email+"\n Message : "+message);

                        startActivity(Intent.createChooser(intent,"Feedback with"));

                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Exception "+e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            feedbackClearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    feedbackPersonName.setText("");
                    feedbackEmail.setText("");
                    feedbackMessage.setText("");
                }
            });

        }
        else if(id==R.id.nav_rate_us){


        }
        else if (id==R.id.nav_about){

            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
