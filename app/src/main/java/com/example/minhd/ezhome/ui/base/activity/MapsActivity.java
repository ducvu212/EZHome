package com.example.minhd.ezhome.ui.base.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.minhd.ezhome.R;

public class MapsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    }

    @Override
    public void onBackPressed() {
      moveTaskToBack(true);
    }
}
