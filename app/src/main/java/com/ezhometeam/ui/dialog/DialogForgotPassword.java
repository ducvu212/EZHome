package com.ezhometeam.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.ezhometeam.R;


/**
 * Created by n on 20/05/2017.
 */

public class DialogForgotPassword extends Dialog implements View.OnClickListener {
    private EditText EdtContent;
    private IForgot mInterf;
    public DialogForgotPassword(@NonNull Context context, IForgot mInterf) {
        super(context);
        this.mInterf = mInterf;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_forgot_password);
        inits();
        setEvents();
    }

    private void inits() {
        EdtContent = (EditText) findViewById(R.id.edt_email_or_phone_number);
    }

    private void setEvents() {
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                if(TextUtils.isEmpty(EdtContent.getText().toString())){
                    EdtContent.setError("Can not be empty");
                }else {
                    mInterf.onClickSend(EdtContent.getText().toString());
                    dismiss();
                }

                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface IForgot{
        void onClickSend(String user);
    }

}
