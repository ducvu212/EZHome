package com.ezhometeam.ui.base.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezhometeam.R;
import com.ezhometeam.common.InfomationRegister;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ezhometeam.ui.base.fragment.CustomInfoWindow.tvAdress;
import static com.ezhometeam.ui.base.fragment.CustomInfoWindow.tvPrice;

/**
 * Created by ducnd on 5/28/17.
 */

public class MapFragment extends SupportMapFragment implements
        OnMapReadyCallback, FragmentCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationChangeListener, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.InfoWindowAdapter {

    private View viewMap;

    private static final String TAG = MapFragment.class.getSimpleName();
    public static GoogleMap googleMap;
    public static Marker marker;
    private boolean isFirstChangeLocation;
    //lay dia chi vi tri thong latlong
    private Geocoder geocoder;
    private double longitude, latitude;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        initGoogle(googleMap);
    }

    private void initGoogle(GoogleMap googleMap) {
        //set up goole map

        MapFragment.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //set up UI
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        //lay vi tri hien tai cua dien thoai
        //truoc khi lay vi tri dien thoai phai xin quyen

        int checkPermisonLocation =
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkPermisonLocation == PackageManager.PERMISSION_DENIED) {
            //hien thi dialog yeu cau nguoi dung dong y permission nay
            boolean shouldShow =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
            if (shouldShow) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                        , 100);
            } else {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm")
                        .setMessage("Location")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 101);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

            }
            return;
        }

        initMyLocation();
        makerAdress();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            //permion
            int checkPermisonLocation =
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION);

            if (checkPermisonLocation == PackageManager.PERMISSION_GRANTED) {
                initMyLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int per : grantResults) {
            if (per == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }

        //nguoi dung dong y het
        initMyLocation();

    }

    private void initMyLocation() {
        googleMap.setOnMyLocationChangeListener(this);
        //custom windown adpater: click marker
//        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(this);
        googleMap.setMyLocationEnabled(true);
        checkOpenLocation();
    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.d(TAG, "location lat: " + location.getLatitude());
        Log.d(TAG, "location long: " + location.getLongitude());
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


        if (!isFirstChangeLocation) {
            isFirstChangeLocation = true;
            //co chuc nang di chuyen den vi tri position
            CameraPosition cameraPosition =
                    new CameraPosition(latLng, 15, 0, 0);
            //dua camera position vao google map
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            MarkerOptions options = new MarkerOptions();
            options.
                    icon(BitmapDescriptorFactory.
                            defaultMarker(BitmapDescriptorFactory.HUE_RED));
            options.position(latLng);
            options.title("My location");
            options.snippet(getLocation(latLng));
            marker = googleMap.addMarker(options);
//            marker.showInfoWindow();

        } else {
            marker.setTitle("My location");
            marker.setSnippet(getLocation(latLng));
            marker.setPosition(latLng);

        }

    }

    private String getLocation(LatLng latLng) {
        try {
            List<Address> addresses =
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses == null || addresses.size() == 0) {
                return null;
            }
            String result = "";
            int maxLine = addresses.get(0).getMaxAddressLineIndex();
            result = addresses.get(0).getAddressLine(0);
            for (int i = 1; i < maxLine; i++) {
                result += ", " + addresses.get(0).getAddressLine(i);
            }
            result += ", " + addresses.get(0).getCountryName();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //check open loaction
    //ve nha xem
    public boolean checkOpenLocation() {
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Open location");
            dialog.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(myIntent, 102);
                    //get gps
                }
            });
            dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_marker, null);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvAdress = (TextView) view.findViewById(R.id.tv_address);
        ImageView ivImage = (ImageView)view.findViewById(R.id.iv_image);
        ///thao tao trong view binh thuong
        int position = Integer.valueOf(marker.getSnippet());
        tvPrice.setText(infomationRegisters.get(position).getPrice());
        tvAdress.setText(infomationRegisters.get(position).getAddress());
        String linkImage =  infomationRegisters.get(position).getLinkImg();
        if( linkImage != null && !"".equals(linkImage)) {
            Log.d(TAG, "getInfoWindow " +linkImage );
            Picasso.with(getContext())
                    .load(linkImage)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .into(ivImage);
        }else {
            Picasso.with(getContext())
                    .load(android.R.color.darker_gray)
                    .placeholder(android.R.color.darker_gray)
                    .error(android.R.color.darker_gray)
                    .into(ivImage);
        }

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_marker, null);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvAdress = (TextView) view.findViewById(R.id.tv_address);
        ImageView ivImage = (ImageView)view.findViewById(R.id.iv_image);
        ///thao tao trong view binh thuong
        int position = Integer.valueOf(marker.getSnippet());
        tvPrice.setText(infomationRegisters.get(position).getPrice());
        tvAdress.setText(infomationRegisters.get(position).getAddress());
        String linkImage =  infomationRegisters.get(position).getLinkImg();
        if( linkImage != null && !"".equals(linkImage)) {
            Picasso.with(getContext())
                    .load(infomationRegisters.get(position).getLinkImg())
                    .placeholder(R.drawable.home)
                    .error(R.drawable.home)
                    .into(ivImage);
        }else {
            Picasso.with(getContext())
                    .load(R.drawable.home)
                    .placeholder(R.drawable.home)
                    .error(R.drawable.home)
                    .into(ivImage);
        }

        return view;
    }

    private List<InfomationRegister> infomationRegisters = new ArrayList<>();


    private void makerAdress() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("Master").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    InfomationRegister infomation = dataSnapshot.getValue(InfomationRegister.class);
                    infomationRegisters.add(infomation);

                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                    MarkerOptions options = new MarkerOptions();
                    List<Address> addresses = new ArrayList<>();
                    Address address = geocoder.getFromLocationName(infomation.getAddress(), 1).get(0);
                    addresses.add(address);

                    longitude = address.getLongitude();
                    latitude = address.getLatitude();

                    LatLng latLng = new LatLng(latitude, longitude);
                    options.
                            icon(BitmapDescriptorFactory.
                                    defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    options.position(latLng);

                    options.snippet((infomationRegisters.size() - 1) + "");
                    marker = googleMap.addMarker(options);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
