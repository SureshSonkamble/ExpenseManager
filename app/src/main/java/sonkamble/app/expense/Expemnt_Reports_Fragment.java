package sonkamble.app.expense;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.DatePicker;
import android.widget.EditText;
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

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Expemnt_Reports_Fragment extends Fragment {

    DatabaseHelper databaseHelper;
    TextView list_total,txt_name,txt_number,txt_email;
    static HashMap<String, String> attendance = new HashMap<>();
    public ArrayList<HashMap<String, String>> attendance_data = new ArrayList<>();
    Button btn_pdf_export,btn_pdf_open;
    atnds_recyclerAdapter attendance_recyclerAdapter;
    private RecyclerView recycler_medal_offline_rpt_list;
    //===Grid=================
    Double ptotal=0.0;
    String tot_exp,chk_cat_type,cid,note,amt,dt;
    ArrayList<HashMap<String, String>> contact_arryList;
    private RecyclerView.LayoutManager layoutManager_pe;
    private RecyclerView recyclerView_attendance;
    Toolbar toolbar;
    Spinner spinner_exp_cat;
    String category;
    EditText edit_date,edit_exp_amt,edit_note;
    Button btn_save_exp,btn_del_exp;
    AlertDialog dialog;
    int mYear, mMonth, mDay;
    Button btn_cust_popup_update,btn_cust_popup_cancle;
    //=====PDF=========================
    PdfPTable table = new PdfPTable(7);
    PdfPCell cell1, cell2,cell3,cell4, cell5,cell6,cell7, cell8,cell9,cell10,cell11,cell12,cell13,cell14;
    File cacheDir;
    final Context context = getActivity();
    private static final int PERMISSION_REQUEST_CODE = 1;
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    //==================================
    public Expemnt_Reports_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expemnt__reports_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSignaturePad = (SignaturePad) view.findViewById(R.id.signature_pad);
        mClearButton = (Button) view.findViewById(R.id.clear_button);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(getActivity(), "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mClearButton.setEnabled(true);

            }

            @Override
            public void onClear() {
                mClearButton.setEnabled(false);

            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });
        list_total = (TextView) view.findViewById(R.id.list_total);
        contact_arryList = new ArrayList<HashMap<String, String>>();
        recycler_medal_offline_rpt_list = (RecyclerView) view.findViewById(R.id.recycler_exp_list);
        layoutManager_pe = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_medal_offline_rpt_list.setLayoutManager(layoutManager_pe);
        attendance_recyclerAdapter = new atnds_recyclerAdapter(getActivity(), contact_arryList);
        recycler_medal_offline_rpt_list.setAdapter(attendance_recyclerAdapter);
        btn_pdf_export=(Button)view.findViewById(R.id.btn_pdf_export);


        btn_pdf_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_pdf_permission();
            }
        });
        show_cat_data();
    }
    public void show_cat_data()
    {
        databaseHelper=new DatabaseHelper(getActivity());
        ptotal=0.0;
        list_total.setText(" ");
        contact_arryList.clear();
        Cursor c= databaseHelper.show_exp_Data();
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                String id  =c.getString(0);
                String date  =c.getString(1);
                String cat  =c.getString(2);
                String amt  =c.getString(3);
                String note  =c.getString(4);

                Double pttlamt=Double.parseDouble(amt);
                ptotal=ptotal+pttlamt;
                tot_exp= Double.toString(ptotal);
                //Toast.makeText(getActivity(), "Total Expense:" + tot_exp, Toast.LENGTH_SHORT).show();

                list_total.setText(tot_exp);
                map.put("id", id );
                map.put("date", date );
                map.put("amt", amt );
                map.put("note", note );
                map.put("cat", cat );
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
            holder.contact_list_name.setText(attendance_list.get(position).get("date"));
            holder.contact_list_number.setText(attendance_list.get(position).get("amt"));
            holder.contact_list_email.setText(attendance_list.get(position).get("cat"));
            //holder.list_total.setText(attendance_list.get(position).get("tot_exp"));
            holder.layout_gird_position.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cid=attendance_list.get(position).get("id");
                    note=attendance_list.get(position).get("note");
                    dt=attendance_list.get(position).get("date");
                    amt=attendance_list.get(position).get("amt");
                    expnc_popup_alert();
                }
            });
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

    //------pdf--------------------
    public void check_pdf_permission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                save_pdf();
            } else {
                requestPermission(); // Code for permission
            }
        } else {

            //==========================
            save_pdf();
            // Toast.makeText(Scan_Master_Reports.this, "Below 23 API Oriented Device....", Toast.LENGTH_SHORT).show();
        }
    }
    //-----------------------------
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED ) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)&&ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
    public void save_pdf()
    {
        //-------------PDF-------------------

        String FILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/rpt.pdf";
        //String FILE = Environment.getExternalStorageDirectory().toString() + "/PDF Report/" + "report.pdf";
        // Create New Blank Document
        Document document = new Document(PageSize.A4);

        // Create Pdf Writer for Writting into New Created Document
        try {
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            // Open Document for Writting into document
            document.open();
            document.add(table);

            // User Define Method
            addTitlePage(document);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }
        // Close Document after writting all content
        document.close();
        File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS)+"/rpt.pdf");
        Toast.makeText(getActivity(), "PDF File is Created."+FILE, Toast.LENGTH_LONG).show();

        try {
           // File filePath = new File(""+outputFile);

            Uri uri = Uri.fromFile(outputFile);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage("com.whatsapp");
            startActivity(share);
//            final ComponentName name = new ComponentName("com.whatsapp", "com.whatsapp.ContactPicker");
//            Intent oShareIntent = new Intent();
//            oShareIntent.setComponent(name);
//            oShareIntent.setType("text/plain");
//            oShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Website : www.sonkamble.com");
//            oShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath));
//            oShareIntent.setType("*/*");
//            oShareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            getActivity().startActivity(oShareIntent);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "WhatsApp require..!!", Toast.LENGTH_SHORT).show();
        }

        //-----------------------------------
    }
    //=============================PDF====================================
