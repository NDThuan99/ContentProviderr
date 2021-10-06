package com.example.contentproviderr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.Menu;
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
        Sevicerr sevicerr = new Sevicerr();
        sevicerr.getPlayList(Environment.getExternalStorageDirectory()+"");
        //permision();
        getAudioFiles();
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
    public void getAudioFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        //looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {

                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                @SuppressLint("Range") String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.d("XXX", "title: "+title + " - url: "+url+" - artist: "+artist+" - duration: "+ duration);
                /*ModelAudio modelAudio = new ModelAudio();
                modelAudio.setaudioTitle(title);
                modelAudio.setaudioArtist(artist);
                modelAudio.setaudioUri(Uri.parse(url));
                modelAudio.setaudioDuration(duration);
                audioArrayList.add(modelAudio);*/

            } while (cursor.moveToNext());
        }

        /*AudioAdapter adapter = new AudioAdapter(this, audioArrayList);
        recyclerView.setAdapter(adapter);*/
    }
    private void permision(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
}