package com.grjug.android.chucknorrisjokes.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.grjug.android.chucknorrisjokes.model.Joke;

import java.util.Date;
import java.util.List;

/**
 * Created by emonk on 4/14/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "chuckNorrisApiDB";

    // Table Names
    private static final String TABLE_JOKE = "jokes";
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_JOKE_TO_CATEGORY = "joke_to_category";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Joke Table - column names
    private static final String KEY_JOKE_TEXT = "joke_text";
    private static final String KEY_THUMBS_UP = "thumbs_up";

    // Category Table - column names
    private static final String KEY_CATEGORY_NAME = "category_name";


    // Joke to Category Table - column names
    private static final String KEY_JOKE_ID = "joke_id";
    private static final String KEY_CATEGORY_ID = "category_id";

    // Table Create Statements
    // Joke table create statement
    private static final String CREATE_TABLE_JOKE = "CREATE TABLE "
            + TABLE_JOKE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_JOKE_ID + "INTEGER," + KEY_JOKE_TEXT + " TEXT,"
            + KEY_THUMBS_UP + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // Category table create statement
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // joke_to_category table create statement
    private static final String CREATE_TABLE_JOKE_TO_CATEGORY = "CREATE TABLE "
            + TABLE_JOKE_TO_CATEGORY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_JOKE_ID + " INTEGER," + KEY_CATEGORY_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JOKE);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_JOKE_TO_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOKE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOKE_TO_CATEGORY);

        // create new tables
        onCreate(db);
    }

    /*
 * Creating a joke
 */
    public long createJoke(Joke joke, Integer thumbsUp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, joke.getId());
        values.put(KEY_JOKE_TEXT, joke.getJokeText());
        values.put(KEY_THUMBS_UP, thumbsUp);
        values.put(KEY_CREATED_AT, new Date().toString());

        // insert row
        long joke_id = db.insert(TABLE_JOKE, null, values);

        List<String> categories = joke.getCategories();

        if (!categories.isEmpty()) {
            // assigning categories to joke
            for (String cat_name : categories) {
                createJokeToCategory(joke_id, cat_name);
            }
        }

        return joke_id;
    }

    private long createJokeToCategory(long joke_id, String cat_name) {
        int exists = checkForCategoryByName(cat_name);
        if (exists == 0){
            createCategory(cat_name);
        }

        return 0;
    }

    public int checkForCategoryByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + KEY_CATEGORY_ID + " FROM " + TABLE_CATEGORY
                     + " WHERE " + KEY_CATEGORY_NAME
                     + " = " + name;
        Cursor cursor = db.rawQuery(sql, null);

        return cursor.getCount();
    }
    public long createCategory(String category) {
        //check if category exists
        return 0;
    }
}
