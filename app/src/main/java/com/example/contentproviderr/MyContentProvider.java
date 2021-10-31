package com.example.contentproviderr;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class MyContentProvider extends android.content.ContentProvider {

    static final String PROVIDER_NAME = "com.example.provider.College";
    static final String URL = "content://" + PROVIDER_NAME + "/students";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String GRADE = "grade";

    /*Todo: HashMap
     * giống như Array List nhưng được lưu
     * dưới dạng cặp khóa key-value. HashMap truy cập theo key(là giá trị duy nhất)
     * không theo thứ tự như array.
     * Như ví dụ thì khởi tạo HashMap với key là String và value cũng là String
     * HashMap chỉ chấp nhận kiểu dữ liệu object, không chấp nhận kiểu dữ liệu nguyên thủy
     * như: Integer(không được int), Double(không được double)...
     */
    private static HashMap<String, String> STUDENTS_PROJECTION_MAP; // What is HashMap in android studio ?

    static final int STUDENTS = 1;
    static final int STUDENT_ID = 2;

    /**
     * Thuan code:
     */
    static final UriMatcher uriMatcher; // What is UriMatcher in android studio ?
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "students", STUDENTS); //UriMatcher in content provider
        uriMatcher.addURI(PROVIDER_NAME, "students/#", STUDENT_ID);
    }
    //sql
    /*Todo: SQLiteDatabase:
     * cung cấp giao diện giữa ứng dụng
     * và cơ sở dữ liệu SQLite cho phép tạo,
     * xóa và thực hiện các truy vấn SQL.
     */
    private SQLiteDatabase db;

    static final String DATABASE_NAME = "College";
    static final String STUDENTS_TABLE_NAME = "students";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =   //Create a table sql in content provider
            " CREATE TABLE " + STUDENTS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " grade TEXT NOT NULL);";

    //tạo lớp database
    /*Todo: SQLiteOpenHelper: lớp này giúp cho việc tạo
     * và cập nhật cơ sở dữ liệu được dễ dàng hơn
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            /*todo: execSQL():
             * thực hiện một lệnh truy vấn SQL đơn
             * và không trả về kết quả
             * query(), rawQuery(): thực hiện truy vấn và trả về kết quả
             * phù hợp thông qua một đối tượng Cursor
             */
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /*todo:
             * Thuan code: Xóa 1 bảng nếu đó đã tồn tại.
             */
            db.execSQL("DROP TABLE IF EXISTS " +  STUDENTS_TABLE_NAME);
            //todo: gọi lại hàm onCreate() sau khi xóa
            onCreate(db);
        }
    }

    /**
     * Thuan code: hàm bắt buộc khi implement
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        /*Todo:
         * Thuan code: getWritableDatabase(): mở hay tạo một cơ sở dữ liệu cho việc đọc và ghi.
         * Trả về một tham chiếu đến một cơ sở dữ liệu trong hình thức một đối tượng SQLiteDatabase
         */
        db = dbHelper.getWritableDatabase();
        //return (db == null)? false:true;
        return db != null;
    }

    /**
     * Thuan code: hàm bắt buộc khi implement
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();   // // What is SQLiteQueryBuilder
        qb.setTables(STUDENTS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP); // setProjectionMap in SQLiteQueryBuilder android studio
                break;

            case STUDENT_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1)); // appendWhere in SQLiteQueryBuilder android studio
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (s1 == null || s1 == ""){
            s1 = NAME;
        }
        Cursor c = qb.query(db,	strings, s, strings1,null, null, s1);   // query in SQLiteQueryBuilder android studio
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /**
     * Thuan code: hàm bắt buộc khi implement
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case STUDENTS:
                return "vnd.android.cursor.dir/vnd.example.students";

            case STUDENT_ID:
                return "vnd.android.cursor.item/vnd.example.students";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    /**
     * Thuan code: hàm bắt buộc khi implement
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
         /*Todo: ContentValues
          * cho phép khai báo các cột và giá trị bên trong nó
          * dưới dạng các cặp khóa/giá trị (key/value). Hữu ích khi chèn hay cập nhật dữ liệu đến bảng.
          * put(): thêm một giá trị đến tập dữ liệu
          */
        long rowID = db.insert(	STUDENTS_TABLE_NAME, "", contentValues); // insert in SQLiteDatabase android studio
        Log.d("ThuanNDd", "Nội dung log rowID: "+rowID);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            Log.d("ThuanNDd", "Nội dung log _uri: "+ _uri);
            Log.d("ThuanNDd", "Nội dung log CONTENT_URI: "+ CONTENT_URI);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);   // throw in android studio
    }

    /**
     * Thuan code: hàm bắt buộc khi implement
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count;

        switch (uriMatcher.match(uri)){
            case STUDENTS:
                count = db.delete(STUDENTS_TABLE_NAME, s, strings);
                break;

            case STUDENT_ID:
                String id = uri.getPathSegments().get(1);   // uri.getPathSegments().get() in android studio
                count = db.delete( STUDENTS_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(s) ? " AND (" + s + ')' : ""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Thuan code: hàm bắt buộc khi implement
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int count;

        switch (uriMatcher.match(uri)){
            case STUDENTS:
                count = db.update(STUDENTS_TABLE_NAME, contentValues, s, strings);  // update in SQLiteDatabase android studio
                break;

            case STUDENT_ID:
                count = db.update(STUDENTS_TABLE_NAME, contentValues, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(s) ? " AND (" +s + ')' : ""), strings);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
