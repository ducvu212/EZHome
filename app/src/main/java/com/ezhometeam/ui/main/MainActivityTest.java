package com.ezhometeam.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.ezhometeam.R;
import com.ezhometeam.ui.dialog.DialogRegister;

public class MainActivityTest extends AppCompatActivity implements View.OnClickListener {
    private Context context= getBaseContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        DialogRegister dialog = new DialogRegister(this, new DialogRegister.IRegister() {
            @Override
            public void onClickRegister(String user, String password) {
//                mEdtUser.setText(user);
//                mEdtPass.setText(password);
//                idUser = user;
            }
        });

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int width = display.widthPixels;
        int height = display.heightPixels;

        dialog.getWindow().setLayout(4*width/5, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
