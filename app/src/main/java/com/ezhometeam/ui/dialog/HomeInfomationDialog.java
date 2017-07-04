package com.ezhometeam.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ezhometeam.R;
import com.ezhometeam.ui.base.fragment.MapFragment;

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
