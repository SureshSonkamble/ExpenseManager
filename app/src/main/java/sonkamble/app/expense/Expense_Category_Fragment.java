package sonkamble.app.expense;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Expense_Category_Fragment extends Fragment {

    DatabaseHelper databaseHelper;
    TextView list_total,txt_name,txt_number,txt_email;
    static HashMap<String, String> attendance = new HashMap<>();
    public ArrayList<HashMap<String, String>> attendance_data = new ArrayList<>();
    Button btn_exp_show_data,btn_pdf_export;
    atnds_recyclerAdapter attendance_recyclerAdapter;
    private RecyclerView recycler_medal_offline_rpt_list;
    //===Grid=================
    Double ptotal=0.0;
    String tot_exp,cat;
    ArrayList<HashMap<String, String>> contact_arryList;
    private RecyclerView.LayoutManager layoutManager_pe;
    private RecyclerView recyclerView_attendance;
    Toolbar toolbar;
    Spinner spinner_exp_cat;
    String category;
    //=====PDF=========================
    PdfPTable table = new PdfPTable(7);
    PdfPCell cell1, cell2,cell3,cell4, cell5,cell6,cell7, cell8,cell9,cell10,cell11,cell12,cell13,cell14;
    File cacheDir;
    final Context context = getActivity();
    private static final int PERMISSION_REQUEST_CODE = 1;
    //==================================
    public Expense_Category_Fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cat_exp_report_, container, false);
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

        //----------Milk Type--------------
        spinner_exp_cat = (Spinner) view.findViewById(R.id.spinner_rpt_exp_cat);
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_exp_cat.setAdapter(dataAdapter);

        spinner_exp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                // cid = contact_arryList.get(position).get("id");
                cat = adapterView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selected: " + chk_milk_type, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       /* btn_pdf_export=(Button)view.findViewById(R.id.btn_pdf_export);
        btn_pdf_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_pdf_permission();
            }
        });*/
        btn_exp_show_data=(Button)view.findViewById(R.id.btn_exp_show_data);
        btn_exp_show_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category=cat;
                show_cat_data();
            }
        });
    }
        public void show_cat_data()
        {
            if(category.equals("Select category"))
            {
                Toast.makeText(getActivity(), "Please select valid category", Toast.LENGTH_LONG).show();
            }
        databaseHelper=new DatabaseHelper(getActivity());
            ptotal=0.0;
            list_total.setText(" ");
            contact_arryList.clear();
        Cursor c= databaseHelper.show_cat_exp_Data(category);
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
                list_total.setText(tot_exp);
                map.put("id", id );
                map.put("name", name );
                map.put("number", number );
                map.put("email", email );
                //  map.put("tot_exp", tot_exp );
                contact_arryList.add(map);
            } while (c.moveToNext());
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
