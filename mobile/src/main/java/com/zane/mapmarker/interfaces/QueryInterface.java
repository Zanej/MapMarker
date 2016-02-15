package com.zane.mapmarker.interfaces;

import com.zane.mapmarker.superclasses.Table;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zane2 on 13/02/2016.
 */
public interface QueryInterface {
    /**
     * Fa una select
     * @param what i campi che vuoi ottenere di quella tabella
     * @param where il where di questa tabella
     * @param table l'oggetto table
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    public QueryInterface Select(String[] what,HashMap<String,Object>where,Table table);

    /**
     * Fa un update
     * @param what i campi che vuoi modificare di quella tabella
     * @param where il where di questa tabella
     * @param table l'oggetto table
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    public QueryInterface Update(HashMap<String,Object> what, HashMap<String, Object> where, Table table);

    /**
     * Fa un delete
     * @param where il where di questa tabella
     * @param table l'oggetto table
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    public QueryInterface Delete(HashMap<String,Object> where,Table table);

    /**
     * Fa una join
     * @param what i campi che vuoi prendere di quella tabella
     * @param on array di due elementi => campo e tabella
     * @param where where di questa tabella
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    public QueryInterface Join(String[] what,ArrayList<HashMap<String,Table>> on,HashMap<String,Object> where);
    /**
     * Fa una join update
     * @param what mappa campo => valore
     * @param on array di due tabelle di join con campi
     * @param where where di questa tabella
     */
    public QueryInterface JoinUpdate(HashMap<String,Object> what,ArrayList<HashMap<String,Table>> on,HashMap<String,Object> where);
    /**
     * Group by
     * @param params Array dei parametri
     * @param table tabella
     */
    public QueryInterface GroupBy(String[] params,Table table);
    /**
     * Order by
     * @param params Array dei parametri
     * @param table tabella
     */
    public QueryInterface OrderBy(String[] params,Table table);
    /**
     * Limit
     * @param from from
     * @param to to
     */
    public QueryInterface Limit(String from,String to);
    /**
     * Ritorna la query generata
     * @return String query
     */
    public String getQuery();

    /**
     * Ritorna il risultato sotto forma di hashmap
     * @return risultato della query
     */
    public HashMap<String,Object> getResult();

}
