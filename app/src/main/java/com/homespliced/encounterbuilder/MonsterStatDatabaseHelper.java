package com.homespliced.encounterbuilder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Keith on 12/25/2014.
 */
public class MonsterStatDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MonsterStatTable";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table MonsterStatTable ( _id integer primary key,CR text not null, Proficiency Bonus not null, Armor Class not null, Hit Point Min not null, Hit Point Max not null, Attack Bonus not null, Damage/Round Min not null, Damage/Round Max not null, Save DC not null, Experience Points not null);";

    public MonsterStatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(MonsterStatDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MonsterStatTable");
        onCreate(database);
    }
}
