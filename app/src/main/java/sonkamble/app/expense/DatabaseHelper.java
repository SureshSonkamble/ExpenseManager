package sonkamble.app.expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="exp.db";
    private static final String TABLE_EXP="exp";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

       // SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " +TABLE_EXP+ "(EXP_ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,CAT TEXT,EXP_AMT TEXT,NOTE TEXT,MNAME TEXT)");
       // sqLiteDatabase.execSQL("CREATE TABLE " +TABLE_EXP+ "(EXP_ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,CAT TEXT,EXP_AMT TEXT,NOTE TEXT,MNO INTEGER,DTNO INTEGER,YRNO INTEGER )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_EXP);
            onCreate(sqLiteDatabase);
    }
    //===============Add Expense Data=======================
    public  boolean insert_exp_Data(String date,String cat,String exp_amt,String note,String mname)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE",date);
        contentValues.put("CAT",cat);
        contentValues.put("EXP_AMT",exp_amt);
        contentValues.put("NOTE",note);
        contentValues.put("MNAME",mname);
        Long result = sqLiteDatabase.insert(TABLE_EXP,null,contentValues);
        if (result==-1)
            return  false;
        else
            return true;
    }

    //-----------------------------------------------------
    public Cursor show_exp_Data() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //  String query =("SELECT * FROM " +TABLE_NAME+ );
        String query = "SELECT * FROM " + TABLE_EXP;
       // Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        Cursor cursor = sqLiteDatabase.rawQuery(query,null, null);
        return cursor;
    }

    public Cursor show_cat_exp_Data(String cat) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //  String query =("SELECT * FROM " +TABLE_NAME+ );
    //    String query ="SELECT * FROM " +TABLE_EXP+ " WHERE CAT = " +cat+ " ";
        String sql = "SELECT * FROM " +TABLE_EXP + " WHERE CAT" + " LIKE '%" + cat + "%'";
        //Cursor cursor = sqLiteDatabase.rawQuery(query,null);
      //  String query ="SELECT * FROM " +TABLE_DLVR+ " WHERE ID = " +cid+ " ";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        return cursor;
    }
    public Cursor show_month_exp_Data(String mno) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
       // String sql = "SELECT * FROM " +TABLE_EXP + "";
       // String sql = "SELECT * FROM " +TABLE_EXP + " WHERE MNAME " +mno+ "" ;
        String sql = "SELECT * FROM " +TABLE_EXP + " WHERE MNAME" + " LIKE '%" + mno + "%'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        return cursor;
    }
    public Cursor show_date_exp_Data(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //  String query =("SELECT * FROM " +TABLE_NAME+ );
      //  String query ="SELECT * FROM " +TABLE_EXP+ " WHERE DATE = " +date+ " ";
        String sql = "SELECT * FROM " +TABLE_EXP + " WHERE DATE" + " LIKE '%" + date + "%'";
        //Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        //  String query ="SELECT * FROM " +TABLE_DLVR+ " WHERE ID = " +cid+ " ";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        return cursor;

    }

    public Cursor show_date_exp_Data_Wise(String date,String date2) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //  String query =("SELECT * FROM " +TABLE_NAME+ );
        //  String query ="SELECT * FROM " +TABLE_EXP+ " WHERE DATE = " +date+ " ";
        //String sql = "SELECT * FROM " +TABLE_EXP + " WHERE DATE" + " BETWEEN "+ date + "' AND '" + date2 "+
      //  String sql = "select * from "+ TABLE_EXP + " where DATE BETWEEN " + date + " AND " + date2 + " ORDER BY DATE ASC";
        String sql = "select * from "+ TABLE_EXP + " where DATE  " + "IN (" + date + " ,"+ date2 +" )";
        //Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        //  String query ="SELECT * FROM " +TABLE_DLVR+ " WHERE ID = " +cid+ " ";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        return cursor;
    }
    //========Update data============================
    public void updateData(Integer id,String date,String cat,String exp_amt,String note)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("DATE",date);
        contentValues.put("CAT",cat);
        contentValues.put("EXP_AMT",exp_amt);
        contentValues.put("NOTE",note);
        sqLiteDatabase.update(TABLE_EXP,contentValues, " EXP_ID = " +id+ " ",null);

    }
    //==========Delete data===========================
    public void deleteData(Integer id)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_EXP, " EXP_ID = " +id+ " ",null);

    }
}
