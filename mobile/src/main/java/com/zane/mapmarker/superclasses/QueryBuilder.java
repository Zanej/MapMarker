package com.zane.mapmarker.superclasses;

import android.content.Context;

import com.zane.mapmarker.interfaces.QueryInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by zane2 on 13/02/2016.
 */
public class QueryBuilder implements QueryInterface {
    private String query;
    private HashMap<String,Table> alias;
    private DbHelper helper;
    private String action = "";
    private String from = "";
    private String params = "";
    private String where = "";
    private String groupby = "";
    private String orderby = "";
    private String which = "";
    private String having = "";
    private String limit = "";
    private static ArrayList<String> queryHistory;
    public QueryBuilder(){
        this.query = "";
        this.alias = new HashMap<String,Table>();
        if(queryHistory == null){
            queryHistory = new ArrayList<String>();
        }
        //DbHelper helper = new DbHelper();
    };
    public QueryBuilder(String query,HashMap<String,Table> alias,String action,String from,String params,String where){
        this();
        this.query = query;
        this.action = action;
        this.from = from;
        this.params = params;
        this.where = where;
        Collection<Table> values = alias.values();
        Set<String> keys = alias.keySet();
        helper = new DbHelper();
        Iterator value =  values.iterator();
        Iterator key = keys.iterator();
        while(value.hasNext()){
            Object k = key.next();
            this.alias.put(String.valueOf(k),new Table((Table) value.next()));
        }
    }
    public QueryBuilder(QueryBuilder elem){
        this(elem.getQuery(), elem.getAlias(), elem.getAction(),elem.from,elem.params,elem.where);
    }
    /**
     * Fa una select
     *
     * @param what  i campi che vuoi ottenere di quella tabella
     * @param where il where di questa tabella
     * @param table l'oggetto table
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    @Override
    public QueryInterface Select(String[] what, HashMap<String, Object> where, Table table) {
        flush();
        this.action="SELECT";
        this.alias.put("a",table);
        this.params=setParams(what, this.alias);
        this.from=setFrom(this.alias, null, null);
        this.where+=setWhere(where);
        this.query = this.action+" "+this.params+this.from+this.where;
        
        return this;
    }

    /**
     * Fa un update
     *
     * @param what  i campi che vuoi modificare di quella tabella
     * @param where il where di questa tabella
     * @param table l'oggetto table
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    @Override
    public QueryInterface Update(HashMap<String,Object> what, HashMap<String, Object> where, Table table) {
        flush();
        this.action = "UPDATE";
        this.alias.put("a",new Table(table));
        this.from=table.getName()+" AS a";
        Iterator keys = what.keySet().iterator();
        Iterator values = what.values().iterator();
        while(keys.hasNext()){
            this.params+="a."+keys.next()+" = '"+values.next()+"',";
        }
        this.params = this.params.substring(0, this.params.length() - 1);
        this.where+=setWhere(where);
        this.query = this.action+" "+this.from+" SET " +this.params+this.where;
        return this;
    }

    /**
     * Fa un delete
     *
     * @param where il where di questa tabella
     * @param table l'oggetto table
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    @Override
    public QueryInterface Delete(HashMap<String, Object> where, Table table) {
        flush();
        this.action = "DELETE";
        this.alias.put("a", new Table(table));
        this.from=query+=setFrom(this.alias,null,null);
        this.where=query+=setWhere(where);
        this.query = this.action+" "+this.from+this.where;
        return this;
    }

    /**
     * Fa una join
     * @param what i campi che vuoi prendere di quella tabella
     * @param on array di due elementi => campo e tabella
     * @param where where di questa tabella
     * @return un nuovo oggetto queryinterface, per poter fare join ecc
     */
    public QueryInterface Join(String[] what,ArrayList<HashMap<String,Table>> on,HashMap<String,Object> where){
        if(on.size() != 2)
            throw new IllegalArgumentException("L'on deve essere un'array di lunghezza due!");
        if(this.action=="SELECT") {
            String last_alias = lastAlias();
            String alias_first_table = searchAlias(on);
            Iterator table = on.get(1).values().iterator();
            Table asd = (Table) table.next();
            //while(table.hasNext()){
            //asd = new Table((Table) table.next());
            //}
            String alias = Character.toString((char) (last_alias.codePointAt(0) + 1));
            this.alias.put(alias, asd);
            String join = setJoin(on,alias_first_table,alias,asd);
            this.from += join;
            this.params += ",";
            for (String elem : what) {
                this.params += alias + "." + elem + ",";
            }
            this.params = this.params.substring(0, this.params.length() - 1);

            String addwhere = setWhere(where, alias);
            if (addwhere != "") {
                this.where += " AND" + addwhere;
            }
            this.query = this.action + " " + this.params + this.from + this.where;
        } else if(this.action == "DELETE"){

        } else {
            throw new IllegalArgumentException("wtf are you doing");
        }
        return this;
    }
    /**
     * Fa una join update
     * @param what mappa campo => valore
     * @param on array di due tabelle di join con campi
     * @param where where di questa tabella
     */
    public QueryInterface JoinUpdate(HashMap<String,Object> what,ArrayList<HashMap<String,Table>> on,HashMap<String,Object> where){
        String last_alias = lastAlias();
        String alias_first_table = searchAlias(on);
        String alias = Character.toString((char) (last_alias.codePointAt(0) + 1));
        Iterator table = on.get(1).values().iterator();
        Table asd = (Table) table.next();
        this.alias.put(alias,asd);
        Iterator name = what.keySet().iterator();
        Iterator val = what.values().iterator();
        this.params+=",";
        while(name.hasNext()){
            this.params+=alias+"."+name.next()+" = '"+val.next()+"',";
        }
        this.params = this.params.substring(0, this.params.length() - 1);
        String join = setJoin(on,alias_first_table,alias,asd);
        this.from+=join;
        String where_join = setWhere(where,alias);
        if(where_join != ""){
            this.where+=where_join;
        }
        this.query = this.action+" "+this.from+" SET "+this.params+this.groupby+this.where+this.orderby;
        return this;
    }

