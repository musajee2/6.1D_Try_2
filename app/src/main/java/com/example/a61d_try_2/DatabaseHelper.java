package com.example.a61d_try_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name
    private static final String DATABASE_NAME = "iTubeDB";

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PLAYLIST = "playlist";
    private static final String TABLE_QUIZ_ATTEMPTS = "quiz_attempts";

    // Common column names
    private static final String KEY_ID = "id";

    // Users Table - column names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    // Playlist Table - column names
    private static final String KEY_YOUTUBE_URL = "youtube_url";

    // Quiz Attempts Table - column names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_QUIZ_TOPIC = "quiz_topic";
    private static final String KEY_TOTAL_QUESTIONS = "total_questions";
    private static final String KEY_CORRECT_ANSWERS = "correct_answers";
    private static final String KEY_COMPLETED = "completed";

    // Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
            "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_USERNAME + " TEXT," +
            KEY_EMAIL + " TEXT UNIQUE," +
            KEY_PASSWORD + " TEXT" +
            ")";

    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE " + TABLE_PLAYLIST +
            "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_YOUTUBE_URL + " TEXT" +
            ")";

    private static final String CREATE_TABLE_QUIZ_ATTEMPTS = "CREATE TABLE " + TABLE_QUIZ_ATTEMPTS +
            "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_USER_ID + " INTEGER," +
            KEY_QUIZ_TOPIC + " TEXT," +
            KEY_TOTAL_QUESTIONS + " INTEGER," +
            KEY_CORRECT_ANSWERS + " INTEGER," +
            KEY_COMPLETED + " INTEGER," +
            "FOREIGN KEY(" + KEY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_QUIZ_ATTEMPTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_ATTEMPTS);

        // create new tables
        onCreate(db);
    }

    // Method to reset the database
    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_ATTEMPTS);
        onCreate(db);
        db.close();
    }

    // Adding a new user
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());

        // insert row
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Adding new quiz attempt
    public long addQuizAttempt(int userId, String quizTopic, int totalQuestions, int correctAnswers, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_QUIZ_TOPIC, quizTopic);
        values.put(KEY_TOTAL_QUESTIONS, totalQuestions);
        values.put(KEY_CORRECT_ANSWERS, correctAnswers);
        values.put(KEY_COMPLETED, completed ? 1 : 0);

        // insert row
        long id = db.insert(TABLE_QUIZ_ATTEMPTS, null, values);
        db.close();
        return id;
    }

    // Updating quiz attempt
    public void updateQuizAttempt(long attemptId, int score, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CORRECT_ANSWERS, score);
        values.put(KEY_COMPLETED, completed ? 1 : 0);

        db.update(TABLE_QUIZ_ATTEMPTS, values, KEY_ID + " = ?", new String[]{String.valueOf(attemptId)});
        db.close();
    }

    // Method to check if a user with the given email exists in the database
    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                KEY_ID
        };
        String selection = KEY_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                TABLE_USERS,     // The table to query
                columns,         // The array of columns to return (null to return all)
                selection,       // The columns for the WHERE clause
                selectionArgs,   // The values for the WHERE clause
                null,            // don't group the rows
                null,            // don't filter by row groups
                null             // The sort order
        );

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    // Method to retrieve a user by email
    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String[] columns = {
                KEY_ID,
                KEY_USERNAME,
                KEY_EMAIL,
                KEY_PASSWORD
        };

        String selection = KEY_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                TABLE_USERS,             // The table to query
                columns,                 // The array of columns to return (null to return all)
                selection,               // The columns for the WHERE clause
                selectionArgs,           // The values for the WHERE clause
                null,                    // don't group the rows
                null,                    // don't filter by row groups
                null                     // The sort order
        );

        if (cursor.moveToFirst()) {
            // Retrieve user data from the cursor
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
            String userEmail = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));

            // Create a new User object
            user = new User(username, userEmail, password);
            user.setId(id);
        }

        cursor.close();
        db.close();

        return user;
    }

    public List<QuizAttempt> getQuizAttempts(int userId) {
        List<QuizAttempt> attempts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_QUIZ_ATTEMPTS,
                null,
                KEY_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String quizTopic = cursor.getString(cursor.getColumnIndex(KEY_QUIZ_TOPIC));
                int totalQuestions = cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_QUESTIONS));
                int correctAnswers = cursor.getInt(cursor.getColumnIndex(KEY_CORRECT_ANSWERS));
                boolean completed = cursor.getInt(cursor.getColumnIndex(KEY_COMPLETED)) == 1;

                QuizAttempt attempt = new QuizAttempt(id, userId, quizTopic, totalQuestions, correctAnswers, completed);
                attempts.add(attempt);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return attempts;
    }


    // More methods for database operations
}
