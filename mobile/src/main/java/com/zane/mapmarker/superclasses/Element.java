package com.zane.mapmarker.superclasses;
import com.zane.mapmarker.classes.LinkedElemList;
import com.zane.mapmarker.interfaces.ElementInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zane2 on 11/02/2016.
 */
public class Element implements ElementInterface {
    private String id;
    private Map<String,Object> params;
    private static LinkedElemList<Element> elements;
    /**
     * Costruttore di default
     */
    public Element(){

    }
    public static Object checkElements(String id,Table table){
        return elements.is_in(new Element(id, null, table));
    }
    /**
     * Costruttore, inizializza l'elemento se non esiste (id null), ne prende i parametri se esiste
     * @param id id dell'elemento
     * @param params parametri da inserire
     * @param table Tabella
     */
    public Element(String id, HashMap<String, Object> params, Table table){
        Element e = (Element) checkElements(id,table);
        if(e == null){
            create(params);
        }else{
            this.id = id;
            this.params = params;
        }
    }
    public Element(Element e){
        this.setParams(e.getParams());
        this.id = e.getId();
    }
    /**
     * Crea un elemento del db
     *
     * @param params parametri da inserire
     */
    @Override
    public void create(HashMap<String, Object> params) {

    }

    /**
     * Aggiorna il record
     *
     * @param id     id della chiave del record
     * @param params parametri da aggiornare
     */
    @Override
    public void update(String id, HashMap<String, Object> params) {

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
     * @param add indica se è un aggiunta o un update
     */
    @Override
    public boolean set(String name, Object elem, boolean add) {
        return false;
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

    /**
     * Ritorna tutti i parametri
     */
    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String,Object> arr = new HashMap<String,Object>();
        Collection<Object> col = this.params.values();
        Set<String> keys = this.params.keySet();
        while(col.iterator().hasNext()){
            arr.put(keys.iterator().next(), col.iterator().next());
        }
        return arr;
    }

    /**
     * Setta i parametri
     *
     * @param params
     * @params params parametri
     */
    @Override
    public void setParams(HashMap<String, Object> params) {
        this.params.clear();
        Collection<Object> col = params.values();
        Set<String> keys = params.keySet();
        while(col.iterator().hasNext()){
            this.params.put(keys.iterator().next(), col.iterator().next());
        }
    }

    /**
     * Rimuove tutti i parametri
     */
    @Override
    public void removeParams() {
        this.params.clear();
    }

    /**
     * Rimuove un parametro
     *
     * @param nome nome parametro
     */
    @Override
    public void removeParam(String nome) {
        this.params.remove(nome);
    }


    public String getId(){
        return this.id;
    }
}
