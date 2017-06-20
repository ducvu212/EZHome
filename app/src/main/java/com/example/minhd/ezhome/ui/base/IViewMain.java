package com.t3h.basemvp.ui.base;

import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;

/**
 * Created by ducnd on 5/18/17.
 */

public interface IViewMain {
    int getLayoutMain();

    void findViewByIds();

    void initComponents();

    void setEvents();

    void showProgress();

    void hideProgress();

    void showMessage(String content);

    void showMessage(@StringRes int content);

    void onBackRoot();
}
