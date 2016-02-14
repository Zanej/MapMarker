package com.zane.mapmarker.classes;

import com.zane.mapmarker.superclasses.Element;
import com.zane.mapmarker.superclasses.Table;

import java.util.HashMap;

/**
 * Created by zane2 on 12/02/2016.
 */
public class DbElement extends Element {
    private HashMap<String,Object> params;
    private Table table;
    /**
     * Crea un elemento del db
     *
     * @param params parametri da inserire
     * @param table oggetto table
     */
    public void create(HashMap<String, Object> params,Table table) {

    }

    /**
     * Aggiorna il record
     *
     * @param id     id della chiave del record
     * @param params parametri da aggiornare
     */
    public void update(String id, HashMap<String, Object> params,boolean save) {

    }

    /**
     * Ritorna il valore della colonna
     *
     * @param name nome del parametro
     */
    @Override
    public Object get(String name) {
        return null;
    }

    /**
     * Setta il valore della colonna (nell'oggetto)
     *
     * @param name nome della colonna
     * @param elem oggetto
     * @param add indica se è un update o un add
     */
    @Override
    public boolean set(String name, Object elem,boolean add) {
        this.params.put(name,elem);
        if(add){
            //TODO
            return false;
        }
        return true;
    }

    /**
     * Salva i parametri sul db
     */
    @Override
    public boolean saveParams() {
        return false;
    }

    /**
     * Salva il parametro selezionato sul db
     *
     * @param name Nome del parametro
     */
    @Override
    public boolean saveParam(String name) {
        return false;
    }

    public void setParams(HashMap<String,Object> params){
        super.setParams(params);
    }

    /**
     * Ritorna la tabella
     */
    public Table getTable() {
        return new Table(this.table);
    }

    /**
     * Setta la tabella
     * @param table tabella
     */
    public void setTable(Table table) {
        this.table = new Table(table);
    }
}
