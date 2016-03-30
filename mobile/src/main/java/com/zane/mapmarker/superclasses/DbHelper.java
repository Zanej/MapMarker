package com.zane.mapmarker.superclasses;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by zane2 on 13/02/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME ="test";
    public static Context context;
    public static QueryBuilder qb;
    private final String[] tables = {"aziende","utenti"};
    public DbHelper(){
        this(context);
    }
    public DbHelper(Context context){
        super(context,DB_NAME,null,1);
        QueryBuilder.helper = this;
        this.onCreate(this.getWritableDatabase());
    }
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] campi_azienda = {
                "nome-VARCHAR(255)- ",
                "dipendenti-VARCHAR(255)- "
        };
        HashMap<String,String> chiavi_azienda = new HashMap<>();
        HashMap<String,String> elem_azienda = new HashMap<>();
        for(int i=0;i<campi_azienda.length;i++){
            String[] elems = campi_azienda[i].split("-");
            elem_azienda.put(elems[0],elems[1]);
            chiavi_azienda.put(elems[0],elems[2]);
        }
        String[] campi_utenti = {
                "nome-VARCHAR(255)- ",
                "cognome-VARCHAR(255)- ",
                "azienda-INTEGER-FOREIGN#aziende#id_aziende",
                "citta-VARCHAR(255)- "
        };
        HashMap<String,String> elem_utenti = new HashMap<>();
        HashMap<String,String> chiavi_utenti = new HashMap();
        for(int i=0;i<campi_utenti.length;i++){
            String[] elems = campi_utenti[i].split("-");
            elem_utenti.put(elems[0],elems[1]);
            chiavi_utenti.put(elems[0],elems[2]);
        }
        Table azienda = new Table("aziende","id_aziende",elem_azienda,chiavi_azienda);
        Table utenti = new Table("utenti","id_utenti",elem_utenti,chiavi_utenti);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //this.onCreate(db);
    }

    /**
     * Executes a query
     * @param query query
     * @return boolean
     */
    public boolean execSQL(String query){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL(query);
            return true;
        }catch(SQLException ex){
            return false;
        }
    }
    /**
     * Get all tables name
     */
    public Cursor getTables(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = " SELECT name FROM sqlite_master " + " WHERE type='table' AND name!='android_metadata'";
        return db.rawQuery(sql, null);
    }
    /**
     * Gets all rows of a table
     */
    public Cursor getRows(String table_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql= qb.Select(null,null,new Table(table_name,"id_"+table_name,null,null)).getQuery();
        return db.rawQuery(sql,null);
    }
    /**
     * Insert in a table
     */
    public boolean insert(String table_name,HashMap<String,String> values){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = qb.insert(new Table(table_name, "", null, null), values).getQuery();
        System.out.println(sql);
        return this.execSQL(sql);
    }
    /**
     * Insert in a table
     */
    public boolean insert(String table_name,String[] values){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = qb.insert(new Table(table_name,"",null,null),values).getQuery();
        return this.execSQL(sql);
    }

}
