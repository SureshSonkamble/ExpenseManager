package sonkamble.app.expense;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class AddExpense extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    EditText edit_date,edit_exp_amt,edit_note;
    Button btn_save_exp;
    String crr_date,chk_cat_type;
    int mYear, mMonth, mDay;
    int YRNO, MNO, DNO;
    ImageView img_date;
    Spinner spinner_exp_cat;
    Toolbar toolbar;
    String currentMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //------------------------Toolbar-------------------------------------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);//title
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar_title.setText("Add Expense");
        toolbar_title.setTextColor(0xFFFFFFFF);
        ImageView toolbar_back_arrow=(ImageView)toolbar.findViewById(R.id.toolbar_arrow);//arrow
        toolbar_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExpense.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //current date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        crr_date = df.format(c.getTime());
       // Toast.makeText(getApplicationContext(),"Date:-"+crr_date,Toast.LENGTH_LONG).show();
        databaseHelper=new DatabaseHelper(this);
        final Calendar cd = Calendar.getInstance();
        mYear = cd.get(Calendar.YEAR);
        mMonth = cd.get(Calendar.MONTH);
        mDay = cd.get(Calendar.DAY_OF_MONTH);

        edit_date=(EditText)findViewById(R.id.edit_date);
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpense.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                             //   Toast.makeText(getApplicationContext(),""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,Toast.LENGTH_LONG).show();
                                MNO=monthOfYear + 1;
                                DNO=dayOfMonth;
                                YRNO=year;
                                Log.d("ssss",""+MNO);
                                Log.d("ssss",""+DNO);
                                Log.d("ssss",""+YRNO);
                                edit_date.setText(""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        //=====Date=================
        //Date
       /* img_date=(ImageView) findViewById(R.id.img_date);
        img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExpense.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Toast.makeText(getApplicationContext(),""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,Toast.LENGTH_LONG).show();
                                edit_date.setText(""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });*/
        //----------Category--------------
        spinner_exp_cat=(Spinner)findViewById(R.id.spinner_exp_cat);
        // Spinner Drop down elements
        List<String> list = new ArrayList<String>();
        list.add("Select category");
        list.add("Education");
        list.add("Transportation");
        list.add("Shopping");
        list.add("Food");
        list.add("Medical");
        list.add("Mobile");
        list.add("Other");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_exp_cat.setAdapter(dataAdapter);
        spinner_exp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                chk_cat_type = adapterView.getItemAtPosition(position).toString();
               // Toast.makeText(getApplicationContext(), "Selected: " + chk_cat_type, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edit_exp_amt=(EditText)findViewById(R.id.edit_exp_amt);
        edit_note=(EditText)findViewById(R.id.edit_note);
        btn_save_exp=(Button) findViewById(R.id.btn_save_exp);

        btn_save_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current month name
                SimpleDateFormat formatterMonth = new SimpleDateFormat("MMMM");
                currentMonth = formatterMonth.format(new Date(System.currentTimeMillis()));
                if(MNO==1){ currentMonth="January"; }
                if(MNO==2){ currentMonth="February"; }
                if(MNO==3){ currentMonth="March"; }
                if(MNO==4){ currentMonth="April"; }
                if(MNO==5){ currentMonth="May"; }
                if(MNO==6){ currentMonth="June"; }
                if(MNO==7){ currentMonth="July"; }
                if(MNO==8){ currentMonth="August"; }
                if(MNO==9){ currentMonth="September"; }
                if(MNO==10){ currentMonth="Octomber"; }
                if(MNO==11){ currentMonth="November"; }
                if(MNO==12){ currentMonth="December"; }
                Toast.makeText(getApplicationContext(), ""+currentMonth, Toast.LENGTH_SHORT).show();
                String str = edit_date.getText().toString();
                String str1 = chk_cat_type;
                String str2 = edit_exp_amt.getText().toString();
                String str3 = edit_note.getText().toString();

                if (str.equals("")) {
                    Toast.makeText(getApplicationContext(), "Date cannot be Blank", Toast.LENGTH_LONG).show();
                } else if (str1.equals("Select category")) {
                    Toast.makeText(getApplicationContext(), "Please select valid category", Toast.LENGTH_LONG).show();
                } else if (str2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Amount cannot be Blank", Toast.LENGTH_LONG).show();
                } else if (str3.equals("")) {
                    Toast.makeText(getApplicationContext(), "Notes cannot be Blank ", Toast.LENGTH_LONG).show();
                } else {
                    boolean result = databaseHelper.insert_exp_Data(str, str1, str2, str3,currentMonth);
                    if (result) {
                        edit_exp_amt.setText("");
                        edit_date.setText("");
                        edit_note.setText("");
                        Toast.makeText(getApplicationContext(), "Expense Added Successfully", Toast.LENGTH_LONG).show();
                    } else
                            Toast.makeText(getApplicationContext(), "Error..", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
