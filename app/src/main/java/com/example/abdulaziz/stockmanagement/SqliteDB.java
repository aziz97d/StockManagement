package com.example.abdulaziz.stockmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SqliteDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "stockManagement_DB";
    private static final String CATEGORY_TABLE ="CategoryTB";
    private static final String COMPANY_TABLE ="CompanyTB";
    private static final String ITEM_TABLE = "ItemTB";
    private static final String STOCK_IN_TABLE = "StockInTB";
    private static final String STOCK_OUT_TABLE = "StockOutTB";


    private static final String CATEGORY_ID = "Id";
    private static final String CATEGORY_NAME = "CategoryName";


    private static final String COMPANY_ID = "Id";
    private static final String COMPANY_NAME = "CompanyName";


    private static final String ITEM_ID = "Id";
    private static final String ITEM_CATEGORY_NAME = "CategoryName" ;
    private static final String ITEM_COMPANY_NAME = "CompanyName";
    private static final String ITEM_NAME = "ItemName";
    private static final String ITEM_REORDER = "ReorderLevel";


    private static final String STOCK_IN_ID = "Id";
    private static final String STOCK_IN_CATEGORY_NAME = "CategoryName";
    private static final String STOCK_IN_COMPANY_NAME = "CompanyName";
    private static final String STOCK_IN_ITEM_NAME = "ItemName";
    private static final String STOCK_IN_QUANTITY = "Quantity";
    private static final String STOCK_IN_DATE = "Date";


    private static final String STOCK_OUT_ID = "Id";
    private static final String STOCK_OUT_CATEGORY_NAME = "CategoryName";
    private static final String STOCK_OUT_COMPANY_NAME = "CompanyName";
    private static final String STOCK_OUT_ITEM_NAME = "ItemName";
    private static final String STOCK_OUT_QUANTITY = "Quantity";
    private static final String STOCK_OUT_TYPE = "Type";
    private static final String STOCK_OUT_DATE = "Date";


    String categoryTableQuery = "CREATE TABLE "+CATEGORY_TABLE+" ("+CATEGORY_ID+" integer primary key autoincrement, "+CATEGORY_NAME+" text)";
    String companyTableQuery = "CREATE TABLE "+COMPANY_TABLE+" ("+COMPANY_ID+" integer primary key autoincrement, "+COMPANY_NAME+" text)";
    String itemTableQuery = "CREATE TABLE "+ITEM_TABLE+" ("+ITEM_ID+" integer primary key autoincrement, "+ITEM_CATEGORY_NAME+" text, "+ITEM_COMPANY_NAME+" text, "+ITEM_NAME+" text, "+ITEM_REORDER+" text)";
    String stockInTableQuery = "CREATE TABLE "+STOCK_IN_TABLE+" ("+STOCK_IN_ID+" integer primary key autoincrement, "+STOCK_IN_CATEGORY_NAME+" text, "+STOCK_IN_COMPANY_NAME+" text, "+STOCK_IN_ITEM_NAME+" text, "+STOCK_IN_QUANTITY+" text, "+STOCK_IN_DATE+" text )";
    String stockOutTableQuery = "CREATE TABLE "+STOCK_OUT_TABLE+" ("+STOCK_OUT_ID+" integer primary key autoincrement, "+STOCK_OUT_CATEGORY_NAME+" text, "+STOCK_OUT_COMPANY_NAME+" text, "+STOCK_OUT_ITEM_NAME+" text, "+STOCK_OUT_QUANTITY+" text, "+STOCK_OUT_TYPE+" text, "+STOCK_OUT_DATE+" text )";


    Context context;
    public SqliteDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL(categoryTableQuery);
            sqLiteDatabase.execSQL(companyTableQuery);
            sqLiteDatabase.execSQL(itemTableQuery);
            sqLiteDatabase.execSQL(stockInTableQuery);
            sqLiteDatabase.execSQL(stockOutTableQuery);
        }catch (Exception e){
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try{

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CATEGORY_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+COMPANY_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ITEM_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+STOCK_IN_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+STOCK_OUT_TABLE);


            sqLiteDatabase.execSQL(categoryTableQuery);
            sqLiteDatabase.execSQL(companyTableQuery);
            sqLiteDatabase.execSQL(itemTableQuery);
            sqLiteDatabase.execSQL(stockInTableQuery);
            sqLiteDatabase.execSQL(stockOutTableQuery);

        }catch (Exception e){
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean saveCategory(String categoryName){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_NAME,categoryName);

        long result =  db.insert(CATEGORY_TABLE,null,contentValues);

        if (result>0){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean saveCompany(String companyName){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_NAME,companyName);

        long result =  db.insert(COMPANY_TABLE,null,contentValues);

        if (result>0){
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor getAllCategory(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+CATEGORY_TABLE, null);
        return result;
    }
    public Cursor getAllCompany(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+COMPANY_TABLE, null);
        return result;
    }



    public boolean saveItem(String companyName, String categoryName, String itemName, String reorderLevel){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_CATEGORY_NAME,categoryName);
        contentValues.put(ITEM_COMPANY_NAME,companyName);
        contentValues.put(ITEM_NAME,itemName);
        contentValues.put(ITEM_REORDER,reorderLevel);

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long rowAffect = sqLiteDatabase.insert(ITEM_TABLE,null,contentValues);
        if (rowAffect>0){
            return true;
        }else {
            return false;
        }

    }

    public Cursor getItemByCompany(String companyName){
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+ITEM_TABLE+" WHERE CompanyName=?",new String[] {companyName});
        return result;
    }

    public Cursor getItemReorder(String itemName){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM "+ITEM_TABLE+" WHERE ItemName=?",new String[]{itemName});
        return result;
    }

    public int getItemQuantity(String companyName, String itemName){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM "+STOCK_IN_TABLE+" WHERE CompanyName=? AND ItemName=?",new String[]{companyName,itemName});

        if (result.getCount()==0){
            return 0;
        }
        else {
            String quantity="";
            while (result.moveToNext()){
                 quantity = result.getString(result.getColumnIndex(STOCK_IN_QUANTITY));
            }
            return Integer.parseInt(quantity);
        }

    }


    public boolean isExistsItem(String companyName, String categoryName, String itemName){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor isExists = sqLiteDatabase.rawQuery("SELECT * FROM "+ITEM_TABLE+" WHERE "+ITEM_CATEGORY_NAME+"=? AND "+ITEM_COMPANY_NAME+"=? AND "+ITEM_NAME+"=?",new String[]{categoryName,companyName,itemName});

        if (isExists.getCount()==0){
            return false;
        }
        else {
            return true;
        }

    }

    public boolean saveStockIn(String companyName, String itemName, int quantity){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateNow = dateFormat.format(date);
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOCK_IN_COMPANY_NAME,companyName);
        contentValues.put(STOCK_IN_ITEM_NAME,itemName);
        contentValues.put(STOCK_IN_QUANTITY,quantity);
        contentValues.put(STOCK_IN_DATE,dateNow);

        long rowAffect = sqLiteDatabase.insert(STOCK_IN_TABLE,null,contentValues);

        if (rowAffect>0){
            return true;
        }
        else {
            return false;
        }
    }


    public boolean isExistsItemAndCompany(String companyName, String itemName){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM "+STOCK_IN_TABLE+" WHERE CompanyName=? AND ItemName=?",new String[]{companyName,itemName});
        if (result.getCount()==0){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean updateQuantity(String companyName, String itemName, int quantity){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STOCK_IN_QUANTITY,quantity);
        int result = sqLiteDatabase.update(STOCK_IN_TABLE,contentValues,"CompanyName=? AND ItemName=?",new String[]{companyName,itemName});
        if (result>0){
            return true;
        }
        else {
            return false;
        }
    }

    //..............
    public Cursor getCompanyFromStockIn(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT "+STOCK_IN_COMPANY_NAME+" FROM "+STOCK_IN_TABLE+" UNION SELECT "+STOCK_IN_COMPANY_NAME+" FROM "+STOCK_IN_TABLE,new String[]{});
        return result;
    }

    public Cursor getItemFromStockInByCompanyName(String companyName){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM "+STOCK_IN_TABLE+" WHERE CompanyName=?",new String[]{companyName});
        return result;
    }


    public boolean saveStockOut(String companyName, String itemName, int stockOutQuantity, String stockOutType){

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        //String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateNow = dateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put(STOCK_OUT_COMPANY_NAME,companyName);
        contentValues.put(STOCK_OUT_ITEM_NAME,itemName);
        contentValues.put(STOCK_OUT_QUANTITY,stockOutQuantity);
        contentValues.put(STOCK_OUT_TYPE,stockOutType);
        contentValues.put(STOCK_OUT_DATE,dateNow);

        long result = sqLiteDatabase.insert(STOCK_OUT_TABLE,null,contentValues);

        if (result>0){
            return true;
        }
        else {
            return false;
        }
    }

    public Cursor getAllStockInData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM "+STOCK_IN_TABLE, new String[]{});
        return result;
    }

    public Cursor getAllStockOutData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM "+STOCK_OUT_TABLE, new String[]{});
        return result;
    }

    public Cursor getItemAndQuantityByDate(String type, String date){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT "+STOCK_OUT_ITEM_NAME+", "+STOCK_OUT_QUANTITY+" FROM "+STOCK_OUT_TABLE+" WHERE "+STOCK_OUT_TYPE+"=? AND "+STOCK_OUT_DATE+"=? ",new String[]{type,date});
        return result;
    }
}
