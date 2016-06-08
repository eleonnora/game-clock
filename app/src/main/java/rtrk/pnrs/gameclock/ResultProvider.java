package rtrk.pnrs.gameclock;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import rtrk.pnrs.adapter.Result;

/**
 * Created by nora on 5/31/2016.
 */
public class ResultProvider extends ContentProvider {

    Database database;

    @Override
    public boolean onCreate() {
        database = new Database(this.getContext(), "results", null, 1);

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = database.getReadableDatabase();
        return db.query("results", projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.insert("results", null, values);
        database.close();

        ContentResolver resolver = getContext().getContentResolver();
        resolver.notifyChange(uri, null);

        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = database.getWritableDatabase();
        int is = db.delete("results", null, null);

        database.close();

        return is;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = database.getWritableDatabase();
        int updated = db.update("results", values, selection, selectionArgs);

        database.close();

        return updated;

    }
}
