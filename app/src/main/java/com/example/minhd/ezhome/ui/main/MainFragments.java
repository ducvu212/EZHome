package com.example.minhd.ezhome.ui.main;

import android.support.v4.app.FragmentTransaction;

import com.example.minhd.ezhome.R;
import com.example.minhd.ezhome.ui.base.fragment.BaseFragment;

/**
 * Created by minhd on 17/06/24.
 */

public class MainFragments extends BaseFragment {
    @Override
    public int getLayoutMain() {
      return R.layout.main_fragment_activity;
    }

    @Override
    public void findViewByIds() {

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {

    }

    @Override
    public void onBackRoot() {
        super.onBackRoot();
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MainFragments mainFragments = new MainFragments();
        hideFragment(transaction, mainFragments, null, true, true );
    }



}
