package com.homespliced.encounterbuilder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Keith on 1/21/2015.
 */
public class EncounterBuilderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EncounterBuilderDB";
    private static final String MONSTER_TABLE = "MonsterStatTable";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + MONSTER_TABLE + " ( _id integer primary key,CR text not null, Prof int not null, AC int not null, HPMin int not null, HPMax int not null, Attack Bonus int not null, DPRMin int not null, DPRMax int not null, Save DC int not null, Experience Points int not null);";

    public EncounterBuilderDatabaseHelper(MainActivity context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(EncounterBuilderDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MonsterStatTable");
        onCreate(database);
    }

    public void dropTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.close();
    }

    public void addMonsterLevel(String[] monsterLevel) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("addMonsterLevel", monsterLevel.toString());

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("CR", monsterLevel[0]); // get cr
        values.put("Prof", monsterLevel[1]); // get proficiency bonus
        values.put("AC", monsterLevel[2]); // get proficiency bonus
        values.put("HPMin", monsterLevel[3]); // get proficiency bonus
        values.put("Attack Bonus", monsterLevel[4]); // get proficiency bonus
        values.put("DPRMin", monsterLevel[5]); // get proficiency bonus
        values.put("DPRMax", monsterLevel[6]); // get proficiency bonus
        values.put("Save DC", monsterLevel[7]); // get proficiency bonus
        values.put("Experience Points", monsterLevel[8]); // get proficiency bonus

        // 3. insert
        db.insert("MonsterStatTable", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public boolean ImportMonsterExcel(String fileLocation) {
        try {
            BufferedReader CSVImport = new BufferedReader(new FileReader(fileLocation));

            dropTable("MonsterStatTable");

            String newLine;
            while ((newLine = CSVImport.readLine()) != null) {
                String[] lineParts = newLine.split(",");

                if (!(lineParts.length == 9)) {
                    throw new java.lang.IllegalArgumentException("CSV file did not have 9 columns");
                }

                addMonsterLevel(lineParts);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String[] getMonsterByXP(int XP) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = this.getWritableDatabase();

        queryBuilder.setTables(MONSTER_TABLE);

        Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);

        int rowID = -1;
        int currentRowXP = 0, lastRowXP = 0;
        while (cursor.moveToNext()) {
            currentRowXP = cursor.getInt(cursor.getColumnIndexOrThrow("Experience Points"));
            if (currentRowXP > XP) {
                rowID = cursor.getPosition();
                break;
            } else {
                lastRowXP = currentRowXP;
            }
        }

        //Here we move the cursor to the row with the closet XP value to what
        if (rowID == -1) {
            cursor.moveToLast();
        } else {
            double temp = lastRowXP + (lastRowXP - currentRowXP) * 0.75;
            if (XP > temp) {
                cursor.moveToPosition(rowID);
            } else {
                cursor.moveToPosition(rowID - 1);
            }
        }

        //TODO- WRITE CODE TO MOVE DATA OUT OF DATABASE INTO SOMETHING THAT I CAN RETURN TO THE MONSTER BUILDER

        return null;
    }
}
