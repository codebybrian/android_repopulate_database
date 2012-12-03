package com.codebybrian.sample;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class PopulateDatabase extends Activity {
    private static String DATABASE_NAME = "app_database";
    private static String TABLE_NAME = "phone_numbers";

    private static String LOG_TAG = "PopulateDatabase";

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {

            String createTable = "CREATE TABLE " +TABLE_NAME+ "(" +
                    "id INTEGER PRIMARY KEY, " +
                    "area_code TEXT, " +
                    "phone_number TEXT " +
                    ")";

            database.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(database);
        }

        public boolean addNumber(String areaCode, String phoneNumber){
            if(TextUtils.isEmpty(areaCode) || TextUtils.isEmpty(phoneNumber)){
                Log.e(LOG_TAG, "Must have non-null areaCode and phoneNumber for insert into database");
                return false;
            }

            ContentValues row = new ContentValues();
            row.put("area_code", areaCode);
            row.put("phone_number", phoneNumber);

            SQLiteDatabase database = getWritableDatabase();
            database.insert("phone_numbers", null, row);
            database.close();

            Log.i(LOG_TAG, String.format("(%s) %s inserted", areaCode, phoneNumber));
            return true;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.populate_database);

        // Drop the table if it's there.  Makes row counts on successive runs consistent
        dropTable();

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        int rowsInserted = 0;
        if (databaseHelper.addNumber("123", "111-2222")) rowsInserted++;
        if (databaseHelper.addNumber("555", "222-3333")) rowsInserted++;
        if (databaseHelper.addNumber("800", "999-8888")) rowsInserted++;

        showHelpText(rowsInserted);
    }

    private void dropTable(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        databaseHelper.onUpgrade(database, 0,1);
        database.close();
    }


    private void showHelpText(int rowsInserted){

        String insertText = String.format("%d row%s inserted", rowsInserted, ((rowsInserted == 1) ? "" : "s"));
        ((TextView)findViewById(R.id.num_rows_inserted)).setText(insertText);
        Log.i(LOG_TAG, insertText);

        String packageName = getApplicationContext().getPackageName();
        String extractText = String.format("adb -e pull data/data/%s/databases/%s %s", packageName, DATABASE_NAME, DATABASE_NAME);
        ((TextView)findViewById(R.id.extract_text)).setText(extractText);
        Log.i(LOG_TAG, extractText);
    }
}
