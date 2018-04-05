package com.spogss.sportifypro.data;

/**
 * Created by samue on 28.03.2018.
 */

public class Database {
    private int id = 1;
    private static Database database;

    private Database(){

    }

    public static Database getInstance(){
        if(database == null) database = new Database();
        return database;
    }

    public int getCurrentUserId(){
        return id;
    }
}
