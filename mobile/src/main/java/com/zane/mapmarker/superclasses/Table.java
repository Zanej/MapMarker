package com.zane.mapmarker.superclasses;

import java.util.HashMap;

/**
 * Created by zane2 on 12/02/2016.
 */
public class Table {
    private String key;
    private String name;
    private HashMap<String,Object> elems;

    /**
     * Imposta il nome e la chiave della tabella, se non esiste la crea
     * @param name nome della tabella
     * @param key chiave della tabella
     */
    public Table(String name,String key,HashMap<String,String> elems,HashMap<String,String> types){
        this.name = name;
        this.key = key;
        if(elems != null && types != null){
            QueryBuilder builder = new QueryBuilder();
            if(!builder.createTable(this.name,elems,types)){
                System.out.println(builder.getQuery());
            }
        }
    }
    public Table(Table table){
        this.name = table.getName();
        this.key = table.getKey();
    }

    /**
     * Ritorna il nome
     * @return nome
     */
    public String getName() {
        return this.name;
    }

    /**
     * Ritorna la chiave
     * @return nome della chiave
     */
    public String getKey(){
        return this.key;
    }
    /**
     * Crea la tabella
     */
    @Override
    public boolean equals(Object object){
        return this.getName() ==  ((Table) object).getName() && this.getKey() == ((Table)object).getKey();
    }
}
