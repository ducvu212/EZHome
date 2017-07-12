package com.ezhometeam.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ezhometeam.R;
import com.ezhometeam.VideoCall.LoginActivity;
import com.ezhometeam.ui.base.activity.Main2Activity;
import com.ezhometeam.ui.base.fragment.MapActivity;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.ezhometeam.ui.dialog.SearchDiaglog.address;

/**
 * Created by n on 01/07/2017.
 */

public class HomeInfomationDialog extends Dialog implements View.OnClickListener {
    private TextView tvContent;
    Main2Activity activity;
    private LatLng latLng;

    public HomeInfomationDialog(@NonNull Context context, String content) {
        super(context);
        setContentView(R.layout.dialog_info_home);
        init(content);
    }

    private void init(String content) {
        tvContent = (TextView) findViewById(R.id.tv_info_in_dialog);
        findViewById(R.id.btn_direction).setOnClickListener(this);
        findViewById(R.id.btn_call).setOnClickListener(this);
        tvContent.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_direction:
                Intent intent1 = new Intent(getContext(), MapActivity.class);
                String codirnate = makerAdress(address);
                intent1.putExtra("des",codirnate);
                getContext().startActivity(intent1);
                break;

            case R.id.btn_call:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
                break;

            default:
        }
    }

    private String makerAdress(String adress) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        double latitude = 0, longitude = 0;
        String con = null ;

        try {
            List<Address> addresses = geocoder.getFromLocationName(adress, 1);
            Address address = addresses.get(0);
             longitude = address.getLongitude();
             latitude = address.getLatitude();
            Log.d("Adres", latitude + "\n" + longitude);
            con = latitude + "," + longitude ;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return con;

    }


}