    /**
     * Group by
     *
     * @param params Array dei parametri
     * @param table  tabella
     */
    @Override
    public QueryInterface GroupBy(String[] params, Table table) {

        switch (this.action) {
            case "SELECT":
                this.groupby += setGroupBy(params, table);
                break;
            case "UPDATE":
                break;
            case "DELETE":
                break;
        }
        setInternalQuery();
        return this;
    }

    /**
     * Order by
     *
     * @param params Array dei parametri
     * @param table  tabella
     */
    @Override
    public QueryInterface OrderBy(String[] params, Table table) {
        this.orderby += setOrderBy(params, table);
        setInternalQuery();
        return this;
    }

    /**
     * Limit
     *
     * @param from from
     * @param to   to
     */
    @Override
    public QueryInterface Limit(String from, String to) {
        if(this.action == ""){
            throw new NoSuchElementException("No query inserted!");
        }
        this.limit = " LIMIT "+from;
        if(to != ""){
            this.limit +=" , "+to;
        }

        return this;
    }

    /**
     * Ritorna la query generata
     *
     * @return String query
     */
    @Override
    public String getQuery() {
        return this.query;
    }
    /**
     * Cerca l'alias delle due tabelle passate
     * @param on array di due mappe (prima e seconda tabella con campi)
     * @Ritorna l'alias trovato
     */
    private String searchAlias(ArrayList<HashMap<String,Table>> on){
        String last_alias = "a";
        Iterator al = this.alias.keySet().iterator();
        Iterator table_alias = this.alias.values().iterator();
        Iterator on_1table = on.get(0).values().iterator();
        Table xt = new Table( (Table) on_1table.next());
        String alias_first_table="";
        while(al.hasNext()){
            last_alias = String.valueOf(al.next());
            if (xt.equals(table_alias.next())) {
                alias_first_table = last_alias;
            }
        }
        return alias_first_table == "" ? "a" : alias_first_table;
    }
    /**
     * Cerca un alias da una tabella
     */
    private String searchAlias(Table table){
        String alias = "a";
        Iterator al = this.alias.keySet().iterator();
        Iterator tb = this.alias.values().iterator();
        boolean find = false;
        while(al.hasNext()){
            alias = String.valueOf(al.next());
            if(tb.next().equals((Table) table)){
                find = true;
                break;
            }
        }
        if(find){
            return alias;
        }
        return "";
    }
    /**
     * Ritorna l'ultimo alias
     */
    private String lastAlias(){
        Iterator al = this.alias.keySet().iterator();
        String last = "";
        while(al.hasNext()){
            last = String.valueOf(al.next());
        }
        return last;
    }
    /**
     * Ritorna la parte join della query date le tabelle
     * @param on lista tabelle => valori
     */
    private String setJoin(ArrayList<HashMap<String,Table>> on,String alias_first,String newalias,Table asd){
        String join = " JOIN " + asd.getName() + " AS " + alias + " ON ";
        Iterator on_1field = on.get(0).keySet().iterator();
        Iterator on_2field = on.get(1).keySet().iterator();
        while (on_1field.hasNext()) {
            join += alias_first + "." + on_1field.next() + " = " + alias + "." + on_2field.next();
        }
        return join;
    }
    /**
     * Ritorna il group by relativo a quella tabella
     * @param params parametri
     * @param table tabella
     */
    public String setGroupBy(String[] params,Table table){
        Iterator keys = this.alias.keySet().iterator();
        Iterator values = this.alias.values().iterator();
        String alias = "";
        String order = "";
        while(keys.hasNext()){
            String key = String.valueOf(keys.next());
            if(values.next().equals(table)){
                alias = key;
                break;
            }
        }
        if(alias == ""){
            throw new NoSuchElementException("Table is not in query!");
        }
        for(String elem : params){
            order+=alias+"."+elem+",";
        }
        order = order.substring(0,order.length()-1);
        return order;
    }
    /**
     * Ritorna l'order By relativo a quella tabella
     * @param params parametri
     * @param table tabella
     */
    private String setOrderBy(String[] params,Table table){
        Iterator keys = this.alias.keySet().iterator();
        Iterator values = this.alias.values().iterator();
        String alias = "";
        String order = "";
        while(keys.hasNext()){
            String key = String.valueOf(keys.next());
            if(values.next().equals(table)){
                alias = key;
                break;
            }
        }
        if(alias == ""){
            throw new NoSuchElementException("Table is not in query!");
        }
        for(String elem : params){
            order+=alias+"."+elem+",";
        }
        order = order.substring(0,order.length()-1);
        return order;
    }
    /**
     * Ritorna il risultato sotto forma di hashmap
     *
     * @return risultato della query
     */
    @Override
    public HashMap<String, Object> getResult() {
        return null;
    }
    /**
     * Ritorna gli alias
     */
    public HashMap<String,Table> getAlias(){
        return this.alias;
    }
    /**
     * Crea una tabella
     * @param name nome della tabella
     * @param elems elementi (nome => elemento), la prima viene considerata chiave
     * @param typeofkey tipo delle chiavi dei singoli elementi ("UNIQUE","FOREIGN|table|elemento")
     */
    public boolean createTable(String name,HashMap<String,String> elems,String[] typeofkey){
        String query =" CREATE TABLE IF NOT EXISTS "+name+" (";
        Set<String> names = elems.keySet();
        Collection<String> types = elems.values();
        query+=String.valueOf(" id_"+name).toLowerCase()+" INTEGER PRIMARY KEY AUTO_INCREMENT,";
        Iterator nome = names.iterator();
        Iterator tipl = types.iterator();
        for(String tipo:typeofkey){

            query+=nome.next()+" "+tipl.next()+" ";
            if(tipo.indexOf("FOREIGN") > -1) {
                String[] explode = tipo.split("|");
                query+="REFERENCES "+explode[1]+"."+explode[2];
            }else{
                query+=tipo;
            }
            query+=",";
        }
        query = query.substring(0,query.length() -1);
        query+=")";
        
        return true;
    }

