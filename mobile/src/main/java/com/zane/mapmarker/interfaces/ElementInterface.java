package com.zane.mapmarker.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zane2 on 12/02/2016.
 */
public interface ElementInterface {
    /**
     * Crea un elemento del db
     * @param params parametri da inserire
     */
    public void create(HashMap<String,Object> params);

    /**
     * Aggiorna il record
     * @param id id della chiave del record
     * @param params parametri da aggiornare
     */
    public void update(String id,HashMap<String,Object> params);
    /**
     * Ritorna il valore della colonna
     * @param name nome del parametro
     */
    public Object get(String name);
    /**
     * Setta il valore della colonna (nell'oggetto)
     * @param name nome della colonna
     * @param elem oggetto
     * @param add indica se è un aggiunta o un update
     */
    public boolean set(String name,Object elem,boolean add);
    /**
     * Salva i parametri sul db
     */
    public boolean saveParams();
    /**
     * Salva il parametro selezionato sul db
     * @param name Nome del parametro
     */
    public boolean saveParam(String name);
    /**
     * Ritorna tutti i parametri
     */
    public HashMap<String,Object> getParams();
    /**
     * Ritorna la tabella
     */
    /**
     * Setta i parametri
     * @params params parametri
     */
    public void setParams(HashMap<String,Object> params);
    /**
     * Rimuove tutti i parametri
     */
    public void removeParams();
    /**
     * Rimuove un parametro
     * @param nome nome parametro
     */
    public void removeParam(String nome);
    


}
