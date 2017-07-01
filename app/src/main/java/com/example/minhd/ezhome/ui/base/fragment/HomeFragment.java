package com.example.minhd.ezhome.ui.base.fragment;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.minhd.ezhome.R;
import com.example.minhd.ezhome.interact.FirebaseSever;


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
        sever.getPhongTro(lvInfo);
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {

    }

    @Override
    public void onBackPressed() {

    }
}
