package com.t3h.basemvp.ui.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.t3h.basemvp.R;
import com.t3h.basemvp.ui.base.IViewMain;
import com.t3h.basemvp.ui.base.fragment.BaseFragment;

/**
 * Created by ducnd on 5/18/17.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements IViewMain {
    private ProgressDialog mProgress;
    protected boolean mIsDestroy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsDestroy = false;
        setContentView(getLayoutMain());
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.Loading));
        findViewByIds();
        initComponents();
        setEvents();
    }


    @Override
    public void showProgress() {
        if (mIsDestroy) {
            return;
        }
        mProgress.show();
    }

    @Override
    public void hideProgress() {
        if (mIsDestroy) {
            return;
        }
        mProgress.hide();
    }

    @Override
    public void showMessage(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@StringRes int content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onBackPressed() {
        //1
        onBackRoot();
    }

    @Override
    public void onBackRoot() {
        //2
        BaseFragment fragment = BaseFragment.getCurrentBaseFragment(getSupportFragmentManager());
        if (fragment != null) {
            //3
            fragment.onBackRoot();
        }
    }

    public final void onBackMain(){
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }
}
