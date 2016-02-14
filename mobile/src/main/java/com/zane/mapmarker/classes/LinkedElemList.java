package com.zane.mapmarker.classes;

import com.zane.mapmarker.superclasses.Element;

import java.util.LinkedList;

/**
 * Created by zane2 on 12/02/2016.
 */

/**
 * @see LinkedList
 * @param <Object> Object
 */
public class LinkedElemList<Object> extends LinkedList<Object>{
    /**
     * @
     * @return
     */
    public Object is_in(Element object){
        LinkedElemList<DbElement> copy = new LinkedElemList();
        Object ret = null;
        while(!this.isEmpty()){
            DbElement elem = (DbElement) this.removeFirst();
            copy.addFirst(elem);
            if(elem.getId().equals(object.getId()) && elem.getTable().equals(elem.getTable())){
                ret = (Object) elem;
            }
        }
        while(!copy.isEmpty()){
            this.addLast((Object) copy.removeFirst());
        }
        return ret;
    }
}
