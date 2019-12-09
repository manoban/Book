package com.example.book;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {

    private DBHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;
    private static UriMatcher uriMatcher;
    private static final int WORD_DIR = 0;
    private static final int WORD_ITEM= 1;
    private static final String AUTHORITY ="com.example.book.mycontentprovider";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"wordlist",WORD_DIR);
        uriMatcher.addURI(AUTHORITY,"wordlist/#",WORD_ITEM);

    }
    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(),"wordbook",null, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)){//uri 统一资源标识符
            case WORD_DIR://查询表中所有数据
                cursor=db.query(DBHelper.TABLE_NAME,projection,selection,
                        selectionArgs,null,null,sortOrder);
                break;
            case WORD_ITEM://查询表中单条数据
                String ID = uri.getPathSegments().get(1);
                projection = new String[]{ID};
                cursor=db.query(DBHelper.TABLE_NAME,projection,"id=?",
                        projection,null,null,sortOrder);
                break;
        }


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.book.mycontentprovider.wordlist";
            case WORD_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.book.mycontentprovider.wordlist";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
            case WORD_ITEM:
                long newBookId =db.insert(DBHelper.TABLE_NAME,null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/wordlist/"+newBookId);
                break;
            default:
               break;
        }
        return uriReturn;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       SQLiteDatabase db = dbHelper.getWritableDatabase();
       int deleteRows = 0;
       switch (uriMatcher.match(uri)){
           case WORD_DIR:
               deleteRows = db.delete(DBHelper.TABLE_NAME,selection,selectionArgs);
               break;
           case WORD_ITEM:
               String wordId = uri.getPathSegments().get(1);
               deleteRows = db.delete(DBHelper.TABLE_NAME,"id=?",new String[]{wordId});
               break;
            default:
                break;

       }

        return deleteRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                updateRows = db.update(DBHelper.TABLE_NAME,values,selection,selectionArgs);
                break;
            case WORD_ITEM:
                String wordId = uri.getPathSegments().get(1);
                updateRows= db.update(DBHelper.TABLE_NAME,values,"id=?",new String[]{wordId});
            default:
                break;
        }

        return updateRows;
    }
}
