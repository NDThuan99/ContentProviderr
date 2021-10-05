package com.example.contentproviderr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAddName(View view) {
        ContentValues values = new ContentValues();

        values.put(ContentProviderr.NAME,
                ((EditText)findViewById(R.id.editText2)).getText().toString());

        values.put(ContentProviderr.GRADE,
                ((EditText)findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                ContentProviderr.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = "content://com.example.provider.College/students";

        int i = 1;

        Uri students = Uri.parse(URL);
        //Cursor c = managedQuery(students, null, null, null, "name");
        Cursor cursor = getContentResolver().query(students , null, null, null, "name");
        if (cursor.moveToFirst()) {
            do{
                //@SuppressLint("Range") String str = cursor.getString(cursor.getColumnIndex(ContentProviderr.GRADE));
                @SuppressLint("Range") String str = cursor.getString(cursor.getColumnIndex(ContentProviderr._ID)) +
                        ", " +  cursor.getString(cursor.getColumnIndex( ContentProviderr.NAME)) +
                        ", " + cursor.getString(cursor.getColumnIndex( ContentProviderr.GRADE));
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                i++;
            } while (cursor.moveToNext());
        }
    }
}