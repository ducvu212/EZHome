package com.ezhometeam.ui.base.fragment;

import android.support.v7.widget.RecyclerView;

import com.ezhometeam.R;
import com.ezhometeam.interact.FirebaseSever;


/**
 * Created by n on 01/07/2017.
 */

public class HomeFragment extends BaseFragment {
    private FirebaseSever sever;
    private RecyclerView lvInfo;
    @Override
    public int getLayoutMain() {
        return R.layout.layout_home_page;
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


}