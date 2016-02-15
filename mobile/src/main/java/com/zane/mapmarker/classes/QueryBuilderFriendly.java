package com.zane.mapmarker.classes;

import com.zane.mapmarker.superclasses.QueryBuilder;
import com.zane.mapmarker.superclasses.Table;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zane2 on 14/02/2016.
 */
public class QueryBuilderFriendly extends QueryBuilder {
    /**
     * Select friendly
     * @param from Tabella
     * @param what parametri select
     * @param wherename nomi campi where
     * @param whereval valori campi where
     * @return this, per permettere più operazioni
     */
    public QueryBuilderFriendly Select(String from,String[] what,String[] wherename,Object[] whereval){
        Table l = new Table(from,"",null,null);
        HashMap<String,Object> where = setWhereFromParams(wherename,whereval);
        super.Select(what,where,l);
        return this;
    }

    /**
     * Update friendly
     * @param from Tabella
     * @param setname nomi campi SET
     * @param setval valori campi SET
     * @param wherename nomi campi WHERE
     * @param whereval valori campi WHERE
     * @return this, per permettere più operazioni
     */
    public QueryBuilderFriendly Update(String from,String[] setname,Object[] setval,String[] wherename,Object[] whereval){
        Table l = new Table(from,"",null,null);
        HashMap<String,Object> where = setWhereFromParams(wherename,whereval);
        HashMap<String,Object> set = setWhereFromParams(setname,setval);
        super.Update(set, where, l);
        return this;
    }

    /**
     * Delete friendly
     * @param from Tabella
     * @param wherename nomi campi WHERE
     * @param whereval valori campi WHERE
     * @return this, per permettere più operazioni
     */
    public QueryBuilderFriendly Delete(String from,String[] wherename,Object[] whereval){
        Table l = new Table(from,"",null,null);
        HashMap<String,Object> where = setWhereFromParams(wherename,whereval);
        super.Delete(where, l);
        return this;
    }

    /**
     * Join
     * @param from Prima tabella
     * @param to Seconda tabella
     * @param what Cosa si vuole ottenere della seconda tabella
     * @param on_first campi della prima
     * @param on_second campi della seconda
     * @param wherename nomi where della seconda
     * @param whereval valori where della seconda
     * @return this, per permettere più operazioni
     */
    public QueryBuilderFriendly Join(String from,String to,String[] what,String[] on_first,String[] on_second,String[] wherename,Object[] whereval){
        Table uno = new Table(from,"",null,null);
        Table due = new Table(to,"",null,null);
        HashMap<String,Object> where = setWhereFromParams(wherename,whereval);
        ArrayList<HashMap<String,Table>> arr = new ArrayList<HashMap<String, Table>>();
        HashMap first = new HashMap<String,Table>();
        HashMap second = new HashMap<String,Table>();
        for(Integer i=0;i<on_first.length;i++){
            first.put(on_first[i],uno);
            second.put(on_second[i],due);
        }
        arr.add(first);
        arr.add(second);
        super.Join(what,arr,where);
        return this;
    }
    /**
     * Da due array di nomi e valori restituisce un hashmap
     * @param wherename Array di nomi
     * @param whereval Array di valori
     * @return Hashmap nome => valore
     */
    private HashMap<String,Object> setWhereFromParams(String[] wherename,Object[] whereval){
        HashMap<String,Object> where = new HashMap<String,Object>();
        for(Integer i=0;i<wherename.length;i++){
            where.put(wherename[i],whereval[i]);
        }
        return where;
    }

}
