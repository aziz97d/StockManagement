package com.example.abdulaziz.stockmanagement;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

public class SummaryActivity extends AppCompatActivity {

    private BarChart barChart;
    private EditText dateEditText;
    private RadioGroup typeRadioGroup;
    private RadioButton typeRadioButton;
    private Button showChartButton;
    private DatePickerDialog datePickerDialog;
    private Calendar mCalendar;
    int day,month,year;
    private String type;

    private List<BarChartModel> barChartModelList;

    private SqliteDB sqliteDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sqliteDB = new SqliteDB(this);

        barChart = findViewById(R.id.bar_chart_vertical);
        barChart.setBarMaxValue(200);

        dateEditText = findViewById(R.id.dateEDT);
        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        showChartButton = findViewById(R.id.showChartButton);

        barChartModelList = new ArrayList<>();

        mCalendar = Calendar.getInstance();
        day =mCalendar.get(Calendar.DAY_OF_MONTH);
        month = mCalendar.get(Calendar.MONTH);
        year = mCalendar.get(Calendar.YEAR);


        //dateEditText.setText(day+"-"+month+"-"+year);


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = new DatePickerDialog(SummaryActivity.this,

                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month+1;
                                String  zero="";
                                if (month<10){
                                    zero = "0";
                                }
                                dateEditText.setText(day+"-"+zero+""+month+"-"+year);
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });


        showChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int typeId = typeRadioGroup.getCheckedRadioButtonId();


                String date = dateEditText.getText().toString();

                if (date.isEmpty()){
                    Toast.makeText(SummaryActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
                }
                else if (typeId==-1){

                    Toast.makeText(SummaryActivity.this, "Select Type", Toast.LENGTH_SHORT).show();
                }
                else {
                    typeRadioButton = findViewById(typeId);
                    type = typeRadioButton.getText().toString();
                    makeChart(type,date);
                }

            }
        });


    }

    public void makeChart(String type, String date){

        Cursor result = sqliteDB.getItemAndQuantityByDate(type,date);

        if(result.getCount()==0){
            Toast.makeText(this, "No Data Found ", Toast.LENGTH_SHORT).show();
        }


        barChartModelList.clear();
        while (result.moveToNext()){

            String itemName = result.getString(result.getColumnIndex("ItemName"));
            String quantity = result.getString(result.getColumnIndex("Quantity"));
            int quantityInt = Integer.parseInt(quantity);

            BarChartModel barChartModel = new BarChartModel();
            barChartModel.setBarValue(quantityInt);
            //barChartModel.setBarColor(Color.parseColor("#9C27B0"));
            barChartModel.setBarTag(quantityInt); //You can set your own tag to bar model
            barChartModel.setBarText(itemName+"\n"+"("+quantity+")");

            barChartModelList.add(barChartModel);
        }

        barChart.clearAll();
        barChart.addBar(barChartModelList);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
