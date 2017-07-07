package com.ezhometeam.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.ezhometeam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.function.DoubleToIntFunction;


/**
 * Created by n on 20/05/2017.
 */

public class DialogRegister extends Dialog implements View.OnClickListener {
    private EditText edtPassWord;
    private EditText edtUsername;
    private EditText edtConfirmPassword;
    private IRegister mInterf;
    private FirebaseAuth mAuth;
    private Context mContext;


    public DialogRegister(@NonNull Context context, IRegister mInterf) {
        super(context);
        mContext = context;
        this.mInterf = mInterf;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_quick_register);
        findViewByIds();
        setEvents();
    }

    private void findViewByIds() {
        mAuth = FirebaseAuth.getInstance();
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassWord = (EditText) findViewById(R.id.edt_password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
    }

    private void setEvents() {
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                register();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void register() {
        if(0 == edtUsername.getText().toString().trim().length()){
            edtUsername.setError("Username can not be empty");
        }

        else if(0 == edtPassWord.getText().toString().trim().length()){
            edtPassWord.setError("Password can not be empty");
        }
        else if(6 > edtPassWord.getText().toString().trim().length()){
            edtPassWord.setError("Your password must be at least 6 character in length");
        }
        else if(0 == edtConfirmPassword.getText().toString().trim().length()){
            edtConfirmPassword.setError("Confirm can not be empty");
        }

        else if(!edtConfirmPassword.getText().toString().equals(edtPassWord.getText().toString())){
            edtConfirmPassword.setError("Confirm password must be same your password");
        }

        else {
            checkEmail();
        }
    }

    private void checkEmail() {
        String email = edtUsername.getText().toString();
        String password = edtPassWord.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mContext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            mInterf.onClickRegister(email, password);
                            Toast.makeText(getContext(), "Register successfully", Toast.LENGTH_SHORT).show();
                            dismiss();

                        } else {
                            edtUsername.setError("Username invailid or exist");
                            // If sign in fails, display a message to the user.
                        }

                        // ...
                    }
                });

    }

    public interface IRegister{
        void onClickRegister(String user, String password);
    }
}