// Set PDF document Properties
    public void addTitlePage(Document document) throws DocumentException
    {
        // Font Style for Document
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD| Font.UNDERLINE, BaseColor.GRAY);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        // Start New Paragraph
        Paragraph prHead = new Paragraph();
        // Set Font in this Paragraph
        prHead.setFont(titleFont);
        // Add item into Paragraph
        prHead.add("EXPENSE REPORT\n");
        prHead.add("\n");
        prHead.setAlignment(Element.ALIGN_CENTER);

       /* Paragraph cat = new Paragraph();
        cat.setFont(catFont);
        cat.add("\n");
        cat.add("CUSTOMER REPORT \n");
        cat.add("\n");
        cat.add("Customer Name: "+name+"   "+"Mobile: "+number);
        cat.add("\n");
        cat.add("\n");
        cat.setAlignment(Element.ALIGN_CENTER);*/

        // Add all above details into Document
        document.add(prHead);
        // document.add(cat);

        /* Header values*/
        table = new PdfPTable(5);
        cell1 = new PdfPCell(new Phrase("ID"));
        cell2 = new PdfPCell(new Phrase("DATE"));
        cell3 = new PdfPCell(new Phrase("CATEGORY."));
        cell4 = new PdfPCell(new Phrase("AMOUNT"));
        cell5 = new PdfPCell(new Phrase("NOTES"));



        cell1.setVerticalAlignment(Element.ALIGN_LEFT);
        cell2.setVerticalAlignment(Element.ALIGN_LEFT);
        cell3.setVerticalAlignment(Element.ALIGN_LEFT);
        cell4.setVerticalAlignment(Element.ALIGN_LEFT);
        cell5.setVerticalAlignment(Element.ALIGN_LEFT);


        cell1.setBorder(Rectangle.BOX);
        cell1.setPadding(5);

        cell2.setBorder(Rectangle.BOX);
        cell2.setPadding(5);

        cell3.setBorder(Rectangle.BOX);
        cell3.setPadding(5);

        cell4.setBorder(Rectangle.BOX);
        cell4.setPadding(5);

        cell5.setBorder(Rectangle.BOX);
        cell5.setPadding(5);


        cell1.setBackgroundColor(BaseColor.ORANGE);
        cell2.setBackgroundColor(BaseColor.ORANGE);
        cell3.setBackgroundColor(BaseColor.ORANGE);
        cell4.setBackgroundColor(BaseColor.ORANGE);
        cell5.setBackgroundColor(BaseColor.ORANGE);

        /*//Table values*//**//*
    cell5 = new PdfPCell(new Phrase(b));
    cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell5.setBorder(Rectangle.NO_BORDER);
    cell5.setPadding(5);*/
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        //=================================================================


        ptotal=0.0;
        list_total.setText(" ");
        contact_arryList.clear();
        Cursor c= databaseHelper.show_exp_Data();
        if (c.getCount() != 0) {
            c.moveToFirst();
            do {

                HashMap<String, String> map = new HashMap<String, String>();

                String id  =c.getString(0);
                String date  =c.getString(1);
                String cat  =c.getString(2);
                String amt  =c.getString(3);
                String note  =c.getString(4);

                Double pttlamt=Double.parseDouble(amt);
                ptotal=ptotal+pttlamt;
                tot_exp= Double.toString(ptotal);


                /**Create the line to write in the .csv file.
                 * We need a String where values are comma separated.
                 * The field date (Long) is formatted in a readable text. The amount field
                 * is converted into String.
                 */
                // String record = df.format(new Date(date)) + "," + item + "," + amount.toString() + "," + currency;

                cell6 = new PdfPCell(new Phrase(id));
                cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell6.setBorder(Rectangle.BOX);
                cell6.setPadding(5);

                cell7 = new PdfPCell(new Phrase(date));
                cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell7.setBorder(Rectangle.BOX);
                cell7.setPadding(5);

                cell8 = new PdfPCell(new Phrase(cat));
                cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell8.setBorder(Rectangle.BOX);
                cell8.setPadding(5);

                cell9 = new PdfPCell(new Phrase(amt));
                cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell9.setBorder(Rectangle.BOX);
                cell9.setPadding(5);

                cell10 = new PdfPCell(new Phrase(note));
                cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell10.setBorder(Rectangle.BOX);
                cell10.setPadding(5);

                table.addCell(cell6);
                table.addCell(cell7);
                table.addCell(cell8);
                table.addCell(cell9);
                table.addCell(cell10);

            /*table.addCell(cell11);
			table.addCell(cell12);*/

                // add table into document
            } while (c.moveToNext());

        }

        document.add(table);
        c.close();
        Paragraph p = new Paragraph();
        p.setFont(catFont);
        p.add("                                  Total : "+tot_exp);
        p.add("\n");

        p.setAlignment(Element.ALIGN_CENTER);
        p.add("\n");
        p.add("\n");
        p.add("\n");
        document.add(p);
        try {

               /* AssetManager assetManager = getActivity().getAssets();
                InputStream ims = assetManager.open("logo.png");
                //  InputStream ims = getActivity().getAssets().open("logo.PNG");
                Bitmap bmp = BitmapFactory.decodeStream(ims);*/
            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scalePercent(30);
            image.setAlignment(Element.ALIGN_LEFT);
            document.add(image);
            Paragraph pP = new Paragraph();
            pP.setFont(catFont);
            pP.add("               Sign Here");

            document.add(pP);
          //  File outputFile = new File( Environment.getExternalStorageDirectory().toString() + "/PDF Report/" + "report.pdf");


        } catch (IOException e) {
            e.printStackTrace();
        }


        //===================================================================
        // Create new Page in PDF
        document.newPage();
        //Toast.makeText(this, "PDF File is Created.", Toast.LENGTH_LONG).show();
    }
    //===================================================================
    public void view_pdf()
    {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/report.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getActivity(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void expnc_popup_alert()
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.customer_popup_form, null);

        spinner_exp_cat=(Spinner)alertLayout.findViewById(R.id.spinner_exp_cat);
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

                chk_cat_type = adapterView.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "Selected: " + chk_cat_type, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final Calendar cd = Calendar.getInstance();
        mYear = cd.get(Calendar.YEAR);
        mMonth = cd.get(Calendar.MONTH);
        mDay = cd.get(Calendar.DAY_OF_MONTH);
        edit_date=(EditText)alertLayout.findViewById(R.id.edit_date);
        edit_date.setText(dt);
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                Toast.makeText(getActivity(),""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,Toast.LENGTH_LONG).show();
                                edit_date.setText(""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        edit_exp_amt=(EditText)alertLayout.findViewById(R.id.edit_exp_amt);
        edit_exp_amt.setText(amt);
        edit_note=(EditText)alertLayout.findViewById(R.id.edit_note);
        edit_note.setText(note);
        btn_del_exp=(Button) alertLayout.findViewById(R.id.btn_del_exp);
        btn_del_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteData(Integer.parseInt(cid));
                refresh();
                Toast.makeText(getActivity(), "Delete Successfully..!!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        btn_save_exp=(Button) alertLayout.findViewById(R.id.btn_save_exp);

        btn_save_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id=cid;
                int c_id=Integer.parseInt(id);
                String str = edit_date.getText().toString();
                String str1 = chk_cat_type;
                String str2 = edit_exp_amt.getText().toString();
                String str3 = edit_note.getText().toString();

                if (str.equals("")) {
                    Toast.makeText(getActivity(), "Date cannot be Blank", Toast.LENGTH_LONG).show();
                } else if (str1.equals("Select category")) {
                    Toast.makeText(getActivity(), "Please select valid category", Toast.LENGTH_LONG).show();
                } else if (str2.equals("")) {
                    Toast.makeText(getActivity(), "Amount cannot be Blank", Toast.LENGTH_LONG).show();
                } else if (str3.equals("")) {
                    Toast.makeText(getActivity(), "Notes cannot be Blank ", Toast.LENGTH_LONG).show();
                } else {
                    databaseHelper.updateData(c_id, str, str1, str2, str3);
                    Toast.makeText(getActivity(), "Updated Successfully..!!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    refresh();
                }
            }
        });
        btn_cust_popup_cancle=(Button)alertLayout.findViewById(R.id.btn_cust_popup_cancle);
        btn_cust_popup_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(alertLayout);

        dialog = alert.create();
        dialog.show();

    }

    public void refresh (){
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}
