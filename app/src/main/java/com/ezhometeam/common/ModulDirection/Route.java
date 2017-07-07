package com.ezhometeam.common.ModulDirection;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Vu Minh Quang on 06-Jul-17.
 */


public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
