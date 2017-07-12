package com.ezhometeam.ui.base.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ezhometeam.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by minhd on 17/07/07.
 */

class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    LayoutInflater inflater = null;
    public static TextView tvPrice, tvAdress;

    CustomInfoWindow(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return (null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup = inflater.inflate(R.layout.layout_marker, null);
         tvPrice = (TextView) popup.findViewById(R.id.tv_price) ;
         tvAdress = (TextView) popup.findViewById(R.id.tv_address) ;

        return (popup);
    }
}
