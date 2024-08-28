package com.example.projectakhirvsga.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "digitaltalent.db";

    // Tabel Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Table for income data
    public static final String TABLE_INCOME = "income";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";

    // Table for expense data
    public static final String TABLE_EXPENSE = "expense";
    public static final String COLUMN_EXP_ID = "exp_id";
    public static final String COLUMN_EXP_AMOUNT = "exp_amount";
    public static final String COLUMN_EXP_CATEGORY = "exp_category";
    public static final String COLUMN_EXP_DATE = "exp_date";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel users
        final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(CREATE_USERS_TABLE);

        final String SQL_CREATE_INCOME_TABLE = "CREATE TABLE " + TABLE_INCOME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_INCOME_TABLE);

        final String SQL_CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_EXPENSE + " (" +
                COLUMN_EXP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXP_AMOUNT + " TEXT NOT NULL, " +
                COLUMN_EXP_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_EXP_DATE + " TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);
    }

    // Metode untuk registrasi pengguna
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    /*method untuk mengecek jika ketahuan ada username dan email yang sama mau register*/
    public boolean checkRegisterUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryCheckRegister = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(queryCheckRegister, new String[]{username});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Metode untuk login pengguna
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Income data methods
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getAllIncomeData() {
        ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_INCOME;
            cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                    map.put(COLUMN_AMOUNT, cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
                    map.put(COLUMN_DATE, cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                    wordList.add(map);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        Log.e("select income", "" + wordList);
        return wordList;
    }

    public void insertIncome(String amount, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_AMOUNT, amount);
            values.put(COLUMN_DATE, date);
            database.insert(TABLE_INCOME, null, values);
            Log.e("insert income", "Insert successful");
        } finally {
            database.close();
        }
    }

    public void updateIncome(int id, String amount, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_AMOUNT, amount);
            values.put(COLUMN_DATE, date);
            database.update(TABLE_INCOME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            Log.e("update income", "Update successful");
        } finally {
            database.close();
        }
    }

    public void deleteIncome(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(TABLE_INCOME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            Log.e("delete income", "Delete successful");
        } finally {
            database.close();
        }
    }

    // Expense data methods
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getAllExpenseData() {
        ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_EXPENSE;
            cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(COLUMN_EXP_ID, cursor.getString(cursor.getColumnIndex(COLUMN_EXP_ID)));
                    map.put(COLUMN_EXP_AMOUNT, cursor.getString(cursor.getColumnIndex(COLUMN_EXP_AMOUNT)));
                    map.put(COLUMN_EXP_CATEGORY, cursor.getString(cursor.getColumnIndex(COLUMN_EXP_CATEGORY)));
                    map.put(COLUMN_EXP_DATE, cursor.getString(cursor.getColumnIndex(COLUMN_EXP_DATE)));
                    wordList.add(map);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        Log.e("select expense", "" + wordList);
        return wordList;
    }

    public void insertExpense(String amount, String category, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EXP_AMOUNT, amount);
            values.put(COLUMN_EXP_CATEGORY, category);
            values.put(COLUMN_EXP_DATE, date);
            database.insert(TABLE_EXPENSE, null, values);
            Log.e("insert expense", "Insert successful");
        } finally {
            database.close();
        }
    }

    public void updateExpense(int id, String amount, String category, String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EXP_AMOUNT, amount);
            values.put(COLUMN_EXP_CATEGORY, category);
            values.put(COLUMN_EXP_DATE, date);
            database.update(TABLE_EXPENSE, values, COLUMN_EXP_ID + "=?", new String[]{String.valueOf(id)});
            Log.e("update expense", "Update successful");
        } finally {
            database.close();
        }
    }

    public void deleteExpense(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.delete(TABLE_EXPENSE, COLUMN_EXP_ID + "=?", new String[]{String.valueOf(id)});
            Log.e("delete expense", "Delete successful");
        } finally {
            database.close();
        }
    }
}