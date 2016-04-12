package com.makokit.mako;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.makokit.mako.MakoContract.*;

/**
 * Created by muhammadkhadafi on 4/7/16.
 */

public class MakoDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mako.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES_LESSONS =
            "CREATE TABLE " + LessonsEntry.TABLE_NAME + " (" +
                    LessonsEntry._ID + " INTEGER PRIMARY KEY," +
                    LessonsEntry.COLUMN_NAME_LESSON_ID + INTEGER_TYPE + COMMA_SEP +
                    LessonsEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    LessonsEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    LessonsEntry.COLUMN_NAME_VERSION + TEXT_TYPE + COMMA_SEP +
                    LessonsEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + " )";
    private static final String SQL_CREATE_ENTRIES_INSTRUCTIONS =
            "CREATE TABLE " + InstructionsEntry.TABLE_NAME + " (" +
                    InstructionsEntry._ID + " INTEGER PRIMARY KEY," +
                    InstructionsEntry.COLUMN_NAME_INSTRUCTION_ID + INTEGER_TYPE + COMMA_SEP +
                    InstructionsEntry.COLUMN_NAME_LESSON_ID + INTEGER_TYPE + COMMA_SEP +
                    InstructionsEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    InstructionsEntry.COLUMN_NAME_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
                    InstructionsEntry.COLUMN_NAME_VIDEO_URL + TEXT_TYPE + COMMA_SEP +
                    InstructionsEntry.COLUMN_NAME_NEXT_INSTRUCTION_ID + INTEGER_TYPE  + " )";
    private static final String SQL_CREATE_ENTRIES_ANSWERS =
            "CREATE TABLE " + AnswersEntry.TABLE_NAME + " (" +
                    AnswersEntry._ID + " INTEGER PRIMARY KEY," +
                    AnswersEntry.COLUMN_NAME_ANSWER_ID + INTEGER_TYPE + COMMA_SEP +
                    AnswersEntry.COLUMN_NAME_INSTRUCTION_ID + INTEGER_TYPE + COMMA_SEP +
                    AnswersEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    AnswersEntry.COLUMN_NAME_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
                    AnswersEntry.COLUMN_NAME_CORRECT + INTEGER_TYPE +  " )";
    private static final String SQL_CREATE_ENTRIES_JUMPS =
            "CREATE TABLE " + JumpsEntry.TABLE_NAME + " (" +
                    JumpsEntry._ID + " INTEGER PRIMARY KEY," +
                    JumpsEntry.COLUMN_NAME_JUMP_ID + INTEGER_TYPE + COMMA_SEP +
                    JumpsEntry.COLUMN_NAME_ANSWER_ID + INTEGER_TYPE + COMMA_SEP +
                    JumpsEntry.COLUMN_NAME_INSTRUCTION_ID + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES_LESSONS =
            "DROP TABLE IF EXISTS " + LessonsEntry.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_INSTRUCTIONS =
            "DROP TABLE IF EXISTS " + InstructionsEntry.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_ANSWERS =
            "DROP TABLE IF EXISTS " + AnswersEntry.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_JUMPS =
            "DROP TABLE IF EXISTS " + JumpsEntry.TABLE_NAME;

    public MakoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void refreshDatabase(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES_LESSONS);
        db.execSQL(SQL_DELETE_ENTRIES_INSTRUCTIONS);
        db.execSQL(SQL_DELETE_ENTRIES_ANSWERS);
        db.execSQL(SQL_DELETE_ENTRIES_JUMPS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_LESSONS);
        db.execSQL(SQL_CREATE_ENTRIES_INSTRUCTIONS);
        db.execSQL(SQL_CREATE_ENTRIES_ANSWERS);
        db.execSQL(SQL_CREATE_ENTRIES_JUMPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_LESSONS);
        db.execSQL(SQL_DELETE_ENTRIES_INSTRUCTIONS);
        db.execSQL(SQL_DELETE_ENTRIES_ANSWERS);
        db.execSQL(SQL_DELETE_ENTRIES_JUMPS);
        onCreate(db);
    }
}
