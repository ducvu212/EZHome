package com.example.minhd.ezhome.ui.base.dialog;


import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.minhd.ezhome.R;
import com.example.minhd.ezhome.ui.base.fragment.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by n on 01/07/2017.
 */

public class HomeInfomationDialog extends Dialog implements View.OnClickListener {
    private TextView tvContent;

    public HomeInfomationDialog(@NonNull Context context, String content) {
        super(context);
        setContentView(R.layout.dialog_info_home);
        init(content);
    }

    private void init(String content) {
        tvContent = (TextView) findViewById(R.id.tv_info_in_dialog);
        findViewById(R.id.btn_direction).setOnClickListener(this);
        tvContent.setText(content);
    }

    @Override
    public void onClick(View v) {
        MapFragment fragment = new MapFragment();
    }

}
