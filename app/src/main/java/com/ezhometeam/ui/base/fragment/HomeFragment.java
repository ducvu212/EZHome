package com.ezhometeam.ui.base.fragment;

import android.os.Bundle;
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
        lvInfo = (RecyclerView) getView().findViewById(R.id.lv_phong_tro);
        sever = new FirebaseSever(getContext());
        Bundle bundle = this.getArguments();
        String place = bundle.getString("PLACE");

        sever.getPhongTro(lvInfo, place);

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {

    }


}
