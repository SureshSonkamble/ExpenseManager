package sonkamble.app.expense;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
     LinearLayout btn_add_expense,btn_reports,lin_month_rpt;
    Double ptotal=0.0;
    Toolbar toolbar;
    TextView txt_jan,txt_feb,txt_mar,txt_apr,txt_may,txt_jun,txt_jly,txt_aug,txt_sept,txt_oct,txt_nov,txt_dec;
    String edu_cat_tot_exp,number;
    DatabaseHelper databaseHelper;
    float a,b,cc,d,e,cat_edu=0,cat_med=0,cat_transf=0,cat_shop=0,cat_mob=0;
    int jan=0,feb=0,mar=0,apr=0,may=0,jun=0,jly=0,aug=0,sept=0,oct=0,nov=0,dec=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // txt_jan=(TextView) findViewById(R.id.txt_jan);
       // txt_feb=(TextView)findViewById(R.id.txt_feb);
        //------------------------Toolbar-------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("Expense Manager");
        toolbar_title.setTextColor(0xFFFFFFFF);
        ImageView toolbar_reset=(ImageView)toolbar.findViewById(R.id.img_reset);//arrow
        toolbar_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Do you Really Want To Reset?");
                alertDialog.setMessage("All of this application's data will be deleted permanently.\n Database will be get clear\nApp will Forced stop");
                alertDialog.setIcon(R.drawable.fail);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAppData();
                    }
                });
                alertDialog.show();

            }
        });
        databaseHelper=new DatabaseHelper(getApplicationContext());
        //---------------------------------------
        /* jan();
         feb();
         mar();
         apr();
         may();
         jun();
         jly();
         aug();
         sept();
         oct();
         nov();
         dec();*/
        //---------------------------------------

        cat_Education();
        cat_Transportation();
        cat_Shopping();
        cat_Mobile();
        cat_Medical();
         //Pie Chart

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList NoOfEmp = new ArrayList();
        NoOfEmp.add(new Entry(cat_edu, 0));
        NoOfEmp.add(new Entry(cat_transf, 1));
        NoOfEmp.add(new Entry(cat_shop, 2));
        NoOfEmp.add(new Entry(cat_med, 3));
        NoOfEmp.add(new Entry(cat_mob, 4));

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Expenses");

        ArrayList year = new ArrayList();

        year.add("Food");
        year.add("Transportation");
        year.add("Shopping");
        year.add("Medical");
        year.add("Mobile");

        PieData data = new PieData(year, dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(4000, 4000);

        btn_add_expense=(LinearLayout)findViewById(R.id.btn_add_expense);
        btn_add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AddExpense.class);
                startActivity(i);
            }
        });


        btn_reports=(LinearLayout)findViewById(R.id.btn_reports);
        btn_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Expense_Reports.class);
                startActivity(i);
            }
        });

       /* lin_month_rpt=(LinearLayout)findViewById(R.id.lin_month_rpt);
        lin_month_rpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Expense_Reports.class);
                startActivity(i);
            }
        });*/
    }

    void cat_Education()
    {
        cat_edu=0;
        Cursor c= databaseHelper.show_cat_exp_Data("Food");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {

                HashMap<String, String> map = new HashMap<String, String>();

                 number = c.getString(3);
                Log.d("eeee",number);
                a = Integer.parseInt(number);
                cat_edu=cat_edu+a;
            }
            while (c.moveToNext());
        }
        else{
          //  Toast.makeText(getApplicationContext(),"No Data found for list ...",Toast.LENGTH_SHORT).show();
        }
        }
    void cat_Transportation()
    {
        cat_transf=0;
        Cursor c= databaseHelper.show_cat_exp_Data("Transportation");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {

                HashMap<String, String> map = new HashMap<String, String>();

                number = c.getString(3);
                Log.d("eeee",number);
                b = Integer.parseInt(number);
                cat_transf=cat_transf+b;
            }
            while (c.moveToNext());
        }
        else{
           // Toast.makeText(getApplicationContext(),"No Data found for list ...",Toast.LENGTH_SHORT).show();
        }
    }
    void cat_Shopping()
    {
        cat_shop=0;
        Cursor c= databaseHelper.show_cat_exp_Data("Shopping");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {

                HashMap<String, String> map = new HashMap<String, String>();

                number = c.getString(3);
                Log.d("eeee",number);
                cc = Integer.parseInt(number);
                cat_shop=cat_shop+cc;
            }
            while (c.moveToNext());
        }
        else{
        //    Toast.makeText(getApplicationContext(),"No Data found for list ...",Toast.LENGTH_SHORT).show();
        }
    }
    void cat_Mobile()
    {
        cat_mob=0;
        Cursor c= databaseHelper.show_cat_exp_Data("Mobile");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {

                HashMap<String, String> map = new HashMap<String, String>();

                number = c.getString(3);
                Log.d("eeee",number);
                e = Integer.parseInt(number);
                cat_mob=cat_mob+e;
            }
            while (c.moveToNext());
        }
        else{
     //       Toast.makeText(getApplicationContext(),"No Data found for list ...",Toast.LENGTH_SHORT).show();
        }
    }
    void cat_Medical()
    {
        cat_med=0;
        Cursor c= databaseHelper.show_cat_exp_Data("Medical");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {

                HashMap<String, String> map = new HashMap<String, String>();
                number = c.getString(3);
                Log.d("eeee",number);
                d = Integer.parseInt(number);
                cat_med=cat_med+d;
            }
            while (c.moveToNext());
        }
        else{
     //       Toast.makeText(getApplicationContext(),"No Data found for list ...",Toast.LENGTH_SHORT).show();
        }
    }

    void jan()
    {
        jan=0;
        Cursor c= databaseHelper.show_month_exp_Data("January");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("eeee",number);
                d = Integer.parseInt(number);
                jan= (int) (jan+d);
            }
            while (c.moveToNext());
        }
        try {
            if(jan!=0)
            {
                txt_jan.setText(""+jan);
            }
        }catch (Exception e){

        }
    }
    void feb()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("February");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                feb= (int) (feb+d);

            }
            while (c.moveToNext());
        }
        try {
            if(feb!=0)
            {
                txt_feb.setText("" + feb);
            }
        }catch (Exception e){   }
    }
    void mar()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("March");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                mar= (int) (mar+d);

            }
            while (c.moveToNext());
        }
       try {
            if(mar!=0)
            {
                txt_mar.setText("" + mar);
            }
        }catch (Exception e){   }
    }
    void apr()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("April");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                 Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                apr= (int) (apr+d);

            }
            while (c.moveToNext());
        }
        try {
            if(apr!=0)
            {
                txt_apr.setText("" + apr);
            }
        }catch (Exception e){

        }
    }
    void may()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("May");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                may= (int) (may+d);

            }
            while (c.moveToNext());
        }
        try {
            if(may!=0)
            {
                txt_may.setText("" + may);
            }
        }catch (Exception e){        }
    }
    void jun()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("June");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                jun= (int) (jun+d);

            }
            while (c.moveToNext());
        }
        try {
            if(jun!=0)
            {
                txt_jun.setText("" + jun);
            }
        }catch (Exception e){     }
    }
    void jly()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("July");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                jly= (int) (jly+d);

            }
            while (c.moveToNext());
        }
        try {
            if(jly!=0)
            {
                txt_jly.setText("" + jly);
            }
        }catch (Exception e){     }
    }
    void aug()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("August");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                aug= (int) (aug+d);

            }
            while (c.moveToNext());
        }
        try {
            if(aug!=0)
            {
                txt_aug.setText("" + aug);
            }
        }catch (Exception e){     }
    }
    void sept()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("September");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                sept= (int) (sept+d);

            }
            while (c.moveToNext());
        }
        try {
            if(sept!=0)
            {
                txt_sept.setText("" + sept);
            }
        }catch (Exception e){     }
    }
    void oct()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("Octomber");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                oct= (int) (oct+d);

            }
            while (c.moveToNext());
        }
        try {
            if(oct!=0)
            {
                txt_oct.setText("" + oct);
            }
        }catch (Exception e){     }
    }
    void nov()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("November");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                nov= (int) (nov+d);

            }
            while (c.moveToNext());

        }
        try {
            if(nov!=0)
            {
                txt_nov.setText("" + nov);
            }
        }catch (Exception e){     }
    }
    void dec()
    {
        feb=0;
        Cursor c= databaseHelper.show_month_exp_Data("December");
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                number = c.getString(3);
                Log.d("ffff",""+number);
                d = Integer.parseInt(number);
                dec= (int) (dec+d);

            }
            while (c.moveToNext());
        }
        try {
            if(dec!=0)
            {
                txt_dec.setText("" + dec);
            }
        }catch (Exception e){     }
    }
    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

