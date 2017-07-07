package com.ezhometeam.gallery;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.ezhometeam.gallery.utils.PermissionUtils;

/**
 * Created by n on 06/07/2017.
 */

public class BaseGalleryActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.checkPermission(this, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
