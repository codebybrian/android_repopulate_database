package com.codebybrian.sample;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RestoreDatabase extends Activity {
    private static final String DATABASE_NAME = "app_database";
    private static String TABLE_NAME = "phone_numbers";

    private static int DB_RAW_RESOURCES_ID = R.raw.app_database;

    private static String LOG_TAG = "RestoreDatabase";


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restore_database);

        importDatabase();
    }

    private void importDatabase() {
        try {

            String packageName = getApplicationContext().getPackageName();

            String appDatabaseDirectory = String.format("/data/data/%s/databases", packageName);
            (new File(appDatabaseDirectory)).mkdir();
            OutputStream dos = new FileOutputStream(appDatabaseDirectory+"/"
                    + DATABASE_NAME);

            InputStream dis = getResources().openRawResource(DB_RAW_RESOURCES_ID);
            byte[] buffer = new byte[1028];
            while ((dis.read(buffer)) > 0) {
                dos.write(buffer);
            }
            dos.flush();
            dos.close();
            dis.close();


            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor c = db.rawQuery("select count(*) from " +TABLE_NAME, null);
            c.moveToFirst();
            int tableRows = c.getInt(0);
            showHelpText(tableRows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showHelpText(int tableRows){
        String formattedText = String.format("%d row%s in the table", tableRows, ((tableRows == 1) ? "" : "s"));
        ((TextView)findViewById(R.id.num_rows)).setText(formattedText);
        Log.i(LOG_TAG, formattedText);
    }
}
