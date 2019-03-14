package com.midterm.group1.herolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MyData extends SQLiteOpenHelper {
    private static final String DB_NAME = "Heros";
    //全部英雄
    private static final String TABLE_NAME = "allHeros";
    //全部用户
    private static final String TABLE_NAME1 = "allUsers";
    //用户喜欢的英雄
    private static final String TABLE_NAME2 = "likedHero";
    //用户添加的英雄
    private static final String TABLE_NAME3 = "userAddHero";
    private static final int DB_VERSION = 1;

    public MyData(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE1 = "CREATE TABLE if not exists " + TABLE_NAME
                + " (imageIcon TEXT, image TEXT, name TEXT PRIMARY KEY, location TEXT, type TEXT," +
                " id INTEGER, viability INTEGER, attack INTEGER, skill INTEGER, difficulty INTEGER, story TEXT)";

        String CREATE_TABLE2 = "CREATE TABLE if not exists " + TABLE_NAME1
                + " (username TEXT PRIMARY KEY, password TEXT, image BLOB)";

        String CREATE_TABLE3 = "CREATE TABLE if not exists " + TABLE_NAME2
                + " (username TEXT, heroname TEXT)";

        String CREATE_TABLE4 = "CREATE TABLE if not exists "+ TABLE_NAME3
                + " (username TEXT, heroname TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE1);
        sqLiteDatabase.execSQL(CREATE_TABLE2);
        sqLiteDatabase.execSQL(CREATE_TABLE3);
        sqLiteDatabase.execSQL(CREATE_TABLE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {

    }

    //第一次启动时通过调用此函数初始化英雄库
    public void insertHero(Hero hero) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imageIcon", hero.getImageIcon());
        values.put("image", hero.getImage());
        values.put("name", hero.getName());
        values.put("location", hero.getLocation());
        values.put("type", hero.getType());
        values.put("id", hero.getIconid());
        values.put("viability", hero.getViability());
        values.put("attack", hero.getAttack());
        values.put("skill", hero.getSkill());
        values.put("difficulty", hero.getDifficulty());
        values.put("story", hero.getStory());

        sqLiteDatabase.insert(TABLE_NAME, null, values);
        sqLiteDatabase.close();
    }

    //得到全部英雄
    public List<Hero> getAllHeros(){
        List<Hero> list = new ArrayList<Hero>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +  TABLE_NAME, null);
        if (cursor.getCount() == 0 || !cursor.moveToFirst()){
            return null;
        }
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            list.add(new Hero(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getInt(5),
                    cursor.getInt(6),cursor.getInt(7),cursor.getInt(8),
                    cursor.getInt(9),cursor.getString(10)));
        }
        cursor.close();
        db.close();
        return list;
    }

    public void updateHero(Hero hero) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("viability", hero.getViability());
        values.put("attack", hero.getAttack());
        values.put("skill", hero.getSkill());
        values.put("difficulty", hero.getDifficulty());

        String whereClause11 = "name=?";
        String[] whereArgs = {hero.getName()};
        sqLiteDatabase.update(TABLE_NAME, values, whereClause11, whereArgs);
        sqLiteDatabase.close();
    }

    //通过英雄名字从全部英雄中选出英雄
    public Hero findHero(String name){
        Hero hero = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = "name = ?";
        String[] selectionArgs = { name };
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null,null, null);
        if (cursor.getCount() == 0 || !cursor.moveToFirst()){
            return null;
        }
        if (cursor.moveToFirst()){
            hero = new Hero(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getInt(5),
                    cursor.getInt(6),cursor.getInt(7),cursor.getInt(8),
                    cursor.getInt(9),cursor.getString(10));
        }
        cursor.close();
        db.close();
        return hero;
    }

    //添加用户
    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("image", user.getImage());

        sqLiteDatabase.insert(TABLE_NAME1, null, values);
        sqLiteDatabase.close();
    }

    //通过用户名查找用户
    public User getByUsername(String name){
        User user = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = "username = ?";
        String[] selectionArgs = { name };
        Cursor cursor = db.query(TABLE_NAME1, null, selection, selectionArgs, null,null, null);
        if (cursor.getCount() == 0 || !cursor.moveToFirst()){
            return null;
        }
        if (cursor.moveToFirst()){
            user = new User(cursor.getString(0),cursor.getString(1), cursor.getBlob(2));
        }
        cursor.close();
        db.close();
        return user;
    }

    //用户收藏英雄，传入用户名跟英雄名
    public void likeHero(String username, String heroname){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("heroname", heroname);

        sqLiteDatabase.insert(TABLE_NAME2, null, values);
        sqLiteDatabase.close();
    }

    //用户取消收藏，传入用户名跟英雄名
    public void unlikeHero(String username, String heroname){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause1 = "username = ? and heroname = ?";
        String[] whereArgs1 = { username, heroname };
        sqLiteDatabase.delete(TABLE_NAME2, whereClause1, whereArgs1);
        sqLiteDatabase.close();
    }

    //传入用户名跟英雄名，返回该用户是否收藏该英雄
    public boolean isLike(String username, String heroname) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME2, new String[] {"username"},
                "username = ? and heroname = ?", new String[] {username, heroname},
                null, null, null);
        if (cursor.getCount() == 0 || !cursor.moveToFirst()){
            cursor.close();
            sqLiteDatabase.close();
            return false;
        }
        return true;
    }

    //传入用户名，返回该用户已收藏的英雄
    public List<Hero> userLike(String username) {
        List<Hero> list = new ArrayList<Hero>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME2, null,
                "username = ?", new String[] {username},
                null, null, null);
        if (cursor.getCount() == 0 || !cursor.moveToFirst()){
            return null;
        }
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            list.add(findHero(cursor.getString(1)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    //用户添加英雄，传入用户名跟英雄名
    public void addHero(String username, String heroname){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("heroname", heroname);

        sqLiteDatabase.insert(TABLE_NAME3, null, values);
        sqLiteDatabase.close();
    }

    //用户删除添加的英雄，传入用户名跟英雄名
    public void unAddHero(String username, String heroname){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "username = ? and heroname = ?";
        String[] whereArgs = { username, heroname };
        sqLiteDatabase.delete(TABLE_NAME2, whereClause, whereArgs);

        String whereClause1 = "username = ? and heroname = ?";
        String[] whereArgs1 = { username, heroname };
        sqLiteDatabase.delete(TABLE_NAME3, whereClause1, whereArgs1);
        sqLiteDatabase.close();
    }

    //传入用户名，返回该用户已添加的英雄
    public List<Hero> userAdd(String username) {
        List<Hero> list = new ArrayList<Hero>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME3, null,
                "username = ?", new String[] {username},
                null, null, null);
        if (cursor.getCount() == 0 || !cursor.moveToFirst()){
            return null;
        }
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            list.add(findHero(cursor.getString(1)));
        }
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    //传入用户名，返回用户未添加的英雄
    public List<Hero> userToAdd(String username) {
        List<Hero> list = new ArrayList<Hero>();
        boolean equal;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor1 = sqLiteDatabase.query(TABLE_NAME3, null,
                "username = ?", new String[] {username},
                null, null, null);
        Cursor cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM " +  TABLE_NAME, null);
        if (cursor1.getCount() == 0 || !cursor1.moveToFirst()){
            for(cursor2.moveToFirst();!cursor2.isAfterLast();cursor2.moveToNext()){
                list.add(new Hero(cursor2.getString(0),cursor2.getString(1),cursor2.getString(2),
                        cursor2.getString(3),cursor2.getString(4),cursor2.getInt(5),
                        cursor2.getInt(6),cursor2.getInt(7),cursor2.getInt(8),
                        cursor2.getInt(9),cursor2.getString(10)));
            }
        }
        else{
            for(cursor2.moveToFirst();!cursor2.isAfterLast();cursor2.moveToNext()){
                equal = false;
                for(cursor1.moveToFirst();!cursor1.isAfterLast();cursor1.moveToNext()){
                    if(TextUtils.equals(cursor1.getString(1),cursor2.getString(2))){
                        equal = true;
                        break;
                    }
                }
                if(equal){
                    continue;
                }
                list.add(new Hero(cursor2.getString(0),cursor2.getString(1),cursor2.getString(2),
                        cursor2.getString(3),cursor2.getString(4),cursor2.getInt(5),
                        cursor2.getInt(6),cursor2.getInt(7),cursor2.getInt(8),
                        cursor2.getInt(9),cursor2.getString(10)));
            }
        }
        cursor1.close();
        cursor2.close();
        sqLiteDatabase.close();
        return list;
    }
}
