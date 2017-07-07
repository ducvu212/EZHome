package com.ezhometeam.ui.base.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ezhometeam.R;
import com.ezhometeam.common.InfomationRegister;
import com.ezhometeam.interact.FirebaseSever;
import com.ezhometeam.ui.base.activity.RegisterInformationActivity;


public class RegisterFragment extends BaseFragment {
    private String userId;
    public RegisterFragment(String user) {
        userId = user;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public int getLayoutMain() {
        Intent i = new Intent(getActivity(), RegisterInformationActivity.class);
        i.putExtra("EMAIL", userId);
        getActivity().startActivity(i);
        return R.layout.layout_re;
    }

    @Override
    public void findViewByIds() {

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {

//        if (personName != null) {
//            tvRegName.setText(personName);
//        }
//        if (name != null) {
//            tvRegName.setText(name);
//        }
    }

}
