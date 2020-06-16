package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentOutput output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (FragmentOutput) getSupportFragmentManager().findFragmentById(R.id.fragment_output);
    }

    public void transferData(String author, String year) {
        output.outputSelectedItems(author, year);
        insertRecord(author, year);
    }

    public void insertRecord(String author, String year) {
        String db_name = getResources().getString(R.string.db_name);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(db_name, MODE_PRIVATE, null);

        String tblName = getResources().getString(R.string.table_name);
        String createTbl = getResources().getString(R.string.create_table);
        String authorColName = getResources().getString(R.string.author_column_name);
        String yearColName = getResources().getString(R.string.year_column_name);

        ContentValues cv = new ContentValues();
        cv.put(authorColName, author);
        cv.put(yearColName, year);

        db.execSQL(String.format(createTbl, tblName, authorColName, yearColName));
        db.insert(tblName, null, cv);
        db.close();

        String msg = getResources().getString(R.string.record_was_added_msg);
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void clearTable() {
        String db_name = getResources().getString(R.string.db_name);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(db_name, MODE_PRIVATE, null);

        String recordsTblName = getResources().getString(R.string.table_name);
        String deleteFromTbl = getResources().getString(R.string.delete_from_table);

        db.execSQL(String.format(deleteFromTbl, recordsTblName));
        db.close();

        String msg = getResources().getString(R.string.table_was_cleared_msg);
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public List<String> getAllRecords() {
        List<String> records = new ArrayList<>();
        String db_name = getResources().getString(R.string.db_name);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(db_name, MODE_PRIVATE, null);

        String selectAllFromTbl = getResources().getString(R.string.select_all_from_table);
        String recordsTblName = getResources().getString(R.string.table_name);

        Cursor query = db.rawQuery(String.format(selectAllFromTbl, recordsTblName), null);

        if (query.moveToFirst()) {
            do {
                int id = query.getInt(0);
                String author = query.getString(1);
                String year = query.getString(2);

                records.add("id:"+id+"author:"+author+"year:"+year+"\n");
            } while(query.moveToNext());
        }

        query.close();
        db.close();

        return records;
    }

    public void showTable() {
        Intent intent = new Intent(this, ShowTableActivity.class);

        intent.putStringArrayListExtra("records", (ArrayList<String>) getAllRecords());

        startActivity(intent);
    }
}