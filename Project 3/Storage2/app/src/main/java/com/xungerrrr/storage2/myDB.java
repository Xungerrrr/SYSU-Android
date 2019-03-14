package com.xungerrrr.storage2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME= "storage2.db";
    private static final String TABLE_NAME_1 = "users";
    private static final String TABLE_NAME_2 = "comments";
    private static final String TABLE_NAME_3 = "likes";
    private static final int DB_VERSION = 1;

    public myDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_1 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME_1
                + " (name TEXT PRIMARY KEY, password TEXT, avatar BLOB)";
        sqLiteDatabase.execSQL(CREATE_TABLE_1);
        String CREATE_TABLE_2 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME_2
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, time TEXT, content TEXT, number INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE_2);
        String CREATE_TABLE_3 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME_3
                + " (name TEXT, comment INTEGER, PRIMARY KEY (name, comment), FOREIGN KEY (name) REFERENCES "
                + TABLE_NAME_1 + " (name) ON DELETE CASCADE, FOREIGN KEY(comment) REFERENCES "
                + TABLE_NAME_2 + " (_id) ON DELETE CASCADE)";
        sqLiteDatabase.execSQL(CREATE_TABLE_3);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {}

    public boolean login(Context context, String name, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String SELECT_USER = "SELECT * FROM "
                + TABLE_NAME_1
                + " WHERE name = ?";
        Cursor cursor = db.rawQuery(SELECT_USER, new String[]{name});
        if (cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("password");
            String pass = cursor.getString(passwordColumnIndex);
            if (!TextUtils.equals(pass, password)) {
                // 密码不匹配
                Toast.makeText(context, R.string.password_invalid, Toast.LENGTH_SHORT).show();
                cursor.close();
                return false;
            }
            else {
                // 登录成功
                Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show();
                cursor.close();
                return true;
            }
        }
        else {
            // 用户不存在
            Toast.makeText(context, R.string.no_user, Toast.LENGTH_SHORT).show();
            cursor.close();
            return false;
        }
    }

    public void register(Context context, String name, String password, byte[] avatar) {
        SQLiteDatabase db = getWritableDatabase();
        String SELECT_USER = "SELECT * FROM "
                + TABLE_NAME_1
                + " WHERE name = ?";
        Cursor cursor = db.rawQuery(SELECT_USER, new String[]{name});
        if (cursor.getCount() != 0) {
            // 用户名已被注册
            Toast.makeText(context, R.string.user_exists, Toast.LENGTH_SHORT).show();
        }
        else {
            // 向数据库中插入新用户信息
            cursor.moveToFirst();
            String INSERT_SQL = "INSERT INTO "
                    + TABLE_NAME_1
                    + " (name, password, avatar) values (?, ?, ?)";
            db.execSQL(INSERT_SQL, new Object[]{name, password, avatar});
            Toast.makeText(context, "Register successfully.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    public Bitmap getAvatar(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String SELECT_USER = "SELECT * FROM "
                + TABLE_NAME_1
                + " WHERE name = ?";
        Cursor cursor = db.rawQuery(SELECT_USER, new String[]{name});
        if (cursor.moveToFirst()) {
            byte[] avatar = cursor.getBlob(cursor.getColumnIndex("avatar"));
            Bitmap bmp = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
            cursor.close();
            return bmp;
        }
        return null;
    }

    public List<Map<String, Object>> getComments(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String SELECT_COMMENTS = "SELECT * FROM "
                + TABLE_NAME_2;
        Cursor cursor = db.rawQuery(SELECT_COMMENTS, null);
        List<Map<String, Object>> comments = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // 获取评论信息
                Map<String, Object> comment = new LinkedHashMap<>();
                comment.put("_id", cursor.getInt(cursor.getColumnIndex("_id")));
                comment.put("name", cursor.getString(cursor.getColumnIndex("name")));
                comment.put("time", cursor.getString(cursor.getColumnIndex("time")));
                comment.put("content", cursor.getString(cursor.getColumnIndex("content")));
                comment.put("avatar", getAvatar(comment.get("name").toString()));
                comment.put("number", cursor.getInt(cursor.getColumnIndex("number")));
                // 查询评论是否被用户点赞
                String SELECT_LIKE = "SELECT * FROM "
                        + TABLE_NAME_3
                        + " WHERE name = ? AND comment = ?";
                Cursor c = db.rawQuery(SELECT_LIKE, new String[]{name, comment.get("_id").toString()});
                if (c.moveToFirst()) {
                    comment.put("tag", 1);
                }
                else {
                    comment.put("tag", 0);
                }
                c.close();
                comments.add(comment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return comments;
    }
    public int addComment(Map<String, Object> comment) {
        SQLiteDatabase db = getWritableDatabase();
        String name = comment.get("name").toString();
        String time = comment.get("time").toString();
        String content = comment.get("content").toString();
        String INSERT_SQL = "INSERT INTO "
                + TABLE_NAME_2
                + " (name, time, content, number) values (?, ?, ?, ?)";
        db.execSQL(INSERT_SQL, new Object[]{name, time, content, 0});
        String SELECT_COMMENTS = "SELECT * FROM "
                + TABLE_NAME_2;
        // 获取新评论id
        Cursor cursor = db.rawQuery(SELECT_COMMENTS, null);
        cursor.moveToLast();
        return cursor.getInt(cursor.getColumnIndex("_id"));
    }
    public void deleteComment(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        String DELETE_SQL = "DELETE FROM "
                + TABLE_NAME_2
                + " WHERE _id = ?";
        db.execSQL(DELETE_SQL, new Object[]{_id});
    }
    public void like(String name, int _id) {
        SQLiteDatabase db = getWritableDatabase();
        // 添加点赞记录
        String INSERT_SQL = "INSERT INTO "
                + TABLE_NAME_3
                + " (name, comment) values (?, ?)";
        db.execSQL(INSERT_SQL, new Object[]{name, _id});
        // 更新评论点赞数
        String UPDATE_SQL = "UPDATE "
                + TABLE_NAME_2
                + " SET number = number + ? WHERE _id = ?";
        db.execSQL(UPDATE_SQL, new Object[]{1, _id});
    }
    public void unlike(String name, int _id) {
        SQLiteDatabase db = getWritableDatabase();
        // 删除点赞记录
        String DELETE_SQL = "DELETE FROM "
                + TABLE_NAME_3
                + " WHERE name = ? AND comment = ?";
        db.execSQL(DELETE_SQL, new Object[]{name, _id});
        // 更新评论点赞数
        String UPDATE_SQL = "UPDATE "
                + TABLE_NAME_2
                + " SET number = number - ? WHERE _id = ?";
        db.execSQL(UPDATE_SQL, new Object[]{1, _id});
    }
}