    /**
     * Setta il where se è presente solo una tabella (niente join)
     * @param values valori del where
     * @return stringa
     */
    private String setWhere(HashMap<String,Object> values){
        String query =" WHERE ";
        Iterator value = values.values().iterator();
        Iterator name = values.keySet().iterator();
        while(value.hasNext()){
            query+=" a."+name.next()+" = '"+value.next()+"' AND";
        }
        query = query.substring(0,query.length()-3);
        return query;
    }

    /**
     * Imposta il where se più tabelle sono coinvolte (JOIN)
     * @param val array di array di nome => valore
     * @param alias alias delle tabelle
     * @return stringa
     */
    private String setWhere(ArrayList<HashMap<String,Object>> val,HashMap<String,Table> alias){
        Iterator tables = alias.keySet().iterator();
        String query = " WHERE ";
        for(HashMap<String,Object> where : val){
            Iterator values = where.values().iterator();
            Iterator keys = where.keySet().iterator();
            String table = String.valueOf(tables.next());
            while(values.hasNext()){
                query+=" "+table+"."+keys.next()+" = '"+values.next()+"' AND";
            }
        }
        query = query.substring(0,query.length()-3);
        return query;
    }
    /**
     * Aggiunge elementi al where in base all'alias passato
     * @param where hashmap dei where
     * @param alias nome dell'alias
     */
    private String setWhere(HashMap<String,Object> where,String alias){
        String query = "";
        Iterator value = where.values().iterator();
        Iterator name = where.keySet().iterator();
        while(value.hasNext()){
            query+=" "+alias+"."+name.next()+" = '"+value.next()+"' AND";
        }
        if(query.length() > 3)
            query = query.substring(0,query.length()-3);
        return query;
    }
    /**
     * Setta il from, e gli eventuali JOIN se ci sono più alias
     * @param alias alias
     * @param on on (prima dell'uguale) dei join
     * @param on_this on (dopo l'uguale) dei join
     * @return
     */
    private String setFrom(HashMap<String,Table> alias,HashMap<String,Table> on,HashMap<String,Table> on_this){
        if(alias.size() == 1)
            return " FROM "+alias.values().iterator().next().getName()+" AS "+alias.keySet().iterator().next()+" ";
        return "";
    }
    private String setParams(String[] what,HashMap<String,Table> alias){
        if(alias.size() == 1){
            for(String elem : what){
                query+="a."+elem+",";
            }
            query = query.substring(0,query.length()-1);
            return query;
        }
        return "";
    }
    public void setContext(Context context){
        DbHelper.context = context;
    }
    public String getAction(){
        return this.action;
    }
    public static String queryHistoryString(){
        String ret = "";
        for(String query : queryHistory){
            ret+=query+";";
        }
        //ret+=" "+this.query;
        return ret;
    }
    public static ArrayList<String> queryHistory(){
        return queryHistory;
    }
    private void setInternalQuery(){
        switch(this.action){
            case "SELECT":
                this.query=this.action+" "+this.params+this.from+this.where;
                if(this.groupby != ""){
                    this.query+=" GROUP BY "+this.groupby;
                }
                this.query+="ORDER BY "+this.orderby;
                this.query+=this.limit;
                break;
            case "UPDATE":
                this.query=this.action+" "+this.from+" SET "+this.params+this.where;
                if(this.groupby != ""){
                    this.query+=" GROUP BY "+this.groupby;
                }
                this.query+="ORDER BY "+this.orderby;
                this.query+=this.limit;
                break;
            case "DELETE":
                this.query=this.action+" "+this.from+this.where;
                if(this.groupby != ""){
                    this.query+=" GROUP BY "+this.groupby;
                }
                this.query+="ORDER BY "+this.orderby;
                this.query+=this.limit;
                break;
        }
    }
    public void flush(){
        queryHistory.add(this.query);
        this.query="";
        this.action="";
        this.where = "";
        this.from = "";
        this.params = "";
        this.having = "";
        this.orderby = "";
        this.which = "";
        this.groupby = "";
        this.alias.clear();
    }

}
