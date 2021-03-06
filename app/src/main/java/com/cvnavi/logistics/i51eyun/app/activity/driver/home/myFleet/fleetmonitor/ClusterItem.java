/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myFleet.fleetmonitor;


import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.model.LatLng;

/**
 * ClusterItem represents a marker on the map.
 */
public interface ClusterItem {

    /**
     * The position of this marker. This must always return the same value.
     */
    LatLng getPosition();

    BitmapDescriptor getBitmapDescriptor();
}