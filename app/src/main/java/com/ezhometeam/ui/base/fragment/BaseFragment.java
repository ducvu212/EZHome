package com.ezhometeam.ui.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ezhometeam.ui.base.IViewMain;
import com.ezhometeam.ui.base.activity.BaseActivity;


public abstract class BaseFragment extends Fragment
        implements IViewMain {
    protected boolean mIsDestroy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutMain(), container, false);
        mIsDestroy = false;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds();
        initComponents();
        setEvents();
    }

    public void showProgress() {
        if (mIsDestroy) {
            return;
        }
        getBaseActivity().showProgress();
    }

    public void hideProgress() {
        if (mIsDestroy) {
            return;
        }
        getBaseActivity().hideProgress();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void showMessage(String content) {
        Toast.makeText(getBaseActivity(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@StringRes int content) {
        Toast.makeText(getBaseActivity(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackRoot() {
        //4
        getBaseActivity().onBackMain();
    }

    @Override
    public void onDestroyView() {
        mIsDestroy = true;
        super.onDestroyView();
    }

    public abstract void onBackPressed();
}
