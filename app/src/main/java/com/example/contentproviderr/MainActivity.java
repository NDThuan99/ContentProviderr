package com.example.contentproviderr;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAddName(View view) {
        /*Todo: ContentValues
         * cho phép khai báo các cột và giá trị bên trong nó
         * dưới dạng các cặp khóa/giá trị (key/value). Hữu ích khi chèn hay cập nhật dữ liệu đến bảng.
         * put(): thêm một giá trị đến tập dữ liệu
         */
        ContentValues values = new ContentValues();

        values.put(MyContentProvider.NAME,
                ((EditText)findViewById(R.id.editText2)).getText().toString());

        values.put(MyContentProvider.GRADE,
                ((EditText)findViewById(R.id.editText3)).getText().toString());

        Uri uri = getContentResolver().insert(
                MyContentProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }

    @SuppressLint("Range")
    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = "content://com.example.provider.College/students";

        int i = 1;

        Uri students = Uri.parse(MyContentProvider.URL);
        //Cursor c = managedQuery(students, null, null, null, "name");
        Cursor cursor = getContentResolver().query(students , null, null, null, MyContentProvider.NAME);
        if (cursor.moveToFirst()) {
            do{
                //@SuppressLint("Range") String str = cursor.getString(cursor.getColumnIndex(ContentProviderr.GRADE));
                @SuppressLint("Range") String str = cursor.getString(cursor.getColumnIndex(MyContentProvider._ID)) +
                        ", " +  cursor.getString(cursor.getColumnIndex( MyContentProvider.NAME)) +
                        ", " + cursor.getString(cursor.getColumnIndex( MyContentProvider.GRADE));
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                Log.d("ThuanNDd", "đây là lần: " + i + ": " + cursor.getString(cursor.getColumnIndex( MyContentProvider.NAME))
                        + ", " + cursor.getString(cursor.getColumnIndex( MyContentProvider.GRADE)));
                i++;
            } while (cursor.moveToNext());
        }
    }
}