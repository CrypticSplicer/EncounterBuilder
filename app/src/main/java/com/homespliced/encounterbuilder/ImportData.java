package com.homespliced.encounterbuilder;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Keith on 1/21/2015.
 */
public class ImportData extends MonsterStatDatabaseHelper{
    private Context context;

    public ImportData(MainActivity context) {
        super(context);
    }

    public boolean ImportMonster(String fileLocation) {
        try {
            BufferedReader CSVImport = new BufferedReader(new FileReader(fileLocation));

            String newLine;
            while ((newLine = CSVImport.readLine()) != null) {
                String[] lineParts = newLine.split(",");

                //TODO- ERROR CHECK STRING ARRAY BEFORE SENDING TO MHELPER
                dropTable("MonsterStatTable");
                addMonsterLevel(lineParts);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
