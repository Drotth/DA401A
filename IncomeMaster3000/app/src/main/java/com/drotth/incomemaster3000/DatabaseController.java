package com.drotth.incomemaster3000;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseController extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DB_NAME = "COMPANY_DATABASE";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_INCOMES = "Incomes";
    private static final String DB_TABLE_EXPENSES = "Expenses";


    public DatabaseController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE_INCOMES +
                "(_id integer primary key autoincrement, " +
                "Date text not null, " +
                "Amount int not null,"+
                "Title text not null);");


        db.execSQL("CREATE TABLE " + DB_TABLE_EXPENSES +
                "(_id integer primary key autoincrement, " +
                "Date text not null, " +
                "Amount int not null,"+
                "Title text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_INCOMES);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_EXPENSES);

        onCreate(db);
    }

    public void open(){
        db = getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public long newData(String table, String title, String amount, String date) {
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Date", date);
        values.put("Amount", Integer.parseInt(amount));

        if (table.equals("income")){
            return db.insert("Incomes",null,values);
        }
        else{
            return db.insert("Expenses",null,values);
        }
    }

    public Cursor getData(String table) {
        return db.query(
                table,
                new String[]{"_id", "Date", "Amount", "Title"},
                null, null, null, null, null);
    }

    public int getIncomesSum(){
        int total = 0;
        Cursor c = db.rawQuery("SELECT sum(Amount) as AmountSumIncomes FROM Incomes", null);

        if (c != null && c.moveToFirst());

        do {
            total=c.getInt(0);
        } while (c.moveToNext());

        return total;
    }

    public int getExpensesSum(){
        int total = 0;
        Cursor c2 = db.rawQuery("SELECT sum(Amount) AS AmountSumExpenses FROM Expenses", null);

        if (c2 != null && c2.moveToFirst());

        do {
            total=c2.getInt(0);
        } while (c2.moveToNext());

        return total;
    }
}
