package com.ezhometeam.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ezhometeam.R;
import com.ezhometeam.VideoCall.LoginActivity;
import com.ezhometeam.ui.base.activity.Main2Activity;
import com.ezhometeam.ui.base.fragment.MapFragment;

import java.util.List;

/**
 * Created by n on 08/07/2017.
 */

public class InforHomeUserDialog extends Dialog implements View.OnClickListener {
    private TextView tvContent;
    Main2Activity activity;
    private ITrash mInterf;

    public InforHomeUserDialog(@NonNull Context context, String content, ITrash mInterf) {
        super(context);
        setContentView(R.layout.dialog_home_of_user);
        this.mInterf = mInterf;
        init(content);
    }

    private void init(String content) {
        tvContent = (TextView) findViewById(R.id.tv_info_in_dialog);
        findViewById(R.id.btn_trash).setOnClickListener(this);
        tvContent.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_trash:
                mInterf.trash();
                dismiss();
                break;

            default:
                break;
        }
    }

    public interface ITrash {
        void trash();
    }

}
