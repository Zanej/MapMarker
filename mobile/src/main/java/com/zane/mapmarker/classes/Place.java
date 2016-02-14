package com.zane.mapmarker.classes;

import com.zane.mapmarker.classes.DbElement;

/**
 * Created by zane2 on 11/02/2016.
 */
public class Place extends DbElement {
    public void setPosition(Double Lat,Double Lng){
        set("lat", Lat, false);
        set("lng", Lng, false);
    }
}
