package sonkamble.app.expense;


import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Expense_To_ans_From_Datewise_Fragment extends Fragment {

    DatabaseHelper databaseHelper;
    TextView list_total,txt_name,txt_number,txt_email;
    static HashMap<String, String> attendance = new HashMap<>();
    public ArrayList<HashMap<String, String>> attendance_data = new ArrayList<>();
    int YRNO, MNO, DNO,YRNO2, MNO2, DNO2;
    atnds_recyclerAdapter attendance_recyclerAdapter;
    private RecyclerView recycler_medal_offline_rpt_list;
    //===Grid=================
    Double ptotal=0.0;
    String tot_exp;
    ArrayList<HashMap<String, String>> contact_arryList;
    private RecyclerView.LayoutManager layoutManager_pe;
    private RecyclerView recyclerView_attendance;
    Toolbar toolbar;
    ImageView exp_rpt_img_date,exp_rpt_img_date2;
    int mYear, mMonth, mDay;
    EditText exp_rpt_edit_date,exp_rpt_edit_date2;
    Button btn_date_exp_show_data;
    String date,date2;
    public Expense_To_ans_From_Datewise_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.to_and_from_date_exp_report_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list_total = (TextView) view.findViewById(R.id.list_total);
        contact_arryList = new ArrayList<HashMap<String, String>>();
        recycler_medal_offline_rpt_list = (RecyclerView) view.findViewById(R.id.recycler_exp_list);
        layoutManager_pe = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_medal_offline_rpt_list.setLayoutManager(layoutManager_pe);
        attendance_recyclerAdapter = new atnds_recyclerAdapter(getActivity(), contact_arryList);
        recycler_medal_offline_rpt_list.setAdapter(attendance_recyclerAdapter);

        //=====Date=================
        //Date
        final Calendar cd = Calendar.getInstance();
        mYear = cd.get(Calendar.YEAR);
        mMonth = cd.get(Calendar.MONTH);
        mDay = cd.get(Calendar.DAY_OF_MONTH);

        exp_rpt_edit_date = (EditText) view.findViewById(R.id.exp_rpt_edit_date);
        exp_rpt_img_date = (ImageView) view.findViewById(R.id.exp_rpt_img_date);
        exp_rpt_img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Toast.makeText(getActivity(), "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_LONG).show();
                                exp_rpt_edit_date.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                MNO=monthOfYear + 1;
                                DNO=dayOfMonth;
                                YRNO=year;
                                Log.d("ssss",""+MNO);
                                Log.d("ssss",""+DNO);
                                Log.d("ssss",""+YRNO);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        //===========================================

        //=====Date=================
        //Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        exp_rpt_edit_date2 = (EditText) view.findViewById(R.id.exp_rpt_edit_date2);
        exp_rpt_img_date2 = (ImageView) view.findViewById(R.id.exp_rpt_img_date2);
        exp_rpt_img_date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Toast.makeText(getActivity(), "" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_LONG).show();
                                exp_rpt_edit_date2.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                MNO2=monthOfYear + 1;
                                DNO2=dayOfMonth;
                                YRNO2=year;
                                Log.d("ssss",""+MNO2);
                                Log.d("ssss",""+DNO2);
                                Log.d("ssss",""+YRNO2);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        //===========================================

        btn_date_exp_show_data=(Button)view.findViewById(R.id.btn_date_exp_show_data);
        btn_date_exp_show_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  date=exp_rpt_edit_date.getText().toString();
                date2=exp_rpt_edit_date2.getText().toString();
                show_date_exp_data();
            }
        });

    }
    public  void show_date_exp_data()
    {
        String exp_date=date;
        String exp_date2=date2;
        databaseHelper=new DatabaseHelper(getActivity());

        ptotal=0.0;
        list_total.setText(" ");
        contact_arryList.clear();
        Cursor c= databaseHelper.show_date_exp_Data_Wise(exp_date,exp_date2);
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {

                HashMap<String, String> map = new HashMap<String, String>();

                String id  =c.getString(0);
                String name  =c.getString(1);
                String number  =c.getString(3);
                String email  =c.getString(2);

                Double pttlamt=Double.parseDouble(number);
                ptotal=ptotal+pttlamt;
                tot_exp= Double.toString(ptotal);
                //Toast.makeText(getApplicationContext(), "Total Expense:" + tot_exp, Toast.LENGTH_SHORT).show();


                map.put("id", id );
                map.put("name", name );
                map.put("number", number );
                map.put("email", email );
                //  map.put("tot_exp", tot_exp );


                contact_arryList.add(map);

            } while (c.moveToNext());

            list_total.setText(tot_exp);
        }
        else{
            Toast.makeText(getActivity(),"No Data found for list ...",Toast.LENGTH_SHORT).show();
        }

        Log.d("Attendance_End_Data",""+contact_arryList.toString());

        if (attendance_recyclerAdapter != null) {
            attendance_recyclerAdapter.notifyDataSetChanged();
            System.out.println("Adapter " + attendance_recyclerAdapter.toString());
        }
    }

    public class atnds_recyclerAdapter extends RecyclerView.Adapter<atnds_recyclerAdapter.Pex_ViewHolder>{
        Context context;
        ArrayList<HashMap<String, String>> attendance_list;

        public atnds_recyclerAdapter(Context context, ArrayList<HashMap<String,String>>antds_list)
        {
            this.attendance_list=antds_list;
            this.context=context;
        }

        @Override
        public atnds_recyclerAdapter.Pex_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.exp_rep_list,parent,false);
            atnds_recyclerAdapter.Pex_ViewHolder viewHolder=new atnds_recyclerAdapter.Pex_ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(atnds_recyclerAdapter.Pex_ViewHolder holder, final int position) {
            holder.contact_list_id.setText(attendance_list.get(position).get("id"));
            holder.contact_list_name.setText(attendance_list.get(position).get("name"));
            holder.contact_list_number.setText(attendance_list.get(position).get("number"));
            holder.contact_list_email.setText(attendance_list.get(position).get("email"));
            //holder.list_total.setText(attendance_list.get(position).get("tot_exp"));

        }

        @Override
        public int getItemCount() {
            return attendance_list.size();
        }


        public class Pex_ViewHolder extends RecyclerView.ViewHolder {
            TextView contact_list_id, contact_list_name,contact_list_number,contact_list_email,list_total;
            TableLayout layout_gird_position;
            public Pex_ViewHolder(View itemView) {
                super(itemView);
                this.layout_gird_position = (TableLayout) itemView.findViewById(R.id.table_gird_position);
                this.contact_list_id = (TextView) itemView.findViewById(R.id.list_id);
                this.contact_list_name = (TextView) itemView.findViewById(R.id.list_name);
                this.contact_list_number = (TextView) itemView.findViewById(R.id.list_number);
                this.contact_list_email = (TextView) itemView.findViewById(R.id.list_email);
                this.list_total = (TextView) itemView.findViewById(R.id.list_total);
            }
        }
    }
}

